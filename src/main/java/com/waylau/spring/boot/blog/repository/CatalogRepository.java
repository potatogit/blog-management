package com.waylau.spring.boot.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waylau.spring.boot.blog.domain.Catalog;
import com.waylau.spring.boot.blog.domain.User;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    List<Catalog> findByUser(User user);

    List<Catalog> findByUserAndName(User user, String name);

}
