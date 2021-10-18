package com.chex.api.challenges;

import com.chex.authentication.AccountStatus;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.modules.challenges.ChallengeLevel;
import com.chex.modules.challenges.model.Challenge;
import com.chex.modules.challenges.model.ChallengeDescription;
import com.chex.modules.challenges.model.ChallengeName;
import com.chex.modules.challenges.model.ChallengePoint;
import com.chex.modules.challenges.repository.ChallengeDescriptionRepository;
import com.chex.modules.challenges.repository.ChallengeNameRepository;
import com.chex.modules.challenges.repository.ChallengePointRepository;
import com.chex.modules.challenges.repository.ChallengeRepository;
import com.chex.modules.challenges.view.ChallengeShortView;
import com.chex.modules.challenges.view.ChallengeView;
import com.chex.modules.places.model.Place;
import com.chex.modules.places.model.PlaceName;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockUser(username="testuser")
@Transactional
class ChallengeAPIControllerTest {

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
    private final UserChallengeRepository userChallengeRepository;
    private final UserChallengePointRepository userChallengePointRepository;

    @Autowired
    public ChallengeAPIControllerTest(MockMvc mockMvc, UserRepository userRepository, AuthRepository authRepository, PasswordEncoder passwordEncoder, ObjectMapper mapper, ChallengeRepository challengeRepository, ChallengePointRepository challengePointRepository, ChallengeNameRepository challengeNameRepository, ChallengeDescriptionRepository challengeDescriptionRepository, PlaceRepository placeRepository, PlaceNameRepository placeNameRepository, UserChallengeRepository userChallengeRepository, UserChallengePointRepository userChallengePointRepository) {
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
        this.userChallengeRepository = userChallengeRepository;
        this.userChallengePointRepository = userChallengePointRepository;
    }

    User user;
    Challenge challenge;
    ChallengePoint p1, p2, p3;
    @BeforeEach
    void setUp() {
        this.authRepository.deleteAll();
        this.userRepository.deleteAll();
        this.challengeRepository.deleteAll();
        this.challengePointRepository.deleteAll();
        this.challengeNameRepository.deleteAll();
        this.challengeDescriptionRepository.deleteAll();
        this.placeRepository.deleteAll();
        this.placeNameRepository.deleteAll();

        Auth aUser = new Auth("testuser", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);
        this.authRepository.save(aUser);
        user = new User(aUser.getId(), "Jan", "Nowak");
        this.userRepository.save(user);

        Place place = new Place();
        place.setId("PP11");
        place.setLatitude(51.10965388954731);
        place.setLongitude(17.031302771469175);
        place.setRadius(20);
        place.setPoints(30);
        this.placeRepository.save(place);
        this.placeNameRepository.save(new PlaceName(place.getId(), "Pomnik Fredry", "Fredo's statue"));

        challenge = new Challenge();
        challenge.setPoints(50);
        challenge.setTimelimit(20 * 1000);
        this.challengeRepository.save(challenge);
        this.challengeNameRepository.save(new ChallengeName(challenge.getId(), "Wyzwanie rynkowe", "Market square challenge"));
        this.challengeDescriptionRepository.save(new ChallengeDescription(challenge.getId(), "cześć", "hello"));

        p1 = new ChallengePoint();
        p1.setChallengeid(challenge.getId());
        p1.setSeq(0);
        p1.setName("START - Iglica");
        p1.setLatitude(51.109415619694644);
        p1.setLongitude(17.029433852049866);
        p1.setRadius(20);
        p1.setIsplace(false);

        p2 = new ChallengePoint();
        p2.setChallengeid(challenge.getId());
        p2.setSeq(1);
        p2.setIsplace(true);
        p2.setPlaceid(place.getId());

        p3 = new ChallengePoint();
        p3.setChallengeid(challenge.getId());
        p3.setSeq(2);
        p3.setName("END - Pręgierz");
        p3.setLatitude(51.10944148170755);
        p3.setLongitude(17.032670713640492);
        p3.setRadius(20);
        p3.setIsplace(false);
        this.challengePointRepository.saveAll(Arrays.asList(p1, p2, p3));
    }


