package org.example.toolsproject.models;

import java.util.List;
import java.util.stream.Collectors;

public class userDTO {
    public int id;
    public String name;
    public String email;
    public String bio;
    public List<userFriendDTO> friends;

    public userDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.friends = user.getFriends().stream()
                .map(userFriendDTO::new)
                .collect(Collectors.toList());
    }
}

