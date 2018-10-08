package com.potato.spring.boot.blog.service;

import com.potato.spring.boot.blog.domain.Authority;

public interface AuthorityService {
    Authority getAuthorityById(Long id);
}
