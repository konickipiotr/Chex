package com.chex.api.post;

import com.chex.api.place.AchievedPlaceDTO;
import com.chex.api.place.service.PlaceNameService;
import com.chex.config.GlobalSettings;
import com.chex.files.FileType;
import com.chex.modules.category.CategoryRepository;
import com.chex.modules.places.model.Place;
import com.chex.modules.places.model.PlaceShortView;
import com.chex.modules.places.repository.PlaceRepository;
import com.chex.modules.post.model.*;
import com.chex.modules.post.repository.CommentRepository;
import com.chex.modules.post.repository.PostPhotoRepository;
import com.chex.modules.post.repository.PostRepository;
import com.chex.modules.post.repository.StarRepository;
import com.chex.user.User;
import com.chex.user.UserRepository;
import com.chex.user.place.VisitedPlacesRepository;
import com.chex.utils.DateUtils;
import com.chex.files.FileNameStruct;
import com.chex.files.FileService;
import com.chex.utils.IdUtils;
import com.chex.utils.exceptions.FailedSaveFileException;
import com.chex.utils.exceptions.PostNotFoundException;
import com.chex.utils.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {
    private final PlaceRepository placeRepository;
    private final PlaceNameService placeNameService;
    private final VisitedPlacesRepository visitedPlacesRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final StarRepository starRepository;
    private final FileService fileService;
    private final PostPhotoRepository photoRepository;
    private static final int PAGE_SIZE = 10;

    @Autowired
    public PostService(PlaceRepository placeRepository, PlaceNameService placeNameService, VisitedPlacesRepository visitedPlacesRepository, CategoryRepository categoryRepository, UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, StarRepository starRepository, FileService fileService, PostPhotoRepository photoRepository) {
        this.placeRepository = placeRepository;
        this.placeNameService = placeNameService;
        this.visitedPlacesRepository = visitedPlacesRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.starRepository = starRepository;
        this.fileService = fileService;
        this.photoRepository = photoRepository;
    }

    public void addNewPost(User user, AchievedPlaceDTO dto){
        Post post = new Post();
        post.setUserid(user.getId());
        post.setDescription(dto.getDescription());
        post.setCreated(dto.getTimestamp());
        post.setPlaces(IdUtils.placeIdsToString(dto.getAchievedPlaces().keySet()));
        post.setSubplaces(IdUtils.subplaceIdsToString(dto.getAchievedPlaces().keySet()));
        post.setPostvisibility(dto.getPostvisibility());
        this.postRepository.save(post);

        if(!dto.getSfiles().isEmpty())
            savePostFiles(post.getId(), user, dto.getSfiles());
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

        List<Star> stars = starRepository.findByPostid(postid);

        postView.setStanum(stars.size());
        boolean starSelected = false;
        for(Star s : stars){
            if(s.getUserid().equals(userid)){
                starSelected = true;
                break;
            }
        }
        postView.setVoted(starSelected);


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

        List<PostPhoto> photos = this.photoRepository.findByPostid(postid);
        postView.setPhotos(photos);

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

    public void removeComment(Long commentid){
        if(this.commentRepository.existsById(commentid)){
            this.commentRepository.deleteById(commentid);
        }
    }

    public void addComment(Comment comment){
        this.commentRepository.save(comment);
    }

    public void deletePost(Long postid){
        this.commentRepository.deleteByPostid(postid);
        this.postRepository.deleteById(postid);
        this.starRepository.deleteByPostid(postid);

        List<PostPhoto> postPhotos = this.photoRepository.findByPostid(postid);
        fileService.deletePostsPhotos(postPhotos);
        this.photoRepository.deleteAll(postPhotos);
    }

    public boolean changePostStar(Star star){
        if(starRepository.existsByPostidAndUserid(star.getPostid(), star.getUserid())){
            starRepository.deleteByPostidAndUserid(star.getPostid(), star.getUserid());
            return false;
        }else {
            starRepository.save(star);
            return true;
        }
    }

    public void savePostFiles(Long postid, User user, List<String> imagesStringBytes) {
        if(this.postRepository.existsById(postid)) {
            List<MultipartFile> multipartFiles = fileService.convertToMultipartFiles(imagesStringBytes);
            List<FileNameStruct> fileNameStructs = fileService.uploadPhotos(multipartFiles, user, FileType.POSTPHOTO);
            if (fileNameStructs == null || fileNameStructs.isEmpty())
                throw new FailedSaveFileException();

            for(FileNameStruct fns : fileNameStructs){
                PostPhoto photo = new PostPhoto(postid, user.getId());
                photo.setRealPath(fns.realPath);
                photo.setWebAppPath(fns.webAppPath);
                this.photoRepository.save(photo);
            }
        }
    }

}
