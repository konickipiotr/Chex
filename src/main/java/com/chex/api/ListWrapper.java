package com.chex.api;

import java.util.List;

public class ListWrapper<T> {

    private List<T> list;

    public ListWrapper() {
    }

    public boolean isEmpty(){
        if(list == null || list.isEmpty())
            return true;
        return false;
    }

    public ListWrapper(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
