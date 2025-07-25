package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Expense;
import com.example.Splitwise.Entity.ExpenseShare;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Exception.ExpenseNotFoundException;
import com.example.Splitwise.Exception.ExpenseShareNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Repository.ExpenseRepository;
import com.example.Splitwise.Repository.ExpenseShareRepository;
import com.example.Splitwise.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseShareService {

    @Autowired
    private ExpenseShareRepository expenseShareRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    // Save a new expense share
    public ExpenseShare addExpenseShare(ExpenseShare expenseShare) {
        return expenseShareRepository.save(expenseShare);
    }

    // Get all expense shares
    public List<ExpenseShare> getAllExpenseShares() {
        return expenseShareRepository.findAll();
    }

    // Get an expense share by ID
    public Optional<ExpenseShare> getExpenseShareById(Long id) {
        return Optional.ofNullable(expenseShareRepository.findById(id)
            .orElseThrow(() -> new ExpenseShareNotFoundException("ExpenseShare not found: " + id)));
    }

    // Delete an expense share
    public void deleteExpenseShare(Long id) {
        if (!expenseShareRepository.existsById(id)) {
            throw new ExpenseShareNotFoundException("ExpenseShare not found: " + id);
        }
        expenseShareRepository.deleteById(id);
    }


    public ExpenseShare createExpenseShare(ExpenseShare share) {
        Long expenseId = share.getExpense().getId();
        Long userId = share.getUser().getId();

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found: " + expenseId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        share.setExpense(expense);
        share.setUser(user);
        return expenseShareRepository.save(share);
    }

}
