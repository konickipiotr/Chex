package com.chex.user.model;

import com.chex.user.FriendStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class UserFriend {

    @Id
    @SequenceGenerator(name = "friend_sequence", sequenceName = "friend_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friend_sequence")
    private Long id;
    private Long userid;
    private Long friendid;
    @Enumerated(EnumType.STRING)
    private FriendStatus status;
    private LocalDate friendsince;

    public UserFriend(Long userid, Long friendid, FriendStatus status) {
        this.userid = userid;
        this.friendid = friendid;
        this.status = status;
        this.friendsince = LocalDate.now();
    }

    public UserFriend() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getFriendid() {
        return friendid;
    }

    public void setFriendid(Long friendid) {
        this.friendid = friendid;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }

    public LocalDate getFriendsince() {
        return friendsince;
    }

    public void setFriendsince(LocalDate friendsince) {
        this.friendsince = friendsince;
    }
}
