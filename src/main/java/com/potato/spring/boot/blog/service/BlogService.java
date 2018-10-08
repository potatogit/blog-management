package com.potato.spring.boot.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.potato.spring.boot.blog.domain.User;
import com.potato.spring.boot.blog.domain.Blog;
import com.potato.spring.boot.blog.domain.Catalog;

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

    Blog createVote(Long blogId);

    void removeVote(Long blogId, Long voteId);

    Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable);
}
