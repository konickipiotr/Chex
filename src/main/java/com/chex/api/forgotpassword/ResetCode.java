package com.chex.api.forgotpassword;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class ResetCode {

    @Id
    private Long userid;
    private String resetcode;
    private LocalDate requestedDate;

    public ResetCode() {
    }

    public ResetCode(Long userid, String resetcode) {
        this.userid = userid;
        this.resetcode = resetcode;
        this.requestedDate = LocalDate.now();
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getResetcode() {
        return resetcode;
    }

    public void setResetcode(String resetcode) {
        this.resetcode = resetcode;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }
}
