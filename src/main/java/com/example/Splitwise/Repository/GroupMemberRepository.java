package com.example.Splitwise.Repository;

import com.example.Splitwise.Entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByGroupId(Long groupId);
    // Custom methods like finding members by group or user can be added later
}

