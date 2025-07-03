package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    // Create a new group
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    // Get all groups
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    // Get a group by ID
    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    // Delete a group by ID
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }


    public List<Group> getGroupsByUserId(Long userId) {
        return groupRepository.findByCreatedById(userId);

    }
}
