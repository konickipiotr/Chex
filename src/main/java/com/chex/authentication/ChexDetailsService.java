package com.chex.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChexDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Autowired
    public ChexDetailsService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Auth> oAuth = this.authRepository.findByUsername(username);
        if(oAuth.isEmpty())
            throw new UsernameNotFoundException("Not found user: " + username);
        return oAuth.get();
    }
}
