package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.GroupMember;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Repository.GroupMemberRepository;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.UserRepository;
import com.example.Splitwise.Service.GroupMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/group-members")
public class GroupMemberController {

    private final GroupMemberService groupMemberService;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberController(GroupMemberService groupMemberService,
                                 GroupRepository groupRepository,
                                 UserRepository userRepository,
                                 GroupMemberRepository groupMemberRepository) {
        this.groupMemberService = groupMemberService;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    // Add a member to a group
    @PostMapping
    public ResponseEntity<GroupMember> addMember(@RequestBody GroupMember groupMember) {
        GroupMember created = groupMemberService.addMember(groupMember);
        return ResponseEntity.ok(created);
    }

    // Get all members of a group
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<GroupMember>> getMembersByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupMemberService.getMembersByGroupId(groupId));
    }

    // Delete a member from a group
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        groupMemberService.removeMember(id);
        return ResponseEntity.noContent().build();
    }



    // Get all group members
    @GetMapping
    public ResponseEntity<List<GroupMember>> getAllMembers() {
        return ResponseEntity.ok(groupMemberService.getAllMembers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupMember> updateGroupMember(@PathVariable Long id, @RequestBody GroupMember updatedGroupMember) {
        Optional<GroupMember> existingMemberOpt = groupMemberService.getMemberById(id);

        if (existingMemberOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        GroupMember existingMember = existingMemberOpt.get();

        Long groupId = updatedGroupMember.getGroup().getId();
        Long userId = updatedGroupMember.getUser().getId();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        existingMember.setGroup(group);
        existingMember.setUser(user);

        GroupMember saved = groupMemberRepository.save(existingMember);
        return ResponseEntity.ok(saved);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<GroupMember> updateGroupMember(@PathVariable Long id, @RequestBody GroupMember updatedGroupMember) {
//        Optional<GroupMember> optionalMember = groupMemberService.getMemberById(id);
//        if (optionalMember.isPresent()) {
//            GroupMember existingMember = optionalMember.get();
//            existingMember.setGroup(updatedGroupMember.getGroup());
//            existingMember.setUser(updatedGroupMember.getUser());
//            GroupMember savedMember = groupMemberService.addMember(existingMember);
//            return ResponseEntity.ok(savedMember);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

}
