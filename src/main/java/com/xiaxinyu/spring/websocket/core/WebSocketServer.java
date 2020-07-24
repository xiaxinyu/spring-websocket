package com.xiaxinyu.spring.websocket.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * WebSocketServer
 *
 * @author XIAXINYU3
 * @date 2019.8.30
 */
@ServerEndpoint("/websocket/{sid}")
@Component
@Slf4j
public class WebSocketServer {

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收sid
     */
    private String sid = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) throws IOException {
        printThread();

        this.session = session;
        this.sid = sid;
        WebSocketManager.addWebSocketServer(sid, this);

        log.info("有新窗口开始监听: {}, 当前在线人数为: {}, sessionId={}", sid, WebSocketManager.getOnlineCount(), session.getId());
        if (!session.isOpen()) {
            throw new RuntimeException(String.format("Session isn't opened: sid=%s, sessionId=%s", sid, session.getId()));
        } else {
            WebSocketMessageSender.sendMessage(sid, session, "连接成功");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        printThread();

        //从set中删除
        WebSocketManager.removeWebSocketServer(this.sid, this);
        log.info("有一连接关闭！当前在线人数为" + WebSocketManager.getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        printThread();
        log.info("收到消息来自窗口: sid={}, message={}, sessionId={}", this.sid, message, session.getId());

        CopyOnWriteArraySet<WebSocketServer> webSocketSet = WebSocketManager.getWebSocketServer(this.sid);

        if (Objects.isNull(webSocketSet) || webSocketSet.isEmpty()) {
            WebSocketMessageSender.sendMessage(this.sid, session, String.format("未找到可用的连接通道： sid=%s", this.sid));
            return;
        }

        //群发消息
        for (WebSocketServer item : webSocketSet) {
            WebSocketMessageSender.sendMessage(this.sid, item.session, message);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        printThread();

        log.error("发生错误: sid={}, sessionId={}", this.sid, session.getId(), error);
    }

    public Session getSession() {
        return session;
    }

    public String getSid() {
        return sid;
    }

    private void printThread() {
        Thread thread = Thread.currentThread();
        ThreadGroup threadGroup = thread.getThreadGroup();
        log.info("=================[thread={},threadGroup={},threadActiveInGroup={}]==================", thread.getName(), threadGroup.getName(), threadGroup.activeCount());
    }
}
