package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.ExpenseShare;
import com.example.Splitwise.Exception.ExpenseShareNotFoundException;
import com.example.Splitwise.Repository.ExpenseRepository;
import com.example.Splitwise.Repository.ExpenseShareRepository;
import com.example.Splitwise.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseShareServiceTest {
    @Mock
    private ExpenseShareRepository expenseShareRepository;
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ExpenseShareService expenseShareService;

    private ExpenseShare expenseShare;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        expenseShare = new ExpenseShare();
        expenseShare.setId(1L);
    }

    @Test
    void addExpenseShare_success() {
        when(expenseShareRepository.save(expenseShare)).thenReturn(expenseShare);
        ExpenseShare created = expenseShareService.addExpenseShare(expenseShare);
        assertEquals(expenseShare, created);
    }

    @Test
    void getAllExpenseShares_success() {
        when(expenseShareRepository.findAll()).thenReturn(Arrays.asList(expenseShare));
        List<ExpenseShare> shares = expenseShareService.getAllExpenseShares();
        assertEquals(1, shares.size());
        assertEquals(expenseShare, shares.get(0));
    }

    @Test
    void getExpenseShareById_found() {
        when(expenseShareRepository.findById(1L)).thenReturn(Optional.of(expenseShare));
        Optional<ExpenseShare> found = expenseShareService.getExpenseShareById(1L);
        assertTrue(found.isPresent());
        assertEquals(expenseShare, found.get());
    }

    @Test
    void getExpenseShareById_notFound_throwsException() {
        when(expenseShareRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ExpenseShareNotFoundException.class, () -> expenseShareService.getExpenseShareById(1L));
    }

    @Test
    void deleteExpenseShare_success() {
        when(expenseShareRepository.existsById(1L)).thenReturn(true);
        doNothing().when(expenseShareRepository).deleteById(1L);
        assertDoesNotThrow(() -> expenseShareService.deleteExpenseShare(1L));
        verify(expenseShareRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteExpenseShare_notFound_throwsException() {
        when(expenseShareRepository.existsById(1L)).thenReturn(false);
        assertThrows(ExpenseShareNotFoundException.class, () -> expenseShareService.deleteExpenseShare(1L));
    }
}

