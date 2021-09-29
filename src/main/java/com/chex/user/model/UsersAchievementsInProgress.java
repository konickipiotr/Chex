package com.chex.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class UsersAchievementsInProgress {

    @Id
    @SequenceGenerator(name = "userachievementsinprogress_sequence", sequenceName = "userachievementsinprogress_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userachievementsinprogress_sequence")
    private Long id;
    private Long userid;
    private Long achievementid;
    private String placeid;
    private LocalDateTime achievedat;
}
