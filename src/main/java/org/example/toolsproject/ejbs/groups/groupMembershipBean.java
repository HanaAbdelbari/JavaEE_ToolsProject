package org.example.toolsproject.ejbs.groups;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.toolsproject.models.User;
import org.example.toolsproject.models.groups.group;
import org.example.toolsproject.models.groups.groupMembership;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class groupMembershipBean {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = Logger.getLogger(groupMembershipBean.class.getName());

    private static final String STATUS_APPROVED = "APPROVED";
    private static final String STATUS_PENDING = "PENDING";

    public void updateMembership(groupMembership membership) {
        entityManager.merge(membership);
    }

    public void removeMembership(groupMembership membership) {
        if (membership != null) {
            entityManager.remove(entityManager.contains(membership) ? membership : entityManager.merge(membership));
        }
    }

    public String requestToJoinGroup(User user, group group) {
        Objects.requireNonNull(user, "User cannot be null");
        Objects.requireNonNull(group, "Group cannot be null");

        TypedQuery<groupMembership> query = entityManager.createQuery(
                "SELECT gm FROM groupMembership gm WHERE gm.user = :user AND gm.group = :group",
                groupMembership.class);
        query.setParameter("user", user);
        query.setParameter("group", group);

        if (!query.getResultList().isEmpty()) {
            return "you already requested or joined this group.";
        }

        groupMembership membership = new groupMembership();
        membership.setUser(user);
        membership.setGroup(group);
        membership.setRole("MEMBER");

        if (group.isOpen()) {
            membership.setStatus(STATUS_APPROVED);
        } else {
            membership.setStatus(STATUS_PENDING);
        }

        entityManager.persist(membership);

        if (logger.isLoggable(Level.INFO)) {
            logger.info("User " + user.getUserId() + " requested to join this group " + group.getId());
        }

        return group.isOpen()
                ? "You have joined the group."
                : "Join request sent and  pend approval.";
    }

    public void leaveGroup(User user, group group) {
        Objects.requireNonNull(user, "user cannot be null");
        Objects.requireNonNull(group, "group cannot be null");

        TypedQuery<groupMembership> query = entityManager.createQuery(
                "SELECT gm FROM groupMembership gm WHERE gm.user = :user AND gm.group = :group",
                groupMembership.class);
        query.setParameter("user", user);
        query.setParameter("group", group);

        groupMembership membership = query.getResultStream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Membership is not found,please try again"));

        entityManager.remove(entityManager.contains(membership) ? membership : entityManager.merge(membership));

        if (logger.isLoggable(Level.INFO)) {
            logger.info("User " + user.getUserId() + " has left the group " + group.getId());
        }
    }

    public void approveMembership(Long membershipId) {
        Objects.requireNonNull(membershipId, "the membership ID must not be null");

        groupMembership membership = entityManager.find(groupMembership.class, membershipId);
        if (membership == null) {
            throw new IllegalArgumentException(" not found.");
        }

        if (!STATUS_PENDING.equalsIgnoreCase(membership.getStatus())) {
            throw new IllegalStateException("not pending approval.");
        }

        membership.setStatus(STATUS_APPROVED);
        entityManager.merge(membership);

        if (logger.isLoggable(Level.INFO)) {
            logger.info(" Approved group membership with ID: " + membershipId);
        }
    }

    public String promoteToAdmin(User promoter, group group, User targetUser) {
        if (promoter == null || group == null || targetUser == null) {
            throw new IllegalArgumentException("Promoter, group, and target user must not be null");
        }

        if (!group.isAdmin(promoter)) {
            return "Only group admins can promote users";
        }

        groupMembership membership = group.getMembershipForUser(targetUser);
        if (membership == null || !STATUS_APPROVED.equalsIgnoreCase(membership.getStatus())) {
            return "user is not an approved group member";
        }

        membership.setRole("ADMIN");
        updateMembership(membership);

        return "user promoted to group admin";
    }
}
