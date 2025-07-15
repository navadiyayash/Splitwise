package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.GroupMember;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Exception.GroupMemberNotFoundException;
import com.example.Splitwise.Exception.GroupNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Repository.GroupMemberRepository;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupMemberService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private GroupRepository groupRepository;


    // Get all group members
    public List<GroupMember> getAllMembers() {
        return groupMemberRepository.findAll();
    }

    // Get a member by ID
    public Optional<GroupMember> getMemberById(Long id) {
        return Optional.ofNullable(groupMemberRepository.findById(id)
            .orElseThrow(() -> new GroupMemberNotFoundException("Group member not found with id: " + id)));
    }

    // Remove a member
    public void removeMember(Long id) {
        if (!groupMemberRepository.existsById(id)) {
            throw new GroupMemberNotFoundException("Group member not found with id: " + id);
        }
        groupMemberRepository.deleteById(id);
    }

    public List<GroupMember> getMembersByGroupId(Long groupId) {
        return groupMemberRepository.findByGroupId(groupId);

    }

    public GroupMember updateMember(Long id, GroupMember updatedMember) {
        return groupMemberRepository.findById(id).map(existing -> {
            existing.setGroup(updatedMember.getGroup());
            existing.setUser(updatedMember.getUser());
            return groupMemberRepository.save(existing);
        }).orElseThrow(() -> new GroupMemberNotFoundException("Group member not found with id: " + id));
    }

    public GroupMember addMember(GroupMember groupMember) {
        // Fetch the full User and Group entities by ID
        Long userId = groupMember.getUser().getId();
        Long groupId = groupMember.getGroup().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found with ID: " + groupId));

        groupMember.setUser(user);
        groupMember.setGroup(group);

        return groupMemberRepository.save(groupMember);
    }
}
