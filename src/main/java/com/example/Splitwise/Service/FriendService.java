package com.example.Splitwise.Service;

import com.example.Splitwise.Entity.Friend;
import com.example.Splitwise.Entity.User;
import com.example.Splitwise.Repository.FriendRepository;
import com.example.Splitwise.Repository.UserRepository;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Exception.FriendNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;

    // Save a friend relationship
    public Friend addFriend(Friend friend) {
        Long userId = friend.getUser().getId();
        Long friendId = friend.getFriend().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        User friendUser = userRepository.findById(friendId)
                .orElseThrow(() -> new UserNotFoundException("Friend not found with ID: " + friendId));

        friend.setUser(user);
        friend.setFriend(friendUser);
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
        if (!friendRepository.existsById(id)) {
            throw new FriendNotFoundException("Friend relationship not found with id: " + id);
        }
        friendRepository.deleteById(id);
    }


    public Friend updateFriend(Long id, Friend updatedFriend) {
        Long userId = updatedFriend.getUser().getId();
        Long friendId = updatedFriend.getFriend().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        User friendUser = userRepository.findById(friendId)
                .orElseThrow(() -> new UserNotFoundException("Friend not found with ID: " + friendId));

        return friendRepository.findById(id)
                .map(existingFriend -> {
                    existingFriend.setUser(user);
                    existingFriend.setFriend(friendUser);
                    return friendRepository.save(existingFriend);
                })
                .orElseThrow(() -> new FriendNotFoundException("Friend relationship not found with id: " + id));
    }

//    public Friend updateFriend(Long id, Friend updatedFriend) {
//        Long userId = updatedFriend.getUser().getId();
//        Long friendId = updatedFriend.getFriend().getId();
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
//        User friendUser = userRepository.findById(friendId)
//                .orElseThrow(() -> new RuntimeException("Friend not found with ID: " + friendId));
//        return friendRepository.findById(id)
//                .map(existingFriend -> {
//                    existingFriend.setUser(updatedFriend.getUser());
//                    existingFriend.setFriend(updatedFriend.getFriend());
//                    return friendRepository.save(existingFriend);
//                })
//                .orElseThrow(() -> new RuntimeException("Friend relationship not found with id: " + id));
//    }

}
