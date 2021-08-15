package com.chex.webapp.admin.places.newplace;

import com.chex.modules.category.CategoryRepository;
import com.chex.modules.places.Place;
import com.chex.modules.places.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/admin/places/new")
public class AddNewPlaceController {

    private PlaceRepository placeRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public AddNewPlaceController(PlaceRepository placeRepository, CategoryRepository categoryRepository) {
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String startAddingNewPlace(){
        return "admin/places/continent";
    }

    @GetMapping("/continent")
    public String selectContinent(@RequestParam("continent") String continent, Model model){
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<Place> countries = this.placeRepository.getCountries(continent);
        model.addAttribute("placetype", "countries");
        model.addAttribute("list", countries);
        model.addAttribute("placeForm", new PlaceForm(continent, "00000000000"));
        return "admin/places/intermediateplaces";
    }

    @GetMapping("/{placetype}")
    public String selectContinent(@PathVariable("placetype") String placetype, String selectedPlace, Model model){
        //model.addAttribute("list", list);

        return "admin/places/intermediateplaces";
    }
    @PostMapping("/{placetype}")
    public String addCountry(@PathVariable("placetype")String placetype, PlaceForm placeForm,  Model model){
        System.out.println(placeForm);
        Place newPlace = new Place(placeForm);
        this.placeRepository.save(newPlace);
        List<Place> countries = this.placeRepository.getCountries(placeForm.getPrefix());
        model.addAttribute("list", countries);
        model.addAttribute("placeForm", placeForm);
        return "admin/places/intermediateplaces";
    }
}
