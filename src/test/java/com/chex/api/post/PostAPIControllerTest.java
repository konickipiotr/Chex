package com.chex.api.post;

import com.chex.authentication.AccountStatus;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.config.GlobalSettings;
import com.chex.modules.places.model.Place;
import com.chex.modules.places.model.PlaceDescription;
import com.chex.modules.places.model.PlaceName;
import com.chex.modules.places.model.PlaceShortView;
import com.chex.modules.places.repository.PlaceDescriptionRepository;
import com.chex.modules.places.repository.PlaceNameRepository;
import com.chex.modules.places.repository.PlaceRepository;
import com.chex.modules.post.model.Comment;
import com.chex.modules.post.model.CommentView;
import com.chex.modules.post.model.Post;
import com.chex.modules.post.model.PostView;
import com.chex.modules.post.repository.CommentRepository;
import com.chex.modules.post.repository.PostRepository;
import com.chex.user.model.User;
import com.chex.user.repository.UserRepository;
import com.chex.user.repository.VisitedPlacesRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockUser(username="testuser")
@Transactional
class PostAPIControllerTest {

    private final MockMvc mockMvc;
    private final PlaceRepository placeRepository;
    private final VisitedPlacesRepository visitedPlacesRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PlaceNameRepository placeNameRepository;
    private final PlaceDescriptionRepository placeDescriptionRepository;
    private final ObjectMapper mapper = new ObjectMapper();
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PostAPIControllerTest(MockMvc mockMvc, PlaceRepository placeRepository, VisitedPlacesRepository visitedPlacesRepository, PostRepository postRepository, CommentRepository commentRepository, PlaceNameRepository placeNameRepository, PlaceDescriptionRepository placeDescriptionRepository, UserRepository userRepository, AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.mockMvc = mockMvc;
        this.placeRepository = placeRepository;
        this.visitedPlacesRepository = visitedPlacesRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.placeNameRepository = placeNameRepository;
        this.placeDescriptionRepository = placeDescriptionRepository;
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    void setUp() {
        this.placeRepository.deleteAll();
        this.userRepository.deleteAll();
        this.authRepository.deleteAll();
        this.visitedPlacesRepository.deleteAll();
        this.postRepository.deleteAll();
        this.commentRepository.deleteAll();
        this.placeNameRepository.deleteAll();
        this.placeDescriptionRepository.deleteAll();
    }

    @Test
    void get_post() throws Exception {
        Place nationalmuseum = new Place("EU.POL.DLS.WRO.00001");
        nationalmuseum.setLatitude(51.11056175173228);
        nationalmuseum.setLongitude(17.04744525485578);
        nationalmuseum.setRadius(70);
        nationalmuseum.setPoints(25);
        nationalmuseum.setRating(5);
        nationalmuseum.setVotesnum(3);
        this.placeRepository.save(nationalmuseum);

        Place wroclaw = new Place("EU.POL.DLS.WRO.00000");
        this.placeRepository.save(wroclaw);

        PlaceName placeName = new PlaceName(nationalmuseum.getId(), "Muzeum Narodowe", "National Museum");
        PlaceName placeName2 = new PlaceName(wroclaw.getId(), "Wrocław", "Wroclaw");
        PlaceDescription placeDescription = new PlaceDescription(nationalmuseum.getId(), "elo", "melo");
        PlaceDescription placeDescription2 = new PlaceDescription(nationalmuseum.getId(), "zz", "333");
        this.placeNameRepository.saveAll(Arrays.asList(placeName, placeName2));
        this.placeDescriptionRepository.saveAll(Arrays.asList(placeDescription, placeDescription2));

        Auth auth = new Auth("testuser", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);
        Auth auth1 = new Auth("testuser1", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);
        Auth auth2 = new Auth("testuser2", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);
        this.authRepository.saveAll(Arrays.asList(auth, auth1, auth2));

        User user = new User(auth.getId(), "Jan", "Nowak");
        User user1 = new User(auth1.getId(), "Adam", "Kaczka");
        User user2 = new User(auth2.getId(), "Jadwiga", "Lis");
        this.userRepository.saveAll(Arrays.asList(user, user1, user2));

        Post post = new Post();
        post.setUserid(user.getId());
        post.setDescription("super miejsce");
        post.setPlaces("EU.POL.DLS.WRO.00001");
        post.setSubplaces("EU.POL.DLS.WRO.00000");
        post.setPostvisibility(PostVisibility.PUBLIC);
        post.setStanum(4);
        this.postRepository.save(post);

        Comment c1 = new Comment(post.getId(), user.getId(), "xxxx", LocalDateTime.of(2020, 5, 20, 20, 30));
        Comment c2 = new Comment(post.getId(), user1.getId(), "gggg", LocalDateTime.of(2020, 5, 21, 20, 30));
        Comment c3 = new Comment(post.getId(), user2.getId(), "hhh", LocalDateTime.of(2020, 5, 19, 20, 30));
        Comment c4 = new Comment(post.getId(), GlobalSettings.USER_ACCOUNT_REMOVED, "zzz", LocalDateTime.of(2020, 5, 18, 20, 30));
        this.commentRepository.saveAll(Arrays.asList(c1, c2, c3, c4));

        MvcResult mvcResult = mockMvc.perform(get("/api/post/" + post.getId()))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        PostView postView = mapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertEquals(user.getId(), postView.getAuthorId());
        assertEquals(user.getName(), postView.getAuthorName());
        assertEquals("super miejsce", postView.getDescription());
        assertNotNull(postView.getCreatedAt());
        assertEquals(1, postView.getPlaces().size());
        assertEquals(1, postView.getSubPlaces().size());
        assertEquals(4, postView.getCommentViews().size());

        PlaceShortView placeShortView = postView.getPlaces().get(0);
        assertEquals("EU.POL.DLS.WRO.00001", placeShortView.getId());
        assertEquals("Muzeum Narodowe", placeShortView.getName());

        List<CommentView> cv = postView.getCommentViews();

        assertEquals(c2.getId(), cv.get(0).getId());
        assertEquals(user1.getId(), cv.get(0).getAuthorid());
        assertEquals("gggg", cv.get(0).getContent());

        assertEquals(c1.getId(), cv.get(1).getId());
        assertEquals(user.getId(), cv.get(1).getAuthorid());
        assertEquals("xxxx", cv.get(1).getContent());
        assertEquals(c3.getId(), cv.get(2).getId());
        assertEquals(user2.getId(), cv.get(2).getAuthorid());
        assertEquals("hhh", cv.get(2).getContent());
        assertEquals(c4.getId(), cv.get(3).getId());
        assertEquals(GlobalSettings.USER_ACCOUNT_REMOVED, cv.get(3).getAuthorid());
        assertEquals("zzz", cv.get(3).getContent());

    }
}