package com.chex.modules.places.model;

import com.chex.lang.LanguageGetter;
import com.chex.webapp.admin.places.newplace.PlaceForm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PlaceDescription implements LanguageGetter {

    @Id
    @Column(length = 20)
    private String id;
    private String pl;
    private String eng;

    public PlaceDescription(String id, String pl, String eng) {
        this.id = id;
        this.pl = pl;
        this.eng = eng;
    }

    public PlaceDescription(String id, PlaceForm pf) {
        this.id = id;
        this.pl = pf.getDescriptionPl();
        this.eng = pf.getDescriptionEng();
    }

    public PlaceDescription() {
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
