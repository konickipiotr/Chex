package com.chex.webapp.admin.achievements;

import com.chex.utils.Duo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/achievement")
public class AdminAchievementController {

    private final AchievementAdminService achievementAdminService;

    @Autowired
    public AdminAchievementController(AchievementAdminService achievementAdminService) {
        this.achievementAdminService = achievementAdminService;
    }

    @GetMapping
    public String showAchievements(Model model){
        model.addAttribute("aList", achievementAdminService.getShortViewList());
        return "admin/achievements/achievements";
    }

    @GetMapping("/new")
    public String toNewAchievement(Model model){
        List<Duo<String>> places = this.achievementAdminService.getListOfAllPlaces();
        //Map<String, String> places = this.achievementService.getListOfAllPlaces();
        model.addAttribute("achievementForm", new AchievementForm());
        model.addAttribute("places", places);
        return "admin/achievements/newachievement";
    }

    @PostMapping
    public String saveNewAchievement(AchievementForm achievementForm, Model model){
        if(achievementForm.getAchievementPlaces() == null || achievementForm.getAchievementPlaces().length < 2){
            model.addAttribute("err_msg", "Achievement musi składać się z conajmniej 2 miejsc");
            model.addAttribute("achievementForm", achievementForm);
            model.addAttribute("places", this.achievementAdminService.getListOfAllPlaces());
            return "admin/achievements/newachievement";
        }
        if(achievementAdminService.achievementExist(achievementForm)){
            model.addAttribute("err_msg", "Achievement o takiej nazwie już istnieje");
            model.addAttribute("achievementForm", achievementForm);
            model.addAttribute("places", this.achievementAdminService.getListOfAllPlaces());
            return "admin/achievements/newachievement";
        }

        achievementAdminService.saveAchievement(achievementForm);
        return "redirect:/admin/achievement";
    }
}
