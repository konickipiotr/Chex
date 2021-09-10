package com.chex.config;

import com.chex.api.post.PostVisibility;
import com.chex.authentication.AccountStatus;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.modules.category.Category;
import com.chex.modules.category.CategoryRepository;
import com.chex.modules.places.model.Place;
import com.chex.modules.places.model.PlaceDescription;
import com.chex.modules.places.model.PlaceName;
import com.chex.modules.places.repository.PlaceDescriptionRepository;
import com.chex.modules.places.repository.PlaceNameRepository;
import com.chex.modules.places.repository.PlaceRepository;
import com.chex.modules.post.model.Comment;
import com.chex.modules.post.model.Post;
import com.chex.modules.post.repository.CommentRepository;
import com.chex.modules.post.repository.PostRepository;
import com.chex.user.User;
import com.chex.user.UserRepository;
import com.chex.user.place.VisitedPlace;
import com.chex.user.place.VisitedPlacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

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

    private User u1;
    private User u2;

    @Autowired
    public DbInit(AuthRepository authRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, PlaceRepository placeRepository, CategoryRepository categoryRepository, PlaceNameRepository placeNameRepository, PlaceDescriptionRepository placeDescriptionRepository, VisitedPlacesRepository visitedPlacesRepository, PostRepository postRepository, CommentRepository commentRepository) {
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
    }

    @Override
    public void run(String... args) {
        this.authRepository.deleteAll();
        this.userRepository.deleteAll();

        Auth admin = new Auth("admin", passwordEncoder.encode("11"), "ADMIN", AccountStatus.ACTIVE);
        Auth user1 = new Auth("user1", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);
        Auth user2 = new Auth("user2", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);

        this.authRepository.saveAll(Arrays.asList(admin, user1, user2));

        u1 = new User(user1.getId(), "Jan", "Nowak");
        u1.setImgurl("/photos/profile.png");
        u2 = new User(user2.getId(), "Anna", "Kowalska");
        this.userRepository.saveAll(Arrays.asList(u1, u2));

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
        europa.setImgurl("/photos/eurpa.JPG");
        if(!this.placeRepository.existsById("EU.000.000.000.00000")){
            this.placeRepository.save(europa);
            this.placeNameRepository.save(new PlaceName(europa.getId(), "Europa", "Europe"));
            this.placeDescriptionRepository.save(new PlaceDescription(europa.getId(), "", ""));
        }

        Place northAmerica = new Place("NA.000.000.000.00000");
        if(!this.placeRepository.existsById("NA.000.000.000.00000")){
            this.placeRepository.save(northAmerica);
            this.placeNameRepository.save(new PlaceName(northAmerica.getId(), "Ameryka Północna", "North America"));
            this.placeDescriptionRepository.save(new PlaceDescription(northAmerica.getId(), "", ""));
        }

        Place southAmerica = new Place("SA.000.000.000.00000");
        if(!this.placeRepository.existsById("SA.000.000.000.00000")){
            this.placeRepository.save(southAmerica);
            this.placeNameRepository.save(new PlaceName(southAmerica.getId(), "Ameryka Południowa", "South America"));
            this.placeDescriptionRepository.save(new PlaceDescription(southAmerica.getId(), "", ""));
        }

        Place africa = new Place("AF.000.000.000.00000");
        if(!this.placeRepository.existsById("AF.000.000.000.00000")){
            this.placeRepository.save(africa);
            this.placeNameRepository.save(new PlaceName(africa.getId(), "Afryka", "Africa"));
            this.placeDescriptionRepository.save(new PlaceDescription(africa.getId(), "", ""));
        }

        Place asia = new Place("AS.000.000.000.00000");
        if(!this.placeRepository.existsById("AS.000.000.000.00000")){
            this.placeRepository.save(asia);
            this.placeNameRepository.save(new PlaceName(asia.getId(), "Azja", "Asia"));
            this.placeDescriptionRepository.save(new PlaceDescription(asia.getId(), "", ""));
        }

        Place australia = new Place("AU.000.000.000.00000");
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

        //COUNTRIES
        Place poland = new Place("EU.POL.000.000.00000");
        poland.setImgurl("/photos/poland.JPG");
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
        dolnoslaskie.setImgurl("/photos/dolnośląskie.JPG");
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
        wroclaw.setImgurl("/photos/wroclaw.JPG");
        Place legnica = new Place("EU.POL.DLS.LEG.00000");
        Place stronie = new Place("EU.POL.DLS.STR.00000");
        this.placeRepository.saveAll(Arrays.asList(region, wroclaw, legnica, stronie));

        this.placeNameRepository.save(new PlaceName(region.getId(), "Region", "Region"));
        this.placeNameRepository.save(new PlaceName(wroclaw.getId(), "Wrocław", "Wroclaw"));
        this.placeNameRepository.save(new PlaceName(legnica.getId(), "Legnica", "Legnica"));
        this.placeNameRepository.save(new PlaceName(stronie.getId(), "Stronie Sląskie", "Stronie Slaskie"));

        this.placeDescriptionRepository.save(new PlaceDescription(region.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(wroclaw.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(legnica.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(stronie.getId(), "", ""));


        Place church = new Place("EU.POL.DLS.WRO.00002");
        church.setLatitude(51.115358289422275);
        church.setLongitude(17.01351628762617);
        church.setRadius(70);
        church.setPoints(60);

        Place trash = new Place("EU.POL.DLS.WRO.00003");
        trash.setLatitude(51.114834340788576);
        trash.setLongitude(17.010754042527363);
        trash.setRadius(10);
        trash.setPoints(10);

        Place someplace = new Place("EU.POL.DLS.WRO.00004");
        someplace.setLatitude(51.114851611073455);
        someplace.setLongitude(17.012198333585737);
        someplace.setRadius(10);
        someplace.setPoints(10);


        Place nationalmuseum = new Place("EU.POL.DLS.WRO.00005");
        nationalmuseum.setLatitude(51.11056175173228);
        nationalmuseum.setLongitude(17.04744525485578);
        nationalmuseum.setRadius(70);
        nationalmuseum.setPoints(25);
        nationalmuseum.setImgurl("/photos/nationalmuseum.JPG");

        Place odrariver = new Place("EU.POL.DLS.WRO.00006");

        odrariver.setLatitude(51.111252368679075);
        odrariver.setLongitude(17.048359420006754);
        odrariver.setPoints(15);
        odrariver.setRadius(70);
        odrariver.setImgurl("/photos/odrariver.JPG");

        Place fredrostatue = new Place("EU.POL.DLS.WRO.00007");
        fredrostatue.setLatitude(51.10967218306398);
        fredrostatue.setLongitude(17.03130839352053);
        fredrostatue.setRadius(20);
        fredrostatue.setPoints(30);
        fredrostatue.setImgurl("/photos/fredromonument.JPG");
        this.placeRepository.saveAll(Arrays.asList(church, trash, someplace, nationalmuseum, odrariver, fredrostatue));

        this.placeNameRepository.save(new PlaceName(church.getId(), "Kościół", "Church"));
        this.placeNameRepository.save(new PlaceName(trash.getId(), "śmietnik", "Trash"));
        this.placeNameRepository.save(new PlaceName(someplace.getId(), "Jakieś miejsce", "Some place"));
        this.placeNameRepository.save(new PlaceName(nationalmuseum.getId(), "Muzeum narodowoe", "National museum"));
        this.placeNameRepository.save(new PlaceName(odrariver.getId(), "Rzeka Odra", "Odra river"));
        this.placeNameRepository.save(new PlaceName(fredrostatue.getId(), "Pomnik Fredry", "Fredro statue"));

        this.placeDescriptionRepository.save(new PlaceDescription(church.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(trash.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(someplace.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(nationalmuseum.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(odrariver.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(fredrostatue.getId(), "", ""));

        VisitedPlace v1 = new VisitedPlace(u1.getId());
        v1.setPlaceid(nationalmuseum.getId());
        v1.setVdate(LocalDateTime.of(2021, 7, 20, 11, 30));
        v1.setRating(5);

        VisitedPlace v2 = new VisitedPlace(u1.getId());
        v1.setPlaceid("EU.POL.DLS.WRO.00000");
        v1.setVdate(LocalDateTime.of(2021, 7, 20, 11, 30));

        VisitedPlace v3 = new VisitedPlace(u1.getId());
        v1.setPlaceid("EU.POL.DLS.000.00000");
        v1.setVdate(LocalDateTime.of(2021, 7, 20, 11, 30));

        VisitedPlace v4 = new VisitedPlace(u1.getId());
        v1.setPlaceid("EU.POL.000.000.00000");
        v1.setVdate(LocalDateTime.of(2021, 7, 20, 11, 30));

        VisitedPlace v5 = new VisitedPlace(u1.getId());
        v1.setPlaceid("EU.000.000.000.00000");
        v1.setVdate(LocalDateTime.of(2021, 7, 20, 11, 30));

        VisitedPlace v6 = new VisitedPlace(u1.getId());
        v1.setPlaceid(fredrostatue.getId());
        v1.setVdate(LocalDateTime.of(2021, 8, 16, 16, 10));
        v1.setRating(3);

        VisitedPlace v7 = new VisitedPlace(u2.getId());
        v1.setPlaceid(odrariver.getId());
        v1.setVdate(LocalDateTime.of(2021, 7, 20, 15, 30));
        v1.setRating(3);

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
    }
}
