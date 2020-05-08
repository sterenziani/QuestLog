package ar.edu.itba.paw.model;
import java.util.Collections;
import java.util.List;

public class User
{
	private final long id;
	private String username;
	private String password;
	private String email;
	private List<Game> backlog;
	private List<Score> scores;
	private List<Run> runs;
	private boolean adminStatus;
	
	public User(long id, String username, String password, String email)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.backlog = Collections.emptyList();
		this.scores = Collections.emptyList();
		this.runs = Collections.emptyList();
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
	
	public List<Game> getBacklog()
	{
		return backlog;
	}
	
	public void setBacklog(List<Game> list)
	{
		backlog = list;
	}
	
	public List<Score> getScores()
	{
		return scores;
	}
	
	public void setScores(List<Score> list)
	{
		scores = list;
	}
	
	public List<Run> getRuns()
	{
		return runs;
	}
	
	public void setRuns(List<Run> list)
	{
		runs = list;
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
