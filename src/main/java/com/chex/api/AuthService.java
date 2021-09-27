package com.chex.api;

import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.user.User;
import com.chex.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        return this.userRepository.findById(getUserId(principal)).get();
    }
    public Auth getAuth(Principal principal){
        Optional<Auth> oUser = this.authRepository.findByUsername(principal.getName());
        if(oUser.isEmpty())
            throw new UsernameNotFoundException("User " + principal.getName() + " not found");

        return oUser.get();
    }

    public Auth findByUsername(String email){
        Optional<Auth> oUser = this.authRepository.findByUsername(email);
        if(oUser.isEmpty())
            throw new UsernameNotFoundException("User " + email + " not found");

        return oUser.get();
    }

    public RestTemplate configRestTemplate(String email){
        Optional<Auth> oAuth = this.authRepository.findByUsername(email);
        if(oAuth.isEmpty())
            throw new UsernameNotFoundException("User " + email + " not found");

        Auth user = oAuth.get();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    public void saveUser(User user){
        this.userRepository.save(user);
    }
}
