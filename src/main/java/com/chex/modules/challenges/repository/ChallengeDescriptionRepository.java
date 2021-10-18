package com.chex.modules.challenges.repository;

import com.chex.modules.challenges.model.ChallengeDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeDescriptionRepository extends JpaRepository<ChallengeDescription, Long> {
}
