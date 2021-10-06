package com.chex.modules.friends;

import com.chex.user.FriendStatus;
import com.chex.user.model.User;

public class ShortFriendView {

    private Long id;
    private String name;
    private String img;
    private FriendStatus friendStatus;

    public ShortFriendView() {
        this.friendStatus = FriendStatus.UNKNOWN;
    }

    public ShortFriendView(User user) {
        this();
        this.id = user.getId();
        this.name = user.getName();
        this.img = user.getImg();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public FriendStatus getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(FriendStatus friendStatus) {
        this.friendStatus = friendStatus;
    }

    public ShortFriendView setStatus(FriendStatus friendStatus) {
        this.friendStatus = friendStatus;
        return this;
    }
}
