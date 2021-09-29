package com.chex.modules.achievements.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class AchievementPlaces {

    @Id
    @SequenceGenerator(name = "achievementplaces_sequence", sequenceName = "achievementplaces_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "achievementplaces_sequence")
    private Long id;
    private Long achievementid;
    private String placeid;

    public AchievementPlaces(Long achievementid, String placeid) {
        this.achievementid = achievementid;
        this.placeid = placeid;
    }
}
