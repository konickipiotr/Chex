package com.chex.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StringHolder {

    @JsonIgnore
    private String value;

    @JsonCreator
    public static StringHolder create(String value){
        return new StringHolder(value);
    }
}