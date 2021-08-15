package com.chex.webapp.admin.places;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/places")
public class PlacesController {

    @GetMapping
    public String showAllPlaces(){
        return "admin/places/places";
    }

    @GetMapping("category")
    public String showAllCategories(){
        return "admin/places/addcategory";
    }


}
