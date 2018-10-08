package com.potato.spring.boot.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.potato.spring.boot.blog.domain.Comment;
import com.potato.spring.boot.blog.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override public Comment getCommentById(Long id) {
        return commentRepository.findOne(id);
    }

    @Override public void removeComment(Long id) {
        commentRepository.delete(id);
    }
}
