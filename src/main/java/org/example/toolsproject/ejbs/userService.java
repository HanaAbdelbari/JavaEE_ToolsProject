package org.example.toolsproject.ejbs;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import org.example.toolsproject.models.*;

@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
public class userService {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @POST
    @Path("/add")
    public String addUser(User account) {
        try {
            em.persist(account);
            return "User Persisted Successfully";
        } catch (Exception e) {
            return "Error Persisting User";
        }
    }

    @POST
    @Path("/add-admin")
    public String addAdminUser(User adminAccount) {
        try {
            em.persist(adminAccount);
            return "Admin Persisted Successfully";
        } catch (Exception e) {
            return "Error Persisting Admin";
        }
    }

    @GET
    @Path("getUser/{accountId}")
    public userDTO getUser(@PathParam("accountId") int accountId) {
        try {
            User account = em.find(User.class, accountId);
            return new userDTO(account);
        } catch (Exception e) {
            return null;
        }
    }

    @GET
    @Path("getAllUsers")
    public List<userDTO> getAllUsers() {
        String jpqlQuery = "SELECT u FROM User u";
        TypedQuery<User> queryInstance = em.createQuery(jpqlQuery, User.class);
        List<User> accountList = queryInstance.getResultList();
        return accountList.stream().map(userDTO::new).collect(Collectors.toList());
    }

    @POST
    @Path("signin")
    public User signin(@QueryParam("email") String emailAddr, @QueryParam("password") String passKey) {
        try {
            String jpqlQuery = "SELECT u FROM User u WHERE u.emailAddress = :emailAddr AND u.userPassword = :passKey";
            Query queryInstance = em.createQuery(jpqlQuery);
            queryInstance.setParameter("emailAddr", emailAddr);
            queryInstance.setParameter("passKey", passKey);
            return (User) queryInstance.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @PUT
    @Path("update/{accountId}")
    public String update_user(@PathParam("accountId") int accountId, @QueryParam("name") String userName, @QueryParam("email") String emailAddr, @QueryParam("password") String passKey, @QueryParam("bio") String bioText) {
        try {
            String jpqlQuery = "SELECT u FROM User u WHERE u.userId = :accountId";
            Query queryInstance = em.createQuery(jpqlQuery);
            queryInstance.setParameter("accountId", accountId);
            User account = (User) queryInstance.getSingleResult();
            account.setFullName(userName);
            account.setEmailAddress(emailAddr);
            account.setUserPassword(passKey);
            account.setProfileBio(bioText);
            return "User Information Updated Successfully";
        } catch (Exception e) {
            return "Error Updating User Information";
        }
    }

    @POST
    @Path("sendRequest")
    public String sendFriendRequest(@QueryParam("sender") int senderAccountId, @QueryParam("receiver") int receiverAccountId) {
        try {
            User senderAccount = em.find(User.class, senderAccountId);
            User receiverAccount = em.find(User.class, receiverAccountId);

            if (senderAccount.equals(receiverAccount)) {
                throw new Exception();
            }

            if (em.find(friendRequest.class, senderAccountId) != null && em.find(friendRequest.class, senderAccountId).getReceiver().equals(receiverAccount)) {
                throw new Exception();
            }

            if (senderAccount.getConnections().contains(receiverAccount)) {
                throw new Exception();
            }

            friendRequest friendshipRequest = new friendRequest(senderAccount, receiverAccount);
            em.persist(friendshipRequest);

            return "Friend Request Sent";
        } catch (Exception e) {
            return "Error Sending Friend Request";
        }
    }

    @PUT
    @Path("accept/{friendshipId}")
    public String acceptFriendRequest(@PathParam("friendshipId") int friendshipId) {
        try {
            friendRequest friendshipRequest = em.find(friendRequest.class, friendshipId);
            if (friendshipRequest != null && friendshipRequest.getStatus() == friendRequest.Status.PENDING) {
                friendshipRequest.setStatus(friendRequest.Status.ACCEPTED);
                friendshipRequest.getSender().getConnections().add(friendshipRequest.getReceiver());
                friendshipRequest.getReceiver().getConnections().add(friendshipRequest.getSender());
                return "Friend Request Accepted";
            }
            throw new Exception();
        } catch (Exception e) {
            return "Error Accepting Friend Request";
        }
    }

    @PUT
    @Path("reject/{friendshipId}")
    public String rejectFriendRequest(@PathParam("friendshipId") int friendshipId) {
        try {
            friendRequest friendshipRequest = em.find(friendRequest.class, friendshipId);
            if (friendshipRequest != null && friendshipRequest.getStatus() == friendRequest.Status.PENDING) {
                friendshipRequest.setStatus(friendRequest.Status.REJECTED);
                return "Friend Request Rejected";
            }
            throw new Exception();
        } catch (Exception e) {
            return "Error Rejecting Friend Request";
        }
    }

    @GET
    @Path("getRequests")
    public List<friendRequest> getRequests(@QueryParam("accountId") int accountId) {
        try {
            String friendshipQuery = "SELECT fr FROM friendRequest fr WHERE fr.receiver.userId = :accountId AND fr.status = :status";
            TypedQuery<friendRequest> queryFriendship = em.createQuery(friendshipQuery, friendRequest.class);
            queryFriendship.setParameter("accountId", accountId);
            queryFriendship.setParameter("status", friendRequest.Status.PENDING);
            return queryFriendship.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @GET
    @Path("getUserConnections/{accountId}")
    public List<userFriendDTO> getUserConnections(@PathParam("accountId") int accountId) {
        try {
            User account = em.find(User.class, accountId);
            return account.getConnections().stream().map(userFriendDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }
}