package ar.edu.itba.paw.model;

public class Region
{
	private final long region;
	private String name;
	private String shortName;
	
	public Region(long region, String name, String shortName)
	{
		this.region = region;
		this.name = name;
		this.shortName = shortName;
	}
	
	public long getId()
	{
		return region;
	}
	
	public void setName(String s)
	{
		name = s;
	}
	
	public String getName()
	{
		return name;
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
