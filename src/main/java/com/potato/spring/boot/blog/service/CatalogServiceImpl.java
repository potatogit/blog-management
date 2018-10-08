package com.potato.spring.boot.blog.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.potato.spring.boot.blog.domain.Catalog;
import com.potato.spring.boot.blog.domain.User;
import com.potato.spring.boot.blog.repository.CatalogRepository;

@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;

    @Override public Catalog saveCatalog(Catalog catalog) {
        List<Catalog> catalogs = catalogRepository.findByUserAndName(catalog.getUser(), catalog.getName());
        if((!Objects.isNull(catalogs)) && (!catalogs.isEmpty())) {
            throw new IllegalArgumentException("该分类已经存在了");
        }
        return catalogRepository.save(catalog);
    }

    @Override public void removeCatalog(Long id) {
        catalogRepository.delete(id);
    }

    @Override public List<Catalog> listCatalogs(User user) {
        return catalogRepository.findByUser(user);
    }

    @Override public Catalog getCatalogById(Long id) {
        return catalogRepository.findOne(id);
    }
}
