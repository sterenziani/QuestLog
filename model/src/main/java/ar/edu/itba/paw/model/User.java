package ar.edu.itba.paw.model;

public class User
{
	private final long id;
	private String username;
	private String password;
	
	public User(long id, String username, String password)
	{
		this.id = id;
		this.username = username;
		this.password = password;
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
}
