package com.waylau.spring.boot.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Response {
    private boolean success;
    private String message;
    private Object body;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
