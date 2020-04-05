package ar.edu.itba.paw.model;

public class Genre {
	private final long genre;
	private String name;
	private String logo;
	
	public Genre(long genre, String name, String logo)
	{
		this.genre = genre;
		this.name = name;
		this.logo = logo;
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

}
