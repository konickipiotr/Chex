package com.chex.user.repository;

import com.chex.user.model.UserAchievements;
import com.chex.user.model.UsersAchievementsInProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersAchievementsInProgressRepository extends JpaRepository<UsersAchievementsInProgress, Long> {
    boolean existsByUseridAndAchievementid(Long userid, Long achievementid);
    boolean existsByUseridAndAchievementidAndPlaceid(Long userid, Long achievementid, String placeid);
    List<UsersAchievementsInProgress> findByUseridAndAchievementid(Long userid, Long achievementid);
    List<UsersAchievementsInProgress> findByUserid(Long userid);

}
