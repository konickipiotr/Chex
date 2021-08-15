package com.chex.config;

import com.chex.authentication.AccountStatus;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.modules.category.Category;
import com.chex.modules.category.CategoryRepository;
import com.chex.modules.places.Place;
import com.chex.modules.places.PlaceRepository;
import com.chex.user.User;
import com.chex.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DbInit implements CommandLineRunner {

    private AuthRepository authRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private PlaceRepository placeRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public DbInit(AuthRepository authRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, PlaceRepository placeRepository, CategoryRepository categoryRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        this.authRepository.deleteAll();
        this.userRepository.deleteAll();

        Auth admin = new Auth("admin", passwordEncoder.encode("11"), "ADMIN", AccountStatus.ACTIVE);
        Auth user1 = new Auth("user1", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);

        this.authRepository.saveAll(Arrays.asList(admin, user1));

        User u1 = new User(user1.getId(), "Jan", "Nowak");
        this.userRepository.saveAll(Arrays.asList(u1));
    }

    private void initBaseData(){
        if(!this.categoryRepository.existsByEng("continent"))
            this.categoryRepository.save(new Category("kontynent", "continent"));

        if(!this.categoryRepository.existsByEng("country"))
            this.categoryRepository.save(new Category("państwo", "państwo"));

        if(!this.categoryRepository.existsByEng("province"))
            this.categoryRepository.save(new Category("prowincja", "province"));

        if(!this.categoryRepository.existsByEng("city"))
            this.categoryRepository.save(new Category("miasto", "city"));

        if(!this.categoryRepository.existsByEng("region"))
            this.categoryRepository.save(new Category("region", "region"));

        if(!this.categoryRepository.existsByEng("peak"))
            this.categoryRepository.save(new Category("szczyt", "peak"));



//        if(!this.categoryRepository.existsByCategory("")
//        if(!this.placeRepository.existsById("EU00000000000000"))
//            this.placeRepository.save(new Place("EU00000000000000", "Europa");)
//
//        Place p1 = new Place("EU00000000000000", "Europa");
//        Place p2 = new Place("AN00000000000000", "Ameryka Północna");
    }
}
