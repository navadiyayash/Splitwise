package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.GroupBalance;
import com.example.Splitwise.Entity.Settlement;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.SettlementRepository;
import com.example.Splitwise.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Exception.GroupNotFoundException;
import com.example.Splitwise.Exception.SettlementNotFoundException;


@Service
public class SettlementService {

    private static final Logger log = LoggerFactory.getLogger(SettlementService.class);

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupBalanceService groupBalanceService;

    // Save a new settlement (user settles amount with another user)
    public Settlement addSettlement(Settlement settlement) {
        return settlementRepository.save(settlement);
    }

    // Get all settlements
    public List<Settlement> getAllSettlements() {
        return settlementRepository.findAll();
    }

    // Get a settlement by ID
    public Optional<Settlement> getSettlementById(Long id) {
        return settlementRepository.findById(id);
    }


    // Delete a settlement
    public void deleteSettlement(Long id) {
        settlementRepository.deleteById(id);
    }

//    public Settlement createSettlement(Settlement settlement) {
//        // Save the settlement transaction
//        Settlement savedSettlement = settlementRepository.save(settlement);
//
//        // Reduce the payer's balance toward the payee
//        groupBalanceService.reduceBalance(
//                settlement.getPayer(),
//                settlement.getPayee(),
//                settlement.getGroup(),
//                settlement.getAmount()
//        );
//
//        return savedSettlement;
//    }

    public Settlement createSettlement(Settlement settlement) {
        // Step 1: Validate and fetch actual entities
        User payer = userRepository.findById(settlement.getPayer().getId())
                .orElseThrow(() -> new UserNotFoundException("Payer not found"));
        User payee = userRepository.findById(settlement.getPayee().getId())
                .orElseThrow(() -> new UserNotFoundException("Payee not found"));
        Group group = groupRepository.findById(settlement.getGroup().getId())
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        // Step 2: Set references and timestamp
        settlement.setPayer(payer);
        settlement.setPayee(payee);
        settlement.setGroup(group);
        settlement.setPaidAt(LocalDateTime.now());

        // Step 3: Save the settlement
        Settlement savedSettlement = settlementRepository.save(settlement);

        // Step 4: Update balances (deduct what was paid)
        BigDecimal paidAmount = settlement.getAmount();
        BigDecimal originalBalance = groupBalanceService.getBalanceAmount(payer, payee, group);
        groupBalanceService.reduceBalance(payer, payee, group, paidAmount.doubleValue());

        // 👉 Step 4.5: Handle Over-Settlement (Auto-Reverse Logic)
        if (originalBalance != null && paidAmount.compareTo(originalBalance) > 0) {
            BigDecimal overPaid = paidAmount.subtract(originalBalance);
            GroupBalance reverse = new GroupBalance();
            reverse.setGroup(group);
            reverse.setFromUser(payee); // reversed direction
            reverse.setToUser(payer);
            reverse.setAmount(overPaid);
            groupBalanceService.saveGroupBalance(reverse);

            log.info("Over-settlement: {} overpaid {} by {} in group {}. Reversed.",
                    payer.getName(), payee.getName(), overPaid, group.getName());
        }

        // Step 5: Return saved settlement
        return savedSettlement;
    }




//    public Settlement createSettlement(Settlement settlement) {
//        // Validate users
//        User fromUser = userRepository.findById(settlement.getFromUser().getId())
//                .orElseThrow(() -> new RuntimeException("FromUser not found"));
//        User toUser = userRepository.findById(settlement.getToUser().getId())
//                .orElseThrow(() -> new RuntimeException("ToUser not found"));
//
//        // Set linked users and timestamp
//        settlement.setFromUser(fromUser);
//        settlement.setToUser(toUser);
//        settlement.setPaidAt(LocalDateTime.now());
//
//        return settlementRepository.save(settlement);
//    }

    public Settlement updateSettlement(Long id, Settlement updated) {
        Settlement existing = settlementRepository.findById(id)
                .orElseThrow(() -> new SettlementNotFoundException("Settlement not found"));

        if (updated.getFromUser() == null || updated.getFromUser().getId() == null) {
            throw new UserNotFoundException("FromUser is required");
        }
        if (updated.getToUser() == null || updated.getToUser().getId() == null) {
            throw new UserNotFoundException("ToUser is required");
        }
        if (updated.getAmount() == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        if (updated.getPaidAt() == null) {
            throw new IllegalArgumentException("PaidAt is required");
        }

        User fromUser = userRepository.findById(updated.getFromUser().getId())
                .orElseThrow(() -> new UserNotFoundException("FromUser not found"));
        User toUser = userRepository.findById(updated.getToUser().getId())
                .orElseThrow(() -> new UserNotFoundException("ToUser not found"));

        if (fromUser.getId().equals(toUser.getId())) {
            throw new IllegalArgumentException("FromUser and ToUser cannot be the same");
        }

        existing.setFromUser(fromUser);
        existing.setToUser(toUser);
        existing.setAmount(updated.getAmount());
        existing.setPaidAt(updated.getPaidAt());

        return settlementRepository.save(existing);
    }
}
