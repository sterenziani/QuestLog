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
	
	@Override
	public String toString()
	{
		return name;
	}
}
