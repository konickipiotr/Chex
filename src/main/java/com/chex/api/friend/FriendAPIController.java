package com.chex.api.friend;

import com.chex.api.AuthService;
import com.chex.modules.friends.ShortFriendView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendAPIController {

    private final FriendAPIService friendAPIService;
    private final AuthService authService;

    @Autowired
    public FriendAPIController(FriendAPIService friendAPIService, AuthService authService) {
        this.friendAPIService = friendAPIService;
        this.authService = authService;
    }


    @PostMapping("/invite/{invitedid}")
    public ResponseEntity<Void> inviteNewFriend(@PathVariable("invitedid") Long invitedid, Principal principal){
        Long userId = authService.getUserId(principal);
        return friendAPIService.inviteNewFriend(userId, invitedid);
    }

    @PostMapping("/revoke/{otherid}")
    public ResponseEntity<Void> revokeIvitation(@PathVariable("otherid") Long otherid, Principal principal){
        Long userId = authService.getUserId(principal);
        return friendAPIService.removeRelation(userId, otherid);
    }

    @PostMapping("/accept/{otherUserId}")
    public ResponseEntity<Void> acceptIvitation(@PathVariable("otherUserId") Long otherUserId, Principal principal){
        Long userId = authService.getUserId(principal);
        return friendAPIService.acceptInvitation(userId, otherUserId);
    }

    @PostMapping("/remove/{friendid}")
    public ResponseEntity<Void> removeFriend(@PathVariable("friendid") Long friendid, Principal principal){
        Long userId = authService.getUserId(principal);
        return friendAPIService.removeRelation(userId, friendid);
    }

    @PostMapping("/block/{banedid}")
    public ResponseEntity<Void> blockUser(@PathVariable("banedid") Long banedid, Principal principal){
        Long userId = authService.getUserId(principal);
        return friendAPIService.blockUser(userId, banedid);
    }

    @PostMapping("/unblock/{banedid}")
    public ResponseEntity<Void> unblockUser(@PathVariable("banedid") Long banedid, Principal principal){
        Long userId = authService.getUserId(principal);
        return friendAPIService.removeRelation(userId, banedid);
    }

    @GetMapping
    public ResponseEntity<FriendViewResponse> getFriendList(Principal principal){
        Long userId = authService.getUserId(principal);
        return this.friendAPIService.getFriendsList(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<FoundFriendResult> searchNewFriends(@RequestParam("phrase") String phrase, Principal principal){
        Long userId = authService.getUserId(principal);
        return this.friendAPIService.getSearchResult(userId, phrase);
    }
}
