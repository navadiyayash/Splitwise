package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // Create a new group
    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group createdGroup = groupService.createGroup(group);
        return ResponseEntity.ok(createdGroup);
    }

    // Get group by ID
    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        Optional<Group> group = groupService.getGroupById(id);
        return group.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all groups created by a user
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Group>> getGroupsByUserId(@PathVariable Long userId) {
        List<Group> groups = groupService.getGroupsByUserId(userId);
        return ResponseEntity.ok(groups);
    }
}
