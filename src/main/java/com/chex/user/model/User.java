package com.chex.user.model;

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

    private int exp;
    private int level;
    private int nextlevel;
    private String title;

    public String getName(){
        return this.firstname + " " + this.lastname;
    }

    public User() {
        this.exp = 0;
        this.level = 1;
        this.nextlevel = 20;
        this.title = "Nowicjusz";
    }

    public User(Long id, String firstname, String lastname) {
        this();
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = "M";
    }

    public void addExp(int gainedExperience){

        int newExp = this.exp + gainedExperience;
        while (newExp > this.nextlevel) {
            newExp -= this.nextlevel;
            this.level++;
            this.nextlevel = this.nextlevel + (int) (this.nextlevel * 0.3);
        }

        this.exp = newExp;
    }
}
