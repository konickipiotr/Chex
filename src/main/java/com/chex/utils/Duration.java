package com.chex.utils;


public class Duration {

    private int days;
    private int hours;
    private int minutes;
    private int seconds;

    public Duration() {
        this.days = 0;
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
    }

    public Duration(int minutes, int seconds) {
        this();
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Duration(int hours, int minutes, int seconds) {
        this();
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Duration(int days, int hours, int minutes, int seconds) {
        this();
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Duration clear(){
        this.days = 0;
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        return this;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        if(hours > 23)
            throw  new IllegalArgumentException("Value hours in Duration class should be between 0-23.");
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        if(minutes > 59)
            throw  new IllegalArgumentException("Value minutes in Duration class should be between 0-59.");
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        if(seconds > 59)
            throw  new IllegalArgumentException("Value seconds in Duration class should be between 0-59.");
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(days > 0)
            sb.append(days).append("D ");
        if(hours > 0)
            sb.append(hours).append("H ");
        if(minutes > 0)
            sb.append(minutes).append("m ");
        if(seconds > 0)
            sb.append(seconds).append("s ");
        return sb.toString();
    }
}
