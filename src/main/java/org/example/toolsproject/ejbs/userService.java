package org.example.toolsproject.ejbs;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import org.example.toolsproject.models.*;
import org.example.toolsproject.models.User.*;
import org.mindrot.jbcrypt.BCrypt;


@Stateless
public class userService {

    @PersistenceContext(unitName = "default")
    private EntityManager em;


    public String addUser(User user) {
        try{
            String hashedPassword = BCrypt.hashpw(user.getUserPassword(), BCrypt.gensalt());
            user.setUserPassword(hashedPassword);
            em.persist(user);
            return "User Persisted Successfully";
        }
        catch(Exception e){
            return "Error Persisting User";
        }
    }

    public String addAdminUser(admin Admin) {
        try{
            em.persist(Admin);
            return "Admin Persisted Successfully";
        }
        catch(Exception e){
            return "Error Persisting Admin";
        }
    }


    public userDTO getUser(@PathParam("id") int id) {
        try {
            User user = em.find(User.class, id);
            return new userDTO(user);
        }
        catch(Exception e){
            return null;
        }
    }


    public List<userDTO> getAllUsers() {
        String query = "select u FROM User u";
        TypedQuery<User> queryObject = em.createQuery(query, User.class);
        List<User>  users = queryObject.getResultList();

        return users.stream().map(userDTO::new).collect(Collectors.toList());
    }


    public User signin(@QueryParam("email") String email, @QueryParam("password") String password) {
        try{
            String query = "select u from User u where u.emailAddress=:email";
            Query queryObject = em.createQuery(query);
            queryObject.setParameter("email", email);
            User user = (User) queryObject.getSingleResult();

            if(BCrypt.checkpw(password, user.getUserPassword())){
                return user;
            }
            else{
                return null;
            }
        }
        catch(Exception e){
            return null;
        }
    }


    public String updateUser(@PathParam("id") int id,@QueryParam("name") String name,@QueryParam("email") String email,@QueryParam("password") String password,@QueryParam("bio") String bio) {
        try{
            String query = "select u from User u where u.id = :id";
            Query queryObject = em.createQuery(query);
            queryObject.setParameter("id", id);
            User user = (User) queryObject.getSingleResult();
            user.setFullName(name);
            user.setEmailAddress(email);
            user.setUserPassword(password);
            user.setProfileBio(bio);
            return "User Information Updated Successfully";
        } catch (Exception e) {
            return "Error Updating User Information";
        }
    }


    public String sendFriendRequest(@QueryParam("sender") int senderId, @QueryParam("receiver") int receiverId) {
        try{
            User sender =  em.find(User.class, senderId);
            User receiver =  em.find(User.class, receiverId);

            if(sender.equals(receiver)){
                throw new Exception();
            }

            if(em.find(friendRequest.class, senderId).getReceiver().equals(receiver)){
                throw new Exception();
            }

            if(sender.getConnections().contains(receiver)){
                throw new Exception();
            }

            friendRequest request = new friendRequest(sender, receiver);
            em.persist(request);

            return "Friend Request Sent";
        }
        catch(Exception e){
            return "Error Sending Friend Request";
        }
    }


    public String acceptFriendRequest(@PathParam("id") int id) {
        try{
            friendRequest request = em.find(friendRequest.class, id);
            if(request != null && request.getStatus() == friendRequest.Status.PENDING){
                request.setStatus(friendRequest.Status.ACCEPTED);
                request.getSender().getConnections().add(request.getReceiver());
                request.getReceiver().getConnections().add(request.getSender());
                return "Friend Request Accepted";
            }
            throw new Exception();
        }
        catch(Exception e){
            return "Error Accepting Friend Request";
        }
    }


    public String rejectFriendRequest(@PathParam("id") int id) {
        try{
            friendRequest request = em.find(friendRequest.class, id);
            if(request != null && request.getStatus() == friendRequest.Status.PENDING){
                request.setStatus(friendRequest.Status.REJECTED);
                return "Friend Request Rejected";
            }
            throw new Exception();
        }
        catch(Exception e){
            return "Error Rejecting Friend Request";
        }
    }


    public List<friendRequest> getRequests(@QueryParam("id") int id) {
        try{
            String requestQuery = "SELECT fr FROM friendRequest fr WHERE fr.receiver.id = :userId AND fr.status = :status";
            TypedQuery<friendRequest> queryRequest = em.createQuery(requestQuery, friendRequest.class);
            queryRequest.setParameter("userId", id);
            queryRequest.setParameter("status", friendRequest.Status.PENDING);
            return queryRequest.getResultList();
        }
        catch(Exception e){
            return null;
        }
    }


    public List<userFriendDTO> getUserFriends(@PathParam("id") int id) {
        try {
            User user = em.find(User.class, id);
            return user.getConnections().stream().map(userFriendDTO::new).collect(Collectors.toList());
        }
        catch(Exception e){
            return null;
        }
    }
}
