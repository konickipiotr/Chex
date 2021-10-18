package com.chex.api.friend;

import com.chex.authentication.AccountStatus;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.modules.friends.ShortFriendView;
import com.chex.user.FriendStatus;
import com.chex.user.model.User;
import com.chex.user.model.UserFriend;
import com.chex.user.repository.UserFriendRepository;
import com.chex.user.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockUser(username="testuser")
@Transactional
class FriendAPIControllerTest {

    private final MockMvc mockMvc;
    private final UserFriendRepository userFriendRepository;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper mapper;

    @Autowired
    public FriendAPIControllerTest(MockMvc mockMvc, UserFriendRepository userFriendRepository, UserRepository userRepository, AuthRepository authRepository, PasswordEncoder passwordEncoder, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.userFriendRepository = userFriendRepository;
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    User user, userfriend1, userfriend2, userfriend3,userfriend4, userfriend5, userfriend6;
    Long friend1Id = 998L;
    Long friend2Id = 999L;
    Long friend3Id = 1000L;
    Long friend4Id = 1001L;
    Long friend5Id = 1002L;
    Long friend6Id = 1003L;

    @BeforeEach
    void setUp() {
        this.authRepository.deleteAll();
        this.userRepository.deleteAll();
        this.userFriendRepository.deleteAll();

        Auth aUser = new Auth("testuser", passwordEncoder.encode("11"), "USER", AccountStatus.ACTIVE);
        this.authRepository.save(aUser);

        user = new User(aUser.getId(), "Jan", "Nowak");
        userfriend1 = new User(friend1Id, "Adam", "Kowalski");
        userfriend2 = new User(friend2Id, "Jola", "Patola");
        userfriend3 = new User(friend3Id, "Zosia", "Samsosia");
        userfriend4 = new User(friend4Id, "Hubert", "Jawor");
        userfriend5 = new User(friend5Id, "Marta", "Kot");
        userfriend6 = new User(friend6Id, "Adam", "Kot");
        this.userRepository.saveAll(Arrays.asList(user, userfriend1, userfriend2, userfriend3, userfriend4, userfriend5, userfriend6));
    }



    @Test
    void invite_new_friend() throws Exception {
        mockMvc.perform(post("/api/friends/invite/"  + friend1Id))
                .andExpect(status().isCreated());

        LocalDate today = LocalDate.now();

        List<UserFriend> all = this.userFriendRepository.findAll();
        assertEquals(2, all.size());

        UserFriend me, newfriend;
        if(all.get(0).getUserid().equals(user.getId())){
            me = all.get(0);
            newfriend = all.get(1);
        }else {
            me = all.get(1);
            newfriend = all.get(0);
        }

        assertEquals(user.getId(), me.getUserid());
        assertEquals(userfriend1.getId(), me.getFriendid());
        assertEquals(FriendStatus.YOUINVITED, me.getStatus());
        assertEquals(today, me.getFriendsince());

        assertEquals(userfriend1.getId(), newfriend.getUserid());
        assertEquals(user.getId(), newfriend.getFriendid());
        assertEquals(FriendStatus.INVITEDYOU, newfriend.getStatus());
        assertEquals(today, newfriend.getFriendsince());
    }

    @Test
    void revoke_the_invitation() throws Exception {

        UserFriend me = new UserFriend(user.getId(), friend1Id, FriendStatus.INVITEDYOU);
        UserFriend futureFriend = new UserFriend(friend1Id, user.getId(), FriendStatus.YOUINVITED);
        this.userFriendRepository.saveAll(Arrays.asList(me, futureFriend));
        assertEquals(2, this.userFriendRepository.findAll().size());

        mockMvc.perform(post("/api/friends/revoke/"  + me.getFriendid()))
                .andExpect(status().isOk());

        assertEquals(0, this.userFriendRepository.findAll().size());
    }

    @Test
    void discard_the_invitation() throws Exception {

        UserFriend futureFriend = new UserFriend(friend1Id, user.getId(), FriendStatus.YOUINVITED);
        UserFriend me = new UserFriend(user.getId(), friend1Id, FriendStatus.INVITEDYOU);

        this.userFriendRepository.saveAll(Arrays.asList(me, futureFriend));
        assertEquals(2, this.userFriendRepository.findAll().size());

        mockMvc.perform(post("/api/friends/revoke/"  + me.getFriendid()))
                .andExpect(status().isOk());

        assertEquals(0, this.userFriendRepository.findAll().size());
    }

    @Test
    void accept_the_invitation() throws Exception {

        UserFriend me = new UserFriend(user.getId(), friend1Id, FriendStatus.INVITEDYOU);
        UserFriend futureFriend = new UserFriend(friend1Id, user.getId(), FriendStatus.YOUINVITED);

        this.userFriendRepository.saveAll(Arrays.asList(me, futureFriend));

        mockMvc.perform(post("/api/friends/accept/"  + me.getFriendid()))
                .andExpect(status().isOk());

        List<UserFriend> all = this.userFriendRepository.findAll();
        assertEquals(2, all.size());
        assertEquals(FriendStatus.FRIEND, all.get(0).getStatus());
        assertEquals(FriendStatus.FRIEND, all.get(1).getStatus());
    }

    @Test
    void remove_friend() throws Exception {

        UserFriend futureFriend = new UserFriend(friend1Id, user.getId(), FriendStatus.FRIEND);
        UserFriend me = new UserFriend(user.getId(), friend1Id, FriendStatus.FRIEND);
        this.userFriendRepository.saveAll(Arrays.asList(me, futureFriend));

        mockMvc.perform(post("/api/friends/remove/"  + me.getFriendid()))
                .andExpect(status().isOk());

        assertEquals(0, this.userFriendRepository.findAll().size());
    }

    @Test
    void block_user() throws Exception {

        mockMvc.perform(post("/api/friends/block/"  + friend1Id))
                .andExpect(status().isOk());

        List<UserFriend> all = this.userFriendRepository.findAll();
        assertEquals(2, all.size());

        UserFriend me, banned;
        if(all.get(0).getUserid().equals(user.getId())){
            me = all.get(0);
            banned = all.get(1);
        }else {
            me = all.get(1);
            banned = all.get(0);
        }

        assertEquals(FriendStatus.BANNED, me.getStatus());
        assertEquals(FriendStatus.BLOCKED, banned.getStatus());
    }

    @Test
    void unblock_user() throws Exception {

        UserFriend me = new UserFriend(user.getId(), friend1Id, FriendStatus.BANNED);
        UserFriend futureFriend = new UserFriend(friend1Id, user.getId(), FriendStatus.BLOCKED);
        this.userFriendRepository.saveAll(Arrays.asList(me, futureFriend));

        mockMvc.perform(post("/api/friends/unblock/"  + friend1Id))
                .andExpect(status().isOk());

        List<UserFriend> all = this.userFriendRepository.findAll();
        assertEquals(0, all.size());
    }

    @Test
    void get_list_of_friends_and_invitation() throws Exception {
        UserFriend me = new UserFriend(user.getId(), friend1Id, FriendStatus.FRIEND);
        UserFriend me2 = new UserFriend(user.getId(), friend2Id, FriendStatus.YOUINVITED);
        UserFriend me3 = new UserFriend(user.getId(), friend3Id, FriendStatus.INVITEDYOU);
        UserFriend me4 = new UserFriend(user.getId(), friend4Id, FriendStatus.BANNED);
        UserFriend me5 = new UserFriend(user.getId(), friend5Id, FriendStatus.BLOCKED);

        UserFriend f1 = new UserFriend(friend1Id, user.getId(), FriendStatus.FRIEND);
        UserFriend f2 = new UserFriend(friend2Id, user.getId(), FriendStatus.INVITEDYOU);
        UserFriend f3 = new UserFriend(friend3Id, user.getId(), FriendStatus.YOUINVITED);
        UserFriend f4 = new UserFriend(friend4Id, user.getId(), FriendStatus.BLOCKED);
        UserFriend f5 = new UserFriend(friend5Id, user.getId(), FriendStatus.BANNED);

        this.userFriendRepository.saveAll(Arrays.asList(me, me2, me3, me4, me5, f1, f2, f3, f4, f5));

        MvcResult mvcResult = mockMvc.perform(get("/api/friends"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        FriendViewResponse friendViewResponse = mapper.readValue(contentAsString, new TypeReference<>() {});

        List<ShortFriendView> friends = friendViewResponse.getFriends();
        List<ShortFriendView> invited = friendViewResponse.getInvited();
        List<ShortFriendView> yourinvitation = friendViewResponse.getYourinvitation();
        List<ShortFriendView> banned = friendViewResponse.getBanned();
        List<ShortFriendView> block = friendViewResponse.getBlock();

        assertEquals(1, friends.size());
        assertEquals(1, invited.size());
        assertEquals(1, yourinvitation.size());
        assertEquals(1, banned.size());
        assertEquals(1, block.size());

        assertEquals(userfriend1.getId(), friends.get(0).getId());
        assertEquals(userfriend2.getId(), invited.get(0).getId());
        assertEquals(userfriend3.getId(), yourinvitation.get(0).getId());
        assertEquals(userfriend4.getId(), banned.get(0).getId());
        assertEquals(userfriend5.getId(), block.get(0).getId());

        assertEquals("Adam Kowalski", friends.get(0).getName());
        assertEquals("Jola Patola", invited.get(0).getName());
        assertEquals("Zosia Samsosia", yourinvitation.get(0).getName());
        assertEquals("Hubert Jawor", banned.get(0).getName());
        assertEquals("Marta Kot", block.get(0).getName());
    }

    @Test
    void search_friends() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/friends/search")
                        .param("phrase", "Jola Patola"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        FoundFriendResult ffr = mapper.readValue(contentAsString, new TypeReference<>() {});
        List<ShortFriendView> views = ffr.getList();

        assertEquals(1, views.size());
        assertEquals("Jola Patola", views.get(0).getName());
    }

    @Test
    void search_friends_by_phrase() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/friends/search")
                        .param("phrase", "dam"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        FoundFriendResult ffr = mapper.readValue(contentAsString, new TypeReference<>() {});

        assertEquals(2, ffr.getList().size());
    }
}