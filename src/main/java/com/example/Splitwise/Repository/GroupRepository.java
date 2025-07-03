package com.example.Splitwise.Repository;

import com.example.Splitwise.Entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByCreatedById(Long userId);
    // You can add custom query methods here later if needed
}
