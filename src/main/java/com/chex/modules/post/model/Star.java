package com.chex.modules.post.model;

import javax.persistence.*;

@Entity
public class Star {

    @Id
    @SequenceGenerator(name = "star_sequence", sequenceName = "star_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "star_sequence")
    private Long id;
    private Long userid;
    private Long postid;

    public Star() {
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
}