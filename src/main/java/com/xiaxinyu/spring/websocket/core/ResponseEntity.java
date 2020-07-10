package com.xiaxinyu.spring.websocket.core;

/**
 * API响应报文格式
 *
 * @param <T>
 * @author XIAXINYU3
 * @date 2019.8.30
 */
public class ResponseEntity<T> {
    private T data;
    private Integer code = 20000;
    private String message;

    public static ResponseEntity ok() {
        return new ResponseEntity();
    }

    public static ResponseEntity error(String message) {
        return new ResponseEntity(30000, message);
    }

    public ResponseEntity(T data) {
        this.data = data;
    }

    public ResponseEntity() {
    }

    public ResponseEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
