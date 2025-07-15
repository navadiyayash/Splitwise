package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Settlement;
import com.example.Splitwise.Exception.GroupNotFoundException;
import com.example.Splitwise.Exception.SettlementNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Service.SettlementService;
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
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SettlementController.class)
class SettlementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private SettlementService settlementService;
    @Autowired
    private ObjectMapper objectMapper;
    private Settlement settlement;

    @BeforeEach
    void setUp() {
        settlement = new Settlement();
        settlement.setId(1L);
    }

    @Test
    void createSettlement_success() throws Exception {
        Mockito.when(settlementService.createSettlement(any(Settlement.class))).thenReturn(settlement);
        mockMvc.perform(post("/api/settlements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(settlement)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createSettlement_userNotFound() throws Exception {
        Mockito.when(settlementService.createSettlement(any(Settlement.class))).thenThrow(new UserNotFoundException("User not found"));
        mockMvc.perform(post("/api/settlements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(settlement)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    void createSettlement_groupNotFound() throws Exception {
        Mockito.when(settlementService.createSettlement(any(Settlement.class))).thenThrow(new GroupNotFoundException("Group not found"));
        mockMvc.perform(post("/api/settlements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(settlement)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Group not found"));
    }

    @Test
    void createSettlement_dataIntegrityViolation() throws Exception {
        Mockito.when(settlementService.createSettlement(any(Settlement.class))).thenThrow(new DataIntegrityViolationException("Invalid data"));
        mockMvc.perform(post("/api/settlements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(settlement)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invalid settlement data")));
    }

    @Test
    void createSettlement_unexpectedError() throws Exception {
        Mockito.when(settlementService.createSettlement(any(Settlement.class))).thenThrow(new RuntimeException("Something went wrong"));
        mockMvc.perform(post("/api/settlements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(settlement)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Unexpected error")));
    }

    @Test
    void getSettlementById_found() throws Exception {
        Mockito.when(settlementService.getSettlementById(1L)).thenReturn(Optional.of(settlement));
        mockMvc.perform(get("/api/settlements/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getSettlementById_notFound() throws Exception {
        Mockito.when(settlementService.getSettlementById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/settlements/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Settlement not found")));
    }

    @Test
    void getSettlementById_unexpectedError() throws Exception {
        Mockito.when(settlementService.getSettlementById(1L)).thenThrow(new RuntimeException("Something went wrong"));
        mockMvc.perform(get("/api/settlements/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Unexpected error")));
    }
}

