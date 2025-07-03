package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.ExpenseShare;
import com.example.Splitwise.Repository.ExpenseShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseShareService {

    @Autowired
    private ExpenseShareRepository expenseShareRepository;

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
        return expenseShareRepository.findById(id);
    }

    // Delete an expense share
    public void deleteExpenseShare(Long id) {
        expenseShareRepository.deleteById(id);
    }

    public ExpenseShare createExpenseShare(ExpenseShare expenseShare) {
        return expenseShareRepository.save(expenseShare);

    }
}
