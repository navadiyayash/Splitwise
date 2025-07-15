package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.GroupMember;
import com.example.Splitwise.Repository.GroupMemberRepository;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.UserRepository;
import com.example.Splitwise.Service.GroupMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupMemberController.class)
class GroupMemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GroupMemberService groupMemberService;
    @MockBean
    private GroupRepository groupRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private GroupMember groupMember;

    @BeforeEach
    void setUp() {
        groupMember = new GroupMember();
        groupMember.setId(1L);
    }

    @Test
    void addMember_success() throws Exception {
        Mockito.when(groupMemberService.addMember(any(GroupMember.class))).thenReturn(groupMember);
        mockMvc.perform(post("/api/group-members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupMember)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getMembersByGroup_success() throws Exception {
        List<GroupMember> members = Arrays.asList(groupMember);
        Mockito.when(groupMemberService.getMembersByGroupId(eq(2L))).thenReturn(members);
        mockMvc.perform(get("/api/group-members/group/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getMembersByGroup_emptyList() throws Exception {
        Mockito.when(groupMemberService.getMembersByGroupId(eq(2L))).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/group-members/group/2"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}

