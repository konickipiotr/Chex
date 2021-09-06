package com.chex.api.post;

import com.chex.modules.post.model.PostView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsResponse {

    List<PostView> posts;
}
