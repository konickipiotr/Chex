package com.chex.user;

import com.chex.user.model.User;
import com.chex.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    private UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        this.userRepository.deleteAll();
    }

    @Test
    void not_found_user_by_name_because_db_is_empty() {
        List<User> list = this.userRepository.getUserContains("rff");
        assertTrue(list.isEmpty());
    }

    @Test
    void user_found_by_firstname() {
        this.userRepository.save(new User(1L, "Jan", "Nowak"));
        List<User> list = userRepository.getUserContains("Jan");

        assertFalse(list.isEmpty());
        assertEquals(1L, list.get(0).getId());
    }

    @Test
    void user_found_by_lastname() {
        this.userRepository.save(new User(2L, "Jan", "Bowak"));
        this.userRepository.save(new User(1L, "Jan", "Nowak"));
        List<User> list = userRepository.getUserContains("Nowak");

        assertFalse(list.isEmpty());
        assertEquals(1L, list.get(0).getId());
    }

    @Test
    void user_found_by_name() {
        this.userRepository.save(new User(2L, "Jan", "Bowak"));
        this.userRepository.save(new User(1L, "Jan", "Nowak"));
        List<User> list = userRepository.getUserContains("Jan Nowak");

        assertFalse(list.isEmpty());
        assertEquals(1L, list.get(0).getId());
    }
}