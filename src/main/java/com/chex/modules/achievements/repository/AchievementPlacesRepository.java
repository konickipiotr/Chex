package com.chex.modules.achievements.repository;

import com.chex.modules.achievements.model.AchievementPlaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementPlacesRepository extends JpaRepository<AchievementPlaces, Long> {

    List<AchievementPlaces> findByPlaceid(String placeid);
    List<AchievementPlaces> findByAchievementid(Long achievementid);
}
