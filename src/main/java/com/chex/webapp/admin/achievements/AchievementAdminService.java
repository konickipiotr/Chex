package com.chex.webapp.admin.achievements;

import com.chex.files.FileNameStruct;
import com.chex.files.FileService;
import com.chex.files.FileType;
import com.chex.modules.achievements.model.Achievement;
import com.chex.modules.achievements.model.AchievementDescription;
import com.chex.modules.achievements.model.AchievementName;
import com.chex.modules.achievements.model.AchievementPlaces;
import com.chex.modules.achievements.repository.AchievementDescriptionRepository;
import com.chex.modules.achievements.repository.AchievementNameRepository;
import com.chex.modules.achievements.repository.AchievementPlacesRepository;
import com.chex.modules.achievements.repository.AchievementRepository;
import com.chex.modules.places.model.Place;
import com.chex.modules.places.model.PlaceName;
import com.chex.modules.places.repository.PlaceNameRepository;
import com.chex.modules.places.repository.PlaceRepository;
import com.chex.utils.Duo;
import com.chex.modules.achievements.model.AchievementShortView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AchievementAdminService {

    private final PlaceRepository placeRepository;
    private final PlaceNameRepository placeNameRepository;
    private final AchievementRepository achievementRepository;
    private final AchievementNameRepository achievementNameRepository;
    private final AchievementDescriptionRepository achievementDescriptionRepository;
    private final AchievementPlacesRepository achievementPlacesRepository;
    private final FileService fileService;

    @Autowired
    public AchievementAdminService(PlaceRepository placeRepository, PlaceNameRepository placeNameRepository, AchievementRepository achievementRepository, AchievementNameRepository achievementNameRepository, AchievementDescriptionRepository achievementDescriptionRepository, AchievementPlacesRepository achievementPlacesRepository, FileService fileService) {
        this.placeRepository = placeRepository;
        this.placeNameRepository = placeNameRepository;
        this.achievementRepository = achievementRepository;
        this.achievementNameRepository = achievementNameRepository;
        this.achievementDescriptionRepository = achievementDescriptionRepository;
        this.achievementPlacesRepository = achievementPlacesRepository;
        this.fileService = fileService;
    }

    public List<AchievementShortView> getShortViewList(){
        List<AchievementShortView> list = new ArrayList<>();
        List<Achievement> all = this.achievementRepository.findAll();
        String language = LocaleContextHolder.getLocale().getLanguage();

        for(Achievement a : all){
            AchievementShortView asv = new AchievementShortView();
            asv.setId(a.getId());
            asv.setImg(a.getImgurl());
            AchievementName achievementName = this.achievementNameRepository.findById(a.getId()).get();
            switch (language){
                case "pl": asv.setName(achievementName.getPl());break;
                default: asv.setName(achievementName.getEng());
            }
            list.add(asv);
        }
        return list;
    }

    public boolean achievementExist(AchievementForm form){
        if(this.achievementNameRepository.existsByPl(form.getNamepl()))
            return true;
        if(this.achievementNameRepository.existsByEng(form.getNameen()))
            return true;
        return false;
    }

    public void saveAchievement(AchievementForm form){

        Achievement ach = new Achievement();
        ach.setPoints(form.getPoints());

        if(form.getPicture() != null && !form.getPicture().isEmpty()){
            FileNameStruct fileNameStruct = fileService.uploadAssets(form.getPicture(), form.getNameen().replaceAll("\\s+",""), FileType.ACHIEVEMENTASSET);
            ach.setImgpath(fileNameStruct.realPath);
            ach.setImgurl(fileNameStruct.webAppPath);
        }
        this.achievementRepository.save(ach);

        for(String placesid : form.getAchievementPlaces()){
            this.achievementPlacesRepository.save(new AchievementPlaces(ach.getId(), placesid));
        }

        AchievementName achievementName = new AchievementName();
        achievementName.setId(ach.getId());
        achievementName.setEng(form.getNameen());
        achievementName.setPl(form.getNamepl());
        this.achievementNameRepository.save(achievementName);

        AchievementDescription achievementDescription = new AchievementDescription();
        achievementDescription.setId(ach.getId());
        achievementDescription.setEng(form.getDescriptionen());
        achievementDescription.setPl(form.getDescriptionpl());
        this.achievementDescriptionRepository.save(achievementDescription);
    }

    public List<Duo<String>> getListOfAllPlaces(){
        List<Duo<String>> list = new ArrayList<>();
        List<Place> all = this.placeRepository.findAll();
        String language = LocaleContextHolder.getLocale().getLanguage();
        for(Place p : all){
            PlaceName placeName = this.placeNameRepository.findById(p.getId()).get();
            String name;
            if(language.equals("pl"))
                name = placeName.getPl();
            else
                name = placeName.getEng();
            list.add(new Duo<String>(p.getId(), name));
        }
        return list;
    }
}
