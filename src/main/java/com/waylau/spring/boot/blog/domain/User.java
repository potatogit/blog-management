package com.waylau.spring.boot.blog.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message="姓名不能为空")
    @Column(nullable=false)
    private String name;

    @NotEmpty(message="邮箱不能为空")
    @Size(max = 50)
    @Email(message= "邮箱格式不对")
    @Column(nullable=false, length = 50, unique = true)
    private String email;

    @NotEmpty(message="账号不能为空")
    @Size(min=3, max=20)
    @Column(nullable=false, length=20, unique=true)
    private String username;

    @NotEmpty(message="密码不能为空")
    @Size(min=6, max=100)
    @Column(length=100)
    private String password;

    @Column(length = 200)
    private String avatar;

}
