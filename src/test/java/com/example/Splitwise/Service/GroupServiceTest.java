package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Group;
import com.example.Splitwise.Exception.GroupNotFoundException;
import com.example.Splitwise.Repository.GroupRepository;
import com.example.Splitwise.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private GroupService groupService;

    private Group group;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        group = new Group();
        group.setId(1L);
        group.setName("Test Group");
    }

    @Test
    void createGroup_success() {
        when(groupRepository.save(group)).thenReturn(group);
        Group created = groupService.createGroup(group);
        assertEquals(group, created);
    }

    @Test
    void getAllGroups_success() {
        when(groupRepository.findAll()).thenReturn(Arrays.asList(group));
        List<Group> groups = groupService.getAllGroups();
        assertEquals(1, groups.size());
        assertEquals(group, groups.get(0));
    }

    @Test
    void getGroupById_found() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        Optional<Group> found = groupService.getGroupById(1L);
        assertTrue(found.isPresent());
        assertEquals(group, found.get());
    }

    @Test
    void getGroupById_notFound() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Group> found = groupService.getGroupById(1L);
        assertFalse(found.isPresent());
    }

    @Test
    void deleteGroup_success() {
        when(groupRepository.existsById(1L)).thenReturn(true);
        doNothing().when(groupRepository).deleteById(1L);
        assertDoesNotThrow(() -> groupService.deleteGroup(1L));
        verify(groupRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteGroup_notFound_throwsException() {
        when(groupRepository.existsById(1L)).thenReturn(false);
        assertThrows(GroupNotFoundException.class, () -> groupService.deleteGroup(1L));
    }

    @Test
    void getGroupsByUserId_success() {
        when(groupRepository.findByCreatedById(2L)).thenReturn(Arrays.asList(group));
        List<Group> groups = groupService.getGroupsByUserId(2L);
        assertEquals(1, groups.size());
        assertEquals(group, groups.get(0));
    }
}

