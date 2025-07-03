package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.GroupBalance;
import com.example.Splitwise.Service.GroupBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/group-balances")
public class GroupBalanceController {

    private final GroupBalanceService groupBalanceService;

    public GroupBalanceController(GroupBalanceService groupBalanceService) {
        this.groupBalanceService = groupBalanceService;
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

    // ✅ Create or update a balance
    @PostMapping
    public ResponseEntity<GroupBalance> createGroupBalance(@RequestBody GroupBalance groupBalance) {
        GroupBalance saved = groupBalanceService.createGroupBalance(groupBalance);
        return ResponseEntity.ok(saved);
    }

    // ✅ Delete a group balance
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupBalance(@PathVariable Long id) {
        groupBalanceService.deleteGroupBalance(id);
        return ResponseEntity.noContent().build();
    }
}
