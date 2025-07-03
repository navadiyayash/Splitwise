package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.ExpenseShare;
import com.example.Splitwise.Service.ExpenseShareService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expense-shares")
public class ExpenseShareController {

    private final ExpenseShareService expenseShareService;

    public ExpenseShareController(ExpenseShareService expenseShareService) {
        this.expenseShareService = expenseShareService;
    }

    // Create a new ExpenseShare
    @PostMapping
    public ResponseEntity<ExpenseShare> createExpenseShare(@RequestBody ExpenseShare expenseShare) {
        ExpenseShare created = expenseShareService.createExpenseShare(expenseShare);
        return ResponseEntity.ok(created);
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseShare> getById(@PathVariable Long id) {
        Optional<ExpenseShare> expenseShare = expenseShareService.getExpenseShareById(id);
        return expenseShare.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<ExpenseShare>> getAll() {
        return ResponseEntity.ok(expenseShareService.getAllExpenseShares());
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseShareService.deleteExpenseShare(id);
        return ResponseEntity.noContent().build();
    }
}
