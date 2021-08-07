package com.chex.config;

import com.chex.authentication.AccountStatus;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DbInit implements CommandLineRunner {

    private AuthRepository authRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DbInit(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        this.authRepository.deleteAll();

        Auth admin = new Auth("admin", passwordEncoder.encode("11"), "ADMIN", AccountStatus.ACTIVE);
        Auth user1 = new Auth("user1", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);

        this.authRepository.saveAll(Arrays.asList(admin, user1));
    }
}
