package com.chex.api.forgotpassword;

import com.chex.api.registration.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetCodeRepository extends JpaRepository<ResetCode, Long> {
    Optional<ResetCode> findByResetcode(String resetcode);
}
