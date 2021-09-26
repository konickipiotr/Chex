package com.chex.api.registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ActivationCode {

    @Id
    private Long userid;
    private String activationcode;
}

