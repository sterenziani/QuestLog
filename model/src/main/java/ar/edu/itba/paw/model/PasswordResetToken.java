package ar.edu.itba.paw.model;

import java.sql.Date;

public class PasswordResetToken
{
	private String token;
	private User user;
	private Date expiryDate;
	
	public PasswordResetToken(String token, User u)
	{
		this.token = token;
		this.user = u;
	}
	
	public PasswordResetToken(String token, User u, Date d)
	{
		this.token = token;
		this.user = u;
		this.expiryDate = d;
	}
	
	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public User getUser()
	{
		return user;
	}
	public void setUser(User user)
	{
		this.user = user;
	}
	public Date getExpiryDate()
	{
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate)
	{
		this.expiryDate = expiryDate;
	}
}