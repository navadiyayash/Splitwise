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

class GroupMemberServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupMemberRepository groupMemberRepository;
    @Mock
    private GroupRepository groupRepository;
    @InjectMocks
    private GroupMemberService groupMemberService;

    private GroupMember groupMember;
    private User user;
    private Group group;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(); user.setId(1L);
        group = new Group(); group.setId(2L);
        groupMember = new GroupMember();
        groupMember.setId(3L);
        groupMember.setUser(user);
        groupMember.setGroup(group);
    }

    @Test
    void getAllMembers_success() {
        when(groupMemberRepository.findAll()).thenReturn(Arrays.asList(groupMember));
        List<GroupMember> members = groupMemberService.getAllMembers();
        assertEquals(1, members.size());
        assertEquals(groupMember, members.get(0));
    }

    @Test
    void getMemberById_found() {
        when(groupMemberRepository.findById(3L)).thenReturn(Optional.of(groupMember));
        Optional<GroupMember> found = groupMemberService.getMemberById(3L);
        assertTrue(found.isPresent());
        assertEquals(groupMember, found.get());
    }

    @Test
    void getMemberById_notFound_throwsException() {
        when(groupMemberRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(GroupMemberNotFoundException.class, () -> groupMemberService.getMemberById(3L));
    }

    @Test
    void removeMember_success() {
        when(groupMemberRepository.existsById(3L)).thenReturn(true);
        doNothing().when(groupMemberRepository).deleteById(3L);
        assertDoesNotThrow(() -> groupMemberService.removeMember(3L));
        verify(groupMemberRepository, times(1)).deleteById(3L);
    }

    @Test
    void removeMember_notFound_throwsException() {
        when(groupMemberRepository.existsById(3L)).thenReturn(false);
        assertThrows(GroupMemberNotFoundException.class, () -> groupMemberService.removeMember(3L));
    }

    @Test
    void getMembersByGroupId_success() {
        when(groupMemberRepository.findByGroupId(2L)).thenReturn(Arrays.asList(groupMember));
        List<GroupMember> members = groupMemberService.getMembersByGroupId(2L);
        assertEquals(1, members.size());
        assertEquals(groupMember, members.get(0));
    }

    @Test
    void updateMember_success() {
        GroupMember updated = new GroupMember();
        updated.setGroup(group);
        updated.setUser(user);
        when(groupMemberRepository.findById(3L)).thenReturn(Optional.of(groupMember));
        when(groupMemberRepository.save(any(GroupMember.class))).thenReturn(groupMember);
        GroupMember result = groupMemberService.updateMember(3L, updated);
        assertNotNull(result);
        verify(groupMemberRepository).save(any(GroupMember.class));
    }

    @Test
    void updateMember_notFound_throwsException() {
        GroupMember updated = new GroupMember();
        when(groupMemberRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(GroupMemberNotFoundException.class, () -> groupMemberService.updateMember(3L, updated));
    }

    @Test
    void addMember_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(groupRepository.findById(2L)).thenReturn(Optional.of(group));
        when(groupMemberRepository.save(any(GroupMember.class))).thenReturn(groupMember);
        GroupMember toAdd = new GroupMember();
        toAdd.setUser(user); toAdd.setGroup(group);
        GroupMember result = groupMemberService.addMember(toAdd);
        assertNotNull(result);
        verify(groupMemberRepository).save(any(GroupMember.class));
    }

    @Test
    void addMember_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        GroupMember toAdd = new GroupMember();
        User u = new User(); u.setId(1L);
        toAdd.setUser(u); toAdd.setGroup(group);
        assertThrows(UserNotFoundException.class, () -> groupMemberService.addMember(toAdd));
    }

    @Test
    void addMember_groupNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(groupRepository.findById(2L)).thenReturn(Optional.empty());
        GroupMember toAdd = new GroupMember();
        toAdd.setUser(user); toAdd.setGroup(group);
        assertThrows(GroupNotFoundException.class, () -> groupMemberService.addMember(toAdd));
    }
}

