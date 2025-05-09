package org.example.toolsproject.models.Post;


import jakarta.persistence.*;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int ActionMaker;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;



    public Like(Post post, int userId) {
        this.post = post;
        this.ActionMaker = userId;
    }


    public Like() {

    }


    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public int getLikerid() {
        return ActionMaker;
    }

    public void setLikerid(int likerid) {
        ActionMaker = likerid;
    }
}