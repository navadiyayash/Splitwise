package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.GroupBalance;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Exception.GroupMemberNotFoundException;
import com.example.Splitwise.Exception.GroupNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.UserRepository;
import com.example.Splitwise.Service.GroupBalanceService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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

    @GetMapping("/group/{groupId}/net-balances")
    public ResponseEntity<Map<Long, BigDecimal>> getNetBalances(@PathVariable Long groupId) {
        Map<Long, BigDecimal> balances = groupBalanceService.getNetBalancesByGroup(groupId);
        return ResponseEntity.ok(balances);
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
    public ResponseEntity<?> createGroupBalance(@RequestBody GroupBalance groupBalance) {
        try {
            Group group = groupRepository.findById(groupBalance.getGroup().getId())
                    .orElseThrow(() -> new GroupNotFoundException("Group not found"));
            User fromUser = userRepository.findById(groupBalance.getFromUser().getId())
                    .orElseThrow(() -> new UserNotFoundException("FromUser not found"));
            User toUser = userRepository.findById(groupBalance.getToUser().getId())
                    .orElseThrow(() -> new UserNotFoundException("ToUser not found"));
            groupBalance.setGroup(group);
            groupBalance.setFromUser(fromUser);
            groupBalance.setToUser(toUser);
            GroupBalance saved = groupBalanceService.saveGroupBalance(groupBalance);
            return ResponseEntity.ok(saved);
        } catch (GroupNotFoundException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Invalid group balance: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    // ✅ Create or update a balance

    // ✅ Delete a group balance
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroupBalance(@PathVariable Long id) {
        try {
            groupBalanceService.deleteGroupBalance(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroupBalance(@PathVariable Long id, @RequestBody GroupBalance updatedBalance) {
        try {
            Optional<GroupBalance> existingOpt = groupBalanceService.getGroupBalanceById(id);
            if (existingOpt.isEmpty()) {
                throw new GroupMemberNotFoundException("Group balance not found with id: " + id);
            }
            GroupBalance existing = existingOpt.get();
            Group group = groupRepository.findById(updatedBalance.getGroup().getId())
                    .orElseThrow(() -> new GroupNotFoundException("Group not found"));
            User fromUser = userRepository.findById(updatedBalance.getFromUser().getId())
                    .orElseThrow(() -> new UserNotFoundException("From user not found"));
            User toUser = userRepository.findById(updatedBalance.getToUser().getId())
                    .orElseThrow(() -> new UserNotFoundException("To user not found"));
            existing.setGroup(group);
            existing.setFromUser(fromUser);
            existing.setToUser(toUser);
            existing.setAmount(updatedBalance.getAmount());
            GroupBalance saved = groupBalanceService.saveGroupBalance(existing);
            return ResponseEntity.ok(saved);
        } catch (GroupNotFoundException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (GroupMemberNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Invalid group balance: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<?> getAllGroupBalances() {
        try {
            List<GroupBalance> balances = groupBalanceService.getAllGroupBalances();
            return ResponseEntity.ok(balances);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }


}
