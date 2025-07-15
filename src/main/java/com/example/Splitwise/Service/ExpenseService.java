package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Expense;
import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Repository.ExpenseRepository;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

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
        // Fetch actual User and Group from DB using IDs
        Long userId = expense.getPayer().getId();
        Long groupId = expense.getGroup().getId();

        User payer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        // Replace partial objects with fully-loaded ones
        expense.setPayer(payer);
        expense.setGroup(group);

        return expenseRepository.save(expense);
    }
    // Update an expense
    public Expense updateExpense(Long id, Expense updatedExpense) {
        return expenseRepository.findById(id)
                .map(expense -> {
                    // Fetch full payer and group from DB
                    Long payerId = updatedExpense.getPayer().getId();
                    Long groupId = updatedExpense.getGroup().getId();

                    User payer = userRepository.findById(payerId)
                            .orElseThrow(() -> new com.example.Splitwise.Exception.UserNotFoundException("User not found with id: " + payerId));

                    Group group = groupRepository.findById(groupId)
                            .orElseThrow(() -> new com.example.Splitwise.Exception.GroupNotFoundException("Group not found with id: " + groupId));

                    // Set updated fields
                    expense.setDescription(updatedExpense.getDescription());
                    expense.setAmount(updatedExpense.getAmount());
                    expense.setCreatedAt(updatedExpense.getCreatedAt());
                    expense.setPayer(payer);
                    expense.setGroup(group);

                    return expenseRepository.save(expense);
                })
                .orElseThrow(() -> new com.example.Splitwise.Exception.ExpenseNotFoundException("Expense not found with id: " + id));
    }

//    public Expense updateExpense(Long id, Expense updatedExpense) {
//        return expenseRepository.findById(id)
//                .map(expense -> {
//                    expense.setAmount(updatedExpense.getAmount());
//                    expense.setDescription(updatedExpense.getDescription());
//                    expense.setGroupId(updatedExpense.getGroupId());
//                    expense.setPaidByUserId(updatedExpense.getPaidByUserId());
//                    expense.setExpenseDate(updatedExpense.getExpenseDate());
//                    return expenseRepository.save(expense);
//                })
//                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
//    }

    // Get expenses by group ID
    public List<Expense> getExpensesByGroupId(Group group) {
        return expenseRepository.findByGroup(group);
    }

    // Get expenses by user ID
    public List<Expense> getExpensesByPaidByUserId(User user) {
        return expenseRepository.findByPayer(user);
    }

    public List<Expense> getExpensesByGroup(Group group) {
        return expenseRepository.findByGroup(group);
    }

    public List<Expense> getExpensesByPayer(User user) {
        return expenseRepository.findByPayer(user);
    }

}
