package com.example.Splitwise.Repository;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.GroupBalance;
import com.example.Splitwise.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupBalanceRepository extends JpaRepository<GroupBalance, Long> {
    List<GroupBalance> findByGroupId(Long groupId);
    Optional<GroupBalance> findByGroupAndFromUserAndOwedToUser(Group group, User fromUser, User owedToUser);


    // Custom queries for balances can be added later
}

