package com.chex.webapp.admin.places.newplace;

import com.chex.modules.places.PlaceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin/places/new")
public class AddNewPlaceController {

    private final AddPlaceService addPlaceService;

    @Autowired
    public AddNewPlaceController(AddPlaceService addPlaceService) {
        this.addPlaceService = addPlaceService;
    }

    @GetMapping
    public String startAddingNewPlace(){
        return "admin/places/continent";
    }

    @GetMapping("/continent")
    public String selectContinent(@RequestParam("continent") String continent, Model model){
        String language = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("placetype", PlaceType.COUNTRY);
        model.addAttribute("list", this.addPlaceService.getListOfPlaces(continent, PlaceType.COUNTRY, language));
        model.addAttribute("placeForm", new PlaceForm(continent, ".000.000.00000"));
        return "admin/places/newplace";
    }

    @GetMapping("/{placetype}")
    public String selectPlace(@PathVariable("placetype") PlaceType placetype, String selectedPlace, Model model){
        String language = LocaleContextHolder.getLocale().getLanguage();
        int idx = placetype.ordinal() + 1;
        String[] ss = selectedPlace.split("\\.");

        StringBuilder prefix = new StringBuilder();
        for(int i = 0; i < idx; i++)
            prefix.append(i == 0 ? ss[i] : ("." + ss[i]));

        StringBuilder suffix = new StringBuilder();
        for(int i = idx + 1; i < ss.length; i++)
            suffix.append(".").append(ss[i]);

        placetype = PlaceType.values()[placetype.ordinal() + 1];

        model.addAttribute("placetype", placetype);
        model.addAttribute("list", this.addPlaceService.getListOfPlaces(prefix.toString(), placetype, language));
        model.addAttribute("placeForm", new PlaceForm(prefix.toString(), suffix.toString()));
        return "admin/places/newplace";
    }
    @PostMapping("/{placetype}")
    public String addCountry(@PathVariable("placetype")PlaceType placetype, PlaceForm placeForm,  Model model, BindingResult br){
        String language = LocaleContextHolder.getLocale().getLanguage();
        if(this.addPlaceService.idAlreadyExists(placeForm)){
            br.rejectValue("placeid","error_id_already_exists");
            model.addAttribute("placeForm", placeForm);
        }else {
            addPlaceService.addNewPlace(placeForm);
            model.addAttribute("placeForm", new PlaceForm(placeForm.getPrefix(), placeForm.getSuffix()));
        }
        model.addAttribute("list", this.addPlaceService.getListOfPlaces(placeForm.getPrefix(), placetype, language));
        model.addAttribute("placetype", placetype);
        return "admin/places/newplace";
    }
}
