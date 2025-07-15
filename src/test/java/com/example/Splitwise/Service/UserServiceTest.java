package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Exception.UserAlreadyExistsException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
    }

    @Test
    void createUser_success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        User created = userService.createUser(user);
        assertEquals(user, created);
    }

    @Test
    void createUser_duplicateEmail_throwsException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(user));
    }

    @Test
    void findByEmail_userExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> found = userService.findByEmail(user.getEmail());
        assertTrue(found.isPresent());
        assertEquals(user, found.get());
    }

    @Test
    void findByEmail_userNotFound_throwsException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(user.getEmail()));
    }

    @Test
    void getUserById_userExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> found = userService.getUserById(user.getId());
        assertTrue(found.isPresent());
        assertEquals(user, found.get());
    }

    @Test
    void getUserById_userNotFound_throwsException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(user.getId()));
    }

    @Test
    void deleteUser_success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(user.getId());
        assertDoesNotThrow(() -> userService.deleteUser(user.getId()));
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void deleteUser_userNotFound_throwsException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(user.getId()));
    }
}

