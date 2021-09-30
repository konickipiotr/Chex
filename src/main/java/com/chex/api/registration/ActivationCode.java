package com.chex.api.registration;

import javax.persistence.*;

@Entity
public class ActivationCode {

    @Id
    private Long userid;
    private String activationcode;

    public ActivationCode() {
    }

    public ActivationCode(Long userid, String activationcode) {
        this.userid = userid;
        this.activationcode = activationcode;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getActivationcode() {
        return activationcode;
    }

    public void setActivationcode(String activationcode) {
        this.activationcode = activationcode;
    }
}

