package com.potato.spring.boot.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.potato.spring.boot.blog.domain.User;
import com.potato.spring.boot.blog.domain.Blog;
import com.potato.spring.boot.blog.domain.Catalog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title, Pageable pageable);

    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);

    Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);

}
