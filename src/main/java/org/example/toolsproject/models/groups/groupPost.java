package org.example.toolsproject.models.groups;


import jakarta.persistence.*;
import org.example.toolsproject.models.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_posts")
public class groupPost {
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
    private String Content;

    @Column(nullable = false)
    private LocalDateTime CreatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public org.example.toolsproject.models.groups.group getGroup() { return group; }
    public void setGroup(org.example.toolsproject.models.groups.group group) { this.group = group; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getContent() { return Content; }
    public void setContent(String content) { this.Content = content; }
    public LocalDateTime getCreatedAt() { return CreatedAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.CreatedAt = createdAt; }
}