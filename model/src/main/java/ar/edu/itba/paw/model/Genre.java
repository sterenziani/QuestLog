package ar.edu.itba.paw.model;
import java.util.HashSet;
import java.util.Set;

public class Genre {
	private final long genre;
	private String name;
	private String logo;
	private Set<Game> games;
	
	public Genre(long genre, String name, String logo)
	{
		this.genre = genre;
		this.name = name;
		this.logo = logo;
		this.games = new HashSet<Game>();
	}
	
	public long getId()
	{
		return genre;
	}

	public void setName(String s)
	{
		name = s;
	}
	
	public void setLogo(String link)
	{
		logo = link;
	}
	
	public String getLogo()
	{
		return logo;
	}
	
	public String getName()
	{
		return name;
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
		if(o instanceof Genre)
		{
			Genre toCompare = (Genre) o;
			return this.getName().equals(toCompare.getName());
		}
		return false;
	}
}
