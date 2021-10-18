package com.chex.api.place;

import com.chex.api.place.response.CheckPlaceRequest;
import com.chex.api.place.response.CheckPlaceResponse;
import com.chex.api.place.response.CheckPlaceResponseStatus;
import com.chex.authentication.AccountStatus;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.modules.challenges.ChallengeLevel;
import com.chex.modules.challenges.CheckpointType;
import com.chex.modules.challenges.model.Challenge;
import com.chex.modules.challenges.model.ChallengeDescription;
import com.chex.modules.challenges.model.ChallengeName;
import com.chex.modules.challenges.model.ChallengePoint;
import com.chex.modules.challenges.repository.ChallengeDescriptionRepository;
import com.chex.modules.challenges.repository.ChallengeNameRepository;
import com.chex.modules.challenges.repository.ChallengePointRepository;
import com.chex.modules.challenges.repository.ChallengeRepository;
import com.chex.modules.challenges.view.ChallengeShortView;
import com.chex.modules.challenges.view.CheckpointView;
import com.chex.modules.places.model.Coords;
import com.chex.modules.places.model.Place;
import com.chex.modules.places.model.PlaceDescription;
import com.chex.modules.places.model.PlaceName;
import com.chex.modules.places.repository.PlaceDescriptionRepository;
import com.chex.modules.places.repository.PlaceNameRepository;
import com.chex.modules.places.repository.PlaceRepository;
import com.chex.user.ChallengeStatus;
import com.chex.user.model.User;
import com.chex.user.model.UserChallenge;
import com.chex.user.model.UserChallengePoint;
import com.chex.user.repository.UserChallengePointRepository;
import com.chex.user.repository.UserChallengeRepository;
import com.chex.user.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockUser(username="testuser")
@Transactional
class CheckPlaceAPIControllerChallengesIntegrationTest {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper mapper;
    private final ChallengeRepository challengeRepository;
    private final ChallengePointRepository challengePointRepository;
    private final ChallengeNameRepository challengeNameRepository;
    private final ChallengeDescriptionRepository challengeDescriptionRepository;
    private final PlaceRepository placeRepository;
    private final PlaceNameRepository placeNameRepository;
    private final PlaceDescriptionRepository placeDescriptionRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserChallengePointRepository userChallengePointRepository;

    private User user;
    private Challenge challenge;
    private ChallengePoint p1, p2, p3;

    @Autowired
    public CheckPlaceAPIControllerChallengesIntegrationTest(MockMvc mockMvc, UserRepository userRepository, AuthRepository authRepository, PasswordEncoder passwordEncoder, ObjectMapper mapper, ChallengeRepository challengeRepository, ChallengePointRepository challengePointRepository, ChallengeNameRepository challengeNameRepository, ChallengeDescriptionRepository challengeDescriptionRepository, PlaceRepository placeRepository, PlaceNameRepository placeNameRepository, PlaceDescriptionRepository placeDescriptionRepository, UserChallengeRepository userChallengeRepository, UserChallengePointRepository userChallengePointRepository) {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.challengeRepository = challengeRepository;
        this.challengePointRepository = challengePointRepository;
        this.challengeNameRepository = challengeNameRepository;
        this.challengeDescriptionRepository = challengeDescriptionRepository;
        this.placeRepository = placeRepository;
        this.placeNameRepository = placeNameRepository;
        this.placeDescriptionRepository = placeDescriptionRepository;
        this.userChallengeRepository = userChallengeRepository;
        this.userChallengePointRepository = userChallengePointRepository;
    }

