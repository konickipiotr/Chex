package com.chex.config;

import com.chex.api.forgotpassword.ResetCodeRepository;
import com.chex.api.post.PostVisibility;
import com.chex.api.registration.ActivationCodeRepository;
import com.chex.authentication.AccountStatus;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.modules.achievements.model.Achievement;
import com.chex.modules.achievements.model.AchievementDescription;
import com.chex.modules.achievements.model.AchievementName;
import com.chex.modules.achievements.model.AchievementPlaces;
import com.chex.modules.achievements.repository.AchievementDescriptionRepository;
import com.chex.modules.achievements.repository.AchievementNameRepository;
import com.chex.modules.achievements.repository.AchievementPlacesRepository;
import com.chex.modules.achievements.repository.AchievementRepository;
import com.chex.modules.category.Category;
import com.chex.modules.category.CategoryRepository;
import com.chex.modules.challenges.CheckpointType;
import com.chex.modules.challenges.model.Challenge;
import com.chex.modules.challenges.model.ChallengeDescription;
import com.chex.modules.challenges.model.ChallengeName;
import com.chex.modules.challenges.model.ChallengePoint;
import com.chex.modules.challenges.repository.ChallengeDescriptionRepository;
import com.chex.modules.challenges.repository.ChallengeNameRepository;
import com.chex.modules.challenges.repository.ChallengePointRepository;
import com.chex.modules.challenges.repository.ChallengeRepository;
import com.chex.modules.places.model.Place;
import com.chex.modules.places.model.PlaceDescription;
import com.chex.modules.places.model.PlaceName;
import com.chex.modules.places.repository.PlaceDescriptionRepository;
import com.chex.modules.places.repository.PlaceNameRepository;
import com.chex.modules.places.repository.PlaceRepository;
import com.chex.modules.post.model.Comment;
import com.chex.modules.post.model.Post;
import com.chex.modules.post.repository.CommentRepository;
import com.chex.modules.post.repository.PostPhotoRepository;
import com.chex.modules.post.repository.PostRepository;
import com.chex.user.FriendStatus;
import com.chex.user.model.User;
import com.chex.user.model.UserFriend;
import com.chex.user.model.VisitedPlace;
import com.chex.user.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class DbInit implements CommandLineRunner {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;
    private final PlaceNameRepository placeNameRepository;
    private final PlaceDescriptionRepository placeDescriptionRepository;
    private final VisitedPlacesRepository visitedPlacesRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ActivationCodeRepository activationCodeRepository;
    private final ResetCodeRepository resetCodeRepository;
    private final PostPhotoRepository postPhotoRepository;

    private final AchievementRepository achievementRepository;
    private final AchievementPlacesRepository achievementPlacesRepository;
    private final AchievementNameRepository achievementNameRepository;
    private final AchievementDescriptionRepository achievementDescriptionRepository;
    private final UserAchievementsRepository userAchievementsRepository;
    private final UsersAchievementsInProgressRepository usersAchievementsInProgressRepository;

    private final ChallengeRepository challengeRepository;
    private final ChallengePointRepository challengePointRepository;
    private final ChallengeNameRepository challengeNameRepository;
    private final ChallengeDescriptionRepository challengeDescriptionRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserChallengePointRepository userChallengePointRepository;

    private final UserFriendRepository userFriendRepository;

    private User u1, u2, u3, u4, u5;


    @Autowired
    public DbInit(AuthRepository authRepository, UserRepository userRepository, PasswordEncoder passwordEncoder,
                  PlaceRepository placeRepository, CategoryRepository categoryRepository,
                  PlaceNameRepository placeNameRepository, PlaceDescriptionRepository placeDescriptionRepository,
                  VisitedPlacesRepository visitedPlacesRepository, PostRepository postRepository,
                  CommentRepository commentRepository, ActivationCodeRepository activationCodeRepository,
                  ResetCodeRepository resetCodeRepository, PostPhotoRepository postPhotoRepository,
                  AchievementRepository achievementRepository, AchievementPlacesRepository achievementPlacesRepository,
                  AchievementNameRepository achievementNameRepository,
                  AchievementDescriptionRepository achievementDescriptionRepository,
                  UserAchievementsRepository userAchievementsRepository,
                  UsersAchievementsInProgressRepository usersAchievementsInProgressRepository,
                  ChallengeRepository challengeRepository, ChallengePointRepository challengePointRepository,
                  ChallengeNameRepository challengeNameRepository,
                  ChallengeDescriptionRepository challengeDescriptionRepository,
                  UserChallengeRepository userChallengeRepository,
                  UserChallengePointRepository userChallengePointRepository, UserFriendRepository userFriendRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
        this.placeNameRepository = placeNameRepository;
        this.placeDescriptionRepository = placeDescriptionRepository;
        this.visitedPlacesRepository = visitedPlacesRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.activationCodeRepository = activationCodeRepository;
        this.resetCodeRepository = resetCodeRepository;
        this.postPhotoRepository = postPhotoRepository;
        this.achievementRepository = achievementRepository;
        this.achievementPlacesRepository = achievementPlacesRepository;
        this.achievementNameRepository = achievementNameRepository;
        this.achievementDescriptionRepository = achievementDescriptionRepository;
        this.userAchievementsRepository = userAchievementsRepository;
        this.usersAchievementsInProgressRepository = usersAchievementsInProgressRepository;
        this.challengeRepository = challengeRepository;
        this.challengePointRepository = challengePointRepository;
        this.challengeNameRepository = challengeNameRepository;
        this.challengeDescriptionRepository = challengeDescriptionRepository;
        this.userChallengeRepository = userChallengeRepository;
        this.userChallengePointRepository = userChallengePointRepository;
        this.userFriendRepository = userFriendRepository;
    }

    @Override
    public void run(String... args) {
        this.authRepository.deleteAll();
        this.userRepository.deleteAll();
        this.userFriendRepository.deleteAll();

        Auth admin = new Auth("admin", passwordEncoder.encode("11"), "ADMIN", AccountStatus.ACTIVE);
        Auth user1 = new Auth("user1", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);
        Auth user2 = new Auth("user2", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);
        Auth user3 = new Auth("konicki.piotr@gmail.com", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);
        Auth user4 = new Auth("user4", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);
        Auth user5 = new Auth("user5", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);

        this.authRepository.saveAll(Arrays.asList(admin, user1, user2, user3, user4, user5));

        u1 = new User(user1.getId(), "Jan", "Nowak");
        u2 = new User(user2.getId(), "Anna", "Kowalska");
        u2.setGender("F");
        u3 = new User(user3.getId(), "Piotr", "Konicki");
        u4 = new User(user4.getId(), "Monika", "Malina");
        u5 = new User(user5.getId(), "Marek", "Kowal");
        this.userRepository.saveAll(Arrays.asList(u1, u2, u3, u4, u5));

        this.userFriendRepository.save(new UserFriend(u2.getId(), u1.getId(), FriendStatus.YOUINVITED));
        this.userFriendRepository.save(new UserFriend(u1.getId(), u2.getId(), FriendStatus.INVITEDYOU));

        this.userFriendRepository.save(new UserFriend(u1.getId(), u3.getId(), FriendStatus.YOUINVITED));
        this.userFriendRepository.save(new UserFriend(u3.getId(), u1.getId(), FriendStatus.INVITEDYOU));


        this.userFriendRepository.save(new UserFriend(u1.getId(), u4.getId(), FriendStatus.BANNED));
        this.userFriendRepository.save(new UserFriend(u4.getId(), u1.getId(), FriendStatus.BLOCKED));



        savePlacesForTesting();
        initBaseData();
    }

    private void initBaseData(){
        if(!this.categoryRepository.existsByEng("continent"))
            this.categoryRepository.save(new Category("kontynent", "continent"));

        if(!this.categoryRepository.existsByEng("country"))
            this.categoryRepository.save(new Category("państwo", "country"));

        if(!this.categoryRepository.existsByEng("province"))
            this.categoryRepository.save(new Category("prowincja", "province"));

        if(!this.categoryRepository.existsByEng("city"))
            this.categoryRepository.save(new Category("miasto", "city"));

        if(!this.categoryRepository.existsByEng("region"))
            this.categoryRepository.save(new Category("region", "region"));

        if(!this.categoryRepository.existsByEng("peak"))
            this.categoryRepository.save(new Category("szczyt", "peak"));


        Place europa = new Place("EU.000.000.000.00000");
        europa.setImg("/assets/places/EU00000000000000.png");
        if(!this.placeRepository.existsById("EU.000.000.000.00000")){
            this.placeRepository.save(europa);
            this.placeNameRepository.save(new PlaceName(europa.getId(), "Europa", "Europe"));
            this.placeDescriptionRepository.save(new PlaceDescription(europa.getId(), "", ""));
        }

        Place northAmerica = new Place("NA.000.000.000.00000");
        northAmerica.setImg("/assets/places/NA00000000000000.png");
        if(!this.placeRepository.existsById("NA.000.000.000.00000")){
            this.placeRepository.save(northAmerica);
            this.placeNameRepository.save(new PlaceName(northAmerica.getId(), "Ameryka Północna", "North America"));
            this.placeDescriptionRepository.save(new PlaceDescription(northAmerica.getId(), "", ""));
        }

        Place southAmerica = new Place("SA.000.000.000.00000");
        southAmerica.setImg("/assets/places/SA00000000000000.png");
        if(!this.placeRepository.existsById("SA.000.000.000.00000")){
            this.placeRepository.save(southAmerica);
            this.placeNameRepository.save(new PlaceName(southAmerica.getId(), "Ameryka Południowa", "South America"));
            this.placeDescriptionRepository.save(new PlaceDescription(southAmerica.getId(), "", ""));
        }

        Place africa = new Place("AF.000.000.000.00000");
        africa.setImg("/assets/places/AF00000000000000.png");
        if(!this.placeRepository.existsById("AF.000.000.000.00000")){
            this.placeRepository.save(africa);
            this.placeNameRepository.save(new PlaceName(africa.getId(), "Afryka", "Africa"));
            this.placeDescriptionRepository.save(new PlaceDescription(africa.getId(), "", ""));
        }

        Place asia = new Place("AS.000.000.000.00000");
        asia.setImg("/assets/places/AS00000000000000.png");
        if(!this.placeRepository.existsById("AS.000.000.000.00000")){
            this.placeRepository.save(asia);
            this.placeNameRepository.save(new PlaceName(asia.getId(), "Azja", "Asia"));
            this.placeDescriptionRepository.save(new PlaceDescription(asia.getId(), "", ""));
        }

        Place australia = new Place("AU.000.000.000.00000");
        australia.setImg("/assets/places/AU00000000000000.png");
        if(!this.placeRepository.existsById("AU.000.000.000.00000")){
            this.placeRepository.save(australia);
            this.placeNameRepository.save(new PlaceName(australia.getId(), "Australia", "Australia"));
            this.placeDescriptionRepository.save(new PlaceDescription(australia.getId(), "", ""));
        }

        Place antarctica = new Place("AA.000.000.000.00000");
        if(!this.placeRepository.existsById("AA.000.000.000.00000")){
            this.placeRepository.save(australia);
            this.placeNameRepository.save(new PlaceName(antarctica.getId(), "Antarktyda", "Antarctica"));
            this.placeDescriptionRepository.save(new PlaceDescription(antarctica.getId(), "", ""));
        }
    }

    private void savePlacesForTesting(){

        this.placeRepository.deleteAll();
        this.placeDescriptionRepository.deleteAll();
        this.placeNameRepository.deleteAll();
        this.visitedPlacesRepository.deleteAll();
        this.postRepository.deleteAll();
        this.commentRepository.deleteAll();
        this.activationCodeRepository.deleteAll();
        this.resetCodeRepository.deleteAll();
        this.postPhotoRepository.deleteAll();

        achievementRepository.deleteAll();
        achievementPlacesRepository.deleteAll();
        achievementNameRepository.deleteAll();
        achievementDescriptionRepository.deleteAll();
        userAchievementsRepository.deleteAll();
        usersAchievementsInProgressRepository.deleteAll();

        this.challengeRepository.deleteAll();
        this.challengePointRepository.deleteAll();
        this.challengeNameRepository.deleteAll();
        this.challengeDescriptionRepository.deleteAll();
        this.userChallengeRepository.deleteAll();
        this.userChallengePointRepository.deleteAll();


        //COUNTRIES
        Place poland = new Place("EU.POL.000.000.00000");
        poland.setImg("/assets/places/EUPOL00000000000.png");
        Place germany = new Place("EU.GER.000.000.00000");
        Place spain = new Place("EU.SPA.000.000.00000");
        Place italy = new Place("EU.ITL.000.000.00000");
        this.placeRepository.saveAll(Arrays.asList(poland, germany, spain, italy));

        this.placeNameRepository.save(new PlaceName(poland.getId(), "Polska", "Poland"));
        this.placeNameRepository.save(new PlaceName(germany.getId(), "Niemcy", "Germany"));
        this.placeNameRepository.save(new PlaceName(spain.getId(), "Hiszpania", "Spain"));
        this.placeNameRepository.save(new PlaceName(italy.getId(), "Włochy", "Italy"));

        this.placeDescriptionRepository.save(new PlaceDescription(poland.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(germany.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(spain.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(italy.getId(), "", ""));

        //PROVINCES
        Place dolnoslaskie = new Place("EU.POL.DLS.000.00000");
        dolnoslaskie.setImg("/assets/places/EUPOLDLS00000000.png");
        Place opolskie = new Place("EU.POL.OPL.000.00000");
        Place malopolskie = new Place("EU.POL.MAL.000.00000");
        Place wielkopolskie = new Place("EU.POL.WLP.000.00000");
        this.placeRepository.saveAll(Arrays.asList(dolnoslaskie, opolskie, malopolskie, wielkopolskie));

        this.placeNameRepository.save(new PlaceName(dolnoslaskie.getId(), "Dolnośląskie", "Dolnoslaskie"));
        this.placeNameRepository.save(new PlaceName(opolskie.getId(), "Opolskie", "Opolskie"));
        this.placeNameRepository.save(new PlaceName(malopolskie.getId(), "Małopolskie", "Malopolskie"));
        this.placeNameRepository.save(new PlaceName(wielkopolskie.getId(), "Wielkopolskie", "Wielkopolskie"));

        this.placeDescriptionRepository.save(new PlaceDescription(dolnoslaskie.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(opolskie.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(malopolskie.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(wielkopolskie.getId(), "", ""));

        Place region = new Place("EU.POL.DLS.REG.00000");
        Place wroclaw = new Place("EU.POL.DLS.WRO.00000");
        wroclaw.setImg("/assets/places/EUPOLDLSWRO00000.png");
        Place legnica = new Place("EU.POL.DLS.LEG.00000");
        Place stronie = new Place("EU.POL.DLS.STR.00000");

        Place region2 = new Place("EU.POL.MAL.REG.00000");
        Place krakow = new Place("EU.POL.MAL.KRK.00000");

        this.placeRepository.saveAll(Arrays.asList(region, wroclaw, legnica, stronie, region2, krakow));

        this.placeNameRepository.save(new PlaceName(region.getId(), "Region", "Region"));
        this.placeNameRepository.save(new PlaceName(wroclaw.getId(), "Wrocław", "Wroclaw"));
        this.placeNameRepository.save(new PlaceName(legnica.getId(), "Legnica", "Legnica"));
        this.placeNameRepository.save(new PlaceName(stronie.getId(), "Stronie Sląskie", "Stronie Slaskie"));
        this.placeNameRepository.save(new PlaceName(region2.getId(), "Region", "Region"));
        this.placeNameRepository.save(new PlaceName(krakow.getId(), "Kraków", "Cracow"));

        this.placeDescriptionRepository.save(new PlaceDescription(region.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(wroclaw.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(legnica.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(stronie.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(region2.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(krakow.getId(), "", ""));

        Place kopiecKraka = new Place("EU.POL.MAL.KRK.00001");
        kopiecKraka.setLatitude(50.03803603952346);
        kopiecKraka.setLongitude(19.95853994569327);
        kopiecKraka.setRadius(60);
        kopiecKraka.setPoints(25);

        Place kopiecKosciuszki = new Place("EU.POL.MAL.KRK.00002");
        kopiecKosciuszki.setLatitude(50.05499153972385);
        kopiecKosciuszki.setLongitude(19.89337378342057);
        kopiecKosciuszki.setRadius(60);
        kopiecKosciuszki.setPoints(25);

        Place kopiecKopiecWandy = new Place("EU.POL.MAL.KRK.00003");
        kopiecKopiecWandy.setLatitude(50.070249355415775);
        kopiecKopiecWandy.setLongitude(20.068072243561463);
        kopiecKopiecWandy.setRadius(60);
        kopiecKopiecWandy.setPoints(25);

        Place odrariver = new Place("EU.POL.DLS.WRO.00006");
        odrariver.setLatitude(51.111252368679075);
        odrariver.setLongitude(17.048359420006754);
        odrariver.setPoints(15);
        odrariver.setRadius(70);

        Place fredrostatue = new Place("EU.POL.DLS.WRO.00007");
        fredrostatue.setLatitude(51.10967218306398);
        fredrostatue.setLongitude(17.03130839352053);
        fredrostatue.setRadius(20);
        fredrostatue.setPoints(30);


        Place nationalmuseum = new Place("EU.POL.DLS.WRO.00005");
        nationalmuseum.setLatitude(51.11056175173228);
        nationalmuseum.setLongitude(17.04744525485578);
        nationalmuseum.setRadius(70);
        nationalmuseum.setPoints(25);
        this.placeRepository.saveAll(Arrays.asList(nationalmuseum, odrariver, fredrostatue, kopiecKraka, kopiecKosciuszki, kopiecKopiecWandy));

        this.placeNameRepository.save(new PlaceName(nationalmuseum.getId(), "Muzeum narodowoe", "National museum"));
        this.placeNameRepository.save(new PlaceName(odrariver.getId(), "Rzeka Odra", "Odra river"));
        this.placeNameRepository.save(new PlaceName(fredrostatue.getId(), "Pomnik Fredry", "Fredro statue"));
        this.placeNameRepository.save(new PlaceName(kopiecKraka.getId(), "Kopiec  Kraka", "Kopiec  Kraka"));
        this.placeNameRepository.save(new PlaceName(kopiecKosciuszki.getId(), "Kopiec  Kościuszki", "Kopiec  Kościuszki"));
        this.placeNameRepository.save(new PlaceName(kopiecKopiecWandy.getId(), "Kopiec  Wandy", "Kopiec  Wandy"));

        this.placeDescriptionRepository.save(new PlaceDescription(nationalmuseum.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(odrariver.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(fredrostatue.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(kopiecKraka.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(kopiecKosciuszki.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(kopiecKopiecWandy.getId(), "", ""));



        //////////////////////////////////////////////////////////////////////////////////////
        Challenge challenge = new Challenge();
        challenge.setPoints(50);
        challenge.setTimelimit(20000);
        this.challengeRepository.save(challenge);
        this.challengeNameRepository.save(new ChallengeName(challenge.getId(), "Wyzwanie rynkowe", "Market square challenge"));
        this.challengeDescriptionRepository.save(new ChallengeDescription(challenge.getId(), "cześć", "hello"));

        ChallengePoint p1 = new ChallengePoint();
        p1.setChallengeid(challenge.getId());
        p1.setSeq(0);
        p1.setName("START - Iglica");

        p1.setLatitude(51.1094);
        p1.setLongitude(17.0294);
        p1.setRadius(40);
        p1.setCheckpointtype(CheckpointType.START);
        p1.setIsplace(false);

        ChallengePoint p2 = new ChallengePoint();
        p2.setChallengeid(challenge.getId());
        p2.setSeq(1);
        p2.setIsplace(true);
        p2.setPlaceid(fredrostatue.getId());
        p2.setCheckpointtype(CheckpointType.CHECKPOINT);

        ChallengePoint p3 = new ChallengePoint();
        p3.setChallengeid(challenge.getId());
        p3.setSeq(2);
        p3.setName("END - Pręgierz");
        p3.setLatitude(51.10944148170755);
        p3.setLongitude(17.032670713640492);
        p3.setRadius(40);
        p3.setIsplace(false);
        p3.setCheckpointtype(CheckpointType.FINISH);
        this.challengePointRepository.saveAll(Arrays.asList(p1, p2, p3));


        VisitedPlace v1 = new VisitedPlace(u1.getId());
        v1.setPlaceid(nationalmuseum.getId());
        v1.setVdate(LocalDateTime.of(2021, 7, 20, 11, 30));
        v1.setRating(5);

        VisitedPlace v2 = new VisitedPlace(u1.getId());
        v2.setPlaceid("EU.POL.DLS.WRO.00000");
        v2.setVdate(LocalDateTime.of(2021, 7, 20, 11, 30));

        VisitedPlace v3 = new VisitedPlace(u1.getId());
        v3.setPlaceid("EU.POL.DLS.000.00000");
        v3.setVdate(LocalDateTime.of(2021, 7, 20, 11, 30));

        VisitedPlace v4 = new VisitedPlace(u1.getId());
        v4.setPlaceid("EU.POL.000.000.00000");
        v4.setVdate(LocalDateTime.of(2021, 7, 20, 11, 30));

        VisitedPlace v5 = new VisitedPlace(u1.getId());
        v5.setPlaceid("EU.000.000.000.00000");
        v5.setVdate(LocalDateTime.of(2021, 7, 20, 11, 30));

        VisitedPlace v6 = new VisitedPlace(u2.getId());
        v6.setPlaceid(fredrostatue.getId());
        v6.setVdate(LocalDateTime.of(2021, 8, 16, 16, 10));
        v6.setRating(3);

        VisitedPlace v7 = new VisitedPlace(u1.getId());
        v7.setPlaceid(odrariver.getId());
        v7.setVdate(LocalDateTime.of(2021, 7, 20, 15, 30));
        v7.setRating(3);

        this.visitedPlacesRepository.saveAll(Arrays.asList(v1, v2, v3, v4, v5, v6, v7));

        Post post1 = new Post();
        post1.setUserid(u1.getId());
        post1.setDescription("Super miejsce");
        post1.setCreated(v1.getVdate());
        post1.setPlaces(nationalmuseum.getId());
        post1.setSubplaces("EU.000.000.000.00000:EU.POL.000.000.00000:EU.POL.DLS.000.00000:EU.POL.DLS.WRO.00000");
        post1.setStanum(0);
        post1.setPostvisibility(PostVisibility.PUBLIC);

        Post post2 = new Post();
        post2.setUserid(u1.getId());
        post2.setDescription("Tu byłem");
        post2.setCreated(v6.getVdate());
        post2.setPlaces(fredrostatue.getId());
        post2.setPostvisibility(PostVisibility.PUBLIC);

        Post post3 = new Post();
        post3.setUserid(u2.getId());
        post3.setDescription("Tu byłem");
        post3.setCreated(v7.getVdate());
        post3.setPlaces(fredrostatue.getId());
        post3.setPostvisibility(PostVisibility.PUBLIC);

        this.postRepository.saveAll(Arrays.asList(post1, post2, post3));

        Comment c1 = new Comment(post1.getId(), u2.getId(), "no nieźle :) ", LocalDateTime.of(2021, 7, 21, 8, 30));
        Comment c2 = new Comment(post1.getId(), u1.getId(), "Aaa dziękuję ", LocalDateTime.of(2021, 7, 21, 9, 00));
        Comment c3 = new Comment(post1.getId(), u2.getId(), "Więcej zdjęć proszę", LocalDateTime.of(2021, 7, 21, 9, 15));
        Comment c33 = new Comment(post1.getId(), u1.getId(), "OK", LocalDateTime.of(2021, 7, 21, 10, 15));

        Comment c4 = new Comment(post2.getId(), u2.getId(), "Też tam byłam. 10/10 ;) ", LocalDateTime.of(2021, 8, 16, 18, 10));

        Comment c5 = new Comment(post3.getId(), u1.getId(), "Piekne miejsce. Czy to rynke", LocalDateTime.of(2021, 7, 20, 20, 30));
        Comment c6 = new Comment(post3.getId(), u2.getId(), "Tak", LocalDateTime.of(2021, 7, 20, 21, 30));
        this.commentRepository.saveAll(Arrays.asList(c1, c2, c3, c33, c4, c5, c6));

        Achievement kopceAch = new Achievement();
        kopceAch.setPoints(50);
        kopceAch.setImg("https://s.twojahistoria.pl/uploads/2018/05/Trzy_kopce_Krakowskie-mini.jpg");
        this.achievementRepository.save(kopceAch);

        this.achievementPlacesRepository.save(new AchievementPlaces(kopceAch.getId(), kopiecKraka.getId()));
        this.achievementPlacesRepository.save(new AchievementPlaces(kopceAch.getId(), kopiecKosciuszki.getId()));
        this.achievementPlacesRepository.save(new AchievementPlaces(kopceAch.getId(), kopiecKopiecWandy.getId()));

        this.achievementNameRepository.save(new AchievementName(kopceAch.getId(), "ach1", "ach1"));
        this.achievementDescriptionRepository.save(new AchievementDescription(kopceAch.getId(), "", ""));
    }
}
