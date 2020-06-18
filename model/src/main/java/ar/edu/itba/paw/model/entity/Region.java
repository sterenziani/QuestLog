package ar.edu.itba.paw.model.entity;

import javax.persistence.*;

@Entity
@Table(name="regions")
public class Region
{
	@Id
	@Column(name = "region")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regions_region_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "regions_region_seq", name = "regions_region_seq")
	private Long region;
	
	@Column(name = "region_name", length = 15, nullable = false, unique = true)
	private String name;
	
	@Column(name = "region_short", length = 5, nullable = false, unique = true)
	private String shortName;
	
	public Region()
	{
		// Let Hibernate do the work
	}
	
	@Deprecated
	public Region(long region, String name, String shortName)
	{
		this.region = region;
		this.name = name;
		this.shortName = shortName;
	}
	
	public Region(String name, String shortName)
	{
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
