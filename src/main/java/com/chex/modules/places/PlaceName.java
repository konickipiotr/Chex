package com.chex.modules.places;

import com.chex.webapp.admin.places.newplace.PlaceForm;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class PlaceName {

    @Id
    @Column(length = 20)
    private String id;
    private String pl;
    private String eng;

    public PlaceName(String id, String pl, String eng) {
        this.id = id;
        this.pl = pl;
        this.eng = eng;
    }

    public PlaceName(String id, PlaceForm pf) {
        this.id = id;
        this.pl = pf.getNamePl();
        this.eng = pf.getNameEng();
    }

}
