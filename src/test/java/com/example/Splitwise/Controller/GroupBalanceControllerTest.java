package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.GroupBalance;
import com.example.Splitwise.Service.GroupBalanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupBalanceController.class)
class GroupBalanceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private GroupBalanceService groupBalanceService;
    @Autowired
    private ObjectMapper objectMapper;
    private GroupBalance groupBalance;

    @BeforeEach
    void setUp() {
        groupBalance = new GroupBalance();
        groupBalance.setId(1L);
    }

    @Test
    void getNetBalances_success() throws Exception {
        Map<Long, BigDecimal> balances = new HashMap<>();
        balances.put(1L, BigDecimal.valueOf(100));
        Mockito.when(groupBalanceService.getNetBalancesByGroup(2L)).thenReturn(balances);
        mockMvc.perform(get("/api/group-balances/group/2/net-balances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.1").value(100));
    }

    @Test
    void getBalancesByGroupId_success() throws Exception {
        List<GroupBalance> balances = Arrays.asList(groupBalance);
        Mockito.when(groupBalanceService.getBalancesByGroupId(2L)).thenReturn(balances);
        mockMvc.perform(get("/api/group-balances/group/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getGroupBalanceById_found() throws Exception {
        Mockito.when(groupBalanceService.getGroupBalanceById(1L)).thenReturn(Optional.of(groupBalance));
        mockMvc.perform(get("/api/group-balances/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getGroupBalanceById_notFound() throws Exception {
        Mockito.when(groupBalanceService.getGroupBalanceById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/group-balances/1"))
                .andExpect(status().isNotFound());
    }
}

