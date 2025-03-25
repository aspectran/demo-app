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
package app.demo.monitoring.log;

import com.aspectran.core.activity.InstantActivitySupport;
import com.aspectran.core.component.bean.annotation.AvoidAdvice;
import com.aspectran.core.component.bean.annotation.Component;
import com.aspectran.utils.ExceptionUtils;
import com.aspectran.utils.StringUtils;
import com.aspectran.utils.annotation.jsr305.NonNull;
import com.aspectran.web.websocket.jsr356.AspectranConfigurator;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.ClosedChannelException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeoutException;

@Component
@ServerEndpoint(
        value = "/monitoring/logtail",
        configurator = AspectranConfigurator.class
)
@AvoidAdvice
public class LogtailEndpoint extends InstantActivitySupport {

    private static final Logger logger = LoggerFactory.getLogger(LogtailEndpoint.class);

    private static final String COMMAND_JOIN = "JOIN:";

    private static final String COMMAND_LEAVE = "LEAVE";

    private static final String HEARTBEAT_PING_MSG = "--ping--";

    private static final String HEARTBEAT_PONG_MSG = "--pong--";

    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    private LogTailerManager logTailerManager;

    public void setLogTailerManager(LogTailerManager logTailerManager) {
        this.logTailerManager = logTailerManager;
    }

    @OnOpen
    public void onOpen(Session session) {
        if (logger.isDebugEnabled()) {
            logger.debug("WebSocket connection established for session {}", session.getId());
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        if (HEARTBEAT_PING_MSG.equals(message)) {
            session.getAsyncRemote().sendText(HEARTBEAT_PONG_MSG);
            return;
        }
        if (message != null && message.startsWith(COMMAND_JOIN)) {
            addSession(session, message);
        } else if (COMMAND_LEAVE.equals(message)) {
            removeSession(session);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        if (logger.isDebugEnabled()) {
            logger.debug("Websocket session {} has been closed. Reason: {}", session.getId(), reason);
        }
        removeSession(session);
    }

    @OnError
    public void onError(@NonNull Session session, Throwable error) {
        if (!ExceptionUtils.hasCause(error, ClosedChannelException.class, TimeoutException.class)) {
            logger.warn("Error in websocket session: {}", session.getId(), error);
        }
        try {
            removeSession(session);
            session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, null));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected void broadcast(String message) {
        for (Session session : sessions) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        }
    }

    private void addSession(Session session, String message) {
        synchronized (sessions) {
            if (sessions.add(session)) {
                String[] names = StringUtils.splitWithComma(message.substring(COMMAND_JOIN.length()));
                logTailerManager.join(session, names);
            }
        }
    }

    private void removeSession(Session session) {
        synchronized (sessions) {
            if (sessions.remove(session)) {
                logTailerManager.release(session);
            }
        }
    }

    Session[] getSessions() {
        return sessions.toArray(new Session[0]);
    }

}
