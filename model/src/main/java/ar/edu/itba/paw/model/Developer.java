package ar.edu.itba.paw.model;

public class Developer {
	private final long developer;
	private String name;
	private String logo;
	
	public Developer(long developer, String name, String logo)
	{
		this.developer = developer;
		this.name = name;
		this.logo = logo;
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

}
