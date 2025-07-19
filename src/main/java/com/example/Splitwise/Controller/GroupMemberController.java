package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.GroupMember;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Exception.GroupMemberNotFoundException;
import com.example.Splitwise.Exception.GroupNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Repository.GroupMemberRepository;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.UserRepository;
import com.example.Splitwise.Service.GroupMemberService;
import org.springframework.dao.DataIntegrityViolationException;
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
//    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberController(GroupMemberService groupMemberService,
                                 GroupRepository groupRepository,
                                 UserRepository userRepository,
                                 GroupMemberRepository groupMemberRepository) {
        this.groupMemberService = groupMemberService;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
//        this.groupMemberRepository = groupMemberRepository;
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
    public ResponseEntity<?> getAllMembers() {
        try {
            return ResponseEntity.ok(groupMemberService.getAllMembers());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroupMember(@PathVariable Long id, @RequestBody GroupMember updatedGroupMember) {
        try {
            Optional<GroupMember> existingMemberOpt = groupMemberService.getMemberById(id);
            if (existingMemberOpt.isEmpty()) {
                throw new GroupMemberNotFoundException("Group member not found with id: " + id);
            }
            GroupMember existingMember = existingMemberOpt.get();
            Long groupId = updatedGroupMember.getGroup().getId();
            Long userId = updatedGroupMember.getUser().getId();
            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new GroupNotFoundException("Group not found with ID: " + groupId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
            existingMember.setGroup(group);
            existingMember.setUser(user);
            GroupMember saved = groupMemberService.updateMember(id, existingMember);
            return ResponseEntity.ok(saved);
        } catch (GroupMemberNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (GroupNotFoundException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Invalid group member data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

}
