package JPA;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="HasFriend")

public class HasFriend {
	private int userId1;
	private int userId2;
	private boolean isAccepted; // true: friend request is accepted, false friend request is pending
	
	public HasFriend(int userId1, int userId2)
	{
		this.userId1 = userId1;
		this.userId2 = userId2;
		this.isAccepted = false;
	}


	public void setUserId1(int userId1)
	{
		this.userId1 = userId1;
	}
	public void setUserId2(int userId2)
	{
		this.userId2 = userId2;
	}
	public void setIsAccepted(boolean isAccepted)
	{
		this.isAccepted = isAccepted;
	}


	public int getUserId1()
	{
		return this.userId1;
	}
	public int getUserId2()
	{
		return this.userId2;
	}
	public boolean getIsAccepted()
	{
		return this.isAccepted;
	}
	
	
	
	
	

}
