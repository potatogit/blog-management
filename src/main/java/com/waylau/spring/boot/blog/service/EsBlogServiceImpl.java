package com.waylau.spring.boot.blog.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchParseException;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.waylau.spring.boot.blog.domain.User;
import com.waylau.spring.boot.blog.domain.es.EsBlog;
import com.waylau.spring.boot.blog.repository.es.EsBlogRepository;
import com.waylau.spring.boot.blog.vo.TagVO;

@Service
public class EsBlogServiceImpl implements EsBlogService {
    @Autowired
    private EsBlogRepository esBlogRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private UserService userService;

    private static final Pageable TOP_5_PAGEABLE = new PageRequest(0, 5);
    private static final String EMPTY_KEYWORD = "";

    @Override public void removeEsBlog(String id) {
        esBlogRepository.delete(id);
    }

    @Override public EsBlog updateEsBlog(EsBlog esBlog) {
        return esBlogRepository.save(esBlog);
    }

    @Override public EsBlog getEsBlogByBlogId(Long blogId) {
        return esBlogRepository.findByBlogId(blogId);
    }

    @Override public Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable) throws SearchParseException {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        if(Objects.isNull(pageable.getSort())) {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(
                keyword, keyword, keyword, keyword, pageable);
    }

    @Override public Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pagable) {
        Sort sort = new Sort(Sort.Direction.DESC, "readingSize", "commentSize", "voteSize", "createTime");
        if(Objects.isNull(pagable.getSort())) {
            pagable = new PageRequest(pagable.getPageNumber(), pagable.getPageSize(), sort);
        }
        return esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(
                keyword, keyword, keyword, keyword, pagable);
    }

    @Override public Page<EsBlog> listEsBlogs(Pageable pageable) {
        return esBlogRepository.findAll(pageable);
    }

    @Override public List<EsBlog> listTop5NewestEsBlogs() {
        Page<EsBlog> page = this.listNewestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
        return page.getContent();
    }

    @Override public List<EsBlog> listTop5HotestEsBlogs() {
        Page<EsBlog> page = this.listHotestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
        return page.getContent();
    }

    @Override public List<TagVO> listTop30Tags() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .withIndices("blog")
                .withTypes("blog")
                .addAggregation(AggregationBuilders.terms("tags").field("tags").order(Terms.Order.count(false)).size(30))
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());

        StringTerms modelTerms = (StringTerms) aggregations.asMap().get("tags");

        List<TagVO> list = modelTerms.getBuckets().stream()
                .map(item -> new TagVO(item.getKey().toString(), item.getDocCount()))
                .collect(Collectors.toList());
        return list;
    }

    @Override public List<User> listTop12Users() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .withIndices("blog")
                .withTypes("blog")
                // TODO why users instead of user
                .addAggregation(AggregationBuilders.terms("users").field("username").order(Terms.Order.count(false)).size(12))
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());
        StringTerms modelTerms = (StringTerms) aggregations.asMap().get("users");

        List<String> userNameList = modelTerms.getBuckets().stream()
                .map(item -> item.getKey().toString())
                .collect(Collectors.toList());
        List<User> userList = userService.listUsersByUsernames(userNameList);
        return userList;
    }
}
