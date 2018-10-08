package com.potato.spring.boot.blog.consts;

public enum RoleUser {

    ROLE_USER_AUTHORITY_ID(2L);

    private Long value;

    RoleUser(Long num) {
        this.value = num;
    }

    public Long value() {
        return this.value;
    }

}
