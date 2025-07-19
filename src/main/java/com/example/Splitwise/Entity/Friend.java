package com.example.Splitwise.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "friends", uniqueConstraints = {
        @UniqueConstraint
                (columnNames = {"user_id", "friend_id"})
})
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "friend_id")
    private User friend;

    // Getters and setters

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public User getFriend() {

        return friend;
    }

    public void setFriend(User friend) {

        this.friend = friend;
    }
}

