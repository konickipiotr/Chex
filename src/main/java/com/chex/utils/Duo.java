package com.chex.utils;

public class Duo<T> implements Comparable<Duo<T>>{

    private T key;
    private String value;

    public Duo(T key, String value) {
        this.key = key;
        this.value = value;
    }

    public Duo() {
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(Duo<T> other) {
        return this.value.compareTo(other.value);
    }
}
