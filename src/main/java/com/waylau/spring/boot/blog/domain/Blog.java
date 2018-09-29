package com.waylau.spring.boot.blog.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.rjeschke.txtmark.Processor;

@Entity
@Getter
@Setter
public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长
    private Long id;

    @NotEmpty(message = "标题不能为空")
    @Size(min=2, max=50)
    @Column(nullable = false, length = 50)
    private String title;

    @NotEmpty(message = "摘要不能为空")
    @Size(min=2, max = 300)
    @Column(nullable = false)
    private String summary;

    @Lob // large obj mapping to Long Text in MySQL
    @Basic(fetch=FetchType.LAZY)
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false)
    private String content;

    @Lob // large obj mapping to Long Text in MySQL
    @Basic(fetch=FetchType.LAZY)
    @NotEmpty(message = "摘要不能为空")
    @Size(min=2)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE) // prohibit to generate setter
    private String htmlContent;

    @OneToOne(cascade=CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id") // 会在表中增加名为user_id的一列,作为一个外键
    private User user;

    @Column(nullable = false)
    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private Timestamp createTime;

    @Column(name="readingSize")
    private Integer readingSize = 0;

    @Column(name = "commentSize")
    private Integer commentSize = 0;

    @Column(name = "voteSize")
    private Integer voteSize = 0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="blog_comment", joinColumns = @JoinColumn(name="blog_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="comment_id", referencedColumnName = "id")) // 会新建一张表,包含blog_id,comment_id两列
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="blog_vote", joinColumns = @JoinColumn(name="blog_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="vote_id", referencedColumnName = "id"))
    private List<Vote> votes;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="catalog_id")
    private Catalog catalog;

    @Column(name="tags", length = 100)
    private String tags; // ex, "a,b,c,d"

    protected Blog() {}

    public Blog( String title, String summary, String content ) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
        this.htmlContent = Processor.process(content);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        this.commentSize = this.comments.size();
    }

    public void removeComment(Long commentId) {
        this.comments.removeIf(item -> item.getId().equals(commentId));
        this.commentSize = this.comments.size();
    }

    public boolean addVote(Vote vote) {
        boolean isExisted = this.votes.stream()
                .map(Vote::getId)
                .anyMatch(item -> item.equals(vote.getId()));
        if (!isExisted) {
            this.votes.add(vote);
            this.voteSize = this.votes.size();
        }
        return isExisted;
    }
    public void removeVote(Long voteId) {
        this.votes.removeIf(item -> item.getId().equals(voteId));
        this.voteSize = this.votes.size();
    }

}
