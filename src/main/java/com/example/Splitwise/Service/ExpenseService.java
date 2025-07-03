package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Expense;
import com.example.Splitwise.Repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    // Save a new expense
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    // Get all expenses
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // Get an expense by ID
    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    // Delete an expense
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);

    }
}
