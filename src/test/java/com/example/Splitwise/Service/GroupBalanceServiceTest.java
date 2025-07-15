package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.GroupBalance;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Repository.GroupBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupBalanceServiceTest {
    @Mock
    private GroupBalanceRepository groupBalanceRepository;
    @InjectMocks
    private GroupBalanceService groupBalanceService;

    private User payer;
    private User payee;
    private Group group;
    private GroupBalance groupBalance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        payer = new User(); payer.setId(1L);
        payee = new User(); payee.setId(2L);
        group = new Group(); group.setId(3L);
        groupBalance = new GroupBalance();
        groupBalance.setId(4L);
        groupBalance.setGroup(group);
        groupBalance.setFromUser(payer);
        groupBalance.setToUser(payee);
        groupBalance.setAmount(BigDecimal.valueOf(100));
    }

    @Test
    void reduceBalance_success() {
        when(groupBalanceRepository.findByGroupAndFromUserAndOwedToUser(group, payer, payee)).thenReturn(Optional.of(groupBalance));
        groupBalanceService.reduceBalance(payer, payee, group, 50);
        // No exception should be thrown
    }

    @Test
    void reduceBalance_noOutstandingBalance_logsWarningAndReturns() {
        when(groupBalanceRepository.findByGroupAndFromUserAndOwedToUser(group, payer, payee)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> groupBalanceService.reduceBalance(payer, payee, group, 50));
    }

    @Test
    void reduceBalance_currentBalanceZeroOrNegative_throwsIllegalStateException() {
        groupBalance.setAmount(BigDecimal.ZERO);
        when(groupBalanceRepository.findByGroupAndFromUserAndOwedToUser(group, payer, payee)).thenReturn(Optional.of(groupBalance));
        assertThrows(IllegalStateException.class, () -> groupBalanceService.reduceBalance(payer, payee, group, 50));
    }

    @Test
    void reduceBalance_paymentAmountZeroOrNegative_throwsIllegalArgumentException() {
        when(groupBalanceRepository.findByGroupAndFromUserAndOwedToUser(group, payer, payee)).thenReturn(Optional.of(groupBalance));
        assertThrows(IllegalArgumentException.class, () -> groupBalanceService.reduceBalance(payer, payee, group, 0));
        assertThrows(IllegalArgumentException.class, () -> groupBalanceService.reduceBalance(payer, payee, group, -10));
    }
}

