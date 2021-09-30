package com.chex.modules.post.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @SequenceGenerator(name = "comment_sequence", sequenceName = "comment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_sequence")
    private Long id;
    private Long postid;
    private Long authorid;
    private String content;
    private LocalDateTime created;

    public Comment() {
    }

    public Comment(Long postid, Long authorid, String content, LocalDateTime created) {
        this.postid = postid;
        this.authorid = authorid;
        this.content = content;
        this.created = created;
    }

    public Comment(Long postid, Long authorid, String content) {
        this.postid = postid;
        this.authorid = authorid;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostid() {
        return postid;
    }

    public void setPostid(Long postid) {
        this.postid = postid;
    }

    public Long getAuthorid() {
        return authorid;
    }

    public void setAuthorid(Long authorid) {
        this.authorid = authorid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
