package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.GroupMember;
import com.example.Splitwise.Repository.GroupMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupMemberService {

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    // Add a user to a group
    public GroupMember addMember(GroupMember groupMember) {
        return groupMemberRepository.save(groupMember);
    }

    // Get all group members
    public List<GroupMember> getAllMembers() {
        return groupMemberRepository.findAll();
    }

    // Get a member by ID
    public Optional<GroupMember> getMemberById(Long id) {
        return groupMemberRepository.findById(id);
    }

    // Remove a member
    public void removeMember(Long id) {
        groupMemberRepository.deleteById(id);
    }

    public List<GroupMember> getMembersByGroupId(Long groupId) {
        return groupMemberRepository.findByGroupId(groupId);

    }
}
