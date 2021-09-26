package com.chex.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
public class User {

    @Id
    private Long id;
    private String firstname;
    private String lastname;
    private String gender;
    private String country;
    private String imgurl;
    private String imgpath;

    public String getName(){
        return this.firstname + " " + this.lastname;
    }

    public User() {
        this.imgurl = "img/user.png";
    }

    public User(Long id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = "M";
    }

    public String getImgurl() {
        if(imgurl == null || imgurl.isBlank())
            return "img/user.png";
        return imgurl;
    }
}
