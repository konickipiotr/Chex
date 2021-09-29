package com.chex.modules.achievements.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class AchievementDescription {

    @Id
    private Long id;
    private String pl;
    private String eng;
}
