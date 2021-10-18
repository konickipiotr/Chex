package com.chex.user.repository;

import com.chex.user.ChallengeStatus;
import com.chex.user.model.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    List<UserChallenge> findByUseridAndChallengeid(Long userid, Long challengeid);
    List<UserChallenge> findByUserid(Long userid);
    int countByStatusAndChallengeid(ChallengeStatus status, Long challengeid);

//    @Query(value = "select u.userid, u.challengeid, count(u) from UserChallenge u where u.challengeid=:challengeid and u.status=:status group by u.userid, u.challengeid")
//////    @Query(value = "select count(u.userid) from UserChallenge u group by u.userid")
//    @Query(value = "select count(*) from UserChallenge where (select  distinct u.userid from UserChallenge u where u.challengeid=:challengeid and u.status=:status)")
//    int countUserCompleted(@Param("status") ChallengeStatus status,
//                           @Param("challengeid") Long challengeid);

    @Query(value = " from UserChallenge u where u.challengeid=:challengeid and u.status='COMPETED'")
    List<UserChallenge> getUserCompleted(@Param("challengeid") Long challengeid);

    @Query(value = "from UserChallenge u where u.status='ONGOING'")
    List<UserChallenge> findInProgress();
}
