package com.chex.api;

import com.chex.api.registration.ActivationCodeRepository;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.config.GlobalSettings;
import com.chex.modules.post.model.Comment;
import com.chex.modules.post.model.Post;
import com.chex.modules.post.repository.CommentRepository;
import com.chex.modules.post.repository.PostRepository;
import com.chex.modules.post.repository.StarRepository;
import com.chex.user.User;
import com.chex.user.UserRepository;
import com.chex.user.place.VisitedPlacesRepository;
import com.chex.files.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public class DeleteAccountService {
    private AuthRepository authRepository;
    private UserRepository userRepository;
    private ActivationCodeRepository activationCodeRepository;
    private VisitedPlacesRepository visitedPlacesRepository;//TODO
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private StarRepository starRepository;
    private FileService fileService;

    @Autowired
    public DeleteAccountService(com.chex.authentication.AuthRepository authRepository, UserRepository userRepository, ActivationCodeRepository activationCodeRepository, VisitedPlacesRepository visitedPlacesRepository, PostRepository postRepository, CommentRepository commentRepository, StarRepository starRepository, FileService fileService) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.activationCodeRepository = activationCodeRepository;
        this.visitedPlacesRepository = visitedPlacesRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.starRepository = starRepository;
        this.fileService = fileService;
    }

    public ResponseEntity<Void> deleteUserAccount(Long userid, Principal principal, boolean permanentlyDelete){
        Optional<Auth> oAuth = this.authRepository.findByUsername(principal.getName());
        Optional<Auth> oAuth2 = this.authRepository.findById(userid);
        if(oAuth.isEmpty() || oAuth2.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Auth current = oAuth.get();
        Auth toDelete = oAuth2.get();

        if(actionIsForbidden(current, toDelete))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        this.starRepository.deleteByUserid(userid);
        this.activationCodeRepository.deleteById(userid);

        User user = this.userRepository.findById(userid).get();
        fileService.deleteUserSpace(user);
        this.userRepository.deleteById(userid);

        List<Post> userPost = this.postRepository.findByUserid(userid);
        if(permanentlyDelete){
            for(Post p : userPost){
                List<Comment> postComments = this.commentRepository.findAllByPostidOrderByCreatedDesc(p.getId());
                this.commentRepository.deleteAll(postComments);
            }
            this.postRepository.deleteAll(userPost);
        }else {
            List<Comment> comments = commentRepository.findByAuthorid(userid);
            for(Comment c : comments){
                c.setAuthorid(GlobalSettings.USER_ACCOUNT_REMOVED);
            }
            this.commentRepository.saveAll(comments);

            List<Post> posts = postRepository.findByUserid(userid);
            for(Post p : posts){
                p.setUserid(GlobalSettings.USER_ACCOUNT_REMOVED);
            }
            this.postRepository.saveAll(posts);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean actionIsForbidden(Auth current, Auth toDelete){
        if(current.getRole().equals("USER")){
            if(!current.getId().equals(toDelete.getId()))
                return true;
        }

        return false;
    }
}
