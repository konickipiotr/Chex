package com.chex.user.repository;

import com.chex.user.ChallengeStatus;
import com.chex.user.model.UserChallenge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserChallengeRepositoryTest {

    private final UserChallengeRepository userChallengeRepository;


    @Autowired
    public UserChallengeRepositoryTest(UserChallengeRepository userChallengeRepository) {
        this.userChallengeRepository = userChallengeRepository;
    }

    @BeforeEach
    void setUp() {
        this.userChallengeRepository.deleteAll();
    }

    @Test
    void check_customized_query() {
        UserChallenge uch0 = new UserChallenge();
        uch0.setChallengeid(1L);
        uch0.setUserid(99L);
        uch0.setStatus(ChallengeStatus.COMPETED);

        UserChallenge uch1 = new UserChallenge();
        uch1.setChallengeid(1L);
        uch1.setUserid(99L);
        uch1.setStatus(ChallengeStatus.COMPETED);

        UserChallenge uch2 = new UserChallenge();
        uch2.setChallengeid(1L);
        uch2.setUserid(99L);
        uch2.setStatus(ChallengeStatus.FAILURE);

        UserChallenge uch3 = new UserChallenge();
        uch3.setChallengeid(1L);
        uch3.setUserid(98L);
        uch3.setStatus(ChallengeStatus.COMPETED);

        UserChallenge uch4 = new UserChallenge();
        uch4.setChallengeid(2L);
        uch4.setUserid(99L);
        uch4.setStatus(ChallengeStatus.COMPETED);

        this.userChallengeRepository.saveAll(Arrays.asList(uch0, uch1, uch2, uch3, uch4));

        List<UserChallenge> userChallengeList= this.userChallengeRepository.getUserCompleted(1L);

        assertEquals(3, userChallengeList.size());

    }
}