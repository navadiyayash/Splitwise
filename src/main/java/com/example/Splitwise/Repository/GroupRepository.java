package com.example.Splitwise.Repository;

import com.example.Splitwise.Entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    // You can add custom query methods here later if needed
}
