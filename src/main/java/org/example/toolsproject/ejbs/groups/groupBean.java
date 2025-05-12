package org.example.toolsproject.ejbs.groups;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.toolsproject.models.User;
import org.example.toolsproject.models.groups.group;
import org.example.toolsproject.models.groups.groupDTO;
import org.example.toolsproject.models.groups.groupMembership;

@Stateless
public class groupBean {

    @PersistenceContext
    private EntityManager entityManager;

    public group createGroup(groupDTO groupDto, User creator) {
        group group = new group();
        group.setName(groupDto.getName());
        group.setDescription(groupDto.getDescription());
        group.setOpen(groupDto.isOpen());
        group.setCreator(creator);

        entityManager.persist(group);

        groupMembership adminMembership = new groupMembership();
        adminMembership.setGroup(group);
        adminMembership.setUser(creator);
        adminMembership.setRole("ADMIN");
        adminMembership.setStatus("APPROVED");

        entityManager.persist(adminMembership);

        return group;
    }

    public group findGroupById(Long groupId) {
        return entityManager.find(group.class, groupId);
    }

    public User findUserById(int userId) {
        return entityManager.find(User.class, userId);
    }
    public void deleteGroup(group group) {
        if (entityManager.contains(group)) {
            entityManager.remove(group);
        } else {
            entityManager.remove(entityManager.merge(group));
        }
    }

    public void UpdateMembership(groupMembership membership) {
        entityManager.merge(membership);
    }
}