    @BeforeEach
    void setUp() {
        this.authRepository.deleteAll();
        this.userRepository.deleteAll();
        this.challengeRepository.deleteAll();
        this.challengePointRepository.deleteAll();
        this.challengeNameRepository.deleteAll();
        this.challengeDescriptionRepository.deleteAll();
        this.userChallengeRepository.deleteAll();
        this.userChallengePointRepository.deleteAll();
        this.placeRepository.deleteAll();
        this.placeNameRepository.deleteAll();
        this.placeDescriptionRepository.deleteAll();

        Auth authUser = new Auth("testuser", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);
        this.authRepository.save(authUser);
        user = new User(authUser.getId(), "Jan", "Nowak");
        this.userRepository.save(user);

        Place europa = new Place("EU.000.000.000.00000");
        Place poland = new Place("EU.POL.000.000.00000");
        Place dolnoslaskie = new Place("EU.POL.DLS.000.00000");
        Place region = new Place("EU.POL.DLS.REG.00000");
        Place wroclaw = new Place("EU.POL.DLS.WRO.00000");
        this.placeRepository.saveAll(Arrays.asList(europa, poland, dolnoslaskie, region, wroclaw));
        this.placeNameRepository.save(new PlaceName(europa.getId(), "Europa", "Europa"));
        this.placeNameRepository.save(new PlaceName(poland.getId(), "Polska", "Polska"));
        this.placeNameRepository.save(new PlaceName(dolnoslaskie.getId(), "Dolnośląskie", "Dolnośląskie"));
        this.placeNameRepository.save(new PlaceName(wroclaw.getId(), "Wrocław", "Wrocław"));
        this.placeDescriptionRepository.save(new PlaceDescription(europa.getId(), "Europa", "Europa"));
        this.placeDescriptionRepository.save(new PlaceDescription(poland.getId(), "Polska", "Polska"));
        this.placeDescriptionRepository.save(new PlaceDescription(dolnoslaskie.getId(), "Dolnośląskie", "Dolnośląskie"));
        this.placeDescriptionRepository.save(new PlaceDescription(wroclaw.getId(), "Wrocław", "Wrocław"));

        Place place = new Place();
        place.setId("EU.POL.DLS.WRO.RRRRR");
        place.setLatitude(51.10965388954731);
        place.setLongitude(17.031302771469175);
        place.setRadius(20);
        place.setPoints(30);
        this.placeRepository.save(place);
        this.placeNameRepository.save(new PlaceName(place.getId(), "Pomnik Fredry", "Pomnik Fredry"));
        this.placeDescriptionRepository.save(new PlaceDescription(place.getId(), "Pomnik Fredry", "Pomnik Fredry"));

        challenge = new Challenge();
        challenge.setPoints(50);
        challenge.setTimelimit(10 * 1000);
        challenge.setLevel(ChallengeLevel.HARD);
        challenge.setImg("zz");
        challenge.setCategories("vv");
        this.challengeRepository.save(challenge);
        this.challengeNameRepository.save(new ChallengeName(challenge.getId(), "Wyzwanie rynkowe", "Wyzwanie rynkowe"));
        this.challengeDescriptionRepository.save(new ChallengeDescription(challenge.getId(), "cześć", "hello"));

        p1 = new ChallengePoint();
        p1.setChallengeid(challenge.getId());
        p1.setSeq(0);
        p1.setName("START - Iglica");
        p1.setLatitude(51.109415619694644);
        p1.setLongitude(17.029433852049866);
        p1.setRadius(20);
        p1.setCheckpointtype(CheckpointType.START);
        p1.setIsplace(false);

        p2 = new ChallengePoint();
        p2.setChallengeid(challenge.getId());
        p2.setSeq(1);
        p2.setIsplace(true);
        p2.setPlaceid(place.getId());
        p2.setCheckpointtype(CheckpointType.CHECKPOINT);

        p3 = new ChallengePoint();
        p3.setChallengeid(challenge.getId());
        p3.setSeq(2);
        p3.setName("END - Pregierz");
        p3.setLatitude(51.10944148170755);
        p3.setLongitude(17.032670713640492);
        p3.setRadius(20);
        p3.setIsplace(false);
        p3.setCheckpointtype(CheckpointType.FINISH);
        this.challengePointRepository.saveAll(Arrays.asList(p1, p2, p3));

        UserChallenge userChallenge = new UserChallenge();
        userChallenge.setUserid(user.getId());
        userChallenge.setStatus(ChallengeStatus.NOTSTARTED);
        userChallenge.setChallengeid(challenge.getId());
        userChallenge.setAttemptnum(1);
        userChallenge.setStartTime(LocalDateTime.now());
        this.userChallengeRepository.save(userChallenge);

        UserChallengePoint uPoint1 = new UserChallengePoint(userChallenge.getId(), p1, user.getId());
        UserChallengePoint uPoint2 = new UserChallengePoint(userChallenge.getId(), p2, user.getId());
        UserChallengePoint uPoint3 = new UserChallengePoint(userChallenge.getId(), p3, user.getId());
        this.userChallengePointRepository.saveAll(Arrays.asList(uPoint1, uPoint2, uPoint3));
    }

