package com.waylau.spring.boot.blog.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.waylau.spring.boot.blog.domain.User;

public class ControllerHelper {
    public static String getCurrentUserName() {
        String currentUserName = "";
        if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal !=null) {
                currentUserName = principal.getUsername();
            }
        }
        return currentUserName;
    }
}
