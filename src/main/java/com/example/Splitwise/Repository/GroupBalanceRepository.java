package com.example.Splitwise.Repository;

import com.example.Splitwise.Entity.GroupBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupBalanceRepository extends JpaRepository<GroupBalance, Long> {
    // Custom queries for balances can be added later
}

