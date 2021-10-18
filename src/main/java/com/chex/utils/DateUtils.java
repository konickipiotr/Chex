package com.chex.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    public static String getNiceDate(LocalDateTime ldt){
        if(ldt == null) return "";
        return ldt.toLocalDate().toString() + " " + ldt.toLocalTime().toString();
    }

    public static Duration convertToDuration(LocalDateTime start, LocalDateTime end){
        java.time.Duration duration = java.time.Duration.between(start, end);
        long millis = duration.toMillis();

        int days =(int)(millis / DAY);
        long rest = (millis % DAY);

        int hours = (int)(rest / HOUR);
        rest = (rest % HOUR);

        int min = (int)(rest / MINUTE);
        rest = (rest % MINUTE);

        int sec = (int)(rest / SECOND);
        return new Duration(days, hours, min, sec);
    }

    public static long convertDurationToMillis(Duration duration){
        return (duration.getDays() * DAY)
                + (duration.getHours() * HOUR)
                + (duration.getMinutes() * MINUTE)
                + (duration.getSeconds() + SECOND);
    }
}
