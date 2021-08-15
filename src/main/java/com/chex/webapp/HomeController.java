package com.chex.webapp;

import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    private final AuthRepository authRepository;

    public HomeController(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/init")
    public String postLogin(Principal principal, HttpServletRequest request){
        if(principal == null)
            return "redirect:/logout";

        Optional<Auth> oUser = this.authRepository.findByUsername(principal.getName());
        if(oUser.isEmpty())
            return "/login";

        Auth auth = oUser.get();
        String role = auth.getRole();
        if(role.equals("ADMIN"))
            return "redirect:/admin";
        if(role.equals("USER"))
            return "redirect:/";

        throw new RuntimeException("Role: " + role + " is wrong");
    }

    @GetMapping
    public String userHome(){
        return "user/home";
    }
}
