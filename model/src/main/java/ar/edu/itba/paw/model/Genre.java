package ar.edu.itba.paw.model;

public class Genre {
	private final long genre;
	private String name;
	
	public Genre(long genre, String name)
	{
		this.genre = genre;
		this.name = name;
	}
	
	public long getId()
	{
		return genre;
	}

	public void setName(String s)
	{
		name = s;
	}
	
	public String getName()
	{
		return name;
	}

}
