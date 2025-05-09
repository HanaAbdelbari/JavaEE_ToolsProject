package org.example.toolsproject.models;

public class userFriendDTO {
    public int id;
    public String name;
    public String email;

    public userFriendDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}

