package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Service.GroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupController.class)
class GroupControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GroupService groupService;
    @Autowired
    private ObjectMapper objectMapper;
    private Group group;

    @BeforeEach
    void setUp() {
        group = new Group();
        group.setId(1L);
        group.setName("Test Group");
    }

    @Test
    void createGroup_success() throws Exception {
        Mockito.when(groupService.createGroup(any(Group.class))).thenReturn(group);
        mockMvc.perform(post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(group)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Group"));
    }

    @Test
    void createGroup_dataIntegrityViolation() throws Exception {
        Mockito.when(groupService.createGroup(any(Group.class))).thenThrow(new DataIntegrityViolationException("Invalid data"));
        mockMvc.perform(post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(group)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invalid group data")));
    }

    @Test
    void createGroup_unexpectedError() throws Exception {
        Mockito.when(groupService.createGroup(any(Group.class))).thenThrow(new RuntimeException("Something went wrong"));
        mockMvc.perform(post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(group)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Unexpected error")));
    }
}

