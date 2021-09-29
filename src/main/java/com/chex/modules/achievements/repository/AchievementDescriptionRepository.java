package com.chex.modules.achievements.repository;

import com.chex.modules.achievements.model.AchievementDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementDescriptionRepository extends JpaRepository<AchievementDescription, Long> {
}
