package com.waylau.spring.boot.blog.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.waylau.spring.boot.blog.domain.User;

public interface UserService {
    User saveUser(User user);
    void removeUser(Long id);
    void removeUsersInBatch(List<User> users);
    User updateUser(User user);
    User getUserById(Long id);
    List<User> listUsers();
    Page<User> listUsersByNameLike(String name, Pageable pageable);
}
