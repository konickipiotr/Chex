package com.chex.api.registration;

import com.chex.authentication.AccountStatus;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.config.GlobalSettings;
import com.chex.user.User;
import com.chex.user.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class RegistrationService {

    public final int ACTIVATION_CODE_LENGTH = 40;

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivationCodeRepository activationCodeRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public RegistrationService(AuthRepository authRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, ActivationCodeRepository activationCodeRepository, JavaMailSender javaMailSender) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.activationCodeRepository = activationCodeRepository;
        this.javaMailSender = javaMailSender;
    }

    public ResponseEntity<Void> registerUser(RegistrationForm form){
        Optional<Auth> oAuth = this.authRepository.findByUsername(form.getEmail());
        if(oAuth.isPresent())
            return new ResponseEntity<>(HttpStatus.FOUND);

        Auth aUser = new Auth();
        aUser.setUsername(form.getEmail());
        aUser.setPassword(passwordEncoder.encode(form.getPassword()));
        aUser.setCreated(LocalDateTime.now());
        aUser.setAccountStatus(AccountStatus.INACTIVE);
        aUser.setRole("USER");
        this.authRepository.save(aUser);

        User user = new User();
        user.setFirstname(form.getFirstname());
        user.setLastname(form.getLastname());
        user.setId(aUser.getId());
        this.userRepository.save(user);

        String activationCode = RandomString.make(ACTIVATION_CODE_LENGTH);
        ActivationCode activation = new ActivationCode(aUser.getId(), activationCode);
        this.activationCodeRepository.save(activation);
        sendActivationLink(aUser.getUsername(), activationCode);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void sendActivationLink(String email, String activationCode) {

        String link = GlobalSettings.domain + "/registration/activation/" + activationCode;

        String message = "Witaj w Chex!\n\n" +
                "Kliknij w link poniżej aby aktywować konto \n" + link;

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setSubject("Chex - aktywacja");
        emailMessage.setText(message);

        javaMailSender.send(emailMessage);
    }

    public ResponseEntity<Void> activateAccount(String code){
        Optional<ActivationCode> oActivationCode = this.activationCodeRepository.findByActivationcode(code);
        if(oActivationCode.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        ActivationCode activationCode = oActivationCode.get();
        Long userid = activationCode.getUserid();
        Auth auth = this.authRepository.getById(userid);

        auth.setAccountStatus(AccountStatus.FIRSTLOGIN);
        this.activationCodeRepository.delete(activationCode);
        this.authRepository.save(auth);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
