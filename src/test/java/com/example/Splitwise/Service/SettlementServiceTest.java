package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.GroupBalance;
import com.example.Splitwise.Entity.Settlement;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Exception.GroupNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.SettlementRepository;
import com.example.Splitwise.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SettlementServiceTest {
    @Mock
    private SettlementRepository settlementRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private GroupBalanceService groupBalanceService;
    @InjectMocks
    private SettlementService settlementService;

    private User payer;
    private User payee;
    private Group group;
    private Settlement settlement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        payer = new User(); payer.setId(1L); payer.setName("Payer");
        payee = new User(); payee.setId(2L); payee.setName("Payee");
        group = new Group(); group.setId(10L); group.setName("Test Group");
        settlement = new Settlement();
        settlement.setPayer(payer);
        settlement.setPayee(payee);
        settlement.setGroup(group);
        settlement.setAmount(BigDecimal.valueOf(100));
    }

    @Test
    void createSettlement_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));
        when(groupRepository.findById(10L)).thenReturn(Optional.of(group));
        when(settlementRepository.save(any(Settlement.class))).thenReturn(settlement);
        when(groupBalanceService.getBalanceAmount(payer, payee, group)).thenReturn(BigDecimal.valueOf(100));
        doNothing().when(groupBalanceService).reduceBalance(any(), any(), any(), anyDouble());

        Settlement result = settlementService.createSettlement(settlement);
        assertNotNull(result);
        verify(groupBalanceService, never()).saveGroupBalance(any());
    }

    @Test
    void createSettlement_overSettlement_triggersReverse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));
        when(groupRepository.findById(10L)).thenReturn(Optional.of(group));
        when(settlementRepository.save(any(Settlement.class))).thenReturn(settlement);
        when(groupBalanceService.getBalanceAmount(payer, payee, group)).thenReturn(BigDecimal.valueOf(50));
        doNothing().when(groupBalanceService).reduceBalance(any(), any(), any(), anyDouble());

        settlement.setAmount(BigDecimal.valueOf(100)); // Overpay
        Settlement result = settlementService.createSettlement(settlement);
        assertNotNull(result);
        ArgumentCaptor<GroupBalance> captor = ArgumentCaptor.forClass(GroupBalance.class);
        verify(groupBalanceService).saveGroupBalance(captor.capture());
        GroupBalance reverse = captor.getValue();
        assertEquals(payee, reverse.getFromUser());
        assertEquals(payer, reverse.getToUser());
        assertEquals(BigDecimal.valueOf(50), reverse.getAmount());
    }

    @Test
    void createSettlement_payerNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        settlement.setPayer(payer);
        assertThrows(UserNotFoundException.class, () -> settlementService.createSettlement(settlement));
    }

    @Test
    void createSettlement_payeeNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        settlement.setPayer(payer);
        settlement.setPayee(payee);
        assertThrows(UserNotFoundException.class, () -> settlementService.createSettlement(settlement));
    }

    @Test
    void createSettlement_groupNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));
        when(groupRepository.findById(10L)).thenReturn(Optional.empty());
        settlement.setPayer(payer);
        settlement.setPayee(payee);
        settlement.setGroup(group);
        assertThrows(GroupNotFoundException.class, () -> settlementService.createSettlement(settlement));
    }
}

