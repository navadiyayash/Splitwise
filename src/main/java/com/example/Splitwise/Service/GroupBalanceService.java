package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.GroupBalance;
import com.example.Splitwise.Repository.GroupBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupBalanceService {

    @Autowired
    private GroupBalanceRepository groupBalanceRepository;

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
}
