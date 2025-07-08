package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.GroupBalance;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.UserRepository;
import com.example.Splitwise.Service.GroupBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/group-balances")
public class GroupBalanceController {

    private final GroupBalanceService groupBalanceService;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupBalanceController(
            GroupBalanceService groupBalanceService,
            GroupRepository groupRepository,
            UserRepository userRepository
    ) {
        this.groupBalanceService = groupBalanceService;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    // ✅ Get all balances for a group
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<GroupBalance>> getBalancesByGroupId(@PathVariable Long groupId) {
        List<GroupBalance> balances = groupBalanceService.getBalancesByGroupId(groupId);
        return ResponseEntity.ok(balances);
    }

    // ✅ Get a specific group balance by ID
    @GetMapping("/{id}")
    public ResponseEntity<GroupBalance> getGroupBalanceById(@PathVariable Long id) {
        Optional<GroupBalance> balance = groupBalanceService.getGroupBalanceById(id);
        return balance.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GroupBalance> createGroupBalance(@RequestBody GroupBalance groupBalance) {
        // Fetch full group and user objects from the DB
        Group group = groupRepository.findById(groupBalance.getGroup().getId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User fromUser = userRepository.findById(groupBalance.getFromUser().getId())
                .orElseThrow(() -> new RuntimeException("FromUser not found"));

        User toUser = userRepository.findById(groupBalance.getToUser().getId())
                .orElseThrow(() -> new RuntimeException("ToUser not found"));

        groupBalance.setGroup(group);
        groupBalance.setFromUser(fromUser);
        groupBalance.setToUser(toUser);

        GroupBalance saved = groupBalanceService.saveGroupBalance(groupBalance);
        return ResponseEntity.ok(saved);
    }

    // ✅ Create or update a balance

    // ✅ Delete a group balance
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupBalance(@PathVariable Long id) {
        groupBalanceService.deleteGroupBalance(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupBalance> updateGroupBalance(@PathVariable Long id, @RequestBody GroupBalance updatedBalance) {
        Optional<GroupBalance> existingOpt = groupBalanceService.getGroupBalanceById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        GroupBalance existing = existingOpt.get();

        Group group = groupRepository.findById(updatedBalance.getGroup().getId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User fromUser = userRepository.findById(updatedBalance.getFromUser().getId())
                .orElseThrow(() -> new RuntimeException("From user not found"));

        User toUser = userRepository.findById(updatedBalance.getToUser().getId())
                .orElseThrow(() -> new RuntimeException("To user not found"));

        existing.setGroup(group);
        existing.setFromUser(fromUser);
        existing.setToUser(toUser);
        existing.setAmount(updatedBalance.getAmount());

        GroupBalance saved = groupBalanceService.saveGroupBalance(existing);
        return ResponseEntity.ok(saved);
    }


    @GetMapping
    public ResponseEntity<List<GroupBalance>> getAllGroupBalances() {
        List<GroupBalance> balances = groupBalanceService.getAllGroupBalances();
        return ResponseEntity.ok(balances);
    }


}
