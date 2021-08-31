package com.chex.config;

import com.chex.authentication.AccountStatus;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.modules.category.Category;
import com.chex.modules.category.CategoryRepository;
import com.chex.modules.places.*;
import com.chex.user.User;
import com.chex.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final PlaceDescriptionRepository  placeDescriptionRepository;

    @Autowired
    public DbInit(AuthRepository authRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, PlaceRepository placeRepository, CategoryRepository categoryRepository, PlaceNameRepository placeNameRepository, PlaceDescriptionRepository placeDescriptionRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
        this.placeNameRepository = placeNameRepository;
        this.placeDescriptionRepository = placeDescriptionRepository;
    }

    @Override
    public void run(String... args) {
        this.authRepository.deleteAll();
        this.userRepository.deleteAll();

        Auth admin = new Auth("admin", passwordEncoder.encode("11"), "ADMIN", AccountStatus.ACTIVE);
        Auth user1 = new Auth("user1", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);

        this.authRepository.saveAll(Arrays.asList(admin, user1));

        User u1 = new User(user1.getId(), "Jan", "Nowak");
        this.userRepository.saveAll(Collections.singletonList(u1));

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

        //COUNTRIES
        Place poland = new Place("AA.POL.000.000.00000");
        Place germany = new Place("AA.GER.000.000.00000");
        Place spain = new Place("AA.SPA.000.000.00000");
        Place italy = new Place("AA.ITL.000.000.00000");
        this.placeRepository.saveAll(Arrays.asList(poland, germany, spain, italy));

        this.placeNameRepository.save(new PlaceName(poland.getId(), "Polska", "Poland"));
        this.placeNameRepository.save(new PlaceName(germany.getId(), "Niemcy", "Germany"));
        this.placeNameRepository.save(new PlaceName(spain.getId(), "Hiszpania", "Spain"));
        this.placeNameRepository.save(new PlaceName(italy.getId(), "Włochy", "Italy"));

        this.placeDescriptionRepository.save(new PlaceDescription(poland.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(germany.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(spain.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(italy.getId(), "", ""));

        //PRIVINCES
        Place dolnoslaskie = new Place("AA.POL.DLS.000.00000");
        Place opolskie = new Place("AA.POL.OPL.000.00000");
        Place malopolskie = new Place("AA.POL.MAL.000.00000");
        Place wielkopolskie = new Place("AA.POL.WLP.000.00000");
        this.placeRepository.saveAll(Arrays.asList(dolnoslaskie, opolskie, malopolskie, wielkopolskie));

        this.placeNameRepository.save(new PlaceName(dolnoslaskie.getId(), "Dolnośląskie", "Dolnoslaskie"));
        this.placeNameRepository.save(new PlaceName(opolskie.getId(), "Opolskie", "Opolskie"));
        this.placeNameRepository.save(new PlaceName(malopolskie.getId(), "Małopolskie", "Malopolskie"));
        this.placeNameRepository.save(new PlaceName(wielkopolskie.getId(), "Wielkopolskie", "Wielkopolskie"));

        this.placeDescriptionRepository.save(new PlaceDescription(dolnoslaskie.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(opolskie.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(malopolskie.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(wielkopolskie.getId(), "", ""));

        Place region = new Place("AA.POL.DLS.REG.00000");
        Place wroclaw = new Place("AA.POL.DLS.WRO.00000");
        Place legnica = new Place("AA.POL.DLS.LEG.00000");
        Place stronie = new Place("AA.POL.DLS.STR.00000");
        this.placeRepository.saveAll(Arrays.asList(region, wroclaw, legnica, stronie));

        this.placeNameRepository.save(new PlaceName(region.getId(), "Region", "Region"));
        this.placeNameRepository.save(new PlaceName(wroclaw.getId(), "Wrocław", "Wroclaw"));
        this.placeNameRepository.save(new PlaceName(legnica.getId(), "Legnica", "Legnica"));
        this.placeNameRepository.save(new PlaceName(stronie.getId(), "Stronie Sląskie", "Stronie Slaskie"));

        this.placeDescriptionRepository.save(new PlaceDescription(region.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(wroclaw.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(legnica.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(stronie.getId(), "", ""));


        Place dom = new Place("AA.POL.DLS.WRO.00001");
        dom.setLatitude(51.11505245942457);
        dom.setLongitude(17.011892356597247);
        dom.setRadius(10);
        dom.setPoints(50);

        Place church = new Place("AA.POL.DLS.WRO.00002");
        church.setLatitude(51.115358289422275);
        church.setLongitude(17.01351628762617);
        church.setRadius(70);
        church.setPoints(60);

        Place trash = new Place("AA.POL.DLS.WRO.00003");
        trash.setLatitude(51.114834340788576);
        trash.setLongitude(17.010754042527363);
        trash.setRadius(10);
        trash.setPoints(10);

        Place someplace = new Place("AA.POL.DLS.WRO.00004");
        someplace.setLatitude(51.114851611073455);
        someplace.setLongitude(17.012198333585737);
        someplace.setRadius(10);
        someplace.setPoints(10);
        this.placeRepository.saveAll(Arrays.asList(dom, church, trash, someplace));

        this.placeNameRepository.save(new PlaceName(dom.getId(), "Dom", "Home"));
        this.placeNameRepository.save(new PlaceName(church.getId(), "Kościół", "Church"));
        this.placeNameRepository.save(new PlaceName(trash.getId(), "śmietnik", "Trash"));
        this.placeNameRepository.save(new PlaceName(someplace.getId(), "Jakieś miejsce", "Some place"));

        this.placeDescriptionRepository.save(new PlaceDescription(dom.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(church.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(trash.getId(), "", ""));
        this.placeDescriptionRepository.save(new PlaceDescription(someplace.getId(), "", ""));
    }
}
