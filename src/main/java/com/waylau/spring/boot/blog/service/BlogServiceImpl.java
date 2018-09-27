package com.waylau.spring.boot.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.waylau.spring.boot.blog.domain.Blog;
import com.waylau.spring.boot.blog.domain.User;
import com.waylau.spring.boot.blog.repository.BlogRepository;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Override public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override public void removeBlog(Long id) {
        blogRepository.delete(id);
    }

    @Override public Blog updateBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override public Blog getBlogById(Long id) {
        return blogRepository.findOne(id);
    }

    @Override public Page<Blog> listBlogsByTitleLike(User user, String title, Pageable pageable) {
        title = "%" + title + "%";
        return blogRepository.findByUserAndTitleLikeOrderByCreateTimeDesc(user, title, pageable);
    }

    @Override public Page<Blog> listBlogsByTitleLikeAndSort(User user, String title, Pageable pageable) {
        title = "%" + title + "%";
        return blogRepository.findByUserAndTitleLike(user, title, pageable);
    }

    @Override public void readingIncrease(Long id) {
        Blog blog = blogRepository.findOne(id);
        blog.setReading(blog.getReading() + 1);
        blogRepository.save(blog);
    }
}
