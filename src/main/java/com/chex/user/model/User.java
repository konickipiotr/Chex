package com.chex.user.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private Long id;
    private String firstname;
    private String lastname;
    private String gender;
    private String country;
    private String img;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNextlevel() {
        return nextlevel;
    }

    public void setNextlevel(int nextlevel) {
        this.nextlevel = nextlevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
