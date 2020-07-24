package com.xiaxinyu.spring.websocket.core;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebSocketManager {
    private final static ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketServer>> webSocketMap;

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    /**
     *  concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    static {
        webSocketMap = new ConcurrentHashMap<>();
    }

    public static void addWebSocketServer(String sid, WebSocketServer webSocketServer) {
        CopyOnWriteArraySet<WebSocketServer> webSocketSet = webSocketMap.get(sid);
        if (Objects.isNull(webSocketSet)) {
            webSocketSet = new CopyOnWriteArraySet<>();
            webSocketMap.put(sid, webSocketSet);
        }
        webSocketSet.add(webSocketServer);

        addOnlineCount();
    }

    public static void removeWebSocketServer(String sid, WebSocketServer webSocketServer) {
        CopyOnWriteArraySet<WebSocketServer> webSocketSet = webSocketMap.get(sid);
        if (Objects.nonNull(webSocketSet) && !webSocketSet.isEmpty()) {
            webSocketSet.remove(webSocketServer);
            subOnlineCount();
        } else {
            throw new RuntimeException(String.format("Can't find any WebSocketServer: sid=%s", sid));
        }
    }

    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketServer(String sid) {
        CopyOnWriteArraySet<WebSocketServer> webSocketSet = webSocketMap.get(sid);
        return webSocketSet;
    }

    public static synchronized void addOnlineCount() {
        WebSocketManager.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketManager.onlineCount--;
    }

    public static synchronized int getOnlineCount() {
        return WebSocketManager.onlineCount;
    }
}
