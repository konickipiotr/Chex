package com.chex.user.repository;

import com.chex.modules.challenges.CheckpointType;
import com.chex.user.ChallengeStatus;
import com.chex.user.model.UserChallengePoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserChallengePointRepositoryTest {

    private final UserChallengePointRepository userChallengePointRepository;

    @Autowired
    public UserChallengePointRepositoryTest(UserChallengePointRepository userChallengePointRepository) {
        this.userChallengePointRepository = userChallengePointRepository;
    }

    @Test
    void getNotStartedStartPoints() {

        UserChallengePoint p1 = new UserChallengePoint();
        p1.setUserchallengeid(999L);
        p1.setUserid(1L);
        p1.setStatus(ChallengeStatus.NOTSTARTED);
        p1.setCheckpointtype(CheckpointType.START);

        UserChallengePoint p2 = new UserChallengePoint();
        p2.setUserchallengeid(888L);
        p2.setUserid(2L);
        p2.setStatus(ChallengeStatus.NOTSTARTED);
        p2.setCheckpointtype(CheckpointType.START);

        UserChallengePoint p3 = new UserChallengePoint();
        p3.setUserchallengeid(888L);
        p3.setUserid(1L);
        p3.setStatus(ChallengeStatus.NOTSTARTED);
        p3.setCheckpointtype(CheckpointType.START);

        UserChallengePoint p4 = new UserChallengePoint();
        p4.setUserchallengeid(999L);
        p4.setUserid(1L);
        p4.setStatus(ChallengeStatus.NOTSTARTED);
        p4.setCheckpointtype(CheckpointType.CHECKPOINT);

        UserChallengePoint p5 = new UserChallengePoint();
        p5.setUserchallengeid(999L);
        p5.setUserid(1L);
        p5.setStatus(ChallengeStatus.ONGOING);
        p5.setCheckpointtype(CheckpointType.START);
        this.userChallengePointRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

        List<UserChallengePoint> list = this.userChallengePointRepository.findByUseridAndStatusAndCheckpointtype(1L, ChallengeStatus.NOTSTARTED, CheckpointType.START);
        assertEquals(2, list.size());
        assertTrue(list.get(0).getUserchallengeid().equals(888L) || list.get(0).getUserchallengeid().equals(999L));
        assertTrue(list.get(1).getUserchallengeid().equals(888L) || list.get(0).getUserchallengeid().equals(999L));
    }

    @Test
    void getInProgressPoints() {

        UserChallengePoint p1 = new UserChallengePoint();
        p1.setUserchallengeid(999L);
        p1.setUserid(1L);
        p1.setStatus(ChallengeStatus.ONGOING);
        p1.setCheckpointtype(CheckpointType.START);

        UserChallengePoint p2 = new UserChallengePoint();
        p2.setUserchallengeid(888L);
        p2.setUserid(2L);
        p2.setStatus(ChallengeStatus.NOTSTARTED);
        p2.setCheckpointtype(CheckpointType.CHECKPOINT);

        UserChallengePoint p3 = new UserChallengePoint();
        p3.setUserchallengeid(888L);
        p3.setUserid(1L);
        p3.setStatus(ChallengeStatus.ONGOING);
        p3.setCheckpointtype(CheckpointType.CHECKPOINT);

        UserChallengePoint p4 = new UserChallengePoint();
        p4.setUserchallengeid(999L);
        p4.setUserid(1L);
        p4.setStatus(ChallengeStatus.NOTSTARTED);
        p4.setCheckpointtype(CheckpointType.CHECKPOINT);

        UserChallengePoint p5 = new UserChallengePoint();
        p5.setUserchallengeid(999L);
        p5.setUserid(1L);
        p5.setStatus(ChallengeStatus.FAILURE);
        p5.setCheckpointtype(CheckpointType.START);
        this.userChallengePointRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

        List<UserChallengePoint> list = this.userChallengePointRepository.findByUseridAndStatus(1L, ChallengeStatus.ONGOING);
        assertEquals(2, list.size());
        assertTrue(list.get(0).getUserchallengeid().equals(888L) || list.get(0).getUserchallengeid().equals(999L));
        assertTrue(list.get(1).getUserchallengeid().equals(888L) || list.get(0).getUserchallengeid().equals(999L));

    }
}