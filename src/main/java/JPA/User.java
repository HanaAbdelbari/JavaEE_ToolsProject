package JPA;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="User")
public class User implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)

	private int id;
	//USE ENUM LATER (ADMIN, USER)
	private String role;
	private String email;
	private String password;
	private String name;
	private String bio;

    public User(){}
	
	public User(String role, String email, String password, String name, String bio)
	{
		//validate inputs
		this.role = role;
		this.email = email;
		this.password = password;
		this.name = name;
		this.bio = bio;
	}



    public void setRole(String role)
    {
        this.role = role;
    }
	
    public void setEmail(String email)
    {
        this.email = email;
    }
	
    public void setPassword(String password)
    {
        this.password = password;
    }
	
    public void setName(String name)
    {
        this.name = name;
    }
	
    public void setBio(String bio)
    {
        this.bio = bio;
    }
	
    public String getRole()
    {
        return this.role;
    }
	
    public String getEmail()
    {
        return this.email;
    }
	
    public String getPassword()
    {
        return this.password;
    }
	
    public String getName()
    {
        return this.name;
    }
	
    public String getBio()
    {
        return this.bio;
    }
	
    public int getId()
    {
        return this.id;
    }
	
	
	

}
