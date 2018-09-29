package com.waylau.spring.boot.blog.service;

import java.util.List;

import com.waylau.spring.boot.blog.domain.Catalog;
import com.waylau.spring.boot.blog.domain.User;

public interface CatalogService {
    Catalog saveCatalog(Catalog catalog);
    void removeCatalog(Long id);
    List<Catalog> listCatalogs(User user);
    Catalog getCatalogById(Long id);

}
