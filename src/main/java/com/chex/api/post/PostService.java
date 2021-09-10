package com.chex.api.post;

import com.chex.api.place.AchievedPlaceDTO;
import com.chex.api.place.service.PlaceNameService;
import com.chex.config.GlobalSettings;
import com.chex.modules.category.CategoryRepository;
import com.chex.modules.places.model.Place;
import com.chex.modules.places.model.PlaceShortView;
import com.chex.modules.places.repository.PlaceRepository;
import com.chex.modules.post.model.Comment;
import com.chex.modules.post.model.CommentView;
import com.chex.modules.post.model.Post;
import com.chex.modules.post.repository.CommentRepository;
import com.chex.modules.post.repository.PostRepository;
import com.chex.modules.post.model.PostView;
import com.chex.user.User;
import com.chex.user.UserRepository;
import com.chex.user.place.VisitedPlacesRepository;
import com.chex.utils.DateUtils;
import com.chex.utils.IdUtils;
import com.chex.utils.exceptions.PostNotFoundException;
import com.chex.utils.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PlaceRepository placeRepository;
    private final PlaceNameService placeNameService;
    private final VisitedPlacesRepository visitedPlacesRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private static final int PAGE_SIZE = 10;

    @Autowired
    public PostService(PlaceRepository placeRepository, PlaceNameService placeNameService, VisitedPlacesRepository visitedPlacesRepository, CategoryRepository categoryRepository, UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.placeRepository = placeRepository;
        this.placeNameService = placeNameService;
        this.visitedPlacesRepository = visitedPlacesRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public void addNewPost(Long userid, AchievedPlaceDTO dto){
        Post post = new Post();
        post.setUserid(userid);
        post.setDescription(dto.getDescription());
        post.setCreated(dto.getTimestamp());
        post.setPlaces(IdUtils.placeIdsToString(dto.getAchievedPlaces().keySet()));
        post.setSubplaces(IdUtils.subplaceIdsToString(dto.getAchievedPlaces().keySet()));
        this.postRepository.save(post);
    }

    public List<PostView> getPublicPost(Long userid, int nr){
        List<PostView> postViewList = new ArrayList<>();
        List<Post> posts = this.postRepository.findAllOrderByCreated(PageRequest.of(0, PAGE_SIZE * nr));

        for(Post p :  posts){
            postViewList.add(getPost(p.getId(), userid));
        }
        return postViewList;
    }

    public PostView getPost(long postid, Long userid) {
        PostView postView = new PostView();

        Optional<Post> oPost = this.postRepository.findById(postid);
        if(oPost.isEmpty())
            throw new PostNotFoundException();

        Post post = oPost.get();
        postView.setId(post.getId());
        postView.setDescription(post.getDescription());
        postView.setCreatedAt(DateUtils.getNiceDate(post.getCreated()));
        postView.setStanum(post.getStanum());


        if(post.getUserid() == GlobalSettings.USER_ACCOUNT_REMOVED){
            postView.setAuthorName("UNKNOWN");
            postView.setAuthorId(GlobalSettings.USER_ACCOUNT_REMOVED);
        }else {
            Optional<User> oUser = this.userRepository.findById(post.getUserid());
            if(oUser.isEmpty())
                throw new UserNotFoundException(post.getUserid());
            User user = oUser.get();
            postView.setAuthorName(user.getName());
            postView.setAuthorId(user.getId());
            postView.setAuthor(userid.equals(post.getUserid()));
            postView.setAuthorPhoto(user.getImgurl());
        }

        postView.setPlaces(getShortPlaces(post.getPlaces()));
        postView.setSubPlaces(getShortPlaces(post.getSubplaces()));
        postView.setCommentViews(getComments(postid, userid));

        return postView;
    }

    private List<PlaceShortView> getShortPlaces(String ids){
        List<PlaceShortView> plist = new ArrayList<>();
        for(String id : IdUtils.idsToList(ids)){
            String name = this.placeNameService.getName(id);
            Place place = this.placeRepository.getById(id);
            PlaceShortView placeShortView = new PlaceShortView(id, name);
            placeShortView.setImgUrl(place.getImgurl());
            plist.add(placeShortView);
        }

        Collections.sort(plist);
        return plist;
    }

    private List<CommentView> getComments(Long postid, Long userid){
        List<Comment> comments = this.commentRepository.findAllByPostidOrderByCreatedDesc(postid);
        List<CommentView> list = new ArrayList<>();
        for(Comment c : comments){
            list.add(prepareComment(c, userid));
        }
        return list;
    }

    private CommentView prepareComment(Comment comment, Long userid){
        CommentView cv = new CommentView();
        cv.setId(comment.getId());
        cv.setPostid(comment.getPostid());
        cv.setContent(comment.getContent());
        cv.setCreatedAt(DateUtils.getNiceDate(comment.getCreated()));


        if(comment.getAuthorid() == GlobalSettings.USER_ACCOUNT_REMOVED){
            cv.setAuthorid(GlobalSettings.USER_ACCOUNT_REMOVED);
            cv.setAuthorName("UNKNOWN");
        }else {
            Optional<User> oUser = this.userRepository.findById(comment.getAuthorid());
            if(oUser.isEmpty())
                throw new UserNotFoundException(comment.getAuthorid());
            User user = oUser.get();
            cv.setAuthorName(user.getName());
            cv.setAuthorid(user.getId());
            cv.setAuthor(userid.equals(comment.getAuthorid()));
            cv.setAuthorPhoto(user.getImgurl());
        }

        return cv;
    }

}
