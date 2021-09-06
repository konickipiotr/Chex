package com.chex.modules.post.model;

import com.chex.api.post.PostVisibility;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
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
}
