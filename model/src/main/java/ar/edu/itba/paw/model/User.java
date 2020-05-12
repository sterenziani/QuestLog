package ar.edu.itba.paw.model;
import java.util.Locale;
public class User
{
	private final long id;
	private String username;
	private String password;
	private String email;
	private Locale locale;
	private boolean adminStatus;
	
	public User(long id, String username, String password, String email, String locale)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.locale = Locale.forLanguageTag(locale);
		this.adminStatus = false;
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
	
	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}
	
	public boolean getAdminStatus()
	{
		return adminStatus;
	}
	
	public void setAdminStatus(boolean value)
	{
		adminStatus = value;
	}
}
