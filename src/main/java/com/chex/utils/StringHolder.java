package com.chex.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class StringHolder {

    @JsonIgnore
    private String value;

    @JsonCreator
    public static StringHolder create(String value){
        return new StringHolder(value);
    }

    public StringHolder() {
    }

    public StringHolder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}