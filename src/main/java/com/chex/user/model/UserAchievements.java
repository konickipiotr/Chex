package com.chex.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class UserAchievements {

    @Id
    @SequenceGenerator(name = "userachievements_sequence", sequenceName = "userachievements_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userachievements_sequence")
    private Long id;
    private Long userid;
    private Long achievementid;
    private LocalDateTime achievedat;
}
