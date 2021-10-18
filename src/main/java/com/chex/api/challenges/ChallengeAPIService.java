package com.chex.api.challenges;

import com.chex.api.place.AchievedPlaceDTO;
import com.chex.api.place.response.CheckPlaceRequest;
import com.chex.api.place.response.CheckPlaceResponse;
import com.chex.api.place.response.CheckPlaceResponseStatus;
import com.chex.api.place.service.CalculateCoords;
import com.chex.lang.LanguageUtils;
import com.chex.modules.challenges.CheckpointType;
import com.chex.modules.challenges.model.Challenge;
import com.chex.modules.challenges.model.ChallengePoint;
import com.chex.modules.challenges.repository.ChallengeDescriptionRepository;
import com.chex.modules.challenges.repository.ChallengeNameRepository;
import com.chex.modules.challenges.repository.ChallengePointRepository;
import com.chex.modules.challenges.repository.ChallengeRepository;
import com.chex.modules.challenges.view.ChallengeShortView;
import com.chex.modules.challenges.view.ChallengeView;
import com.chex.modules.challenges.view.CheckpointView;
import com.chex.modules.places.model.Coords;
import com.chex.modules.places.model.Place;
import com.chex.modules.places.repository.PlaceNameRepository;
import com.chex.modules.places.repository.PlaceRepository;
import com.chex.user.ChallengeStatus;
import com.chex.user.model.UserChallenge;
import com.chex.user.model.UserChallengePoint;
import com.chex.user.repository.UserChallengePointRepository;
import com.chex.user.repository.UserChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChallengeAPIService {

    private final ChallengeRepository challengeRepository;
    private final ChallengePointRepository challengePointRepository;
    private final ChallengeNameRepository challengeNameRepository;
    private final ChallengeDescriptionRepository challengeDescriptionRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserChallengePointRepository userChallengePointRepository;
    private final PlaceRepository placeRepository;
    private final PlaceNameRepository placeNameRepository;

    @Autowired
    public ChallengeAPIService(ChallengeRepository challengeRepository, ChallengePointRepository challengePointRepository, ChallengeNameRepository challengeNameRepository, ChallengeDescriptionRepository challengeDescriptionRepository, UserChallengeRepository userChallengeRepository, UserChallengePointRepository userChallengePointRepository, PlaceRepository placeRepository, PlaceNameRepository placeNameRepository) {
        this.challengeRepository = challengeRepository;
        this.challengePointRepository = challengePointRepository;
        this.challengeNameRepository = challengeNameRepository;
        this.challengeDescriptionRepository = challengeDescriptionRepository;
        this.userChallengeRepository = userChallengeRepository;
        this.userChallengePointRepository = userChallengePointRepository;
        this.placeRepository = placeRepository;
        this.placeNameRepository = placeNameRepository;
    }

    public ResponseEntity<UserChallengeResponse> selectChallenge(Long challengeId, Long userId) {
        UserChallengeResponse response = new UserChallengeResponse();
        Optional<Challenge> oChallenge = this.challengeRepository.findById(challengeId);
        if(oChallenge.isEmpty())
            return new ResponseEntity<>(new UserChallengeResponse(ReturnCode.CHALLENGE_NOT_FOUND), HttpStatus.OK);

        Challenge challenge = oChallenge.get();
        List<UserChallenge> userChallenges = this.userChallengeRepository.findByUseridAndChallengeid(userId, challengeId);
        if(isChallengeOngoing(userChallenges))
            return new ResponseEntity<>(new UserChallengeResponse(ReturnCode.CHALLENGE_ONGOING), HttpStatus.OK);

        UserChallenge uChallenge = new UserChallenge(challenge, userId);
        uChallenge.setAttemptnum(userChallenges.size() + 1);
        List<ChallengePoint> points = this.challengePointRepository.findByChallengeid(challengeId);
        this.userChallengeRepository.save(uChallenge);

        List<UserChallengePoint> userPoints = points.stream()
                .map(i -> new UserChallengePoint(uChallenge.getId(), i, userId))
                .collect(Collectors.toList());

        this.userChallengePointRepository.saveAll(userPoints);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean isChallengeOngoing(List<UserChallenge> userChallenges){

        long count = userChallenges
                .stream()
                .filter(i -> i.getStatus().equals(ChallengeStatus.NOTSTARTED) || i.getStatus().equals(ChallengeStatus.ONGOING))
                .count();
        return count != 0;
    }

    public ResponseEntity<UserChallengeResponse> removeUserChallenge(Long challengeId, Long userId) {
        Optional<UserChallenge> oUserChallenge = this.userChallengeRepository.findById(challengeId);
        if(oUserChallenge.isEmpty())
            return new ResponseEntity<>(new UserChallengeResponse(ReturnCode.CHALLENGE_NOT_FOUND), HttpStatus.OK);

        UserChallenge userChallenge = oUserChallenge.get();
        if(!userId.equals(userChallenge.getUserid()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        this.userChallengePointRepository.deleteByUserchallengeid(userChallenge.getId());
        this.userChallengeRepository.delete(userChallenge);
        return new ResponseEntity<>(new UserChallengeResponse(), HttpStatus.OK);
    }

    public ResponseEntity<UserChallengeResponse> getListOfAvailableChallenges(Long userId) {
        List<Challenge> all = this.challengeRepository.findAll();
        List<UserChallenge> userAll = this.userChallengeRepository.findByUserid(userId);

        Set<Long> ids = userAll.stream()
                .filter(i -> !i.getStatus().equals(ChallengeStatus.FAILURE))
                .map(UserChallenge::getChallengeid)
                .collect(Collectors.toSet());

        List<Challenge> filtredChallenges = all.stream()
                .filter(i -> !ids.contains(i.getId()))
                .collect(Collectors.toList());

        List<ChallengeShortView> list = new ArrayList<>();
        for(Challenge ch : filtredChallenges){
            ChallengeShortView view = new ChallengeShortView(ch);
            view.setName(new LanguageUtils(this.challengeNameRepository.findById(ch.getId()).get()).getName());
            view.setUsersComplete(numberOfUsersCompletedChallenge(ch.getId()));
            list.add(view);
        }
        UserChallengeResponse response = new UserChallengeResponse();
        response.setAvailable(list);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private long numberOfUsersCompletedChallenge(Long challengeid){
        List<UserChallenge> userCompleted = this.userChallengeRepository.getUserCompleted(challengeid);
        return userCompleted.stream().map(UserChallenge::getUserid).count();
    }


    public ResponseEntity<UserChallengeResponse> getUserChallengesInfo(Long userId){
        List<UserChallenge> userChallengeList = this.userChallengeRepository.findByUserid(userId);
        if(userChallengeList.isEmpty())
            return new ResponseEntity<>(new UserChallengeResponse(ReturnCode.CHALLENGE_NOT_FOUND), HttpStatus.OK);

        List<ChallengeView> inProgress = new ArrayList<>();
        List<ChallengeView> complete = new ArrayList<>();
        for(UserChallenge ch : userChallengeList){
            ChallengeView challengeView = prepareChallengeView(ch);
            if(challengeView.getStatus().equals(ChallengeStatus.COMPETED)
                    || challengeView.getStatus().equals(ChallengeStatus.FAILURE)){
                complete.add(challengeView);
            }else {
                inProgress.add(challengeView);
            }
        }
        Collections.sort(inProgress);
        Collections.sort(complete);
        UserChallengeResponse response = new UserChallengeResponse();
        response.setInProgress(inProgress);
        response.setCompleted(complete);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ChallengeView prepareChallengeView(UserChallenge uch){
        long challengeId = uch.getChallengeid();
        ChallengeView challengeView = new ChallengeView(uch);
        Optional<Challenge> oChallenge = this.challengeRepository.findById(challengeId);
        if(oChallenge.isPresent()){
            Challenge challenge = oChallenge.get();
            challengeView.fillFromChallenge(challenge);
        }
        LanguageUtils languageUtils = new LanguageUtils(
                this.challengeNameRepository.findById(challengeId).get(),
                this.challengeDescriptionRepository.findById(challengeId).get());

        challengeView.setName(languageUtils.getName());
        challengeView.setDescription(languageUtils.getDescription());
        challengeView.setUsersComplete(numberOfUsersCompletedChallenge(challengeId));

        List<ChallengePoint> chPoints = this.challengePointRepository.findByChallengeid(challengeId);
        List<UserChallengePoint> userPoints = this.userChallengePointRepository.findByUserchallengeid(uch.getId());

        challengeView.setNumOfCheckpoints(chPoints.size());
        challengeView.setCurrentNumOfCheckpoints((int)userPoints
                .stream()
                .filter(i -> i.getStatus().equals(ChallengeStatus.COMPETED))
                .count());
        challengeView.setPoints(prepareUserCheckpoints(languageUtils, chPoints, userPoints));
        return challengeView;
    }

    private List<CheckpointView> prepareUserCheckpoints(LanguageUtils languageUtils, List<ChallengePoint> chPoints, List<UserChallengePoint> userPoints) {
        List<CheckpointView> checkpoints = new ArrayList<>();
        for(UserChallengePoint uPoint : userPoints){
            CheckpointView view = new CheckpointView(uPoint);
            view.setChallengeName(languageUtils.getName());
            ChallengePoint challengePoint = chPoints.stream().filter(i -> i.getId().equals(uPoint.getCheckpointid())).findFirst().get();
            view.setSeq(challengePoint.getSeq());

            if(challengePoint.isIsplace())
                fillCoordsFromPlace(view, challengePoint);
            else
                view.fillCoordsFromChallengePoint(challengePoint);

            checkpoints.add(view);
        }
        Collections.sort(checkpoints);
        return checkpoints;
    }

    private void fillCoordsFromPlace(CheckpointView view, ChallengePoint challengePoint){
        String placeid = challengePoint.getPlaceid();
        Optional<Place> oPlace = this.placeRepository.findById(placeid);
        if(oPlace.isEmpty()){
            //TODO
        }else {
            Place place = oPlace.get();
            view.setLongitude(place.getLongitude());
            view.setLatitude(place.getLatitude());
            view.setRadius(place.getRadius());
            view.setName(new LanguageUtils(this.placeNameRepository.findById(placeid).get()).getName());
        }
    }

    public void checkIfAnyChallengePointIsInArea(CheckPlaceResponse response, CheckPlaceRequest request, Long userId) {
        List<UserChallengePoint> ongoingList = getListOfOnGoingChallengePoints(userId);
        List<ChallengePoint> pointsInTheAre = getPointsInTheArea(request.getCoords(), ongoingList);
        if(pointsInTheAre.isEmpty())
            return;

        ongoingList = extractAreaPointsFromOngoing(pointsInTheAre, ongoingList);

        List<CheckpointView> checkpoints = new ArrayList<>();
        List<ChallengeShortView> challenges = new ArrayList<>();

        for(UserChallengePoint up : ongoingList){
            ChallengePoint challengePoint = pointsInTheAre
                    .stream()
                    .filter(i -> i.getId().equals(up.getCheckpointid()))
                    .findFirst().get();
            UserChallenge userChallenge = this.userChallengeRepository.findById(up.getUserchallengeid()).get();
            Challenge challenge = this.challengeRepository.findById(up.getChallengeid()).get();
            switch (up.getCheckpointtype()){
                case START: updateUserChallengeOnFirstPoint(userChallenge, request.getTimestamp());
                case CHECKPOINT:
                    updateUserChallengePoint(up);
                    checkpoints.add(prepareCheckpointView(up, challengePoint, challenge));
                    updateStatusOfNextUserPoint(challengePoint, userChallenge);
                    break;
                case FINISH:
                    CheckpointView view = prepareCheckpointView(up, challengePoint, challenge);
                    view.setStatus(ChallengeStatus.COMPETED);
                    view.setReachedat(LocalDateTime.now());
                    checkpoints.add(view);
                    challenges.add(prepareChallengeShortView(challenge, userChallenge));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        response.setResponseStatus(CheckPlaceResponseStatus.FOUND);
        response.setCheckpointViewList(checkpoints);
        response.setUserShortChallengeViews(challenges);
    }

    private ChallengeShortView prepareChallengeShortView(Challenge challenge, UserChallenge userChallenge) {
        ChallengeShortView chView = new ChallengeShortView(challenge);
        chView.setName(new LanguageUtils(this.challengeNameRepository.findById(challenge.getId()).get()).getName());
        chView.setUsersComplete(numberOfUsersCompletedChallenge(challenge.getId()));
        chView.setUserchallengeid(userChallenge.getId());
        return chView;
    }

    private void updateUserChallengePoint(UserChallengePoint userChallengePoint){
        userChallengePoint.setReachedat(LocalDateTime.now());
        userChallengePoint.setStatus(ChallengeStatus.COMPETED);
        this.userChallengePointRepository.save(userChallengePoint);
    }

    private void updateStatusOfNextUserPoint(ChallengePoint challengePoint, UserChallenge userChallenge){
        int nextSeq = challengePoint.getSeq() + 1;
        ChallengePoint nextChallengePoint = this.challengePointRepository.findByChallengeidAndSeq(challengePoint.getChallengeid(), nextSeq).get();
        List<UserChallengePoint> allUserCheckpoint = this.userChallengePointRepository.findByUserchallengeid(userChallenge.getId());

        UserChallengePoint userNextPoint = allUserCheckpoint
                .stream()
                .filter(i -> i.getCheckpointid().equals(nextChallengePoint.getId())
                        && i.getUserchallengeid().equals(userChallenge.getId()))
                .findFirst().get();
        userNextPoint.setStatus(ChallengeStatus.ONGOING);
        this.userChallengePointRepository.save(userNextPoint);
    }

    private CheckpointView prepareCheckpointView(UserChallengePoint userPoint, ChallengePoint challengePoint, Challenge challenge){
        CheckpointView view = new CheckpointView(userPoint);
        view.setChallengeName(new LanguageUtils(this.challengeNameRepository.findById(userPoint.getChallengeid()).get()).getName());
        view.setSeq(challengePoint.getSeq());
        view.setImg(challenge.getImg());

        if(challengePoint.isIsplace())
            fillCoordsFromPlace(view, challengePoint);
        else
            view.fillCoordsFromChallengePoint(challengePoint);
        return view;
    }

    private void updateUserChallengeOnFirstPoint(UserChallenge userChallenge, LocalDateTime startTime){
        userChallenge.setStatus(ChallengeStatus.ONGOING);
        userChallenge.setStartTime(startTime);
        this.userChallengeRepository.save(userChallenge);
    }

    private List<UserChallengePoint> extractAreaPointsFromOngoing(List<ChallengePoint> points, List<UserChallengePoint> ongoing ){
        List<Long> finalIds = points.stream().map(ChallengePoint::getId).collect(Collectors.toList());
        return ongoing.stream().filter(i -> finalIds.contains(i.getCheckpointid())).collect(Collectors.toList());
    }

    private List<ChallengePoint> getPointsInTheArea(Coords coords, List<UserChallengePoint> ongoingList){
        List<ChallengePoint> challengePoints = new ArrayList<>();
        if(ongoingList == null || ongoingList.isEmpty())
            return challengePoints;
        List<Long> ids = ongoingList.stream().map(UserChallengePoint::getCheckpointid).collect(Collectors.toList());
        challengePoints = this.challengePointRepository.findAllById(ids);
        challengePoints.forEach(i -> {
            if(i.isIsplace())
                fillChallengePointFromPlace(i);
        });

        return CalculateCoords.isChallengePointInRange(coords, challengePoints);
    }

    private void fillChallengePointFromPlace(ChallengePoint point){
        String placeid = point.getPlaceid();
        Optional<Place> oPlace = this.placeRepository.findById(placeid);
        if(oPlace.isPresent()){
            Place place = oPlace.get();
            point.setName(new LanguageUtils(this.placeNameRepository.findById(placeid).get()).getName());
            point.setLatitude(place.getLatitude());
            point.setLongitude(place.getLongitude());
            point.setRadius(place.getRadius());
        }
    }

    private List<UserChallengePoint> getListOfOnGoingChallengePoints(Long userId){
        List<UserChallengePoint> startList = this.userChallengePointRepository.findByUseridAndStatusAndCheckpointtype(userId, ChallengeStatus.NOTSTARTED, CheckpointType.START);
        List<UserChallengePoint> ongoingList = this.userChallengePointRepository.findByUseridAndStatus(userId, ChallengeStatus.ONGOING);
        startList.addAll(ongoingList);
        return startList;
    }

    public int finalizeChallenge(AchievedPlaceDTO achievedPlaceDTO) {
        List<Long> ids = achievedPlaceDTO.getUserChallengesComplete();
        int exp = 0;
        if(ids == null || ids.isEmpty())
            return exp;

        List<UserChallenge> userChallenges = this.userChallengeRepository.findAllById(ids);
        for(UserChallenge uch : userChallenges){
            exp += this.challengeRepository.findById(uch.getChallengeid()).get().getPoints();
            this.userChallengePointRepository.deleteByUserchallengeid(uch.getId());
            uch.setEndTime(achievedPlaceDTO.getTimestamp());
            uch.setStatus(ChallengeStatus.COMPETED);
        }
        return exp;
    }
}
