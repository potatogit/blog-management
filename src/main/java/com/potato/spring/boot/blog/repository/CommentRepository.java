package com.potato.spring.boot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.potato.spring.boot.blog.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