    @Test
    void user_is_in_the_start_point() throws Exception {

        CheckPlaceRequest request = new CheckPlaceRequest();
        request.setCoords(new Coords(51.109415619694644, 17.029433852049866));
        request.setTimestamp(LocalDateTime.now());

        String contentAsString = mockMvc.perform(post("/api/checkplace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        CheckPlaceResponse response = mapper.readValue(contentAsString, new TypeReference<>() {});
        assertEquals(CheckPlaceResponseStatus.FOUND, response.getResponseStatus());

        assertTrue(response.getUserShortChallengeViews().isEmpty());
        assertTrue(response.getCheckPlaceViewList().isEmpty());
        assertTrue(response.getAchievementShortViews().isEmpty());

        List<CheckpointView> checkpointViewList = response.getCheckpointViewList();
        assertEquals(1, checkpointViewList.size());

        CheckpointView view = checkpointViewList.get(0);
        assertEquals("Wyzwanie rynkowe", view.getChallengeName());
        assertEquals(CheckpointType.START, view.getCheckpointtype());
        assertEquals("START - Iglica", view.getName());
        assertEquals(51.109415619694644, view.getLatitude());
        assertEquals(17.029433852049866, view.getLongitude());
        assertEquals(20, view.getRadius());
        assertEquals(ChallengeStatus.COMPETED, view.getStatus());
        assertEquals(0, view.getSeq());
        assertTrue(view.getReachedat().isBefore(LocalDateTime.now()) && view.getReachedat().isAfter(request.getTimestamp()));

        UserChallenge userChallenge = this.userChallengeRepository.findAll().get(0);
        assertEquals(ChallengeStatus.ONGOING, userChallenge.getStatus());

        List<UserChallengePoint> uPoints = this.userChallengePointRepository.findByUserchallengeid(userChallenge.getId());

        UserChallengePoint start = uPoints.stream().filter(i -> i.getCheckpointtype().equals(CheckpointType.START)).findFirst().get();
        UserChallengePoint middle = uPoints.stream().filter(i -> i.getCheckpointtype().equals(CheckpointType.CHECKPOINT)).findFirst().get();
        UserChallengePoint end = uPoints.stream().filter(i -> i.getCheckpointtype().equals(CheckpointType.FINISH)).findFirst().get();

        assertEquals(ChallengeStatus.COMPETED, start.getStatus());
        assertEquals(view.getReachedat(), start.getReachedat());

        assertEquals(ChallengeStatus.ONGOING, middle.getStatus());
        assertEquals(ChallengeStatus.NOTSTARTED, end.getStatus());
    }

    @Test
    void user_is_in_the_second_point() throws Exception {

        CheckPlaceRequest request = new CheckPlaceRequest();
        request.setCoords(new Coords(51.109415619694644, 17.029433852049866));
        request.setTimestamp(LocalDateTime.now());

        mockMvc.perform(post("/api/checkplace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Thread.sleep(2000);
        request = new CheckPlaceRequest();
        request.setCoords(new Coords(51.10965388954731, 17.031302771469175));
        request.setTimestamp(LocalDateTime.now());

        String contentAsString = mockMvc.perform(post("/api/checkplace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        CheckPlaceResponse response = mapper.readValue(contentAsString, new TypeReference<>() {});
        assertEquals(CheckPlaceResponseStatus.FOUND, response.getResponseStatus());

        assertTrue(response.getUserShortChallengeViews().isEmpty());
        assertEquals(5, response.getCheckPlaceViewList().size());
        assertTrue(response.getAchievementShortViews().isEmpty());

        List<CheckpointView> checkpointViewList = response.getCheckpointViewList();
        assertEquals(1, checkpointViewList.size());

        CheckpointView view = checkpointViewList.get(0);
        assertEquals("Wyzwanie rynkowe", view.getChallengeName());
        assertEquals(CheckpointType.CHECKPOINT, view.getCheckpointtype());
        assertEquals("Pomnik Fredry", view.getName());
        assertEquals(51.10965388954731, view.getLatitude());
        assertEquals(17.031302771469175, view.getLongitude());
        assertEquals(20, view.getRadius());
        assertEquals(ChallengeStatus.COMPETED, view.getStatus());
        assertEquals(1, view.getSeq());
        assertTrue(view.getReachedat().isBefore(LocalDateTime.now()) && view.getReachedat().isAfter(request.getTimestamp()));

        UserChallenge userChallenge = this.userChallengeRepository.findAll().get(0);
        assertEquals(ChallengeStatus.ONGOING, userChallenge.getStatus());

        List<UserChallengePoint> uPoints = this.userChallengePointRepository.findByUserchallengeid(userChallenge.getId());

        UserChallengePoint start = uPoints.stream().filter(i -> i.getCheckpointtype().equals(CheckpointType.START)).findFirst().get();
        UserChallengePoint middle = uPoints.stream().filter(i -> i.getCheckpointtype().equals(CheckpointType.CHECKPOINT)).findFirst().get();
        UserChallengePoint end = uPoints.stream().filter(i -> i.getCheckpointtype().equals(CheckpointType.FINISH)).findFirst().get();

        assertEquals(ChallengeStatus.COMPETED, start.getStatus());
        assertEquals(ChallengeStatus.COMPETED, middle.getStatus());
        assertEquals(ChallengeStatus.ONGOING, end.getStatus());
    }

    @Test
    void user_is_in_the_last_point() throws Exception {

        CheckPlaceRequest request = new CheckPlaceRequest();
        request.setCoords(new Coords(51.109415619694644, 17.029433852049866));
        request.setTimestamp(LocalDateTime.now());

        mockMvc.perform(post("/api/checkplace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Thread.sleep(2000);
        request = new CheckPlaceRequest();
        request.setCoords(new Coords(51.10965388954731, 17.031302771469175));
        request.setTimestamp(LocalDateTime.now());

        mockMvc.perform(post("/api/checkplace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Thread.sleep(2000);
        request = new CheckPlaceRequest();
        request.setCoords(new Coords(51.10944148170755, 17.032670713640492));
        request.setTimestamp(LocalDateTime.now());


        String contentAsString = mockMvc.perform(post("/api/checkplace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        CheckPlaceResponse response = mapper.readValue(contentAsString, new TypeReference<>() {});
        assertEquals(CheckPlaceResponseStatus.FOUND, response.getResponseStatus());

        assertEquals(1, response.getUserShortChallengeViews().size());
        assertTrue(response.getCheckPlaceViewList().isEmpty());
        assertTrue(response.getAchievementShortViews().isEmpty());

        List<CheckpointView> checkpointViewList = response.getCheckpointViewList();
        assertEquals(1, checkpointViewList.size());

        CheckpointView view = checkpointViewList.get(0);
        assertEquals("Wyzwanie rynkowe", view.getChallengeName());
        assertEquals(CheckpointType.FINISH, view.getCheckpointtype());
        assertEquals("END - Pregierz", view.getName());
        assertEquals(51.10944148170755, view.getLatitude());
        assertEquals(17.032670713640492, view.getLongitude());
        assertEquals(20, view.getRadius());
        assertEquals(ChallengeStatus.COMPETED, view.getStatus());
        assertEquals(2, view.getSeq());
        assertTrue(view.getReachedat().isBefore(LocalDateTime.now()) && view.getReachedat().isAfter(request.getTimestamp()));

        UserChallenge userChallenge = this.userChallengeRepository.findAll().get(0);
        assertEquals(ChallengeStatus.ONGOING, userChallenge.getStatus());

        List<UserChallengePoint> uPoints = this.userChallengePointRepository.findByUserchallengeid(userChallenge.getId());

        UserChallengePoint start = uPoints.stream().filter(i -> i.getCheckpointtype().equals(CheckpointType.START)).findFirst().get();
        UserChallengePoint middle = uPoints.stream().filter(i -> i.getCheckpointtype().equals(CheckpointType.CHECKPOINT)).findFirst().get();
        UserChallengePoint end = uPoints.stream().filter(i -> i.getCheckpointtype().equals(CheckpointType.FINISH)).findFirst().get();


        ChallengeShortView challengeShortView = response.getUserShortChallengeViews().get(0);
        assertEquals(challenge.getId(), challengeShortView.getId());
        assertEquals(ChallengeLevel.HARD, challengeShortView.getLevel());
        assertEquals("Wyzwanie rynkowe", challengeShortView.getName());
        assertEquals(0, challengeShortView.getUsersComplete());
        assertEquals("zz", challengeShortView.getImg());
        assertEquals("vv", challengeShortView.getCategory());
        assertEquals(start.getUserchallengeid(), challengeShortView.getUserchallengeid());

        assertEquals(ChallengeStatus.COMPETED, start.getStatus());
        assertEquals(ChallengeStatus.COMPETED, middle.getStatus());
        assertEquals(ChallengeStatus.ONGOING, end.getStatus());
    }

    @Test
    void finalize_challenge() throws Exception {

        LocalDateTime startTime = LocalDateTime.now();
        CheckPlaceRequest request = new CheckPlaceRequest();
        request.setCoords(new Coords(51.109415619694644, 17.029433852049866));
        request.setTimestamp(startTime);

        mockMvc.perform(post("/api/checkplace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Thread.sleep(2000);
        request = new CheckPlaceRequest();
        request.setCoords(new Coords(51.10965388954731, 17.031302771469175));
        request.setTimestamp(LocalDateTime.now());

        mockMvc.perform(post("/api/checkplace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Thread.sleep(2000);
        request = new CheckPlaceRequest();
        request.setCoords(new Coords(51.10944148170755, 17.032670713640492));
        request.setTimestamp(LocalDateTime.now());


        String contentAsString = mockMvc.perform(post("/api/checkplace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        CheckPlaceResponse response = mapper.readValue(contentAsString, new TypeReference<>() {});
        assertEquals(CheckPlaceResponseStatus.FOUND, response.getResponseStatus());


        ChallengeShortView chView = response.getUserShortChallengeViews().get(0);


        Map<String, Integer> achievedPlaces = new HashMap<>();
        List<Long> userChallengesComplete = new ArrayList<>();
        userChallengesComplete.add(chView.getUserchallengeid());

        AchievedPlaceDTO dto = new AchievedPlaceDTO();
        dto.setAchievedPlaces(achievedPlaces);
        dto.setDescription("elo");
        dto.setTimestamp(response.getTimestamp());
        dto.setUserChallengesComplete(userChallengesComplete);


        mockMvc.perform(post("/api/checkplace/finalize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());


        assertTrue(this.userChallengePointRepository.findByUserchallengeid(chView.getUserchallengeid()).isEmpty());
        UserChallenge userChallenge = this.userChallengeRepository.findById(chView.getUserchallengeid()).get();

        assertEquals(user.getId(), userChallenge.getUserid());
        assertEquals(challenge.getId(), userChallenge.getChallengeid());
        assertEquals(startTime, userChallenge.getStartTime());
        assertEquals(1, userChallenge.getAttemptnum());
        assertEquals(request.getTimestamp(), userChallenge.getEndTime());
        assertEquals(ChallengeStatus.COMPETED, userChallenge.getStatus());
    }

//    @Test
//    void set_failure_if_time_runs_out() throws Exception {
//
//        LocalDateTime startTime = LocalDateTime.now();
//        CheckPlaceRequest request = new CheckPlaceRequest();
//        request.setCoords(new Coords(51.109415619694644, 17.029433852049866));
//        request.setTimestamp(startTime);
//
//        mockMvc.perform(post("/api/checkplace")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(request)))
//                .andExpect(status().isOk());
//
//        Thread.sleep(16000);
//        request = new CheckPlaceRequest();
//        request.setCoords(new Coords(51.10965388954731, 17.031302771469175));
//        request.setTimestamp(LocalDateTime.now());
//
//
//        String contentAsString = mockMvc.perform(post("/api/checkplace")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(request)))
//                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//
//        CheckPlaceResponse response = mapper.readValue(contentAsString, new TypeReference<>() {});
//        assertEquals(CheckPlaceResponseStatus.FOUND, response.getResponseStatus());
//
//        assertEquals(5, response.getCheckPlaceViewList().size());
//        assertTrue(response.getCheckpointViewList().isEmpty());
//        assertTrue(response.getUserShortChallengeViews().isEmpty());
//        assertTrue(response.getAchievementShortViews().isEmpty());
//
//
//        assertTrue(this.userChallengePointRepository.findAll().isEmpty());
//        UserChallenge userChallenge = this.userChallengeRepository.findAll().get(0);
//        assertEquals(ChallengeStatus.FAILURE, userChallenge.getStatus());
//        assertNotNull(userChallenge.getEndTime());
//    }
}