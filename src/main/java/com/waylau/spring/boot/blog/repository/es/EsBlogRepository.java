package com.waylau.spring.boot.blog.repository.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.waylau.spring.boot.blog.domain.es.EsBlog;

public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {
    // 去重搜索
    Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(
            String title, String summary, String content, String tags, Pageable pagable);

    EsBlog findByBlogId(Long blogId);
}
