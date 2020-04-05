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


}
