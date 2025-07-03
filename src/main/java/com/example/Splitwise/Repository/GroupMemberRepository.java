package com.example.Splitwise.Repository;

import com.example.Splitwise.Entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    // Custom methods like finding members by group or user can be added later
}

