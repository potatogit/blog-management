package com.potato.spring.boot.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.potato.spring.boot.blog.domain.User;
import com.potato.spring.boot.blog.domain.Catalog;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    List<Catalog> findByUser(User user);

    List<Catalog> findByUserAndName(User user, String name);

}
