package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.GroupBalance;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Exception.OverPaymentException;
import com.example.Splitwise.Repository.GroupBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@Service
public class GroupBalanceService {

    @Autowired
    private GroupBalanceRepository groupBalanceRepository;

    private static final Logger log = LoggerFactory.getLogger(GroupBalanceService.class);


    public void reduceBalance(User payer, User payee, Group group, double amount) {
        Optional<GroupBalance> optionalBalance = groupBalanceRepository
                .findByGroupAndFromUserAndOwedToUser(group, payer, payee);

        if (optionalBalance.isEmpty()) {
            log.warn("No outstanding balance to reduce between users. Group: {}, Payer: {}, Payee: {}", group.getId(), payer.getId(), payee.getId());
            return;
        }

        GroupBalance balance = optionalBalance.get();

        BigDecimal currentAmount = balance.getAmount();
        BigDecimal paymentAmount = BigDecimal.valueOf(amount);

        if (currentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Current balance is already zero or negative. Group: {}, Payer: {}, Payee: {}", group.getId(), payer.getId(), payee.getId());
            throw new IllegalStateException("Current balance is already settled or negative.");
        }

        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Attempted to settle with zero or negative amount. Group: {}, Payer: {}, Payee: {}", group.getId(), payer.getId(), payee.getId());
            throw new IllegalArgumentException("Settlement amount must be positive.");
        }

        if (currentAmount.compareTo(paymentAmount) < 0) {
            log.error("Attempted to pay more than owed. Owed: {}, Tried to pay: {}", currentAmount, paymentAmount);
            throw new OverPaymentException("Cannot settle more than owed.");
        }

        BigDecimal updatedAmount = currentAmount.subtract(paymentAmount);

        if (updatedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            groupBalanceRepository.delete(balance);
            log.info("Fully settled balance for Group: {}, between {} -> {}", group.getId(), payer.getId(), payee.getId());
        } else {
            balance.setAmount(updatedAmount);
            groupBalanceRepository.save(balance);
            log.info("Updated balance to {} for Group: {}, between {} -> {}", updatedAmount, group.getId(), payer.getId(), payee.getId());
        }
    }

//    public void reduceBalance(User payer, User payee, Group group, double amount) {
//        Optional<GroupBalance> optionalBalance = groupBalanceRepository
//                .findByGroupAndFromUserAndOwedToUser(group, payer, payee);
//
//        if (optionalBalance.isPresent()) {
//            GroupBalance balance = optionalBalance.get();
//            BigDecimal updatedAmount = balance.getAmount().subtract(BigDecimal.valueOf(amount));
//
//
//            if (updatedAmount.compareTo(BigDecimal.ZERO) <= 0) {
//                groupBalanceRepository.delete(balance); // fully settled
//            } else {
//                balance.setAmount(updatedAmount);
//                groupBalanceRepository.save(balance);
//            }
//        } else {
//            // Optional: Log a warning or create reverse record if required
//            System.out.println("No outstanding balance to reduce between users.");
//        }
//    }

    public Map<Long, BigDecimal> getNetBalancesByGroup(Long groupId) {
        List<GroupBalance> balances = groupBalanceRepository.findByGroupId(groupId);
        Map<Long, BigDecimal> netBalances = new HashMap<>();

        for (GroupBalance balance : balances) {
            Long fromUserId = balance.getFromUser().getId();
            Long toUserId = balance.getToUser().getId();
            BigDecimal amount = balance.getAmount();

            // fromUser owes => negative balance
            netBalances.put(fromUserId, netBalances.getOrDefault(fromUserId, BigDecimal.ZERO).subtract(amount));

            // toUser is owed => positive balance
            netBalances.put(toUserId, netBalances.getOrDefault(toUserId, BigDecimal.ZERO).add(amount));
        }

        return netBalances;
    }
    // Save or update a group balance
    public GroupBalance saveGroupBalance(GroupBalance balance) {
        return groupBalanceRepository.save(balance);
    }

    // Get all group balances
    public List<GroupBalance> getAllGroupBalances() {
        return groupBalanceRepository.findAll();
    }

    // Get group balance by ID
    public Optional<GroupBalance> getGroupBalanceById(Long id) {
        return groupBalanceRepository.findById(id);
    }

    // Delete a group balance
    public void deleteGroupBalance(Long id) {
        groupBalanceRepository.deleteById(id);
    }

    public GroupBalance createGroupBalance(GroupBalance groupBalance) {
        return groupBalanceRepository.save(groupBalance);

    }

    public List<GroupBalance> getBalancesByGroupId(Long groupId) {
        return groupBalanceRepository.findByGroupId(groupId);

    }


    public List<GroupBalance> getGroupBalancesByGroupId(Long groupId) {
        return groupBalanceRepository.findByGroupId(groupId);
    }

    /**
     * Returns the current balance amount owed from payer to payee in the given group.
     * If no balance exists, returns BigDecimal.ZERO.
     */
    public BigDecimal getBalanceAmount(User payer, User payee, Group group) {
        return groupBalanceRepository.findByGroupAndFromUserAndOwedToUser(group, payer, payee)
                .map(GroupBalance::getAmount)
                .orElse(BigDecimal.ZERO);
    }
}
