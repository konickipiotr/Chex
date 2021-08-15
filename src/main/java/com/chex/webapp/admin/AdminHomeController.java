package com.chex.webapp.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    @GetMapping
    public String adminHome(HttpSession session){
        return "admin/home";
    }
}
