package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Expense;
import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Exception.ExpenseNotFoundException;
import com.example.Splitwise.Exception.GroupNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Repository.ExpenseRepository;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @InjectMocks
    private ExpenseService expenseService;

    private Expense expense;
    private User payer;
    private Group group;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        payer = new User(); payer.setId(1L);
        group = new Group(); group.setId(2L);
        expense = new Expense();
        expense.setId(3L);
        expense.setPayer(payer);
        expense.setGroup(group);
        expense.setAmount(BigDecimal.valueOf(100));
        expense.setDescription("Test expense");
        expense.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void addExpense_success() {
        when(expenseRepository.save(expense)).thenReturn(expense);
        Expense created = expenseService.addExpense(expense);
        assertEquals(expense, created);
    }

    @Test
    void getAllExpenses_success() {
        when(expenseRepository.findAll()).thenReturn(Arrays.asList(expense));
        List<Expense> expenses = expenseService.getAllExpenses();
        assertEquals(1, expenses.size());
        assertEquals(expense, expenses.get(0));
    }

    @Test
    void getExpenseById_found() {
        when(expenseRepository.findById(3L)).thenReturn(Optional.of(expense));
        Optional<Expense> found = expenseService.getExpenseById(3L);
        assertTrue(found.isPresent());
        assertEquals(expense, found.get());
    }

    @Test
    void getExpenseById_notFound() {
        when(expenseRepository.findById(3L)).thenReturn(Optional.empty());
        Optional<Expense> found = expenseService.getExpenseById(3L);
        assertFalse(found.isPresent());
    }

    @Test
    void deleteExpense_success() {
        doNothing().when(expenseRepository).deleteById(3L);
        assertDoesNotThrow(() -> expenseService.deleteExpense(3L));
        verify(expenseRepository).deleteById(3L);
    }

    @Test
    void createExpense_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(groupRepository.findById(2L)).thenReturn(Optional.of(group));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        Expense toCreate = new Expense();
        toCreate.setPayer(payer); toCreate.setGroup(group);
        Expense result = expenseService.createExpense(toCreate);
        assertNotNull(result);
        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    void createExpense_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Expense toCreate = new Expense();
        toCreate.setPayer(payer); toCreate.setGroup(group);
        assertThrows(RuntimeException.class, () -> expenseService.createExpense(toCreate));
    }

    @Test
    void createExpense_groupNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(groupRepository.findById(2L)).thenReturn(Optional.empty());
        Expense toCreate = new Expense();
        toCreate.setPayer(payer); toCreate.setGroup(group);
        assertThrows(RuntimeException.class, () -> expenseService.createExpense(toCreate));
    }

    @Test
    void updateExpense_success() {
        Expense updated = new Expense();
        updated.setPayer(payer); updated.setGroup(group);
        updated.setDescription("Updated");
        updated.setAmount(BigDecimal.valueOf(200));
        updated.setCreatedAt(LocalDateTime.now());
        when(expenseRepository.findById(3L)).thenReturn(Optional.of(expense));
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(groupRepository.findById(2L)).thenReturn(Optional.of(group));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        Expense result = expenseService.updateExpense(3L, updated);
        assertNotNull(result);
        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    void updateExpense_userNotFound_throwsException() {
        Expense updated = new Expense();
        updated.setPayer(payer); updated.setGroup(group);
        when(expenseRepository.findById(3L)).thenReturn(Optional.of(expense));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> expenseService.updateExpense(3L, updated));
    }

    @Test
    void updateExpense_groupNotFound_throwsException() {
        Expense updated = new Expense();
        updated.setPayer(payer); updated.setGroup(group);
        when(expenseRepository.findById(3L)).thenReturn(Optional.of(expense));
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(groupRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(GroupNotFoundException.class, () -> expenseService.updateExpense(3L, updated));
    }

    @Test
    void updateExpense_expenseNotFound_throwsException() {
        Expense updated = new Expense();
        updated.setPayer(payer); updated.setGroup(group);
        when(expenseRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(ExpenseNotFoundException.class, () -> expenseService.updateExpense(3L, updated));
    }
}

