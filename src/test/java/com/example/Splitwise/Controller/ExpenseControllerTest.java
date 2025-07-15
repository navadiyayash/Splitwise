package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Expense;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.UserRepository;
import com.example.Splitwise.Service.ExpenseService;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExpenseService expenseService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private GroupRepository groupRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private Expense expense;

    @BeforeEach
    void setUp() {
        expense = new Expense();
        expense.setId(1L);
        expense.setDescription("Test Expense");
        expense.setAmount(BigDecimal.valueOf(100));
        expense.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createExpense_success() throws Exception {
        Mockito.when(expenseService.createExpense(any(Expense.class))).thenReturn(expense);
        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expense)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Test Expense"));
    }

    @Test
    void getExpenseById_found() throws Exception {
        Mockito.when(expenseService.getExpenseById(1L)).thenReturn(Optional.of(expense));
        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Test Expense"));
    }

    @Test
    void getExpenseById_notFound() throws Exception {
        Mockito.when(expenseService.getExpenseById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isNotFound());
    }
}

