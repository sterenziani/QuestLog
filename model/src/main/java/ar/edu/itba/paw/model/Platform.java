package ar.edu.itba.paw.model;

public class Platform
{
	private final long platform;
	private String name;
	private String shortName;
	private String logo;
	
	public Platform(long platform, String name, String shortName, String logo)
	{
		this.platform = platform;
		this.name = name;
		this.shortName = shortName;
		this.logo = logo;
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
}
