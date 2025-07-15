package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.ExpenseShare;
import com.example.Splitwise.Exception.ExpenseNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Service.ExpenseShareService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(ExpenseShareController.class)
class ExpenseShareControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExpenseShareService expenseShareService;
    @Autowired
    private ObjectMapper objectMapper;
    private ExpenseShare expenseShare;

    @BeforeEach
    void setUp() {
        expenseShare = new ExpenseShare();
        expenseShare.setId(1L);
    }

    @Test
    void createExpenseShare_success() throws Exception {
        Mockito.when(expenseShareService.createExpenseShare(any(ExpenseShare.class))).thenReturn(expenseShare);
        mockMvc.perform(post("/api/expense-shares")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expenseShare)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createExpenseShare_expenseNotFound() throws Exception {
        Mockito.when(expenseShareService.createExpenseShare(any(ExpenseShare.class))).thenThrow(new ExpenseNotFoundException("Expense not found"));
        mockMvc.perform(post("/api/expense-shares")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expenseShare)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Expense not found"));
    }

    @Test
    void createExpenseShare_userNotFound() throws Exception {
        Mockito.when(expenseShareService.createExpenseShare(any(ExpenseShare.class))).thenThrow(new UserNotFoundException("User not found"));
        mockMvc.perform(post("/api/expense-shares")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expenseShare)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    void createExpenseShare_unexpectedError() throws Exception {
        Mockito.when(expenseShareService.createExpenseShare(any(ExpenseShare.class))).thenThrow(new RuntimeException("Something went wrong"));
        mockMvc.perform(post("/api/expense-shares")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expenseShare)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Unexpected error")));
    }
}

