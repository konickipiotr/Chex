package com.chex.api.forgotpassword;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class ResetCode {

    @Id
    private Long userid;
    private String resetcode;
    private LocalDate requestedDate;

    public ResetCode(Long userid, String resetcode) {
        this.userid = userid;
        this.resetcode = resetcode;
        this.requestedDate = LocalDate.now();
    }
}
