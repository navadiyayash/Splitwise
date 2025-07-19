package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Friend;
import com.example.Splitwise.Exception.FriendNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Service.FriendService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {

        this.friendService = friendService;
    }

    // ✅ Add a friend connection
    @PostMapping
    public ResponseEntity<?> addFriend(@RequestBody Friend friend) {
        try {
            Friend saved = friendService.addFriend(friend);
            return ResponseEntity.ok(saved);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Invalid friend relationship: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    // ✅ Get all friends of a user
    @GetMapping("/{userId}")
    public ResponseEntity<?> getFriendsByUserId(@PathVariable Long userId) {
        try {
            List<Friend> friends = friendService.getFriendsByUserId(userId);
            return ResponseEntity.ok(friends);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    // ✅ Delete a friend relationship by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFriend(@PathVariable Long id) {
        try {
            friendService.deleteFriend(id);
            return ResponseEntity.noContent().build();
        } catch (FriendNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Friend> updateFriend(@PathVariable Long id, @RequestBody Friend updatedFriend) {
//        List<Friend> all = friendService.getAllFriends();
//        for (Friend f : all) {
//            if (f.getId().equals(id)) {
//                f.setUser(updatedFriend.getUser());
//                f.setFriend(updatedFriend.getFriend());
//                Friend saved = friendService.addFriend(f);
//                return ResponseEntity.ok(saved);
//            }
//        }
//        return ResponseEntity.notFound().build();
//    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFriend(@PathVariable Long id, @RequestBody Friend updatedFriend) {
        try {
            Friend saved = friendService.updateFriend(id, updatedFriend);
            return ResponseEntity.ok(saved);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (FriendNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllFriends() {
        try {
            return ResponseEntity.ok(friendService.getAllFriends());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }


}
