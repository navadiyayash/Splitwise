package com.example.Splitwise.Repository;

import com.example.Splitwise.Entity.ExpenseShare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseShareRepository extends JpaRepository<ExpenseShare, Long> {
    // Add custom query methods here later if needed
}

