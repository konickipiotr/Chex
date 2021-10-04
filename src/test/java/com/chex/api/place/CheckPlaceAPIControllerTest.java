package com.chex.api.place;

import com.chex.api.AuthService;
import com.chex.api.place.response.CheckPlaceResponse;
import com.chex.api.place.response.CheckPlaceResponseStatus;
import com.chex.api.place.service.PlaceNameService;
import com.chex.authentication.AccountStatus;
import com.chex.authentication.Auth;
import com.chex.modules.CheckPlaceView;
import com.chex.modules.places.model.Place;
import com.chex.modules.places.repository.PlaceRepository;
import com.chex.modules.post.model.Post;
import com.chex.modules.post.repository.PostRepository;
import com.chex.user.model.User;
import com.chex.user.model.VisitedPlace;
import com.chex.user.repository.VisitedPlacesRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockUser(username="user1")
@Transactional
class CheckPlaceAPIControllerTest {

    private final MockMvc mockMvc;
    private final PlaceRepository placeRepository;
    private final VisitedPlacesRepository visitedPlacesRepository;
    private final PostRepository postRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private AuthService authService;
    @MockBean
    private PlaceNameService placeNameService;

    Place europa = new Place("EU.000.000.000.00000");
    Place northAmerica = new Place("NA.000.000.000.00000");

    Place poland = new Place("EU.POL.000.000.00000");
    Place germany = new Place("EU.GER.000.000.00000");

    Place dolnoslaskie = new Place("EU.POL.DLS.000.00000");
    Place opolskie = new Place("EU.POL.OPL.000.00000");

    Place region = new Place("EU.POL.DLS.REG.00000");
    Place wroclaw = new Place("EU.POL.DLS.WRO.00000");

    Place nationalmuseum = new Place("EU.POL.DLS.WRO.00001");
    Place odrariver = new Place("EU.POL.DLS.WRO.00002");
    Place fredrostatue = new Place("EU.POL.DLS.WRO.00003");

    Auth auth = new Auth("jan", "nowak", "11", AccountStatus.ACTIVE);
    User user = new User();

    @Autowired
    public CheckPlaceAPIControllerTest(MockMvc mockMvc, PlaceRepository placeRepository, VisitedPlacesRepository visitedPlacesRepository, PostRepository postRepository) {
        this.mockMvc = mockMvc;
        this.placeRepository = placeRepository;
        this.visitedPlacesRepository = visitedPlacesRepository;
        this.postRepository = postRepository;
    }

    @BeforeEach
    void setUp() {
        this.placeRepository.deleteAll();
        this.visitedPlacesRepository.deleteAll();
        this.postRepository.deleteAll();

        nationalmuseum.setLatitude(51.11056175173228);
        nationalmuseum.setLongitude(17.04744525485578);
        nationalmuseum.setRadius(70);
        nationalmuseum.setPoints(25);

        odrariver.setLatitude(51.111252368679075);
        odrariver.setLongitude(17.048359420006754);
        odrariver.setPoints(15);
        odrariver.setRadius(70);

        fredrostatue.setLatitude(51.10967218306398);
        fredrostatue.setLongitude(17.03130839352053);
        fredrostatue.setRadius(20);
        fredrostatue.setPoints(30);

        mapper.registerModule(new JavaTimeModule());
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        this.placeRepository.saveAll(Arrays.asList(europa, northAmerica, poland, germany, dolnoslaskie, opolskie, region, wroclaw, nationalmuseum, odrariver, fredrostatue));
        auth.setId(999L);
        user.setId(auth.getId());
    }

