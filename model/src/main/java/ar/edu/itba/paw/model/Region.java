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
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = (int) (31 * hashCode + region);
		hashCode = 31 * hashCode + name.hashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Region)
		{
			Region toCompare = (Region) o;
			return this.region == toCompare.getId() && this.getName().equals(toCompare.getName());
		}
		return false;
	}
}
