package com.chex.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class DateUtils {

    public static String getNiceDate(LocalDateTime ldt){
        if(ldt == null) return "";
        return ldt.toLocalDate().toString() + " " + ldt.toLocalTime().toString();
    }
}
