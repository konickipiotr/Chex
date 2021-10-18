package com.chex.api.friend;

import com.chex.modules.friends.ShortFriendView;

import java.util.List;

public class FoundFriendResult {
    private List<ShortFriendView> list;

    public FoundFriendResult() {
    }

    public FoundFriendResult(List<ShortFriendView> list) {
        this.list = list;
    }

    public List<ShortFriendView> getList() {
        return list;
    }

    public void setList(List<ShortFriendView> list) {
        this.list = list;
    }
}
