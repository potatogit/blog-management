package com.waylau.spring.boot.blog.service;

import com.waylau.spring.boot.blog.domain.Authority;

public interface AuthorityService {
    Authority getAuthorityById(Long id);
}
