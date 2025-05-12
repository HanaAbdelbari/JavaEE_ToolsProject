package org.example.toolsproject.models.Post;


import jakarta.persistence.*;
import org.example.toolsproject.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 2000)
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "link_url")
    private String linkUrl;
    @Column(name = "comment-count")
    private int commentCount;

    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "created_time", nullable = false, updatable = false)
    private String createdTime;

    @PrePersist
    protected void onCreate() {
        this.createdTime = LocalDateTime.now().toString();
    }

    public int getLikecount() {
        return likeCount;
    }

    public void setLikecount(int likecount) {
        this.likeCount = likecount;
    }

    public int getCommentcount() {
        return commentCount;
    }

    public void setCommentcount(int commentcount) {
        this.commentCount = commentcount;
    }





    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Like> likes;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
    public String getcreatedTime() { return createdTime; }
    public void setcreatedTime(String createdAt) { this.createdTime = createdAt; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
    public List<Like> getLikes() { return likes; }
    public void setLikes(List<Like> likes) { this.likes = likes; }
}