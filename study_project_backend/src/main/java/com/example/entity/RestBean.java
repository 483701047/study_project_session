package com.example.entity;

import lombok.Data;

@Data
public class RestBean<T> {

    private int status;
    private T message;
    private boolean success;

    private RestBean(int status, T message, boolean success) {
        this.status = status;
        this.message = message;
        this.success = success;
    }

    public static <T> RestBean<T> success() {
        return new RestBean<>(200, null, true);
    }

    public static <T> RestBean<T> success(T data){
        return new RestBean<>(200, data, true);
    }

    public static <T> RestBean<T> failure(int status) {
        return new RestBean<>(status, null, false);
    }

    public static <T> RestBean<T> failure(int status,T data) {
        return new RestBean<>(status, data, false);
    }
}
