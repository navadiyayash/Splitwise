package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Friend;
import com.example.Splitwise.Service.FriendService;
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
    public ResponseEntity<Friend> addFriend(@RequestBody Friend friend) {
        Friend saved = friendService.addFriend(friend);
        return ResponseEntity.ok(saved);
    }

    // ✅ Get all friends of a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<Friend>> getFriendsByUserId(@PathVariable Long userId) {
        List<Friend> friends = friendService.getFriendsByUserId(userId);
        return ResponseEntity.ok(friends);
    }

    // ✅ Delete a friend relationship by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long id) {
        friendService.deleteFriend(id);
        return ResponseEntity.noContent().build();
    }
}
