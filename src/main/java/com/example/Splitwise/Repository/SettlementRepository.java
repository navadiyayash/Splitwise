package com.example.Splitwise.Repository;

import com.example.Splitwise.Entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    // You can add custom queries if needed later
}
