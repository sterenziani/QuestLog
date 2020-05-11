package ar.edu.itba.paw.model;

public class Publisher {
	
	private final long publisher;
	private String name;
	private String logo;
	
	public Publisher(long publisher, String name, String logo)
	{
		this.publisher = publisher;
		this.name = name;
		this.logo = logo;
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
	
	@Override
	public int hashCode()
	{
	    return getName().hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Publisher)
		{
			Publisher toCompare = (Publisher) o;
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
