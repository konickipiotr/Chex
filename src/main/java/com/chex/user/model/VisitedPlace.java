package com.chex.user.model;

import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;

@Entity
public class VisitedPlace implements Comparable<VisitedPlace> {

    @Id
    @SequenceGenerator(name = "vp_sequence", sequenceName = "vp_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vp_sequence")
    private Long id;
    private Long userid;
    private String placeid;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime vdate;
    private int rating;

    public VisitedPlace() {
    }

    public VisitedPlace(Long userid) {
        this.userid = userid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public LocalDateTime getVdate() {
        return vdate;
    }

    public void setVdate(LocalDateTime vdate) {
        this.vdate = vdate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    @Override
    public int compareTo(VisitedPlace visitedPlace) {
        return this.vdate.compareTo(visitedPlace.vdate);
    }
}
