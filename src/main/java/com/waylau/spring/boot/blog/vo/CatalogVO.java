package com.waylau.spring.boot.blog.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.waylau.spring.boot.blog.domain.Catalog;
@NoArgsConstructor
@Setter
@Getter
public class CatalogVO {
    private String username;
    private Catalog catalog;
}
