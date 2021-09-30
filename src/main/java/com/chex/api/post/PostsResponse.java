package com.chex.api.post;

import com.chex.modules.post.model.PostView;

import java.util.List;

public class PostsResponse {

    List<PostView> posts;

    public PostsResponse(List<PostView> posts) {
        this.posts = posts;
    }

    public PostsResponse() {
    }

    public List<PostView> getPosts() {
        return posts;
    }

    public void setPosts(List<PostView> posts) {
        this.posts = posts;
    }
}
