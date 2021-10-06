package com.chex.api.friend;

import com.chex.modules.friends.ShortFriendView;
import com.chex.user.FriendStatus;
import com.chex.user.model.User;
import com.chex.user.model.UserFriend;
import com.chex.user.repository.UserFriendRepository;
import com.chex.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FriendAPIService {

    private final UserFriendRepository userFriendRepository;
    private final UserRepository userRepository;

    class Relations{
        public UserFriend userRelation;
        public UserFriend otherRelation;
    }

    @Autowired
    public FriendAPIService(UserFriendRepository userFriendRepository, UserRepository userRepository) {
        this.userFriendRepository = userFriendRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Void> inviteNewFriend(Long userid, Long invitedId){
        if(this.userRepository.findById(invitedId).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(this.userFriendRepository.existsByUseridAndFriendid(userid, invitedId))
            return new ResponseEntity<>(HttpStatus.FOUND);

        UserFriend user = new UserFriend(userid, invitedId, FriendStatus.YOUINVITED);
        UserFriend futureFriend = new UserFriend(invitedId, userid, FriendStatus.INVITEDYOU);
        this.userFriendRepository.saveAll(Arrays.asList(user, futureFriend));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<Void> acceptInvitation(Long userId, Long otherUserId){

        try {
            Relations relations = getRelations(userId, otherUserId);
            if(!relations.userRelation.getUserid().equals(userId))
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            relations.userRelation.setStatus(FriendStatus.FRIEND);
            relations.userRelation.setFriendsince(LocalDate.now());
            relations.otherRelation.setStatus(FriendStatus.FRIEND);
            relations.otherRelation.setFriendsince(LocalDate.now());

            this.userFriendRepository.save(relations.userRelation);
            this.userFriendRepository.save(relations.otherRelation);
        }catch (NotFoundRelationsException e){
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> removeRelation(Long userId, Long friendId){

        try {
            Relations relations = getRelations(userId, friendId);

            this.userFriendRepository.delete(relations.userRelation);
            this.userFriendRepository.delete(relations.otherRelation);
        }catch (NotFoundRelationsException e){
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> blockUser(Long userid, Long blockId){

        try {
            Relations relations = getRelations(userid, blockId);

            relations.userRelation.setStatus(FriendStatus.BANNED);
            relations.otherRelation.setStatus(FriendStatus.BLOCKED);

            this.userFriendRepository.save(relations.userRelation);
            this.userFriendRepository.save(relations.otherRelation);
        }catch (NotFoundRelationsException e){
            this.userFriendRepository.save(new UserFriend(userid, blockId, FriendStatus.BANNED));
            this.userFriendRepository.save(new UserFriend(blockId, userid, FriendStatus.BLOCKED));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Relations getRelations(Long userId, Long otherUserId){
        Optional<UserFriend> oUserRelations = this.userFriendRepository.findByUseridAndFriendid(userId, otherUserId);
        if(oUserRelations.isEmpty())
            throw new NotFoundRelationsException(userId, otherUserId);

        UserFriend userRelation = oUserRelations.get();
        Optional<UserFriend> oOtherRelation = this.userFriendRepository.findByUseridAndFriendid(userRelation.getFriendid(), userId);
        if(oOtherRelation.isEmpty())
            throw new NotFoundRelationsException(otherUserId, userId);

        Relations relations = new Relations();
        relations.userRelation = userRelation;
        relations.otherRelation = oOtherRelation.get();
        return relations;
    }

    public ResponseEntity<FriendViewResponse> getFriendsList(Long userid){
        FriendViewResponse response = new FriendViewResponse();
        List<UserFriend> list = this.userFriendRepository.findByUserid(userid);

        for(UserFriend f : list){
            response.add(prepareSingleView(f.getFriendid()).setStatus(f.getStatus()));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ShortFriendView prepareSingleView(Long friendId){
        Optional<User> optionalUser = this.userRepository.findById(friendId);
        if(optionalUser.isEmpty())
            throw new UsernameNotFoundException("Not found user with id: " + friendId);

        return new ShortFriendView(optionalUser.get());
    }

    public ResponseEntity<FoundFriendResult> getSearchResult(Long userid, String phrase){
        List<User> users = this.userRepository.getUserContains(phrase);
        List<ShortFriendView> list = new ArrayList<>();
        List<ShortFriendView> listContains = new ArrayList<>();

        for(User u : users){
            if(u.getId().equals(userid))
                continue;
            if(this.userFriendRepository.existsByUseridAndFriendid(userid, u.getId()))
                continue;

            if(u.getName().equals(phrase)){
                list.add(prepareSingleView(u.getId()));
            }else {
                listContains.add(prepareSingleView(u.getId()));
            }
        }

        if(list.size() < 20){
            int size = listContains.size();
            if(phrase.length() < 4) {
                size = Math.min(size, 10);
                list.addAll(listContains.subList(0, size));
            }else if(phrase.length() < 8) {
                size = Math.min(size, 20);
                list.addAll(listContains.subList(0, size));
            }else
                list.addAll(listContains);
        }
        return new ResponseEntity<>(new FoundFriendResult(list), HttpStatus.OK);
    }
}
