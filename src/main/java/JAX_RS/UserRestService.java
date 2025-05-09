package JAX_RS;

import javax.ejb.Stateless;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import EJBs.UserService;
import JPA.User;

@Stateless
@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestService {
	
	//1. User Management [10 Points] 📄
	//1.1 User Registration:
	//o Users can register for an account with a valid email address and password. DONE
	//1.2 User Login:
	//o Users can authenticate themselves and log in to access the application.
	//1.3 Profile Management:
	//o Users can update their profile information, including their name, bio, email, and
	//password.
	//o A user chooses a role (user or admin) during profile creation.
	
	@Inject
	private UserService userService;
	  @GET
	  @Path("/{id}")
	  public User getUser(@PathParam("id") int id) {
		  
		  //return response message
		  return userService.getUserById(id);
		  
	  }
	  
	  @POST
	  @Path("/register")
	  public void registerUser(User user)
	  {
		  
		  //return response message
		  userService.createUser(user);
		  
	  }
	  
	  
	  @GET
	  @Path("/login")
	  public User loginUser(String userEmail, String userPassword)
	  {
		    return userService.getUserByEmailAndPassword(userEmail, userPassword);
	  }
	  
	  
	  @PUT
	  @Path("edit/{user.getId()}")
	  public void updateUser(User user) {
		  userService.updateUser(user);
	  }
}
