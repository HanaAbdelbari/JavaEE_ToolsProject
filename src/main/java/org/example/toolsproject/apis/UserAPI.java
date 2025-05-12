package org.example.toolsproject.apis;


import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.example.toolsproject.ejbs.userService;
import org.example.toolsproject.models.*;

import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
@DeclareRoles({"Admin", "User"})
public class UserAPI {

    @EJB
    userService userservice;

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/add")
    public String addUser(User user){
        return userservice.addUser(user);
    }

    @POST
    @Path("/add-admin")
    @RolesAllowed("Admin")
    public String AddAdmin(admin Admin)
    {
        return userservice.addAdminUser(Admin);



    }

    @GET
    @Path("getUser/{id}")
    public userDTO getUser(@PathParam("id") int id) {
        return userservice.getUser(id);
    }

    @GET
    @Path("getAllUsers")
    @RolesAllowed("Admin")
    public List<userDTO> getAllUsers() {
        return userservice.getAllUsers();
    }

    @POST
    @Path("signin")
    public String signin(@QueryParam("email") String email, @QueryParam("password") String password) {
        User user = userservice.signin(email, password);
        if(user == null) return "Invalid Credentials";

        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("role", (user instanceof admin) ? "Admin" : "User");
        return  "User Successfully Logged In";
    }

    @PUT
    @Path("update/{id}")
    @RolesAllowed("Admin")
    public String updateUser(@PathParam("id") int id,@QueryParam("name") String name,@QueryParam("email") String email,@QueryParam("password") String password,@QueryParam("bio") String bio) {
        return userservice.updateUser(id, name, email, password, bio);
    }

    @POST
    @Path("sendRequest")
    public String sendFriendRequest(@QueryParam("sender") int senderId, @QueryParam("receiver") int receiverId) {
        return userservice.sendFriendRequest(senderId, receiverId);
    }

    @PUT
    @Path("accept/{id}")
    public String acceptFriendRequest(@PathParam("id") int id) {
        return userservice.acceptFriendRequest(id);
    }

    @PUT
    @Path("reject/{id}")
    public String rejectFriendRequest(@PathParam("id") int id) {
        return userservice.rejectFriendRequest(id);
    }

    @GET
    @Path("getRequests")
    public List<friendRequest> getRequests(@QueryParam("id") int id) {
        return userservice.getRequests(id);
    }

    @GET
    @Path("getUserFriends/{id}")
    public List<userFriendDTO> getUserFriends(@PathParam("id") int id) {
        return userservice.getUserFriends(id);
    }
}
