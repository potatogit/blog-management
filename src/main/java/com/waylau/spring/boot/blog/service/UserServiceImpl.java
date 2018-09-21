package com.waylau.spring.boot.blog.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.waylau.spring.boot.blog.domain.User;
import com.waylau.spring.boot.blog.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override public void removeUser(Long id) {
        userRepository.delete(id);
    }

    @Transactional
    @Override public void removeUsersInBatch(List<User> users) {
        userRepository.deleteInBatch(users);
    }

    @Transactional
    @Override public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override public User getUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Override public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override public Page<User> listUsersByNameLike(String name, Pageable pageable) {
        String nameExp = "%" + name + "%";
        Page<User> users = userRepository.findByNameLike(nameExp, pageable);
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
