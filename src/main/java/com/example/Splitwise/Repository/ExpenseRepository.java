package com.example.Splitwise.Repository;

import com.example.Splitwise.Entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // You can add methods like List<Expense> findByGroupId(Long groupId) later if needed
}

