package com.chex.api.post;

import com.chex.api.place.service.PlaceNameService;
import com.chex.modules.places.model.PlaceShortView;
import com.chex.modules.places.repository.PlaceRepository;
import com.chex.modules.post.model.Post;
import com.chex.modules.post.repository.CommentRepository;
import com.chex.modules.post.repository.PostRepository;
import com.chex.modules.post.model.PostView;
import com.chex.user.User;
import com.chex.user.UserRepository;
import com.chex.user.place.VisitedPlacesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class PostServiceTest {

    @Mock
    private PlaceRepository placeRepository;
    @Mock
    private PlaceNameService placeNameService;
    @Mock
    private VisitedPlacesRepository visitedPlacesRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;

    private MockMvc mockMvc;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postService).build();
    }

    @Test
    void get_single_post() {

        User user = new User(2L, "Jan", "Nowak");
        Post post = new Post();
        post.setId(1L);
        post.setDescription("super");
        post.setUserid(2L);
        post.setStanum(0);
        post.setPlaces("EU.POL.MLP.KRK.00001");
        post.setSubplaces("EU.POL.000.000.00000:EU.POL.MLP.000.00000:EU.POL.MLP.KRK.00000");

        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        Mockito.when(postRepository.findById(any())).thenReturn(Optional.of(post));
        Mockito.when(placeNameService.getName(post.getPlaces())).thenReturn("Wawel");

        Mockito.when(placeNameService.getName("EU.POL.MLP.KRK.00000")).thenReturn("Kraków");
        Mockito.when(placeNameService.getName("EU.POL.MLP.000.00000")).thenReturn("Malopolskie");
        Mockito.when(placeNameService.getName("EU.POL.000.000.00000")).thenReturn("Polska");

        PostView postView = postService.getPost(1L, user.getId());

        assertEquals(1L , postView.getId());
        assertEquals(0, postView.getStanum());
        assertEquals("super", postView.getDescription());
        assertEquals(2L, postView.getAuthorId());
        assertEquals("Jan Nowak", postView.getAuthorName());

        List<PlaceShortView> places = postView.getPlaces();
        assertEquals(1, places.size());
        assertEquals("Wawel", places.get(0).getName());
        assertEquals("EU.POL.MLP.KRK.00001", places.get(0).getId());

        List<PlaceShortView> subPlaces = postView.getSubPlaces();
        assertEquals(3, subPlaces.size());
        assertEquals("Kraków", subPlaces.get(0).getName());
        assertEquals("EU.POL.MLP.KRK.00000", subPlaces.get(0).getId());

        assertEquals("Malopolskie", subPlaces.get(1).getName());
        assertEquals("EU.POL.MLP.000.00000", subPlaces.get(1).getId());

        assertEquals("Polska", subPlaces.get(2).getName());
        assertEquals("EU.POL.000.000.00000", subPlaces.get(2).getId());
    }
}