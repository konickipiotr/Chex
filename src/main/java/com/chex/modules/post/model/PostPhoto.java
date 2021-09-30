package com.chex.modules.post.model;

import javax.persistence.*;

@Entity
public class PostPhoto {

    @Id
    @SequenceGenerator(name = "photo_sequence", sequenceName = "photo_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_sequence")
    private Long id;
    private Long userid;
    private Long postid;
    private String realPath;
    private String webAppPath;

    public PostPhoto(Long postid, Long userid) {
        this.postid = postid;
        this.userid = userid;
    }

    public PostPhoto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getPostid() {
        return postid;
    }

    public void setPostid(Long postid) {
        this.postid = postid;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getWebAppPath() {
        return webAppPath;
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
    }
}
