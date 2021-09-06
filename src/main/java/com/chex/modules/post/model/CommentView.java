package com.chex.modules.post.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CommentView {

    private Long id;
    private Long authorid;
    private boolean isAuthor;
    private String authorName;
    private Long postid;
    private String content;
    private String createdAt;


    public CommentView() {
        this.setAuthor(false);
    }
}
