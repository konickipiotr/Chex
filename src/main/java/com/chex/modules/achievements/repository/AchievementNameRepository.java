package com.chex.modules.achievements.repository;

import com.chex.modules.achievements.model.AchievementName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementNameRepository extends JpaRepository<AchievementName, Long> {

    boolean existsByPl(String pl);
    boolean existsByEng(String en);
}
