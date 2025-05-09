package org.example.toolsproject.models;

import java.util.List;
import java.util.stream.Collectors;


public class userDTO {
    public int userId;
    public String fullName;
    public String emailAddress;
    public String profileBio;
    public List<userFriendDTO> connections;

    public userDTO(User account) {
        this.userId = account.getUserId();
        this.fullName = account.getFullName();
        this.emailAddress = account.getEmailAddress();
        this.profileBio = account.getProfileBio();
        this.connections = account.getConnections().stream()
                .map(userFriendDTO::new)
                .collect(Collectors.toList());
    }
}