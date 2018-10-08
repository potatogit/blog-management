package com.potato.spring.boot.blog.service;

import java.util.List;

import com.potato.spring.boot.blog.domain.User;
import com.potato.spring.boot.blog.domain.Catalog;

public interface CatalogService {
    Catalog saveCatalog(Catalog catalog);
    void removeCatalog(Long id);
    List<Catalog> listCatalogs(User user);
    Catalog getCatalogById(Long id);

}
