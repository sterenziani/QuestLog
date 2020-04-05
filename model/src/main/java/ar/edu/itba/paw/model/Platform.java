package ar.edu.itba.paw.model;
import java.util.HashSet;
import java.util.Set;

public class Platform
{
	private final long platform;
	private String name;
	private String shortName;
	private String logo;
	private Set<Game> games;
	
	public Platform(long platform, String name, String shortName, String logo)
	{
		this.platform = platform;
		this.name = name;
		this.shortName = shortName;
		this.logo = logo;
		this.games = new HashSet<Game>();
	}
	
	public long getId()
	{
		return platform;
	}

	public void setName(String s)
	{
		name = s;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setLogo(String link)
	{
		logo = link;
	}
	
	public String getLogo()
	{
		return logo;
	}
	
	public void setShortName(String s)
	{
		shortName = s;
	}
	
	public String getShortName()
	{
		return shortName;
	}
	
	public void addGame(Game g)
	{
		games.add(g);
	}
	
	public void removeGame(Game g)
	{
		games.remove(g);
	}
	
	public Set<Game> getGames()
	{
		return games;
	}
}
