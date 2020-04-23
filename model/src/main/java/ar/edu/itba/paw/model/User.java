package ar.edu.itba.paw.model;

public class User
{
	private final long id;
	private String username;
	private String password;
	private String email;
	
	public User(long id, String username, String password, String email)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public long getId()
	{
		return id;
	}

	public void setUsername(String s)
	{
		username = s;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
	
	
}
