package com.waylau.spring.boot.blog.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String url;
}
