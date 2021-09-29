package com.chex.user.repository;

import com.chex.user.model.UserAchievements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAchievementsRepository extends JpaRepository<UserAchievements, Long> {
    boolean existsByUseridAndAchievementid(Long userid, Long achievementid);
}
