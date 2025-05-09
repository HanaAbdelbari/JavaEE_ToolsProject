package org.example.toolsproject.models;

import org.example.toolsproject.models.User;

public class userFriendDTO {
    public int friendId;
    public String friendName;
    public String friendEmail;

    public userFriendDTO(User account) {
        this.friendId = account.getUserId();
        this.friendName = account.getFullName();
        this.friendEmail = account.getEmailAddress();
    }
}