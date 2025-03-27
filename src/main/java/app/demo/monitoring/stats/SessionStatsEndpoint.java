/*
 * Copyright (c) 2008-2025 The Aspectran Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.demo.monitoring.stats;

import com.aspectran.core.component.bean.annotation.Component;
import com.aspectran.core.component.session.ManagedSession;
import com.aspectran.core.component.session.SessionManager;
import com.aspectran.core.component.session.SessionStatistics;
import com.aspectran.undertow.server.TowServer;
import com.aspectran.utils.annotation.jsr305.NonNull;
import com.aspectran.web.websocket.jsr356.AspectranConfigurator;
import com.aspectran.web.websocket.jsr356.SimplifiedEndpoint;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@Component
@ServerEndpoint(
        value = "/monitoring/stats",
        configurator = AspectranConfigurator.class
)
public class SessionStatsEndpoint extends SimplifiedEndpoint {

    private static final String COMMAND_JOIN = "JOIN:";

    private static final String COMMAND_LEAVE = "LEAVE";

    private static final String MESSAGE_PING = "--ping--";

    private static final String MESSAGE_PONG = "--pong--";

    private int refreshIntervalInSeconds = 5;

    private volatile Timer timer;

    private volatile boolean first;

    @Override
    protected void registerMessageHandlers(@NonNull Session session) {
        session.addMessageHandler(String.class, message
                -> handleMessage(session, message));
    }

    public void handleMessage(Session session, String message) {
        if (MESSAGE_PING.equals(message)) {
            pong(session);
            return;
        }
        if (message != null && message.startsWith(COMMAND_JOIN)) {
            try {
                refreshIntervalInSeconds = Integer.parseInt(message.substring(COMMAND_JOIN.length()));
            } catch (NumberFormatException e) {
                // ignore
            }
            join(session);
        } else if (COMMAND_LEAVE.equals(message)) {
            removeSession(session);
        }
    }

    @Override
    protected void onSessionRemoved(Session session) {
        if (countSessions() == 0 && timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void pong(Session session) {
        sendText(session, MESSAGE_PONG);
    }

    private void join(Session session) {
        if (addSession(session)) {
            first = true;
            if (timer == null) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    private SessionStatsPayload oldStats;

                    @Override
                    public void run() {
                        SessionStatsPayload newStats = getSessionStatsPayload();
                        if (first || !newStats.equals(oldStats)) {
                            broadcast(newStats.toJson());
                            oldStats = newStats;
                            if (first) {
                                first = false;
                            }
                        }
                    }
                }, 0, refreshIntervalInSeconds * 1000L);
            }
        }
    }

    @NonNull
    private SessionStatsPayload getSessionStatsPayload() {
        TowServer towServer = getBeanRegistry().getBean("tow.server");
        SessionManager sessionManager = towServer.getSessionManager("root");
        SessionStatistics statistics = sessionManager.getStatistics();

        SessionStatsPayload stats = new SessionStatsPayload();
        stats.setCreatedSessionCount(statistics.getNumberOfCreated());
        stats.setExpiredSessionCount(statistics.getNumberOfExpired());
        stats.setActiveSessionCount(statistics.getNumberOfActives());
        stats.setHighestActiveSessionCount(statistics.getHighestNumberOfActives());
        stats.setEvictedSessionCount(statistics.getNumberOfUnmanaged());
        stats.setRejectedSessionCount(statistics.getNumberOfRejected());
        stats.setElapsedTime(formatDuration(statistics.getStartTime()));

        // Current Users
        List<String> currentSessions = new ArrayList<>();
        Set<String> sessionIds = sessionManager.getActiveSessions();
        for (String sessionId : sessionIds) {
            ManagedSession session = sessionManager.getSession(sessionId);
            if (session != null) {
                currentSessions.add("1:Session " + session.getId() + " created at " +
                        formatTime(session.getCreationTime()));
            }
        }
        stats.setCurrentSessions(currentSessions.toArray(new String[0]));
        return stats;
    }

    @NonNull
    private String formatTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).toString();
    }

    @NonNull
    private String formatDuration(long startTime) {
        Instant start = Instant.ofEpochMilli(startTime);
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long seconds = duration.getSeconds();
        return String.format(
                "%02d:%02d:%02d",
                seconds / 3600,
                (seconds % 3600) / 60,
                seconds % 60);
    }

}
