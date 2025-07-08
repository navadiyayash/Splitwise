package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Settlement;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Repository.SettlementRepository;
import com.example.Splitwise.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SettlementService {

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private UserRepository userRepository;

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


    public Settlement createSettlement(Settlement settlement) {
        // Validate users
        User fromUser = userRepository.findById(settlement.getFromUser().getId())
                .orElseThrow(() -> new RuntimeException("FromUser not found"));
        User toUser = userRepository.findById(settlement.getToUser().getId())
                .orElseThrow(() -> new RuntimeException("ToUser not found"));

        // Set linked users and timestamp
        settlement.setFromUser(fromUser);
        settlement.setToUser(toUser);
        settlement.setPaidAt(LocalDateTime.now());

        return settlementRepository.save(settlement);
    }

    public Settlement updateSettlement(Long id, Settlement updated) {
        Settlement existing = settlementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Settlement not found"));

        User fromUser = userRepository.findById(updated.getFromUser().getId())
                .orElseThrow(() -> new RuntimeException("FromUser not found"));
        User toUser = userRepository.findById(updated.getToUser().getId())
                .orElseThrow(() -> new RuntimeException("ToUser not found"));

        existing.setFromUser(fromUser);
        existing.setToUser(toUser);
        existing.setAmount(updated.getAmount());
        existing.setPaidAt(updated.getPaidAt());

        return settlementRepository.save(existing);
    }
}
