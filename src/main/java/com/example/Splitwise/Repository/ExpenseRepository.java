package com.example.Splitwise.Repository;

import com.example.Splitwise.Entity.Expense;
import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // You can add methods like List<Expense> findByGroupId(Long groupId) later if needed
//    List<Expense> findByGroupId(Long groupId);
//    List<Expense> findByPaidByUserId(Long userId);

    List<Expense> findByGroup(Group group);
    List<Expense> findByPayer(User payer);

}

