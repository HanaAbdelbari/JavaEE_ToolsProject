package org.example.toolsproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.example.toolsproject.models.Post.Post;

import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotNull
    private String fullName;

    @NotNull
    @Column(unique = true)
    private String emailAddress;

    @NotNull
    @Column(unique = true)
    private String userPassword;

    @NotNull
    private String profileBio;

    @Column(name = "account_type", insertable = false, updatable = false)
    private String accountType;

    @OneToMany(mappedBy = "sender")
    private List<friendRequest> outgoingRequests;

    @OneToMany(mappedBy = "receiver")
    private List<friendRequest> incomingRequests;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "connection_id")
    )
    private List<User> connections = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Post> userPosts;

    public User(String fullName, String emailAddress, String userPassword, String profileBio) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.userPassword = userPassword;
        this.profileBio = profileBio;
    }

    public User() {}

    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }
    public String getProfileBio() { return profileBio; }
    public void setProfileBio(String profileBio) { this.profileBio = profileBio; }
    public String getAccountType() { return accountType; }
    public List<User> getConnections() { return connections; }
}