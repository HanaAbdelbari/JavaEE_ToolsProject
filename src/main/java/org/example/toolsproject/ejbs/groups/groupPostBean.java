package org.example.toolsproject.ejbs.groups;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.toolsproject.models.User;
import org.example.toolsproject.models.groups.group;
import org.example.toolsproject.models.groups.groupPost;

import java.time.LocalDateTime;

@Stateless
public class groupPostBean {

    @PersistenceContext
    private EntityManager em;

    public groupPost createGroupPost(User user, group group, String content) {
        if (user == null || group == null || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid post data.");
        }

        if (!group.isMember(user)) {
            throw new SecurityException("Only approved members can post in this group.");
        }

        groupPost post = new groupPost();
        post.setUser(user);
        post.setGroup(group);
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());

        em.persist(post);
        return post;
    }

    public groupPost findGroupPostById(Long postId) {
        return em.find(groupPost.class, postId);
    }

    public void deleteGroupPost(groupPost post) {
        if (em.contains(post)) {
            em.remove(post);
        } else {
            em.remove(em.merge(post));
        }
    }

}
