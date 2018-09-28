package com.waylau.spring.boot.blog.service;

import com.waylau.spring.boot.blog.domain.Comment;

public interface CommentService {
    Comment getCommentById(Long id);
    void removeComment(Long id);
}
