package com.chex.modules.post.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Star {

    @Id
    @SequenceGenerator(name = "star_sequence", sequenceName = "star_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "star_sequence")
    private Long id;
    private Long userid;
    private Long postid;
}