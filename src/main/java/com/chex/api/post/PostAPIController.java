package com.chex.api.post;

import com.chex.api.AuthService;
import com.chex.modules.post.model.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostAPIController {

    private final PostService postService;
    private final AuthService authService;

    @Autowired
    public PostAPIController(PostService postService, AuthService authService) {
        this.postService = postService;
        this.authService = authService;
    }

    @GetMapping("/{postid}")
    public ResponseEntity<PostView> getPost(@PathVariable("postid") Long postid, Principal principal){
        Long userid = authService.getUserId(principal);
        PostView postView = postService.getPost(postid, userid);
        return new ResponseEntity<>(postView, HttpStatus.OK);
    }

    @GetMapping("/public/{nr}")
    public ResponseEntity<PostsResponse> getPublicPosts(@PathVariable("nr") int nr, Principal principal){
        Long userid = authService.getUserId(principal);
        List<PostView> publicPost = postService.getPublicPost(userid, nr);
        return new ResponseEntity<>(new PostsResponse(publicPost), HttpStatus.OK);
    }
}
