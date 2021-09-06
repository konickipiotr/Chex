package com.chex.modules.post.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @SequenceGenerator(name = "comment_sequence", sequenceName = "comment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_sequence")
    private Long id;
    private Long postid;
    private Long authorid;
    private String content;
    private LocalDateTime created;

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
}
