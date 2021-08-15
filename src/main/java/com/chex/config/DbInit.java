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
}
