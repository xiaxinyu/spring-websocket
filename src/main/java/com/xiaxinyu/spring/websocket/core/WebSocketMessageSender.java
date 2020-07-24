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
                sendMessage(item.getSid(), item.getSession(), message);
            }
        } else {
            throw new RuntimeException(String.format("Can't find available channel: sid=%s", sid));
        }
    }

    /**
     * 实现服务器主动推送
     */
    public static void sendMessage(String sid, Session session, String message) throws IOException {
        log.info("session.getBasicRemote()=={}, sessionId={}", session.getBasicRemote(), session.getId());
        synchronized (session) {
            if (message.equals("Ping")) {
                session.getBasicRemote().sendText(String.format("Pong  sid=%s  sessionId=%s", sid, session.getId()));
            } else {
                session.getBasicRemote().sendText(String.format("%s  sid=%s  sessionId=%s", message, sid, session.getId()));
            }
        }
    }
}
