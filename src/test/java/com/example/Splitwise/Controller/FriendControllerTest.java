package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Friend;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Service.FriendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FriendController.class)
class FriendControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private FriendService friendService;
    @Autowired
    private ObjectMapper objectMapper;
    private Friend friend;

    @BeforeEach
    void setUp() {
        friend = new Friend();
        friend.setId(1L);
    }

    @Test
    void addFriend_success() throws Exception {
        Mockito.when(friendService.addFriend(any(Friend.class))).thenReturn(friend);
        mockMvc.perform(post("/api/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(friend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void addFriend_userNotFound() throws Exception {
        Mockito.when(friendService.addFriend(any(Friend.class))).thenThrow(new UserNotFoundException("User not found"));
        mockMvc.perform(post("/api/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(friend)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    void addFriend_dataIntegrityViolation() throws Exception {
        Mockito.when(friendService.addFriend(any(Friend.class))).thenThrow(new DataIntegrityViolationException("Invalid data"));
        mockMvc.perform(post("/api/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(friend)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invalid friend relationship")));
    }

    @Test
    void addFriend_unexpectedError() throws Exception {
        Mockito.when(friendService.addFriend(any(Friend.class))).thenThrow(new RuntimeException("Something went wrong"));
        mockMvc.perform(post("/api/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(friend)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Unexpected error")));
    }

    @Test
    void getFriendsByUserId_success() throws Exception {
        List<Friend> friends = Arrays.asList(friend);
        Mockito.when(friendService.getFriendsByUserId(eq(2L))).thenReturn(friends);
        mockMvc.perform(get("/api/friends/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}

