<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="/assets/css/monitoring.css?20231125">
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment-with-locales.min.js"></script>
<script src="/assets/js/monitoring-log.js?20231125"></script>
<script src="/assets/js/monitoring-session.js?20231125"></script>
<div class="grid-x grid-padding-x">
    <div class="cell t20">
        <h3>Server Logs
            <a id="anchor1" href="#anchor1" class="float-right"><span class="icon fi-anchor"></span></a></h3>
        <div class="log-container">
            <div class="log-header">
                <ul class="tab">
                    <li>app-log</li>
                </ul>
                <a class="bite-tail" title="Scroll to End of Log">
                    <span class="tail-status"></span>
                </a>
            </div>
            <div class="missile-route">
                <div class="stack"></div>
            </div>
            <pre id="app-log" class="log-tail"></pre>
        </div>
    </div>
</div>
<div class="grid-x grid-padding-x stats-wrap" style="display: none">
    <div class="cell small-12 large-5 t20">
        <h3>Session Statistics
            <a id="anchor3" href="#anchor3" class="float-right hide-for-large"><span class="icon fi-anchor"></span></a></h3>
        <div class="panel stats">
            <dl>
                <dt>Current Active Sessions</dt>
                <dd><span class="number activeSessionCount">0</span></dd>
                <dt>Current Inactive Sessions</dt>
                <dd><span class="number evictedSessionCount">0</span></dd>
                <dt>Highest Active Sessions</dt>
                <dd><span class="number highestActiveSessionCount">0</span></dd>
                <dt title="Number of sessions created since system bootup">Created Sessions</dt>
                <dd><span class="number createdSessionCount">0</span></dd>
                <dt>Expired Sessions</dt>
                <dd><span class="number expiredSessionCount">0</span></dd>
                <dt>Rejected Sessions</dt>
                <dd><span class="number rejectedSessionCount">0</span></dd>
            </dl>
        </div>
        <p class="text-right"><i>Elapsed <span class="elapsedTime"></span></i></p>
    </div>
    <div class="cell small-12 large-7 t20">
        <h3>Current Sessions
            <a id="anchor2" href="#anchor2" class="float-right show-for-large"><span class="icon fi-anchor"></span></a></h3>
        <div class="panel sessions-wrap">
            <ul class="sessions">
            </ul>
        </div>
    </div>
</div>
<script>
    $(function() {
        let sessionStats = new SessionStats("/monitoring/stats", 5);
        try {
            sessionStats.openSocket();
            $(".stats-wrap").fadeIn();
        } catch (e) {
            console.error("Socket connection failed to [" + sessionStats.endpoint + "]");
        }
    });
</script>
<script>
    $(function() {
        let logTailer = new LogTailer("/monitoring/logtail", "app-log");
        $(".bite-tail").click(function() {
            let logtail = $(this).closest(".log-container").find(".log-tail");
            logTailer.switchTailBite(!logtail.data("bite"), logtail);
        });
        try {
            logTailer.openSocket();
        } catch (e) {
            logTailer.printErrorMessage("Socket connection failed");
        }
    });
</script>