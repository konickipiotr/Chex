package com.chex.webapp;

import com.chex.api.post.PostsResponse;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.config.GlobalSettings;
import com.chex.modules.post.repository.PostRepository;
import com.chex.user.User;
import com.chex.user.UserRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public HomeController(AuthRepository authRepository, UserRepository userRepository, RestTemplate restTemplate) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/login")
    public String login(String msg_info, String msg_err, Model model){
        model.addAttribute("msg_info", msg_info);
        model.addAttribute("msg_err", msg_err);
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
    public String userHome(Principal principal, Model model, HttpSession session){
        Optional<Auth> oAuth = this.authRepository.findByUsername(principal.getName());
        Auth auth = oAuth.get();
        User user = this.userRepository.getById(auth.getId());
        session.setAttribute("user", user);

        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(auth.getUsername(), auth.getPassword()));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String path = GlobalSettings.domain + "/api/post/public/1";

        ResponseEntity<PostsResponse> response = restTemplate.getForEntity(path, PostsResponse.class);

        model.addAttribute("posts", response.getBody().getPosts());
        return "user/home";
    }
}
