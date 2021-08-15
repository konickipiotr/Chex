package com.chex.webapp.admin.places.newplace;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class PlaceForm {

    private String placeid;
    private String prefix;
    private String suffix;
    private boolean subplace;
    private String nameEng;
    private String namePl;
    private boolean sameAsFirst;
    private double longitude;
    private double latitude;
    private double radius;
    private Long category;
    private int points;
    private int difficultylevel;
    private String descriptionEng;
    private String descriptionPl;
    private MultipartFile picture;

    public PlaceForm(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        if(prefix.length()<11)
            subplace = true;
    }

    public String createId(){
        return prefix + "." + placeid + suffix;
    }

    public String getParentId(){
        return prefix + ".000" + suffix;
    }
}
