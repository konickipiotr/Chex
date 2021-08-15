package com.chex.webapp.admin.places.newplace;

import com.chex.config.GlobalSettings;
import com.chex.modules.category.Category;
import com.chex.modules.category.CategoryRepository;
import com.chex.modules.places.*;
import com.chex.utils.Duo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
public class AddPlaceService {

    private CategoryRepository categoryRepository;
    private PlaceRepository placeRepository;
    private PlaceNameRepository placeNameRepository;
    private PlaceDescriptionRepository placeDescriptionRepository;

    @Autowired
    public AddPlaceService(CategoryRepository categoryRepository, PlaceRepository placeRepository, PlaceNameRepository placeNameRepository, PlaceDescriptionRepository placeDescriptionRepository) {
        this.categoryRepository = categoryRepository;
        this.placeRepository = placeRepository;
        this.placeNameRepository = placeNameRepository;
        this.placeDescriptionRepository = placeDescriptionRepository;
    }

    public void setModel(Model model){
        String lang = LocaleContextHolder.getLocale().getLanguage();
    }

    public Map<String, String> getListOfPlaces(String id, PlaceType placeType){
        List<Place> places = new ArrayList<>();
        switch (placeType){
            case CONTINENT: places = this.placeRepository.getCountries(id);break;
            case COUNTRY: places = this.placeRepository.getProvinces(id); break;
            case PROVINCE: places = this.placeRepository.getRegion(id); break;


        }
        return null;
    }

    private Map<Long, String> getAllCategories(String lang){
        List<Category> categories = this.categoryRepository.findAll();
        Map<Long, String> out = new TreeMap<>();
        if(!categories.isEmpty()){
            for(Category c : categories){
                if(lang.equals("pl"))
                    out.put(c.getId(), c.getPl());
                if(lang.equals("en"))
                    out.put(c.getId(), c.getEng());
            }
        }
        return out;
    }


    public boolean addNewGeneralPlace(PlaceForm placeForm){
        if(this.placeRepository.existsById(placeForm.createId()))
            return false;

        Place place = new Place(placeForm);
        place.setCategory(setCategoryAuto(placeForm));
        this.placeRepository.save(place);
        this.placeNameRepository.save(new PlaceName(place.getId(), placeForm));
        this.placeDescriptionRepository.save(new PlaceDescription(place.getId(), placeForm));
        return true;
    }

    private Long setCategoryAuto(PlaceForm pf){
        int prefixLen = pf.getPrefix().length();
        if(prefixLen == 0)
            return this.categoryRepository.findByEng("continent").getId();
        else if(prefixLen <= 2)
            return this.categoryRepository.findByEng("country").getId();
        else if(prefixLen <= 5)
            return this.categoryRepository.findByEng("province").getId();
        else if(prefixLen <= 8){
            if(pf.getPrefix().equals("REG"))
                return this.categoryRepository.findByEng("region").getId();
            else
                return this.categoryRepository.findByEng("city").getId();
        }else
            return pf.getCategory();
    }
}
