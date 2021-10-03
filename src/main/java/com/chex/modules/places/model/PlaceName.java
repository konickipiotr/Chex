package com.chex.modules.places.model;

import com.chex.lang.LanguageGetter;
import com.chex.webapp.admin.places.newplace.PlaceForm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PlaceName implements LanguageGetter {

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
        if(pf.isSameAsFirst())
            this.eng = pf.getNamePl();
        else
            this.eng = pf.getNameEng();
    }

    public PlaceName() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    @Override
    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }
}
