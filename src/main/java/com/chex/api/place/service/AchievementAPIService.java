package com.chex.api.place.service;

import com.chex.modules.achievements.model.Achievement;
import com.chex.modules.achievements.model.AchievementName;
import com.chex.modules.achievements.model.AchievementPlaces;
import com.chex.modules.achievements.model.AchievementShortView;
import com.chex.modules.achievements.repository.AchievementDescriptionRepository;
import com.chex.modules.achievements.repository.AchievementNameRepository;
import com.chex.modules.achievements.repository.AchievementPlacesRepository;
import com.chex.modules.achievements.repository.AchievementRepository;
import com.chex.user.model.UserAchievements;
import com.chex.user.model.UsersAchievementsInProgress;
import com.chex.user.repository.UserAchievementsRepository;
import com.chex.user.repository.UsersAchievementsInProgressRepository;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AchievementAPIService {

    private final AchievementRepository achievementRepository;
    private final AchievementNameRepository achievementNameRepository;
    private final AchievementDescriptionRepository achievementDescriptionRepository;
    private final AchievementPlacesRepository achievementPlacesRepository;
    private final UserAchievementsRepository userAchievementsRepository;
    private final UsersAchievementsInProgressRepository usersAchievementsInProgressRepository;


    public AchievementAPIService(AchievementRepository achievementRepository, AchievementNameRepository achievementNameRepository, AchievementDescriptionRepository achievementDescriptionRepository, AchievementPlacesRepository achievementPlacesRepository, UserAchievementsRepository userAchievementsRepository, UsersAchievementsInProgressRepository usersAchievementsInProgressRepository) {
        this.achievementRepository = achievementRepository;
        this.achievementNameRepository = achievementNameRepository;
        this.achievementDescriptionRepository = achievementDescriptionRepository;
        this.achievementPlacesRepository = achievementPlacesRepository;
        this.userAchievementsRepository = userAchievementsRepository;
        this.usersAchievementsInProgressRepository = usersAchievementsInProgressRepository;
    }


    public List<AchievementShortView> checkAchievement(Set<String> placeIdSet, Long userid){
        List<AchievementShortView> aList = new ArrayList<>();
        for(String sId : placeIdSet){
            List<AchievementPlaces> achPlaces = this.achievementPlacesRepository.findByPlaceid(sId);
            if(achPlaces.isEmpty()) continue;

            for(AchievementPlaces ap : achPlaces){
                if(this.userAchievementsRepository.existsByUseridAndAchievementid(userid, ap.getAchievementid()))
                    continue;
                if(this.usersAchievementsInProgressRepository.existsByUseridAndAchievementidAndPlaceid(userid, ap.getAchievementid(), ap.getPlaceid()))
                    continue;
                if(checkNewAchievementAvailable(ap, userid)){
                    aList.add(prepareView(ap));
                }
            }
        }
        return  aList;
    }

    private AchievementShortView prepareView(AchievementPlaces ap){
        AchievementShortView view = new AchievementShortView();
        Long id = ap.getAchievementid();
        view.setId(id);
        String language = LocaleContextHolder.getLocale().getLanguage();
        AchievementName achievementName = this.achievementNameRepository.findById(id).get();
        switch (language){
            case "pl": view.setName(achievementName.getPl());break;
            default: view.setName(achievementName.getEng());
        }
        Achievement achievement = this.achievementRepository.findById(id).get();
        view.setImg(achievement.getImgurl());
        view.setPoints(achievement.getPoints());
        return view;
    }

    public boolean checkNewAchievementAvailable(AchievementPlaces ap, Long userid){
        Long achId = ap.getAchievementid();

        List<AchievementPlaces> achievementPlaces = this.achievementPlacesRepository.findByAchievementid(achId);
        int needed = achievementPlaces.size();

        List<UsersAchievementsInProgress> userInProg = this.usersAchievementsInProgressRepository.findByUseridAndAchievementid(userid, achId);

        if(userInProg.size()  == (needed - 1)){
            return true;
        }
        return false;
    }

    public int addPlaceToUserAchievements(Set<String> placeIdSet, Long userid){
        int exp = 0;
        for(String sId : placeIdSet){
            List<AchievementPlaces> achPlaces = this.achievementPlacesRepository.findByPlaceid(sId);
            if(achPlaces.isEmpty()) continue;

            for(AchievementPlaces ap : achPlaces){
                if(this.userAchievementsRepository.existsByUseridAndAchievementid(userid, ap.getAchievementid()))
                    continue;
                if(this.usersAchievementsInProgressRepository.existsByUseridAndAchievementidAndPlaceid(userid, ap.getAchievementid(), ap.getPlaceid()))
                    continue;
                if(checkNewAchievementAvailable(ap, userid)){
                    addAchievementToUserCollection(ap, userid);
                    Achievement achievement = this.achievementRepository.findById(ap.getAchievementid()).get();
                    exp += achievement.getPoints();
                }else {
                    this.usersAchievementsInProgressRepository.save(new UsersAchievementsInProgress(userid, ap.getAchievementid(), ap.getPlaceid(), LocalDateTime.now()));
                }
            }
        }
        return exp;
    }

    private void addAchievementToUserCollection(AchievementPlaces ap, Long userid){
        List<UsersAchievementsInProgress> inProgress = this.usersAchievementsInProgressRepository.findByUseridAndAchievementid(userid, ap.getAchievementid());
        this.userAchievementsRepository.save(new UserAchievements(userid, ap.getAchievementid(), LocalDateTime.now()));
        this.usersAchievementsInProgressRepository.deleteAll(inProgress);
    }
}
