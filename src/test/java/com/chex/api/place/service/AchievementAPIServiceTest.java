package com.chex.api.place.service;

import com.chex.modules.achievements.model.Achievement;
import com.chex.modules.achievements.model.AchievementName;
import com.chex.modules.achievements.model.AchievementPlaces;
import com.chex.modules.achievements.model.AchievementShortView;
import com.chex.modules.achievements.repository.AchievementDescriptionRepository;
import com.chex.modules.achievements.repository.AchievementNameRepository;
import com.chex.modules.achievements.repository.AchievementPlacesRepository;
import com.chex.modules.achievements.repository.AchievementRepository;
import com.chex.user.model.User;
import com.chex.user.model.UserAchievements;
import com.chex.user.model.UsersAchievementsInProgress;
import com.chex.user.repository.UserAchievementsRepository;
import com.chex.user.repository.UsersAchievementsInProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AchievementAPIServiceTest {

    private final AchievementRepository achievementRepository;
    private final AchievementNameRepository achievementNameRepository;
    private final AchievementDescriptionRepository achievementDescriptionRepository;
    private final AchievementPlacesRepository achievementPlacesRepository;
    private final UserAchievementsRepository userAchievementsRepository;
    private final UsersAchievementsInProgressRepository usersAchievementsInProgressRepository;
    private final AchievementAPIService achievementAPIService;

    @Autowired
    public AchievementAPIServiceTest(AchievementRepository achievementRepository, AchievementNameRepository achievementNameRepository, AchievementDescriptionRepository achievementDescriptionRepository, AchievementPlacesRepository achievementPlacesRepository, UserAchievementsRepository userAchievementsRepository, UsersAchievementsInProgressRepository usersAchievementsInProgressRepository, AchievementAPIService achievementAPIService) {
        this.achievementRepository = achievementRepository;
        this.achievementNameRepository = achievementNameRepository;
        this.achievementDescriptionRepository = achievementDescriptionRepository;
        this.achievementPlacesRepository = achievementPlacesRepository;
        this.userAchievementsRepository = userAchievementsRepository;
        this.usersAchievementsInProgressRepository = usersAchievementsInProgressRepository;
        this.achievementAPIService = achievementAPIService;
    }

    Achievement ach1;
    Achievement ach2;
    String p1 = "p1";
    String p2 = "p2";
    String p3 = "p3";
    String p4 = "p4";
    String p5 = "p5";

    @BeforeEach
    void setUp() {
        achievementRepository.deleteAll();
        achievementNameRepository.deleteAll();
        achievementDescriptionRepository.deleteAll();
        achievementPlacesRepository.deleteAll();
        userAchievementsRepository.deleteAll();
        usersAchievementsInProgressRepository.deleteAll();

        ach1 = new Achievement();
        ach1.setPoints(20);
        ach2 = new Achievement();
        ach2.setPoints(30);
        this.achievementRepository.save(ach1);
        this.achievementRepository.save(ach2);

        this.achievementNameRepository.save(new AchievementName(ach1.getId(), "ach1", "ach1"));
        this.achievementNameRepository.save(new AchievementName(ach2.getId(), "ach2", "ach2"));

        this.achievementPlacesRepository.save(new AchievementPlaces(ach1.getId(), p1));
        this.achievementPlacesRepository.save(new AchievementPlaces(ach1.getId(), p2));
        this.achievementPlacesRepository.save(new AchievementPlaces(ach1.getId(), p3));
        this.achievementPlacesRepository.save(new AchievementPlaces(ach2.getId(), p3));
        this.achievementPlacesRepository.save(new AchievementPlaces(ach2.getId(), p4));
        this.achievementPlacesRepository.save(new AchievementPlaces(ach2.getId(), p5));
    }

    @Test
    void user_did_not_gain_achievement_because_places_are_not_assign_to_any_achievements() {
        User user = new User();
        user.setId(1L);

        List<AchievementShortView> achievementShortViewList = achievementAPIService.checkAchievement(new HashSet<>(Arrays.asList("p0", "p10")), user.getId());
        assertTrue(achievementShortViewList.isEmpty());
    }

    @Test
    void user_already_have() {

        User user = new User();
        user.setId(1L);

        List<AchievementShortView> achievementShortViewList = achievementAPIService.checkAchievement(new HashSet<>(List.of("p1")), user.getId());
        assertTrue(achievementShortViewList.isEmpty());
    }

    @Test
    void user_reached_new_achievement_in_check_stage() {
        User user = new User();
        user.setId(1L);

        this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(user.getId(), ach1.getId(), p3));
        this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(user.getId(), ach1.getId(), p2));

        List<AchievementShortView> achievementShortViewList = achievementAPIService.checkAchievement(new HashSet<>(List.of("p1")), user.getId());
        assertEquals(1, achievementShortViewList.size());
        AchievementShortView asv = achievementShortViewList.get(0);
        assertEquals(ach1.getId(), asv.getId());
        assertEquals("ach1", asv.getName());
    }

    @Test
    void user_reached_new_achievement_in_save_stage() {
        User user = new User();
        user.setId(1L);

        assertFalse(this.userAchievementsRepository.existsByUseridAndAchievementid(user.getId(), ach1.getId()));

        this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(user.getId(), ach1.getId(), p3));
        this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(user.getId(), ach1.getId(), p2));

        List<Achievement> achievementList = achievementAPIService.addPlaceToUserAchievements(new HashSet<>(List.of("p1")), user.getId());

        List<UsersAchievementsInProgress> inProgress = this.usersAchievementsInProgressRepository.findByUseridAndAchievementid(user.getId(), ach1.getId());
        assertTrue(inProgress.isEmpty());
        assertTrue(this.userAchievementsRepository.existsByUseridAndAchievementid(user.getId(), ach1.getId()));
        assertEquals(20, achievementList.stream().mapToInt(Achievement::getPoints).sum());
    }


    @Test
    void user_reached_two_achievements_in_check_stage() {
        User user = new User();
        user.setId(1L);

        this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(user.getId(), ach1.getId(), p1));
        this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(user.getId(), ach1.getId(), p2));
        this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(user.getId(), ach2.getId(), p4));
        this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(user.getId(), ach2.getId(), p5));

        List<AchievementShortView> achievementShortViewList = achievementAPIService.checkAchievement(new HashSet<>(List.of("p3")), user.getId());
        assertEquals(2, achievementShortViewList.size());
        AchievementShortView asv = achievementShortViewList.get(0);
        assertTrue(ach1.getId().equals(asv.getId()) || ach2.getId().equals(asv.getId()));
    }

    @Test
    void user_reached_two_achievements_in_save_stage() {
        User user = new User();
        user.setId(1L);

        assertFalse(this.userAchievementsRepository.existsByUseridAndAchievementid(user.getId(), ach1.getId()));
        assertFalse(this.userAchievementsRepository.existsByUseridAndAchievementid(user.getId(), ach2.getId()));

        this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(user.getId(), ach1.getId(), p1));
        this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(user.getId(), ach1.getId(), p2));
        this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(user.getId(), ach2.getId(), p4));
        this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(user.getId(), ach2.getId(), p5));


        List<Achievement> achievementList = achievementAPIService.addPlaceToUserAchievements(new HashSet<>(List.of("p3")), user.getId());

        List<UsersAchievementsInProgress> inProgress = this.usersAchievementsInProgressRepository.findByUseridAndAchievementid(user.getId(), ach1.getId());
        List<UsersAchievementsInProgress> inProgress2 = this.usersAchievementsInProgressRepository.findByUseridAndAchievementid(user.getId(), ach2.getId());

        assertTrue(inProgress.isEmpty());
        assertTrue(inProgress2.isEmpty());

        assertTrue(this.userAchievementsRepository.existsByUseridAndAchievementid(user.getId(), ach1.getId()));
        assertTrue(this.userAchievementsRepository.existsByUseridAndAchievementid(user.getId(), ach2.getId()));
        assertEquals(50, achievementList.stream().mapToInt(Achievement::getPoints).sum());
    }
}