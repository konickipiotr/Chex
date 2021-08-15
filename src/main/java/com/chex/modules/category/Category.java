package com.chex.modules.category;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    private Long id;
    private String pl;
    private String eng;

    public Category(String pl, String eng) {
        this.pl = pl;
        this.eng = eng;
    }
}
