package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register or Save a user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get all users
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

