package ar.edu.itba.paw.model;

import java.util.HashSet;
import java.util.Set;

public class Publisher {
	
	private final long publisher;
	private String name;
	private String logo;
	private Set<Game> games;
	
	public Publisher(long publisher, String name, String logo)
	{
		this.publisher = publisher;
		this.name = name;
		this.logo = logo;
		this.games = new HashSet<Game>();
	}
	
	public long getId()
	{
		return publisher;
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

}
