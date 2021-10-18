package com.chex.api.friend;

import com.chex.modules.friends.ShortFriendView;

import java.util.ArrayList;
import java.util.List;

public class FriendViewResponse {
    private List<ShortFriendView> friends = new ArrayList<>();
    private List<ShortFriendView> invited = new ArrayList<>();
    private List<ShortFriendView> yourinvitation = new ArrayList<>();
    private List<ShortFriendView> banned = new ArrayList<>();
    private List<ShortFriendView> block = new ArrayList<>();

    public void add(ShortFriendView view){
        switch (view.getFriendStatus()){
            case FRIEND: friends.add(view); break;
            case YOUINVITED: invited.add(view); break;
            case INVITEDYOU: yourinvitation.add(view); break;
            case BANNED: banned.add(view); break;
            case BLOCKED: block.add(view); break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public List<ShortFriendView> getFriends() {
        return friends;
    }

    public void setFriends(List<ShortFriendView> friends) {
        this.friends = friends;
    }

    public List<ShortFriendView> getInvited() {
        return invited;
    }

    public void setInvited(List<ShortFriendView> invited) {
        this.invited = invited;
    }

    public List<ShortFriendView> getYourinvitation() {
        return yourinvitation;
    }

    public void setYourinvitation(List<ShortFriendView> yourinvitation) {
        this.yourinvitation = yourinvitation;
    }

    public List<ShortFriendView> getBanned() {
        return banned;
    }

    public void setBanned(List<ShortFriendView> banned) {
        this.banned = banned;
    }

    public List<ShortFriendView> getBlock() {
        return block;
    }

    public void setBlock(List<ShortFriendView> block) {
        this.block = block;
    }
}
