package EJBs;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import JPA.User;


@NamedQueries({
	@NamedQuery(name="getUser",
		query="select u from User u where u.email = :email and u.password = :password")
	
})
@Stateless
public class UserService {
	@Inject
	private EntityManager entityManager;
	
	public void createUser(User user)
	{
		try
		{
			entityManager.persist(user);
		}catch(PersistenceException exception){
            System.out.println("Creation failed: " + exception.getMessage());
        }
	}
	
	public User getUserById(int id)
	{
		try {
			
		    return entityManager.find(User.class, id);
		
    } catch (PersistenceException exception) {
        System.out.println("get user failed: " + exception.getMessage());
    }
	return null;
	}
	
	public User getUserByEmailAndPassword(String userEmail, String userPassword) 
	{
		
		try {
				Query query=entityManager.createNamedQuery("getUser");
				query.setParameter("email", userEmail);
				query.setParameter("password", userPassword);
			    return (User) query.getSingleResult();
			
        } catch (PersistenceException exception) {
            System.out.println("login failed: " + exception.getMessage());
        }
		return null;
    }	
	
	public void updateUser(User user)
	{
		try {
            entityManager.merge(user);
        } catch (PersistenceException exception) {
            System.out.println("Update failed: " + exception.getMessage());
        }

		}
	}
