package com.chex.api.forgotpassword;

import com.chex.utils.StringHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forgotpassword")
public class ResetPasswordAPIControler {

    private ResetPasswordService resetPasswordService;


    @Autowired
    public ResetPasswordAPIControler(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> requestRestPassword(@RequestBody StringHolder holder) {
        return resetPasswordService.requestRestPassword(holder.getValue());
    }

    @GetMapping("/{code}")
    public ResponseEntity<PasswordForm> checkResetRequest(@PathVariable("code")String code){
        return resetPasswordService.checkResetRequest(code);
    }

    @PostMapping("/change")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordForm passwordForm){
        return resetPasswordService.changePassword(passwordForm);
    }
}
