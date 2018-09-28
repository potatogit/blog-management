package com.waylau.spring.boot.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.waylau.spring.boot.blog.domain.Blog;
import com.waylau.spring.boot.blog.domain.User;

public interface BlogService {
    Blog saveBlog(Blog blog);

    void removeBlog(Long id);

    Blog updateBlog(Blog blog);

    Blog getBlogById(Long id);

    Page<Blog> listBlogsByTitleLike(User user, String title, Pageable pageable);

    Page<Blog> listBlogsByTitleLikeAndSort(User user, String title, Pageable pageable);

    void readingIncrease(Long id);

    Blog createComment(Long blogId, String commentContent);

    void removeComment(Long blogId, Long commentId);
}
