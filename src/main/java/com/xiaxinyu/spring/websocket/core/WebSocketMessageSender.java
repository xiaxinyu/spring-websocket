package com.xiaxinyu.spring.websocket.core;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author XIAXINYU3
 * @date 2020.7.24
 */
@Slf4j
public class WebSocketMessageSender {
    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);
        CopyOnWriteArraySet<WebSocketServer> webSocketSet = WebSocketManager.getWebSocketServer(sid);

        if (Objects.nonNull(webSocketSet) && !webSocketSet.isEmpty()) {
            for (WebSocketServer item : webSocketSet) {
                sendMessage(item.getSession(), message);
            }
        }
    }

    /**
     * 实现服务器主动推送
     */
    public static void sendMessage(Session session, String message) throws IOException {
        log.info("session.getBasicRemote()=={}, sessionId={}", session.getBasicRemote(), session.getId());
        synchronized (session) {
            if (message.equals("Ping")) {
                session.getBasicRemote().sendText(String.format("Pong  sid=%s", session.getId()));
            } else {
                session.getBasicRemote().sendText(String.format("%s  sid=%s", message, session.getId()));
            }
        }
    }
}
