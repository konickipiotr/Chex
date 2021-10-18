package com.chex.modules.challenges.repository;

import com.chex.modules.challenges.model.ChallengePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengePointRepository extends JpaRepository<ChallengePoint, Long> {

    int countByChallengeid(Long challengeid);
    Optional<ChallengePoint> findByChallengeidAndSeq(Long id, int seq);
    List<ChallengePoint> findByChallengeid(Long challengeid);
}
