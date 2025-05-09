package JPA;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import JPA.keys.HasFriendKey;

@Entity
@Table(name="HasFriend")

public class HasFriend {
	//user and hasFriendRelation
	@EmbeddedId
	private HasFriendKey id;
	
	private boolean isAccepted; // true: friend request is accepted, false friend request is pending
	
	public HasFriend(int userId1, int userId2)
	{
		this.id = new HasFriendKey(userId1, userId2);
		this.isAccepted = false;
	}


	public void setUserId1(int userId1)
	{
		this.id.setUserId1(userId1);
	}
	public void setUserId2(int userId2)
	{
		this.id.setUserId2(userId2);
	}
	public void setIsAccepted(boolean isAccepted)
	{
		this.isAccepted = isAccepted;
	}


	public int getUserId1()
	{
		return this.id.getUserId1();
	}
	public int getUserId2()
	{
		return this.id.getUserId2();
	}
	public boolean getIsAccepted()
	{
		return this.isAccepted;
	}
	
	
	
	
	

}
