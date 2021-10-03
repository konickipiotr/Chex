package com.chex.api.profle;

import com.chex.config.AppStats;
import com.chex.lang.LanguageGetter;
import com.chex.lang.LanguageUtils;
import com.chex.modules.achievements.model.*;
import com.chex.modules.achievements.repository.AchievementDescriptionRepository;
import com.chex.modules.achievements.repository.AchievementNameRepository;
import com.chex.modules.achievements.repository.AchievementPlacesRepository;
import com.chex.modules.achievements.repository.AchievementRepository;
import com.chex.modules.places.model.Place;
import com.chex.modules.places.model.PlaceDescription;
import com.chex.modules.places.model.PlaceName;
import com.chex.modules.places.model.PlaceView;
import com.chex.modules.places.repository.PlaceDescriptionRepository;
import com.chex.modules.places.repository.PlaceNameRepository;
import com.chex.modules.places.repository.PlaceRepository;
import com.chex.user.model.User;
import com.chex.user.model.UserAchievements;
import com.chex.user.model.UsersAchievementsInProgress;
import com.chex.user.model.VisitedPlace;
import com.chex.user.repository.UserAchievementsRepository;
import com.chex.user.repository.UserRepository;
import com.chex.user.repository.UsersAchievementsInProgressRepository;
import com.chex.user.repository.VisitedPlacesRepository;
import com.chex.utils.IdUtils;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final PlaceRepository placeRepository;
    private final PlaceNameRepository placeNameRepository;
    private final PlaceDescriptionRepository placeDescriptionRepository;
    private final VisitedPlacesRepository visitedPlacesRepository;

    private final AchievementRepository achievementRepository;
    private final AchievementPlacesRepository achievementPlacesRepository;
    private final AchievementNameRepository achievementNameRepository;
    private final AchievementDescriptionRepository achievementDescriptionRepository;
    private final UserAchievementsRepository userAchievementsRepository;
    private final UsersAchievementsInProgressRepository usersAchievementsInProgressRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfileService(PlaceRepository placeRepository, PlaceNameRepository placeNameRepository, PlaceDescriptionRepository placeDescriptionRepository, VisitedPlacesRepository visitedPlacesRepository, AchievementRepository achievementRepository, AchievementPlacesRepository achievementPlacesRepository, AchievementNameRepository achievementNameRepository, AchievementDescriptionRepository achievementDescriptionRepository, UserAchievementsRepository userAchievementsRepository, UsersAchievementsInProgressRepository usersAchievementsInProgressRepository, UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.placeNameRepository = placeNameRepository;
        this.placeDescriptionRepository = placeDescriptionRepository;
        this.visitedPlacesRepository = visitedPlacesRepository;
        this.achievementRepository = achievementRepository;
        this.achievementPlacesRepository = achievementPlacesRepository;
        this.achievementNameRepository = achievementNameRepository;
        this.achievementDescriptionRepository = achievementDescriptionRepository;
        this.userAchievementsRepository = userAchievementsRepository;
        this.usersAchievementsInProgressRepository = usersAchievementsInProgressRepository;
        this.userRepository = userRepository;
    }

    public ProfileResponse getProfileInfo(Long userid, String plevel){
        Optional<User> oUser = this.userRepository.findById(userid);
        if(oUser.isEmpty())
            throw new UsernameNotFoundException("user id " + userid);
        User user = oUser.get();

        ProfileResponse pr = new ProfileResponse();
        pr.setAllPlaces(AppStats.placesNum);
        pr.setAllAchievements(AppStats.achievementNum);

        List<VisitedPlace> visitedPlacesList = this.visitedPlacesRepository.findByUserid(userid);

        List<UserAchievements> useAchievements = this.userAchievementsRepository.findByUserid(userid);
        pr.setPlaceNum(visitedPlacesList.size());
        pr.setAchNum(useAchievements.size());
        pr.setPlaces(getVisitedPlacesListView(visitedPlacesList, plevel));
        pr.setLastThree(getLastThree(visitedPlacesList));
        pr.setAchievementViews(getAchievementsViewList(userid, CompleteStatus.COMPLETE));
        pr.setAchInProgress(getAchievementsViewList(userid, CompleteStatus.INPROGRESS));

        return pr;
    }

    private List<PlaceView> getVisitedPlacesListView(List<VisitedPlace> vp, String plevel){

        List<Place> list;
        switch (plevel.length()){
            case 2: list = this.placeRepository.getAllCountriesFromContinent(plevel); break;
            case 3: list = this.placeRepository.getContinents(); break;
            case 6: list = this.placeRepository.getAllProvincesFromCountry(plevel); break;
            case 10: list = this.placeRepository.getRegionsFromProvince(plevel); break;
            default:
                throw new IllegalArgumentException();
        }
        return prepare(vp, list, plevel);
    }

    private List<PlaceView> getLastThree(List<VisitedPlace> vp){
        List<PlaceView> list = new ArrayList<>();
        List<VisitedPlace> newVp = new ArrayList<>();

        Collections.sort(vp);
        int count = 0;
        for(VisitedPlace v : vp){
            if(!v.getPlaceid().endsWith(".00000")){
                count++;
                newVp.add(v);
            }
            if(count == 3)
                break;
        }

        for(VisitedPlace v : newVp){
            Optional<Place> oPlace = this.placeRepository.findById(v.getPlaceid());
            if(oPlace.isEmpty())
                continue;
            list.add(buildPlaceView(oPlace.get(), v));
        }
        return list;
    }

    private PlaceView buildPlaceView(Place place, VisitedPlace vp){
        PlaceView view = new PlaceView();
        view.setId(vp.getPlaceid());
        view.setSubplace(false);
        view.setAchievedAt(vp.getVdate());
        view.setImg(place.getImgurl());
        view.setPoints(place.getPoints());
        view.setSvg(place.getSvgpath());
        view.setUserRating(vp.getRating());
        view.setUsersReached(this.visitedPlacesRepository.countByPlaceid(place.getId()));
        view.setPlaceRating(place.getRating());
        view.setCompleteStatus(CompleteStatus.COMPLETE);

        LanguageUtils languageUtils = new LanguageUtils(
                this.placeNameRepository.findById(place.getId()).get(),
                this.placeDescriptionRepository.findById(place.getId()).get());

        view.setName(languageUtils.getName());
        view.setDescription(languageUtils.getDescription());

        return view;
    }

    private List<PlaceView> prepare(List<VisitedPlace> vp, List<Place> all, String plevel){
        List<PlaceView> list = new ArrayList<>();

        for(Place p : all){
            PlaceView view = new PlaceView();
            view.setId(p.getId());
            view.setImg(p.getImgurl());
            view.setSvg(p.getSvgpath());
            view.setPoints(p.getPoints());
            view.setDifficultLvl(p.getDifficultylevel());
            view.setPlaceRating(p.getRating());
            view.setUsersReached(this.visitedPlacesRepository.countByPlaceid(p.getId()));

            if(p.getId().endsWith(".00000"))
                view.setSubplace(true);

            LanguageUtils languageUtils = new LanguageUtils(
                    this.placeNameRepository.findById(p.getId()).get(),
                    this.placeDescriptionRepository.findById(p.getId()).get());

            view.setName(languageUtils.getName());
            view.setDescription(languageUtils.getDescription());

            for(VisitedPlace visited : vp){
                if(visited.getPlaceid().equals(p.getId())){
                    view.setAchievedAt(visited.getVdate());
                    view.setUserRating(visited.getRating());
                    if(view.isSubplace()){
                        view.setCompleteStatus(isRegionCompleted(vp, visited));
                    }else {
                        view.setCompleteStatus(CompleteStatus.COMPLETE);
                    }
                    break;
                }
            } //for

            list.add(view);
        }
        return list;
    }

    private CompleteStatus isRegionCompleted(List<VisitedPlace> vpList, VisitedPlace visited){
        String id = IdUtils.extractId(visited.getPlaceid());
        List<Place> allChildren = this.placeRepository.getAllChildrenPlaces(id);

        int count = 0;
        for(Place ch : allChildren){
            boolean ok = false;
            for(VisitedPlace v : vpList){
                if(v.getPlaceid().equals(ch.getId())){
                    count++;
                    break;
                }
            }
        }

        if(count == allChildren.size())
            return CompleteStatus.COMPLETE;
        if(count == 0)
            return CompleteStatus.NOTSTARTED;
        return CompleteStatus.INPROGRESS;
    }

    private List<AchievementView> getAchievementsViewList(Long userid, CompleteStatus completeStatus){
        List<AchievementView> list = new ArrayList<>();

        if(completeStatus.equals(CompleteStatus.COMPLETE)){
            List<UserAchievements> userAchievements = this.userAchievementsRepository.findByUserid(userid);
            for(UserAchievements a: userAchievements){
                Achievement achievement = this.achievementRepository.findById(a.getAchievementid()).get();
                AchievementView view = buildAchievementView(achievement);
                view.setAchievedAt(a.getAchievedat());
                view.setCompleteStatus(completeStatus);
                list.add(view);
            }
        }else if(completeStatus.equals(CompleteStatus.INPROGRESS)){
            List<UsersAchievementsInProgress> inProgress = this.usersAchievementsInProgressRepository.findByUserid(userid);
            Set<Long> ids = inProgress.stream().map(i -> i.getAchievementid()).collect(Collectors.toSet());

            for(Long id : ids){
                Achievement achievement = this.achievementRepository.findById(id).get();
                AchievementView view = buildAchievementView(achievement);
                view.setAllPlacesNum(this.achievementPlacesRepository.countByAchievementid(id));
                view.setUserPlacesNum((int)inProgress.stream().filter(i -> i.getAchievementid().equals(id)).count());
                view.setCompleteStatus(completeStatus);
                list.add(view);
            }
        }else {
            throw new NotYetImplementedException(); //TODO?
        }
        return list;
    }

    private AchievementView buildAchievementView(Achievement achievement){
        AchievementView view = new AchievementView();

        view.setId(achievement.getId());
        view.setPoints(achievement.getPoints());
        view.setImg(achievement.getImgurl());
        view.setUsersReached(this.userAchievementsRepository.countByAchievementid(achievement.getId()));


        LanguageUtils languageUtils = new LanguageUtils(
                this.achievementNameRepository.findById(achievement.getId()).get(),
                this.achievementDescriptionRepository.findById(achievement.getId()).get());
        view.setName(languageUtils.getName());
        view.setDescription(languageUtils.getDescription());
        return view;
    }
}
