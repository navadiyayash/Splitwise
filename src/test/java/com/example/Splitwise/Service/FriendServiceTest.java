package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Friend;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Exception.FriendNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Repository.FriendRepository;
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

class FriendServiceTest {
    @Mock
    private FriendRepository friendRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private FriendService friendService;

    private User user;
    private User friendUser;
    private Friend friend;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(); user.setId(1L);
        friendUser = new User(); friendUser.setId(2L);
        friend = new Friend();
        friend.setId(3L);
        friend.setUser(user);
        friend.setFriend(friendUser);
    }

    @Test
    void addFriend_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friendUser));
        when(friendRepository.save(any(Friend.class))).thenReturn(friend);
        Friend result = friendService.addFriend(friend);
        assertNotNull(result);
        verify(friendRepository).save(any(Friend.class));
    }

    @Test
    void addFriend_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> friendService.addFriend(friend));
    }

    @Test
    void addFriend_friendNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> friendService.addFriend(friend));
    }

    @Test
    void getAllFriends_success() {
        when(friendRepository.findAll()).thenReturn(Arrays.asList(friend));
        List<Friend> friends = friendService.getAllFriends();
        assertEquals(1, friends.size());
        assertEquals(friend, friends.get(0));
    }

    @Test
    void getFriendsByUserId_success() {
        when(friendRepository.findAll()).thenReturn(Arrays.asList(friend));
        List<Friend> friends = friendService.getFriendsByUserId(1L);
        assertEquals(1, friends.size());
        assertEquals(friend, friends.get(0));
    }

    @Test
    void deleteFriend_success() {
        when(friendRepository.existsById(3L)).thenReturn(true);
        doNothing().when(friendRepository).deleteById(3L);
        assertDoesNotThrow(() -> friendService.deleteFriend(3L));
        verify(friendRepository).deleteById(3L);
    }

    @Test
    void deleteFriend_notFound_throwsException() {
        when(friendRepository.existsById(3L)).thenReturn(false);
        assertThrows(FriendNotFoundException.class, () -> friendService.deleteFriend(3L));
    }

    @Test
    void updateFriend_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friendUser));
        when(friendRepository.findById(3L)).thenReturn(Optional.of(friend));
        when(friendRepository.save(any(Friend.class))).thenReturn(friend);
        Friend updated = new Friend();
        updated.setUser(user); updated.setFriend(friendUser);
        Friend result = friendService.updateFriend(3L, updated);
        assertNotNull(result);
        verify(friendRepository).save(any(Friend.class));
    }

    @Test
    void updateFriend_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Friend updated = new Friend();
        updated.setUser(user); updated.setFriend(friendUser);
        assertThrows(UserNotFoundException.class, () -> friendService.updateFriend(3L, updated));
    }

    @Test
    void updateFriend_friendNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        Friend updated = new Friend();
        updated.setUser(user); updated.setFriend(friendUser);
        assertThrows(UserNotFoundException.class, () -> friendService.updateFriend(3L, updated));
    }

    @Test
    void updateFriend_relationshipNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friendUser));
        when(friendRepository.findById(3L)).thenReturn(Optional.empty());
        Friend updated = new Friend();
        updated.setUser(user); updated.setFriend(friendUser);
        assertThrows(FriendNotFoundException.class, () -> friendService.updateFriend(3L, updated));
    }
}

