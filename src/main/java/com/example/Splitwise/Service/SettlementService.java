package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Settlement;
import com.example.Splitwise.Repository.SettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SettlementService {

    @Autowired
    private SettlementRepository settlementRepository;

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
        return settlementRepository.save(settlement);

    }
}
