package com.chex.api.post;

import com.chex.api.AuthService;
import com.chex.modules.post.model.Comment;
import com.chex.modules.post.model.PostView;
import com.chex.modules.post.model.Star;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping(value = "/comment/{id}")
    public ResponseEntity<Void> deletComment(@PathVariable("id") Long commentid){
        postService.removeComment(commentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<Void> addcomment(@RequestBody Comment comment, Principal principal){
        postService.addComment(comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long postid, Principal principal){
        Long userId = authService.getUserId(principal);
        postService.deletePost(postid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/changestar")
    public ResponseEntity<Boolean> isLiked(@RequestBody Star star){
        return new ResponseEntity<Boolean>(postService.changePostStar(star), HttpStatus.OK);
    }
}