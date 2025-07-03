package com.example.Splitwise.Repository;

import com.example.Splitwise.Entity.GroupBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupBalanceRepository extends JpaRepository<GroupBalance, Long> {
    List<GroupBalance> findByGroupId(Long groupId);

    // Custom queries for balances can be added later
}

