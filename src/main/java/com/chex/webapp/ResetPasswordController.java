package com.chex.webapp;

import com.chex.api.AuthService;
import com.chex.api.forgotpassword.PasswordForm;
import com.chex.config.GlobalSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/resetpassword")
public class ResetPasswordController {

    private AuthService authService;
    private RestTemplate restTemplate;

    @Autowired
    public ResetPasswordController(AuthService authService, RestTemplate restTemplate) {
        this.authService = authService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{code}")
    public String checkCode(@PathVariable("code") String code, Model model){
        String path = GlobalSettings.domain + "/api/forgotpassword/" + code;
        try {
            ResponseEntity<PasswordForm> response = restTemplate.getForEntity(path, PasswordForm.class);
            model.addAttribute("passwordForm", response.getBody());
        }catch (HttpClientErrorException e){
            setMessage(e, model);
            return "login";
        }
        return "resetpassword";
    }

    @PostMapping
    public String changePassword(PasswordForm passwordForm, Model model){
        String path = GlobalSettings.domain + "/api/forgotpassword/change";

        if(passwordForm.passwordsAreNotTheSame()){
            model.addAttribute("msg_err", "Hasła nie są takie same");
            return "resetpassword";
        }

        try {
            restTemplate.postForEntity(path, passwordForm, Void.class);
        }catch (HttpClientErrorException e){
            setMessage(e, model);
        }

        return "login";
    }

    private void setMessage(HttpClientErrorException e, Model model){
        if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            model.addAttribute("msg_info", "Błędny link do resetu hasła. Może został juz wykorzystany.");
        }else if(e.getStatusCode().is5xxServerError()){
            model.addAttribute("msg_err", "Błąde serwera");
        }else {
            model.addAttribute("msg_err", "Coś poszło nie tak");
        }
    }
}
