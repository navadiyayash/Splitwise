package com.example.Splitwise.Repository;

import com.example.Splitwise.Entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    // You can add custom methods here later if needed
}
