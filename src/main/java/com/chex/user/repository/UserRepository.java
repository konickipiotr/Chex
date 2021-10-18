package com.chex.user.repository;

import com.chex.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u where CONCAT(firstname, ' ',lastname) like %:phrase%")
    List<User> getUserContains(@Param("phrase") String phrase);
}
