package com.chex.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {


    @Test
    void user_gain_new_experience_points_but_no_new_level() {
        User user = new User();
        user.addExp(4);

        assertEquals(4, user.getExp());
        assertEquals(1, user.getLevel());
        assertEquals(20, user.getNextlevel());
    }

    @Test
    void user_achieved_second_level() {
        User user = new User();
        user.addExp(22);

        assertEquals(2, user.getExp());
        assertEquals(2, user.getLevel());
        assertEquals(20 + (20 * 0.3), user.getNextlevel());
    }

    @Test
    void user_achieved_two_levels_at_once() {
        User user = new User();
        user.addExp(50);

        assertEquals(4, user.getExp());
        assertEquals(3, user.getLevel());
        assertEquals(26 + (int)(26 * 0.3), user.getNextlevel());
    }
}