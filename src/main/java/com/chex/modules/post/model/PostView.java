package com.chex.modules.post.model;

import com.chex.modules.post.model.CommentView;
import com.chex.modules.places.model.PlaceShortView;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class PostView {

    private Long id;
    private Long authorId;
    private boolean isAuthor;
    private String authorName;
    private String authorPhoto;
    private String description;
    private String createdAt;
    private int stanum;
    private boolean voted;
    private List<PlaceShortView> places = new ArrayList<>();
    private List<PlaceShortView> subPlaces = new ArrayList<>();
    private List<CommentView> commentViews = new ArrayList<>();
    private List<PostPhoto> photos = new ArrayList<>();

}
