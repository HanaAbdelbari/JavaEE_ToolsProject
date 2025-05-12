package org.example.toolsproject.models.groups;


import jakarta.persistence.*;
import org.example.toolsproject.models.User;

@Entity
public class groupMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private org.example.toolsproject.models.groups.group group;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String Role; // "MEMBER" or "ADMIN"

    @Column(nullable = false)
    private String Status; // "APPROVED", "PENDING", "REJECTED"

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public org.example.toolsproject.models.groups.group getGroup() { return group; }
    public void setGroup(org.example.toolsproject.models.groups.group group) { this.group = group; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getRole() { return Role; }
    public void setRole(String role) { this.Role = role; }
    public String getStatus() { return Status; }
    public void setStatus(String status) { this.Status = status; }
}
