package com.potato.spring.boot.blog.service;

import com.potato.spring.boot.blog.domain.Comment;

public interface CommentService {
    Comment getCommentById(Long id);
    void removeComment(Long id);
}
