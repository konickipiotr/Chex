package com.chex.user.repository;

import com.chex.user.model.UserAchievements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAchievementsRepository extends JpaRepository<UserAchievements, Long> {
    boolean existsByUseridAndAchievementid(Long userid, Long achievementid);
    List<UserAchievements> findByUserid(Long userid);
    long countByAchievementid(Long achievementid);
}
