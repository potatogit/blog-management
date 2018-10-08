package com.potato.spring.boot.blog.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.potato.spring.boot.blog.domain.User;
import com.potato.spring.boot.blog.domain.es.EsBlog;
import com.potato.spring.boot.blog.vo.TagVO;

public interface EsBlogService {
    void removeEsBlog(String id);

    EsBlog updateEsBlog(EsBlog esBlog);

    EsBlog getEsBlogByBlogId(Long blogId);

    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pagable);

    Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pagable);

    Page<EsBlog> listEsBlogs(Pageable pageable);

    List<EsBlog> listTop5NewestEsBlogs();

    List<EsBlog> listTop5HotestEsBlogs();

    List<TagVO> listTop30Tags();

    List<User> listTop12Users();
}
