package com.waylau.spring.boot.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class TagVO {
    private String name;
    private Long count;
}
