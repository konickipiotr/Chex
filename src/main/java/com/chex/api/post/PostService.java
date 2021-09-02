package com.chex.api.post;

import com.chex.api.place.AchievedPlaceDTO;
import com.chex.api.place.service.PlaceNameService;
import com.chex.modules.category.CategoryRepository;
import com.chex.modules.places.repository.PlaceRepository;
import com.chex.modules.post.Post;
import com.chex.modules.post.PostRepository;
import com.chex.user.UserRepository;
import com.chex.user.place.VisitedPlacesRepository;
import com.chex.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PlaceRepository placeRepository;
    private final PlaceNameService placeNameService;
    private final VisitedPlacesRepository visitedPlacesRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostService(PlaceRepository placeRepository, PlaceNameService placeNameService, VisitedPlacesRepository visitedPlacesRepository, CategoryRepository categoryRepository, UserRepository userRepository, PostRepository postRepository) {
        this.placeRepository = placeRepository;
        this.placeNameService = placeNameService;
        this.visitedPlacesRepository = visitedPlacesRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public void addNewPost(Long userid, AchievedPlaceDTO dto){
        Post post = new Post();
        post.setUserid(userid);
        post.setDescription(dto.getDescription());
        post.setTimestamp(dto.getTimestamp());
        post.setPlaces(IdUtils.placeIdsToString(dto.getAchievedPlaces().keySet()));
        post.setSubplaces(IdUtils.subplaceIdsToString(dto.getAchievedPlaces().keySet()));
        this.postRepository.save(post);
    }
}
