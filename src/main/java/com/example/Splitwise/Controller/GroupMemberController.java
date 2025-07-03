package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.GroupMember;
import com.example.Splitwise.Service.GroupMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-members")
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    public GroupMemberController(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
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
}
