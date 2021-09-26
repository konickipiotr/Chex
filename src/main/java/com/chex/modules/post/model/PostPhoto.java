package com.chex.modules.post.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
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
}
