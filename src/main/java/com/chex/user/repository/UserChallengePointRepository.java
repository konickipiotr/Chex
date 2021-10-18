package com.chex.user.repository;

import com.chex.modules.challenges.CheckpointType;
import com.chex.user.ChallengeStatus;
import com.chex.user.model.UserChallengePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChallengePointRepository extends JpaRepository<UserChallengePoint, Long> {

    List<UserChallengePoint> findByUseridAndStatusAndCheckpointtype(Long userid, ChallengeStatus status, CheckpointType type);
    List<UserChallengePoint> findByUseridAndStatus(Long userid, ChallengeStatus status);
    List<UserChallengePoint> findByUserchallengeid(Long userchallengeid);
    void deleteByUserchallengeid(Long userchallengeid);
    UserChallengePoint findByUserchallengeidAndCheckpointid(Long userchallengeid, long checkpointid);


}
