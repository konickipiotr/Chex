package com.chex.api.place;

import com.chex.exceptions.NotFoundElementInEntity;
import com.chex.modules.places.PlaceDescription;
import com.chex.modules.places.PlaceDescriptionRepository;
import com.chex.modules.places.PlaceName;
import com.chex.modules.places.PlaceNameRepository;
import org.aspectj.weaver.ast.Not;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.Optional;

@Service
public class PlaceNameService {

    private final PlaceDescriptionRepository placeDescriptionRepository;
    private final PlaceNameRepository placeNameRepository;

    @Autowired
    public PlaceNameService(PlaceDescriptionRepository placeDescriptionRepository, PlaceNameRepository placeNameRepository) {
        this.placeDescriptionRepository = placeDescriptionRepository;
        this.placeNameRepository = placeNameRepository;
    }

    public String getDescription(String placeid){
        String language = LocaleContextHolder.getLocale().getLanguage();
        Optional<PlaceDescription> oDescriptnion = this.placeDescriptionRepository.findById(placeid);
        if(oDescriptnion.isEmpty())
            throw new NotFoundElementInEntity(placeid, placeDescriptionRepository);

        PlaceDescription placeDescription = oDescriptnion.get();
        if(language.equals("pl"))
            return placeDescription.getPl();
        else
            return placeDescription.getEng();
    }

    public String getName(String placeid){
        String language = LocaleContextHolder.getLocale().getLanguage();
        Optional<PlaceName> oName = this.placeNameRepository.findById(placeid);
        if(oName.isEmpty())
            throw new NotFoundElementInEntity(placeid, placeNameRepository);

        PlaceName placeName = oName.get();
        if(language.equals("pl"))
            return placeName.getPl();
        else
            return placeName.getEng();
    }
}
