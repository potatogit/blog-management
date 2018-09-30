package com.waylau.spring.boot.blog.domain.es;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import com.waylau.spring.boot.blog.domain.Blog;

@AllArgsConstructor
@Getter
@Setter
@Document(indexName = "blog", type = "blog")
@XmlRootElement //MediaType 转为 XML
public class EsBlog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    @Field(index = FieldIndex.not_analyzed) //不做全文检索的字段
    private Long blogId;

    private String title;

    private String summary;

    private String content;

    @Field(index = FieldIndex.not_analyzed)
    private String username;
    @Field(index = FieldIndex.not_analyzed)
    private String avatar;
    @Field(index = FieldIndex.not_analyzed)
    private Timestamp createTime;
    @Field(index = FieldIndex.not_analyzed)
    private Integer readingSize = 0;
    @Field(index = FieldIndex.not_analyzed)
    private Integer commentSize = 0;
    @Field(index = FieldIndex.not_analyzed)
    private Integer voteSize = 0;

    private String tags;

    protected EsBlog() {  // JPA 的规范要求无参构造函数；设为 protected 防止直接使用
    }

    public EsBlog(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public EsBlog(Blog blog){
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.readingSize = blog.getReadingSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
    }

    public void update(Blog blog){
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.readingSize = blog.getReadingSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
    }

    @Override
    public String toString() {
        return String.format("User [id=%d, title='%s', content='%s'", blogId, title, content);
    }
}
