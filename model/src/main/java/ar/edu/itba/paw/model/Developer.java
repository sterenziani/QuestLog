package ar.edu.itba.paw.model;

import java.util.HashSet;
import java.util.Set;

public class Developer {
	private final long developer;
	private String name;
	private String logo;
	private Set<Game> games;
	
	public Developer(long developer, String name, String logo)
	{
		this.developer = developer;
		this.name = name;
		this.logo = logo;
		this.games = new HashSet<Game>();
	}
	
	public long getId()
	{
		return developer;
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
	
	@Override
	public int hashCode()
	{
	    return getName().hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Developer)
		{
			Developer toCompare = (Developer) o;
			return this.getName().equals(toCompare.getName());
		}
		return false;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
