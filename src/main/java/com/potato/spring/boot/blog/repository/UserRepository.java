package com.potato.spring.boot.blog.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.potato.spring.boot.blog.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByNameLike(String name, Pageable pageable);

    User findByUsername(String username);

    List<User> findByUsernameIn(Collection<String> usernames);

}
