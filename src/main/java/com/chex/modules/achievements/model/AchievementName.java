package com.chex.modules.achievements.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class AchievementName {

    @Id
    private Long id;
    private String pl;
    private String eng;
}
