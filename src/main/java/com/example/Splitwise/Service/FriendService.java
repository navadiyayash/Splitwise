package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Friend;
import com.example.Splitwise.Repository.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    // Save a friend relationship
    public Friend addFriend(Friend friend) {
        return friendRepository.save(friend);
    }

    // Get all friend relationships
    public List<Friend> getAllFriends() {
        return friendRepository.findAll();
    }

    // Optional: Get friends of a user
    public List<Friend> getFriendsByUserId(Long userId) {
        return friendRepository.findAll().stream()
                .filter(f -> f.getUser().getId().equals(userId))
                .toList();
    }


    public void deleteFriend(Long id) {
        friendRepository.deleteById(id);
    }
}