    @Test
    void there_is_no_place_in_the_area_and_return_not_found() throws Exception {
        Mockito.when(authService.getUserId(any())).thenReturn(user.getId());

        String contentAsString = mockMvc.perform(get("/api/checkplace?latitude=51.10938588760097&longitude=17.04738960378325"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        CheckPlaceResponse response = mapper.readValue(contentAsString, new TypeReference<>() {});
        assertEquals(CheckPlaceResponseStatus.NOTFOUND, response.getResponseStatus());
        assertNull(response.getCheckPlaceViewList());
    }

    @Test
    void user_is_in_correct_place() throws Exception {

        Mockito.when(placeNameService.getName(any(String.class))).thenReturn("XX");
        Mockito.when(placeNameService.getDescription(any(String.class))).thenReturn("YY");
        Mockito.when(authService.getUserId(any())).thenReturn(user.getId());

        MvcResult mvcResult = mockMvc.perform(get("/api/checkplace?latitude=51.109558190965856&longitude=17.031288094382667"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        CheckPlaceResponse response = mapper.readValue(contentAsString, new TypeReference<>() {});
        List<CheckPlaceView> list = response.getCheckPlaceViewList();

        assertEquals(CheckPlaceResponseStatus.FOUND, response.getResponseStatus());
        assertEquals(5, list.size());
        assertEquals("EU.000.000.000.00000", list.get(4).getId());
        assertEquals("EU.POL.000.000.00000", list.get(3).getId());
        assertEquals("EU.POL.DLS.000.00000", list.get(2).getId());
        assertEquals("EU.POL.DLS.WRO.00000", list.get(1).getId());
        assertEquals("EU.POL.DLS.WRO.00003", list.get(0).getId());
    }

    @Test
    void add_places_to_visitedPlaces() throws Exception {
        Map<String, Integer> achievedPlaces = new HashMap<>();
        achievedPlaces.put("EU.POL.DLS.WRO.00003", 4);
        achievedPlaces.put("EU.POL.DLS.WRO.00000", 3);
        achievedPlaces.put("EU.POL.DLS.000.00000", 5);
        achievedPlaces.put("EU.POL.000.000.00000", 1);
        achievedPlaces.put("EU.000.000.000.00000", 2);
        AchievedPlaceDTO dto = new AchievedPlaceDTO();
        dto.setAchievedPlaces(achievedPlaces);
        dto.setDescription("");
        dto.setTimestamp(LocalDateTime.now());

        Mockito.when(authService.getUser(any())).thenReturn(user);

        mockMvc.perform(post("/api/checkplace/finalize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk()).andReturn();

        List<VisitedPlace> all = this.visitedPlacesRepository.findAll();
        assertEquals(5, all.size());

        assertEquals(3, this.placeRepository.getById("EU.POL.DLS.WRO.00000").getRating());
    }

    @Test
    void user_is_already_been_there_return_empty_list() throws Exception {
        Map<String, Integer> achievedPlaces = new HashMap<>();
        achievedPlaces.put("EU.POL.DLS.WRO.00003", 4);
        achievedPlaces.put("EU.POL.DLS.WRO.00000", 3);
        achievedPlaces.put("EU.POL.DLS.000.00000", 5);
        achievedPlaces.put("EU.POL.000.000.00000", 1);
        achievedPlaces.put("EU.000.000.000.00000", 2);
        AchievedPlaceDTO dto = new AchievedPlaceDTO();
        dto.setAchievedPlaces(achievedPlaces);
        dto.setDescription("");
        dto.setTimestamp(LocalDateTime.now());


        Mockito.when(placeNameService.getName(any(String.class))).thenReturn("XX");
        Mockito.when(placeNameService.getDescription(any(String.class))).thenReturn("YY");

        Mockito.when(authService.getUser(any())).thenReturn(user);

        mockMvc.perform(post("/api/checkplace/finalize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        Mockito.when(authService.getUserId(any())).thenReturn(user.getId());

        MvcResult mvcResult = mockMvc.perform(get("/api/checkplace?latitude=51.109558190965856&longitude=17.031288094382667"))
                .andExpect(status().isOk()).andReturn();


        String contentAsString = mvcResult.getResponse().getContentAsString();
        CheckPlaceResponse response = mapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertEquals(CheckPlaceResponseStatus.ALREADYEXISTS, response.getResponseStatus());
        assertNull(response.getCheckPlaceViewList());
    }

    @Test
    void user_already_has_been_in_other_place_in_the_same_area() throws Exception {
        Map<String, Integer> achievedPlaces = new HashMap<>();
        achievedPlaces.put("EU.POL.DLS.WRO.00002", 4);
        achievedPlaces.put("EU.POL.DLS.WRO.00000", 3);
        achievedPlaces.put("EU.POL.DLS.000.00000", 5);
        achievedPlaces.put("EU.POL.000.000.00000", 1);
        achievedPlaces.put("EU.000.000.000.00000", 2);
        AchievedPlaceDTO dto = new AchievedPlaceDTO();
        dto.setAchievedPlaces(achievedPlaces);
        dto.setDescription("");
        dto.setTimestamp(LocalDateTime.now());

        Mockito.when(placeNameService.getName(any(String.class))).thenReturn("XX");
        Mockito.when(placeNameService.getDescription(any(String.class))).thenReturn("YY");
        Mockito.when(authService.getUser(any())).thenReturn(user);

        mockMvc.perform(post("/api/checkplace/finalize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        Mockito.when(authService.getUserId(any())).thenReturn(user.getId());
        MvcResult mvcResult = mockMvc.perform(get("/api/checkplace?latitude=51.110469965599435&longitude=17.047374508810307"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        CheckPlaceResponse response = mapper.readValue(contentAsString, new TypeReference<>() {});
        List<CheckPlaceView> list = response.getCheckPlaceViewList();

        assertEquals(CheckPlaceResponseStatus.FOUND, response.getResponseStatus());
        assertEquals(1, list.size());
        assertEquals("EU.POL.DLS.WRO.00001", list.get(0).getId());
    }

    @Test
    void there_are_two_places_in_the_user_range() throws Exception {

        Mockito.when(placeNameService.getName(any(String.class))).thenReturn("XX");
        Mockito.when(placeNameService.getDescription(any(String.class))).thenReturn("YY");

        Mockito.when(authService.getUserId(any())).thenReturn(user.getId());
        MvcResult mvcResult = mockMvc.perform(get("/api/checkplace?latitude=51.11071245329221&longitude=17.04830791753398"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        CheckPlaceResponse response = mapper.readValue(contentAsString, new TypeReference<>() {});

        assertEquals(CheckPlaceResponseStatus.FOUND, response.getResponseStatus());

        List<CheckPlaceView> list = response.getCheckPlaceViewList();
        assertEquals(6, list.size());

        assertEquals("EU.000.000.000.00000", list.get(5).getId());
        assertEquals("EU.POL.000.000.00000", list.get(4).getId());
        assertEquals("EU.POL.DLS.000.00000", list.get(3).getId());
        assertEquals("EU.POL.DLS.WRO.00000", list.get(2).getId());
        assertEquals("EU.POL.DLS.WRO.00001", list.get(1).getId());
        assertEquals("EU.POL.DLS.WRO.00002", list.get(0).getId());


        Map<String, Integer> achievedPlaces = new HashMap<>();
        achievedPlaces.put("EU.POL.DLS.WRO.00001", 2);
        achievedPlaces.put("EU.POL.DLS.WRO.00002", 4);
        achievedPlaces.put("EU.POL.DLS.WRO.00000", 3);
        achievedPlaces.put("EU.POL.DLS.000.00000", 5);
        achievedPlaces.put("EU.POL.000.000.00000", 1);
        achievedPlaces.put("EU.000.000.000.00000", 2);
        AchievedPlaceDTO dto = new AchievedPlaceDTO();
        dto.setAchievedPlaces(achievedPlaces);
        dto.setDescription("elo");
        dto.setTimestamp(LocalDateTime.now());


        Mockito.when(placeNameService.getName(any(String.class))).thenReturn("XX");
        Mockito.when(placeNameService.getDescription(any(String.class))).thenReturn("YY");
        Mockito.when(authService.getUser(any())).thenReturn(user);

        mockMvc.perform(post("/api/checkplace/finalize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        List<VisitedPlace> allV = this.visitedPlacesRepository.findAll();
        assertEquals(6, allV.size());

        List<Post> allP = postRepository.findAll();
        assertEquals(1, allP.size());

        Post post = allP.get(0);
        assertEquals(999L, post.getUserid());
        assertEquals("elo", post.getDescription());
        assertEquals("EU.POL.DLS.WRO.00001:EU.POL.DLS.WRO.00002", post.getPlaces());
        assertEquals("EU.000.000.000.00000:EU.POL.000.000.00000:EU.POL.DLS.000.00000:EU.POL.DLS.WRO.00000", post.getSubplaces());

    }
}
