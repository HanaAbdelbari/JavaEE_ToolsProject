package org.example.toolsproject.models.groups;

import jakarta.persistence.*;
import org.example.toolsproject.models.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
public class group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String Gname;

    private String Groupdescription;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User Creator;

    @Column(name = "is_open", nullable = false)
    private boolean isOpen;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<groupMembership> groupmemberships = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<groupPost> posts = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return Gname; }
    public void setName(String name) { this.Gname = name; }

    public String getDescription() { return Groupdescription; }
    public void setDescription(String description) { this.Groupdescription = description; }

    public User getCreator() { return Creator; }
    public void setCreator(User creator) { this.Creator = creator; }

    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean open) { isOpen = open; }

    public List<groupMembership> getMemberships() { return groupmemberships; }
    public void setMemberships(List<groupMembership> memberships) { this.groupmemberships = memberships; }

    public List<groupPost> getPosts() { return posts; }
    public void setPosts(List<groupPost> posts) { this.posts = posts; }

    public boolean isAdmin(User user) {
        if (user == null) return false;
        return Creator.equals(user) || groupmemberships.stream()
                .anyMatch(m -> m.getUser().equals(user)
                        && m.getRole().equalsIgnoreCase("ADMIN")
                        && m.getStatus().equalsIgnoreCase("APPROVED"));
    }

    public boolean isMember(User user) {
        if (user == null) return false;
        return groupmemberships.stream()
                .anyMatch(m -> m.getUser().equals(user)
                        && m.getStatus().equalsIgnoreCase("APPROVED"));
    }

    public groupMembership getMembershipForUser(User user) {
        if (user == null) return null;
        return groupmemberships.stream()
                .filter(m -> m.getUser().equals(user))
                .findFirst()
                .orElse(null);
    }


    public void addMembership(groupMembership membership) {
        groupmemberships.add(membership);
        membership.setGroup(this);
    }

    public void removeMembership(groupMembership membership) {
        groupmemberships.remove(membership);
        membership.setGroup(null);
    }
}
