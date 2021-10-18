package com.chex.webapp.admin.challenges;

import com.chex.files.FileService;
import com.chex.modules.challenges.ChallengeLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/challenges/new")
public class NewChallengesAdminController {

    private FileService fileService;
    private NewChallengeAdminService newChallengeAdminService;

    @Autowired
    public NewChallengesAdminController(FileService fileService, NewChallengeAdminService newChallengeAdminService) {
        this.fileService = fileService;
        this.newChallengeAdminService = newChallengeAdminService;
    }

    @GetMapping
    public String showNewChallengesForm(Model model, HttpSession session) {
        session.removeAttribute("challengeForm");
        session.removeAttribute("checkpointList");

        ChallengeForm challengeForm = new ChallengeForm();
        challengeForm.setNamePl("80 do okoła świata");
        challengeForm.setNameEng("80 days");
        challengeForm.setPoints(20);
        challengeForm.setDescriptionPl("fdfdfd fdf d fd");
        challengeForm.setImgTemp("/tmp/chex/kPxEfiTbvfwSBZWmLbTe8JIe7xjeErJJVGBIa17f.png");
        model.addAttribute("challengeForm", challengeForm);
        //model.addAttribute("challengeForm", new ChallengeForm());
        model.addAttribute("challengeLevel", ChallengeLevel.values());
        return "admin/challenges/newchallenges";
    }

    @PostMapping("/uploadpicture")
    public String uploadPicture(MultipartFile picture, ChallengeForm challengeForm, Model model) {

        String temFileName = null;
        try {
            temFileName = fileService.saveToTmp(picture);
            challengeForm.setImgTemp(temFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        model.addAttribute("challengeLevel", ChallengeLevel.values());
        model.addAttribute("challengeForm", challengeForm);
        return "admin/challenges/newchallenges";
    }

    @PostMapping
    public String addChallengeSecondStep(ChallengeForm challengeForm, Model model, HttpSession session) {


        challengeForm.setSame();
        model.addAttribute("challengeForm", challengeForm);
        if (newChallengeAdminService.isNameExist(challengeForm)) {
            model.addAttribute("error_msg", "Wyzwanie o takiej nazwie już istnieje.");
            model.addAttribute("challengeForm", challengeForm);
            return "admin/challenges/newchallenges";
        }


        session.setAttribute("challengeForm", challengeForm);
        model.addAttribute("challengeLevel", ChallengeLevel.values());
        model.addAttribute("checkPointForm", new CheckPointForm("START"));
        return "admin/challenges/newcheckpoint";
    }

    @PostMapping(path = "/addcheckpoint", params = "action=add")
    public String addCheckpoint(CheckPointForm checkPointForm, Model model, HttpSession session) {

        ChallengeForm challengeForm = (ChallengeForm) session.getAttribute("challengeForm");
        List<CheckPointForm> checkPointFormList = (List<CheckPointForm>) session.getAttribute("checkpointList");
        if (checkPointFormList == null) {
            checkPointFormList = new ArrayList<>();
        }

        checkPointFormList.add(checkPointForm);
        session.setAttribute("checkpointList", checkPointFormList);

        int nextSeq = checkPointFormList.size();
        String suggestedName = "Checkpoint " + nextSeq;
        model.addAttribute("checkPointForm", new CheckPointForm(suggestedName));
        model.addAttribute("challengeForm", challengeForm);
        model.addAttribute("checkpointList", checkPointFormList);
        return "admin/challenges/newcheckpoint";
    }

    @PostMapping(path = "/addcheckpoint", params = "action!=add")
    public String addCheckpointAndSaveChallenge(@RequestParam("action") String action,  CheckPointForm checkPointForm, Model model, HttpSession session) {

        ChallengeForm challengeForm = (ChallengeForm) session.getAttribute("challengeForm");
        List<CheckPointForm> checkPointFormList = (List<CheckPointForm>) session.getAttribute("checkpointList");
        if(action.equals("addandfinish"))
            checkPointFormList.add(checkPointForm);

        if(checkPointFormList.size() < 2){
            session.setAttribute("checkpointList", checkPointFormList);
            int nextSeq = checkPointFormList.size();
            String suggestedName = "Checkpoint " + nextSeq;
            model.addAttribute("checkPointForm", new CheckPointForm(suggestedName));
            model.addAttribute("challengeForm", challengeForm);
            model.addAttribute("checkpointList", checkPointFormList);
            model.addAttribute("error_msg", "Wyzwanie musi składać się z conajmniej 2 punktów");
            return "admin/challenges/newcheckpoint";
        }

        newChallengeAdminService.saveChallenge(challengeForm, checkPointFormList);
        session.removeAttribute("challengeForm");
        session.removeAttribute("checkpointList");

        return "redirect:/admin/challenges";
    }

    @GetMapping("/addcheckpoint/{dir}/{seq}")
    public String addCheckpoint(@PathVariable("dir") String dir, @PathVariable("seq") int seq, Model model, HttpSession session) {

        ChallengeForm challengeForm = (ChallengeForm) session.getAttribute("challengeForm");
        List<CheckPointForm> checkPointFormList = (List<CheckPointForm>) session.getAttribute("checkpointList");

        if (seq != 0 && dir.equals("UP")) {
            Collections.swap(checkPointFormList, seq, seq - 1);
        } else if (seq != (checkPointFormList.size() - 1) && dir.equals("DOWN")) {
            Collections.swap(checkPointFormList, seq, seq + 1);
        }
        session.setAttribute("checkpointList", checkPointFormList);

        int nextSeq = checkPointFormList.size();
        String suggestedName = "Checkpoint " + nextSeq;
        model.addAttribute("checkPointForm", new CheckPointForm(suggestedName));
        model.addAttribute("challengeForm", challengeForm);
        model.addAttribute("checkpointList", checkPointFormList);
        return "admin/challenges/newcheckpoint";
    }

    @GetMapping("/addcheckpoint/delete/{seq}")
    public String addCheckpoint(@PathVariable("seq") int seq, Model model, HttpSession session) {
        ChallengeForm challengeForm = (ChallengeForm) session.getAttribute("challengeForm");
        List<CheckPointForm> checkPointFormList = (List<CheckPointForm>) session.getAttribute("checkpointList");
        checkPointFormList.remove(seq);

        session.setAttribute("checkpointList", checkPointFormList);

        int nextSeq = checkPointFormList.size();
        String suggestedName = "Checkpoint " + nextSeq;
        model.addAttribute("checkPointForm", new CheckPointForm(suggestedName));
        model.addAttribute("challengeForm", challengeForm);
        model.addAttribute("checkpointList", checkPointFormList);
        return "admin/challenges/newcheckpoint";
    }
}
