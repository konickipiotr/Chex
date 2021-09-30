package com.chex.modules.post.model;

import com.chex.api.post.PostVisibility;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @SequenceGenerator(name = "post_sequence", sequenceName = "post_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence")
    private Long id;
    private Long userid;
    private String description;
    private LocalDateTime created;
    private String places;
    private String subplaces;
    private String achivments;
    private int stanum;
    @Enumerated(EnumType.STRING)
    private PostVisibility postvisibility;

    // for testing
    public Post(Long userid, LocalDateTime created) {
        this.userid = userid;
        this.created = created;
        this.postvisibility = PostVisibility.PUBLIC;
    }

    public Post() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public String getSubplaces() {
        return subplaces;
    }

    public void setSubplaces(String subplaces) {
        this.subplaces = subplaces;
    }

    public String getAchivments() {
        return achivments;
    }

    public void setAchivments(String achivments) {
        this.achivments = achivments;
    }

    public int getStanum() {
        return stanum;
    }

    public void setStanum(int stanum) {
        this.stanum = stanum;
    }

    public PostVisibility getPostvisibility() {
        return postvisibility;
    }

    public void setPostvisibility(PostVisibility postvisibility) {
        this.postvisibility = postvisibility;
    }
}