    @Test
    void select_new_challenge() throws Exception {

        String contentAsString = mockMvc.perform(post("/api/challenge/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(challenge.getId())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<UserChallenge> challenges = this.userChallengeRepository.findAll();
        List<UserChallengePoint> points = this.userChallengePointRepository.findAll();

        UserChallengeResponse userResponse = mapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertEquals(ReturnCode.OK, userResponse.getReturnCode());
        assertEquals(1, challenges.size());
        assertEquals(3, points.size());

        UserChallenge userChallenge = challenges.get(0);
        assertEquals(challenge.getId(), userChallenge.getChallengeid());
        assertEquals(1, userChallenge.getAttemptnum());
        assertEquals(ChallengeStatus.NOTSTARTED, userChallenge.getStatus());
        assertNull(userChallenge.getStartTime());
        assertNull(userChallenge.getEndTime());

        for(UserChallengePoint p : points){
            assertEquals(ChallengeStatus.NOTSTARTED, p.getStatus());
            assertNull(p.getReachedat());
            assertEquals(user.getId(), p.getUserid());
            assertEquals(userChallenge.getId(), p.getUserchallengeid());
            assertEquals(challenge.getId(), p.getChallengeid());
        }
    }

    @Test
    void using_nonexisting_challenge_id_return_status_not_found() throws Exception {
        String contentAsString = mockMvc.perform(post("/api/challenge/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(88L)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        UserChallengeResponse userResponse = mapper.readValue(contentAsString, new TypeReference<>() {});
        assertEquals(ReturnCode.CHALLENGE_NOT_FOUND, userResponse.getReturnCode());

        assertTrue(this.userChallengeRepository.findAll().isEmpty());
        assertTrue(this.userChallengePointRepository.findAll().isEmpty());
    }

    @Test
    void using_challenge_id_already_ongoing_return_challenge_ongoing() throws Exception {
        mockMvc.perform(post("/api/challenge/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(challenge.getId())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String contentAsString = mockMvc.perform(post("/api/challenge/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(challenge.getId())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        UserChallengeResponse userResponse = mapper.readValue(contentAsString, new TypeReference<>() {});
        assertEquals(ReturnCode.CHALLENGE_ONGOING, userResponse.getReturnCode());

        List<UserChallenge> challenges = this.userChallengeRepository.findAll();
        List<UserChallengePoint> points = this.userChallengePointRepository.findAll();
        assertEquals(1, challenges.size());
        assertEquals(3, points.size());
    }

    @Test
    void if_challenge_is_completed_or_failed_user_can_start_again() throws Exception {
        mockMvc.perform(post("/api/challenge/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(challenge.getId())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        UserChallenge userChallenge = this.userChallengeRepository.findAll().get(0);
        userChallenge.setStatus(ChallengeStatus.COMPETED);
        this.userChallengeRepository.save(userChallenge);
        this.userChallengePointRepository.deleteAll();


        String contentAsString = mockMvc.perform(post("/api/challenge/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(challenge.getId())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        UserChallengeResponse userResponse = mapper.readValue(contentAsString, new TypeReference<>() {});
        assertEquals(ReturnCode.OK, userResponse.getReturnCode());

        List<UserChallenge> challenges = this.userChallengeRepository.findAll();
        Collections.sort(challenges);
        List<UserChallengePoint> points = this.userChallengePointRepository.findAll();
        assertEquals(2, challenges.size());
        assertEquals(3, points.size());

        assertEquals(challenge.getId(), challenges.get(0).getChallengeid());
        assertEquals(challenge.getId(), challenges.get(1).getChallengeid());
        assertEquals(ChallengeStatus.NOTSTARTED, challenges.get(0).getStatus());
        assertEquals(ChallengeStatus.COMPETED, challenges.get(1).getStatus());

        assertEquals(2, challenges.get(0).getAttemptnum());
        assertEquals(1, challenges.get(1).getAttemptnum());
    }

    @Test
    void remove_user_challenge() throws Exception {

        String contentAsString = mockMvc.perform(post("/api/challenge/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(challenge.getId())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        UserChallengeResponse userResponse = mapper.readValue(contentAsString, new TypeReference<>() {});
        assertEquals(ReturnCode.OK, userResponse.getReturnCode());

        List<UserChallenge> challenges = this.userChallengeRepository.findAll();
        List<UserChallengePoint> points = this.userChallengePointRepository.findAll();
        assertEquals(1, challenges.size());
        assertEquals(3, points.size());

        mockMvc.perform(post("/api/challenge/removeuserchallenge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(challenges.get(0).getId())))
                .andExpect(status().isOk());

        challenges = this.userChallengeRepository.findAll();
        points = this.userChallengePointRepository.findAll();
        assertEquals(0, challenges.size());
        assertEquals(0, points.size());
    }

    @Test
    void give_me_list_of_available_challenges() throws Exception {
        Challenge ch1 = new Challenge();
        Challenge ch2 = new Challenge();
        Challenge ch3 = new Challenge();
        this.challengeRepository.saveAll(Arrays.asList(ch1, ch2, ch3));
        this.challengeNameRepository.save(new ChallengeName(ch3.getId(), "xx", "xx"));
        this.challengeNameRepository.save(new ChallengeName(ch2.getId(), "yy", "yy"));
        this.challengeNameRepository.save(new ChallengeName(ch1.getId(), "vv", "vv"));
        UserChallenge uch1 = new UserChallenge();
        uch1.setChallengeid(ch1.getId());
        uch1.setStatus(ChallengeStatus.COMPETED);
        uch1.setUserid(user.getId());

        UserChallenge uch2 = new UserChallenge();
        uch2.setChallengeid(ch2.getId());
        uch2.setStatus(ChallengeStatus.ONGOING);
        uch2.setUserid(user.getId());

        UserChallenge uch3 = new UserChallenge();
        uch3.setChallengeid(ch3.getId());
        uch3.setStatus(ChallengeStatus.FAILURE);
        uch3.setUserid(user.getId());
        this.userChallengeRepository.saveAll(Arrays.asList(uch1, uch2, uch3));


        String contentAsString = mockMvc.perform(get("/api/challenge/available"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserChallengeResponse userResponse = mapper.readValue(contentAsString, new TypeReference<>() {});
        assertEquals(ReturnCode.OK, userResponse.getReturnCode());

        List<ChallengeShortView> available = userResponse.getAvailable();

        assertEquals(2, available.size());

        assertTrue(challenge.getId().equals(available.get(0).getId()) || challenge.getId().equals(available.get(1).getId()));
        assertTrue(ch3.getId().equals(available.get(0).getId()) || ch3.getId().equals(available.get(1).getId()));
    }

    @Test
    void give_me_all_user_challenge_info() throws Exception {
        Challenge ch1 = new Challenge();
        Challenge ch2 = new Challenge();
        Challenge ch3 = new Challenge();
        Challenge ch4 = new Challenge();
        ch1.setImg("ch1Img");
        ch2.setImg("ch1Img");
        ch3.setImg("ch1Img");
        ch4.setImg("ch1Img");
        ch1.setCategories("pick");
        ch2.setCategories("city");
        ch3.setCategories("statue");
        ch4.setCategories("pick");
        ch1.setLevel(ChallengeLevel.NORMAL);
        ch2.setLevel(ChallengeLevel.EASY);
        ch3.setLevel(ChallengeLevel.HARD);
        ch4.setLevel(ChallengeLevel.SUPERHARD);
        ch1.setPoints(10);
        ch2.setPoints(20);
        ch3.setPoints(30);
        ch4.setPoints(40);
        ch1.setTimelimit(20000);
        ch2.setTimelimit(30000);
        ch3.setTimelimit(40000);
        ch4.setTimelimit(50000);

        this.challengeRepository.saveAll(Arrays.asList(ch1, ch2, ch3, ch4));
        this.challengeNameRepository.save(new ChallengeName(ch1.getId(), "aa", "aa"));
        this.challengeNameRepository.save(new ChallengeName(ch2.getId(), "bb", "bb"));
        this.challengeNameRepository.save(new ChallengeName(ch3.getId(), "xx", "xx"));
        this.challengeNameRepository.save(new ChallengeName(ch4.getId(), "yy", "yy"));
        this.challengeDescriptionRepository.save(new ChallengeDescription(ch1.getId(), "daa", "daa"));
        this.challengeDescriptionRepository.save(new ChallengeDescription(ch2.getId(), "dbb", "dbb"));
        this.challengeDescriptionRepository.save(new ChallengeDescription(ch3.getId(), "dxx", "dxx"));
        this.challengeDescriptionRepository.save(new ChallengeDescription(ch4.getId(), "dyy", "dyy"));
        UserChallenge uch1 = new UserChallenge();
        uch1.setChallengeid(ch1.getId());
        uch1.setStatus(ChallengeStatus.COMPETED);
        uch1.setUserid(user.getId());


        UserChallenge uch2 = new UserChallenge();
        uch2.setChallengeid(ch2.getId());
        uch2.setStatus(ChallengeStatus.ONGOING);
        uch2.setUserid(user.getId());

        UserChallenge uch3 = new UserChallenge();
        uch3.setChallengeid(ch3.getId());
        uch3.setStatus(ChallengeStatus.FAILURE);
        uch3.setUserid(user.getId());

        UserChallenge uch5 = new UserChallenge();
        uch5.setChallengeid(ch3.getId());
        uch5.setStatus(ChallengeStatus.COMPETED);
        uch5.setUserid(user.getId());

        UserChallenge uch4 = new UserChallenge();
        uch4.setChallengeid(ch4.getId());
        uch4.setStatus(ChallengeStatus.NOTSTARTED);
        uch4.setUserid(user.getId());
        this.userChallengeRepository.saveAll(Arrays.asList(uch1, uch2, uch3, uch4, uch5));

        String contentAsString = mockMvc.perform(get("/api/challenge"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        UserChallengeResponse userResponse = mapper.readValue(contentAsString, new TypeReference<>() {});
        List<ChallengeView> completed = userResponse.getCompleted();
        List<ChallengeView> inProgress = userResponse.getInProgress();

        assertEquals(ReturnCode.OK, userResponse.getReturnCode());
        assertEquals(2, inProgress.size());
        assertEquals(3, completed.size());
    }
}