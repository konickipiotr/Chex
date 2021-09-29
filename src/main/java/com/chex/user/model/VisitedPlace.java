package com.chex.user.model;

import com.chex.api.place.AchievedPlaceDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class VisitedPlace {

    @Id
    @SequenceGenerator(name = "vp_sequence", sequenceName = "vp_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vp_sequence")
    private Long id;
    private Long userid;
    private String placeid;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime vdate;
    private int rating;

    public VisitedPlace(Long userid) {
        this.userid = userid;
    }
}
