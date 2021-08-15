package com.chex.modules.places;

import com.chex.webapp.admin.places.newplace.PlaceForm;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Place {

    /*
    id - Field of 16 characters
    1-2 - continent
    3-5 - country
    6-8 - region
    9-11 - city/province
    12-16 - specific point
     */
    @Id
    @Column(length = 20)
    private String id;
    private double longitude;
    private double latitude;
    private double radius;
    private Long category;
    private String achievements;
    private int points;
    private int difficultylevel;
    private double rating;
    private String imgurl;
    private String imgpath;

    public Place() {
        this.latitude = 10000;
        this.longitude = 10000;
        this.radius = 0;
    }

    public Place(PlaceForm placeForm){
        this();

        this.id = placeForm.createId();
        this.points = placeForm.getPoints();
        if(placeForm.getPrefix().length() > 11) {
            this.longitude = placeForm.getLongitude();
            this.latitude = placeForm.getLatitude();
            this.category = placeForm.getCategory();
            this.radius = placeForm.getRadius();
            this.difficultylevel = placeForm.getDifficultylevel();
        }
    }

    public Place(String id, Long category, int points, String imgurl) {
        this.id = id;
        this.category = category;
        this.points = points;
        this.imgurl = imgurl;
    }

    public Place(String id) {
        this.id = id;
    }


}
