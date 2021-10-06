package com.chex.user.repository;

import com.chex.user.model.User;
import com.chex.user.model.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {

    boolean existsByUseridAndFriendid(Long userid, Long friendid);
    Optional<UserFriend> findByUseridAndFriendid(Long userid, Long friendid);
    List<UserFriend> findByUserid(Long userid);
}
