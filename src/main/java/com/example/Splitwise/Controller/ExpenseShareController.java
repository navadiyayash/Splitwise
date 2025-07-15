package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.ExpenseShare;
import com.example.Splitwise.Service.ExpenseShareService;
import com.example.Splitwise.Exception.ExpenseNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Exception.ExpenseShareNotFoundException;
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
    public ResponseEntity<?> createExpenseShare(@RequestBody ExpenseShare expenseShare) {
        try {
            ExpenseShare created = expenseShareService.createExpenseShare(expenseShare);
            return ResponseEntity.ok(created);
        } catch (ExpenseNotFoundException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Optional<ExpenseShare> expenseShare = expenseShareService.getExpenseShareById(id);
            return expenseShare.map(ResponseEntity::ok)
                    .orElseThrow(() -> new ExpenseShareNotFoundException("ExpenseShare not found with id: " + id));
        } catch (ExpenseShareNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<ExpenseShare>> getAll() {
        return ResponseEntity.ok(expenseShareService.getAllExpenseShares());
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            expenseShareService.deleteExpenseShare(id);
            return ResponseEntity.noContent().build();
        } catch (ExpenseShareNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
