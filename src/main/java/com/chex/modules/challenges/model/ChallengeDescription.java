package com.chex.modules.challenges.model;

import com.chex.lang.LanguageGetter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ChallengeDescription implements LanguageGetter {

    @Id
    private Long id;
    private String pl;
    private String eng;

    public ChallengeDescription() {
    }

    public ChallengeDescription(Long id, String pl, String eng) {
        this.id = id;
        this.pl = pl;
        this.eng = eng;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
