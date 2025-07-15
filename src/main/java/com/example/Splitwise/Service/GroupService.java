package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Exception.GroupNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;


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
        if (!groupRepository.existsById(id)) {
            throw new GroupNotFoundException("Group not found with id: " + id);
        }
        groupRepository.deleteById(id);
    }


    public List<Group> getGroupsByUserId(Long userId) {
        return groupRepository.findByCreatedById(userId);

    }

    public Group updateGroup(Long id, Group updatedGroup) {
        return groupRepository.findById(id).map(existingGroup -> {
            existingGroup.setName(updatedGroup.getName());
            existingGroup.setAutoSimplifyEnabled(updatedGroup.getAutoSimplifyEnabled());

            // Make sure to fetch and set full user object if only ID is passed
            Long userId = updatedGroup.getCreatedBy().getId();
            User creator = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
            existingGroup.setCreatedBy(creator);

            return groupRepository.save(existingGroup);
        }).orElseThrow(() -> new GroupNotFoundException("Group not found with id: " + id));
    }


}
