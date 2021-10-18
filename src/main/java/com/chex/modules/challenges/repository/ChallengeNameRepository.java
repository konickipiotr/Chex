package com.chex.modules.challenges.repository;

import com.chex.modules.challenges.model.ChallengeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeNameRepository extends JpaRepository<ChallengeName, Long> {
    ChallengeName findByEngOrPl(String eng, String pl);
}
