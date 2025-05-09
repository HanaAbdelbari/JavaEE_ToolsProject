package EJBs;

import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import JPA.HasFriend;
import JPA.User;

@Stateful
public class HasFriendService {
	private EntityManager entityManager;
	
	public void sendRequest(User user1, User user2)
	{
		HasFriend friendRequest = new HasFriend(user1.getId(), user2.getId());
		entityManager.persist(friendRequest);
	}
	
	public void acceptRequest(HasFriend pendingFriendRequest)
	{
		try {
			pendingFriendRequest.setIsAccepted(true);
            entityManager.merge(pendingFriendRequest);
        } catch (PersistenceException exception) {
            System.out.println("Friend request is not accepted: " + exception.getMessage());
        }

		}
	}

public List<User> getAllFriendsOf(User user) {
	//fill query
	String getFriendsQuery = "";
	TypedQuery<User> query = entityManager.createQuery(getFriendsQuery, User.class);
	List<User> friends = query.getResultList();
	return friends;
}   

	//2. Connection Management (Social Networking Feature) [30 Points]👥
	//2.1 Friend Requests:
	//o Users can send and receive friend requests to connect with other users.
	//2.2 Manage Connections:
	//o Users can accept or reject pending friend requests.
	//2.3 View Connections:
	//o Users can view all their friends and their profiles.
}
