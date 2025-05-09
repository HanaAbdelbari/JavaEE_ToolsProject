package org.example.toolsproject.ejbs;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.toolsproject.models.Post.DTOs.PostDTO;
import org.example.toolsproject.models.Post.Comment;
import org.example.toolsproject.models.Post.Like;
import org.example.toolsproject.models.Post.Post;
import org.example.toolsproject.models.User;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PostEJB {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    // Cr e a t  e Post from main
    public Post CreatePost(int userId, String content, String imageUrl, String linkUrl) {
        User user = em.find(User.class, userId);
        if (user == null) throw new IllegalArgumentException("User not found");

        Post post = new Post();
        post.setLikecount(0);
        post.setcreatedTime(LocalDateTime.now().toString());
        post.setUser(user);
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setLinkUrl(linkUrl);
        post.setCommentcount(0);

        em.persist(post);
        return post;
    }



    // Edit Post
    public PostDTO Update(int postId, int userId, String content, String imageUrl, String linkUrl,int CommentCount, int LikeCount) {
        if (postId <= 0 || userId <= 0) {
            throw new IllegalArgumentException("postId and userId must be positive integers");
        }
        Post post = em.find(Post.class, postId);
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + postId + " not found");
        }


        post.setImageUrl(imageUrl);
        post.setLinkUrl(linkUrl);
        post.setCommentcount(CommentCount);
        // Update fields
        post.setContent(content);

        post.setLikecount(LikeCount);
        em.merge(post);

        return new PostDTO(post.getId(), post.getUser().getUserId(), post.getContent(), post.getImageUrl(), post.getLinkUrl(),
                post.getComments() != null ? post.getComments().size() : 0,
                post.getLikes() != null ? post.getLikes().size() : 0,post.getcreatedTime());
    }
    public List<PostDTO> getPostsById(int userId) {
        // Example implementation: Fetch posts for the user (e.g., from friends)
        return em.createQuery(
                        "SELECT new org.example.toolsproject.models.Post.DTOs.PostDTO(p.id, p.user.id, p.content, p.imageUrl, p.linkUrl, " +
                                "SIZE(p.comments), SIZE(p.likes),p.createdTime) " +
                                "FROM Post p WHERE p.user.id = :userId", PostDTO.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // Delete Post
    public void deletePost(int postId, int userId) {
        Post post = em.find(Post.class, postId);
        if (post == null || post.getUser().getUserId()!=userId) {
            throw new IllegalArgumentException("Invalid post or unauthorized");
        }
        em.remove(post);
    }

    // Like Post
    public Like likePost(int postId, int userId) {
        Post post = em.find(Post.class, postId);
        User user = em.find(User.class, userId);
        if (post == null || user == null) throw new IllegalArgumentException("Invalid post or user");

        Query query = em.createQuery("SELECT l FROM Like l WHERE l.post.id = :postId AND l.ActionMaker = :userId");
        query.setParameter("postId", postId);
        query.setParameter("userId", userId);
        List<Like> existingLikes = query.getResultList();
        if (!existingLikes.isEmpty()) throw new IllegalStateException("Post already liked");

        Like like = new Like();
        like.setPost(post);
        like.setLikerid(user.getUserId());

        em.persist(like);
        return like;
    }

    // Comment on Post
    public Comment commentPost(int postId, int userId, String content) {
        User user = em.find(User.class, userId);
        Post post = em.find(Post.class, postId);

        if (post == null || user == null) throw new IllegalArgumentException("Invalid And user");

        Comment commentt = new Comment();
        commentt.setcreatedPost(post);
        commentt.setCommenterid(user.getUserId());
        commentt.setContent(content);

        em.persist(commentt);
        return commentt;
    }
}