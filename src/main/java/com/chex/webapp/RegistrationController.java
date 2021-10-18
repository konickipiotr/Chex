package com.chex.webapp;

import com.chex.api.AuthService;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.config.GlobalSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private AuthService authService;

    @Autowired
    public RegistrationController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String toRegistrationPage(){
        return "registration";
    }

    @GetMapping("/activation/{code}")
    public String checkResetCode(@PathVariable("code") String code, Principal principal, RedirectAttributes ra){
        RestTemplate restTemplate = new RestTemplate();
        String path = GlobalSettings.domain + "/api/registration/" + code;
        try {
            restTemplate.getForEntity(path, Void.class);
            ra.addAttribute("info_msg", "Konto zostało aktywowane");
        }catch (HttpClientErrorException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                ra.addAttribute("info_msg", "Błędny kod aktywacyny. Może został juz wykorzystany.");
            }else if(e.getStatusCode().is5xxServerError()){
                ra.addAttribute("error_msg", "Błąde serwera");
            }else {
                ra.addAttribute("error_msg", "Coś poszło nie tak");
            }
        }
        return "redirect:/login";
    }
}
