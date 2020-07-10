package com.xiaxinyu.spring.websocket.controller;

import com.xiaxinyu.spring.websocket.core.ResponseEntity;
import com.xiaxinyu.spring.websocket.core.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

/**
 * Controller
 *
 * @author XIAXINYU3
 * @date 2019.8.30
 */
@Controller
@Slf4j
public class HomeController {
    @RequestMapping("/websocket/push/{cid}")
    @ResponseBody
    public ResponseEntity<String> pushToWeb(@PathVariable String cid, String message) {
        try {
            WebSocketServer.sendInfo(message, cid);
        } catch (IOException e) {
            return ResponseEntity.error(cid + "#" + e.getMessage());
        }
        return  new ResponseEntity<>(cid);
    }
}
