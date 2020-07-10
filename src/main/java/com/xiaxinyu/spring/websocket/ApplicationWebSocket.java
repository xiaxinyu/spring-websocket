package com.xiaxinyu.spring.websocket;

import com.xiaxinyu.spring.websocket.listener.StartUpListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application Entry
 *
 * @author XIAXINYU3
 * @date 2019.8.30
 */
@SpringBootApplication
public class ApplicationWebSocket {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ApplicationWebSocket.class);
        springApplication.addListeners(new StartUpListener());
        springApplication.run(args);
    }
}