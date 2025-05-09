package org.example.toolsproject.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.toolsproject.models.Post.Comment;
import org.example.toolsproject.models.Post.DTOs.CommentDTO;
import org.example.toolsproject.models.Post.Like;
import org.example.toolsproject.models.Post.Post;

import java.util.List;
import java.util.stream.Collectors;
@Stateless
public class CommentLikeService {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    // Comment CRUD
    public CommentDTO CreateAComment(int postId, int userId, String content) {
        Post Thepost = em.find(Post.class, postId);
        if (Thepost == null) {
            throw new IllegalArgumentException("Post with the following  ID " + postId + " not found");
        }
        Comment commentt = new Comment(content, Thepost);
        commentt.setCommenterid(userId); // Assuming Comment has a userId field
        em.persist(commentt);
        em.flush();

        // Notify the post owner
        int postOwnerId = Thepost.getUser().getUserId();
        if (postOwnerId != userId) { // Don't notify if the user is commenting on their own post
            Notifier(postOwnerId, userId, "commented on", postId);
        }

        return new CommentDTO(commentt.getId(), commentt.getContent());
    }

    public List<CommentDTO> getcommentsbyPostid(int postId) {
        return em.createQuery("SELECT c FROM Comment c WHERE c.post.id = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList()
                .stream()
                .map(c -> new CommentDTO(c.getId(), c.getContent()))
                .collect(Collectors.toList());
    }



    public void DeleteAComment(int postId, int commentId, int userId) {
        Comment commentt = em.find(Comment.class, commentId);
        if (commentt == null) {
            throw new IllegalArgumentException("Comment with ID " + commentId + " not found");
        }

        em.remove(commentt);
    }

    // Like CRUD
    public void CreateALike(int postId, int userId) {
        Post post = em.find(Post.class, postId);

        // Check for duplicate like
        long existingLikes = em.createQuery(
                        "SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId AND l.ActionMaker = :userId", Long.class)
                .setParameter("postId", postId)
                .setParameter("userId", userId)
                .getSingleResult();
        if (existingLikes > 0) {
            throw new IllegalArgumentException("Post is liked");
        }
        Like like = new Like(post, userId);
        em.persist(like);
        em.flush();

        // Notify the post owner
        int postOwnerId = post.getUser().getUserId();
        if (postOwnerId != userId) { // Don't notify if the user is liking their own post
            Notifier(postOwnerId, userId, " done a liked", postId);
        }
    }

    public List<Integer> getLikesByPostId(int postId) {
        return em.createQuery("SELECT l.ActionMaker FROM Like l WHERE l.post.id = :postId", Integer.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public void deleteLike(int postId, int userId) {
        List<Like> likes = em.createQuery("SELECT l FROM Like l WHERE l.post.id = :postId AND l.ActionMaker = :userId", Like.class)
                .setParameter("postId", postId)
                .setParameter("userId", userId)
                .getResultList();
        if (likes.isEmpty()) {
            throw new IllegalArgumentException("Like not found for post ID " + postId + " and user ID " + userId);
        }
        em.remove(likes.get(0));
    }

    // Placeholder for notification (to be implemented in phase 2)
    private void Notifier (int postOwnerId, int actorId, String action, int postId) {
        System.out.println("Notification: User " + postOwnerId + " should be notified that User " + actorId +
                " performed action '" + action + "' on post " + postId);
    }

}