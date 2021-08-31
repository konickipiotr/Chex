package com.chex.user.place;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class VisitedPlace {

    @Id
    @SequenceGenerator(name = "visitedplaces_sequence", sequenceName = "visitedplaces_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visitedplaces_sequence")
    private Long id;
    private Long iduser;
    private String idplace;
    private LocalDateTime vdate;
}
