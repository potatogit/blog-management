package com.waylau.spring.boot.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.waylau.spring.boot.blog.domain.Blog;
import com.waylau.spring.boot.blog.domain.Comment;
import com.waylau.spring.boot.blog.domain.User;
import com.waylau.spring.boot.blog.domain.Vote;
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
        blog.setReadingSize(blog.getReadingSize() + 1);
        blogRepository.save(blog);
    }

    @Override public Blog createComment(Long blogId, String commentContent) {
        Blog blog = blogRepository.findOne(blogId);
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = new Comment(user, commentContent);
        blog.addComment(comment);
        return blogRepository.save(blog);
    }

    @Override public void removeComment(Long blogId, Long commentId) {
        Blog blog = blogRepository.findOne(blogId);
        blog.removeComment(commentId);
        blogRepository.save(blog);
    }

    @Override public Blog createVote(Long blogId) {
        Blog blog = blogRepository.findOne(blogId);
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote = new Vote(user);
        boolean isExisted = blog.addVote(vote);
        if(isExisted) {
            throw new IllegalArgumentException("该用户已经点过赞");
        }
        return blogRepository.save(blog);
    }

    @Override public void removeVote(Long blogId, Long voteId) {
        Blog blog = blogRepository.findOne(blogId);
        blog.removeVote(voteId);
        blogRepository.save(blog);
    }
}
