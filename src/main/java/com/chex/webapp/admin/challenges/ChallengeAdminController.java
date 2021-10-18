//package com.chex.webapp.admin.challenges;
//
//import com.chex.modules.challenges_old.ChallengeService_old;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//
//@Controller
//@RequestMapping("/admin/challenges")
//public class ChallengeAdminController {
//
//    private ChallengeService_old challengeServiceOld;
//
//    @Autowired
//    public ChallengeAdminController(ChallengeService_old challengeServiceOld) {
//        this.challengeServiceOld = challengeServiceOld;
//    }
//
//    @GetMapping
//    public String showChallenges(Model model){
//        model.addAttribute("challenges" , challengeServiceOld.getChallengeShortListView());
//        return "admin/challenges/challenges";
//    }
//
//}
