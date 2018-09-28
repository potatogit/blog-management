package com.waylau.spring.boot.blog.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.waylau.spring.boot.blog.domain.Blog;
import com.waylau.spring.boot.blog.domain.Comment;
import com.waylau.spring.boot.blog.domain.User;
import com.waylau.spring.boot.blog.service.BlogService;
import com.waylau.spring.boot.blog.service.CommentService;
import com.waylau.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.waylau.spring.boot.blog.vo.Response;

@Controller
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String listComments(@RequestParam(value="blogId",required=true) Long blogId, Model model) {
        Blog blog = blogService.getBlogById(blogId);
        List<Comment> comments = blog.getComments();

        // 判断操作用户是否是评论的所有者
        String commentOwner = getCurrentUserName();
//        if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
//                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
//            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            if (principal !=null) {
//                commentOwner = principal.getUsername();
//            }
//        }

        model.addAttribute("commentOwner", commentOwner);
        model.addAttribute("comments", comments);
        return "/userspace/blog :: #mainContainerRepleace";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Response> createComment(Long blogId, String commentContent) {
        // TODO comment number is not updated after new comment is created
        try{
            blogService.createComment(blogId, commentContent);
        } catch (ConstraintViolationException e) { // 单独获取输入验证的失败信息
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteComment(@PathVariable("id") Long id, Long blogId) {
        User user = commentService.getCommentById(id).getUser();
        String currentUserName = getCurrentUserName();
        boolean isOwner = user.getUsername().equals(currentUserName);

        if(!isOwner) {
            return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
        }

        try{
            blogService.removeComment(blogId, id);
            commentService.removeComment(id);
        } catch (ConstraintViolationException e) { // 单独获取输入验证的失败信息
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    public String getCurrentUserName() {
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
