package com.chex.api;

import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.user.User;
import com.chex.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(AuthRepository authRepository, UserRepository userRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }

    public Long getUserId(Principal principal){
        Optional<Auth> oUser = this.authRepository.findByUsername(principal.getName());
        if(oUser.isEmpty())
            throw new UsernameNotFoundException("User " + principal.getName() + " not found");

        return oUser.get().getId();
    }

    public User getUser(Principal principal){
        return this.userRepository.getById(getUserId(principal));
    }
}
