package com.chex.api.forgotpassword;

import com.chex.api.AuthService;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.config.GlobalSettings;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class ResetPasswordService {

    private final static int RESET_CODE_LENGTH = 20;

    private AuthRepository authRepository;
    private ResetCodeRepository resetCodeRepository;
    private JavaMailSender javaMailSender;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ResetPasswordService(AuthRepository authRepository, ResetCodeRepository resetCodeRepository, JavaMailSender javaMailSender, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.resetCodeRepository = resetCodeRepository;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Void> requestRestPassword(String email){
        Optional<Auth> optionalAuth = authRepository.findByUsername(email);
        if(optionalAuth.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Auth auth = optionalAuth.get();
        String resetCode = RandomString.make(RESET_CODE_LENGTH);
        this.resetCodeRepository.save(new ResetCode(auth.getId(), resetCode));
        sendResetLink(auth.getUsername(), resetCode);
        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    private void sendResetLink(String email, String resetCode) {

        String link = GlobalSettings.domain + "/resetpassword/" + resetCode;

        String message = " Chex!\n\n" +
                "Kliknij w link poniżej aby zresetować hasło \n" + link;

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setSubject("Chex - reset hasła");
        emailMessage.setText(message);

        javaMailSender.send(emailMessage);
    }

    public ResponseEntity<PasswordForm> checkResetRequest(String code){

        Optional<ResetCode> oResetcode = resetCodeRepository.findByResetcode(code);
        if(oResetcode.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PasswordForm pf = new PasswordForm();
        pf.setCode(oResetcode.get().getResetcode());
        return new ResponseEntity<>(pf, HttpStatus.OK);
    }

    public ResponseEntity<Void> changePassword(PasswordForm pf){
        String code = pf.getCode();
        Optional<ResetCode> optionalResetCode = this.resetCodeRepository.findByResetcode(code);
        if(optionalResetCode.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        ResetCode resetCode = optionalResetCode.get();
        Auth auth = this.authRepository.getById(resetCode.getUserid());
        auth.setPassword(passwordEncoder.encode(pf.getPassword1()));
        this.authRepository.save(auth);
        this.resetCodeRepository.deleteById(auth.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
