package ar.edu.itba.paw.model;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name="platforms")
public class Platform
{
	@Id
	@Column(name = "platform")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platforms_platform_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "platforms_platform_seq", name = "platforms_platform_seq")
	private Long id;
	
	@Column(name = "platform_name", length = 30, nullable = false, unique = true)
	private String name;
	
	@Column(name = "platform_name_short", length = 6, nullable = false, unique = true)
	private String shortName;
	
	@Column(name = "platform_logo")
	private String logo;
	
	@ManyToMany(mappedBy = "platforms")
	private Set<Game> games = new HashSet<>();
	
	public Platform()
	{
		// Let Hibernate do the work
	}
	
	@Deprecated
	public Platform(long platform, String name, String shortName, String logo)
	{
		this.id = platform;
		this.name = name;
		this.shortName = shortName;
		this.logo = logo;
	}
	
	public Platform(String name, String shortName, String logo)
	{
		this.name = name;
		this.shortName = shortName;
		this.logo = logo;
	}
	
	public Long getId()
	{
		return id;
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
	
	@Override
	public int hashCode()
	{
	    return getName().hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Platform)
		{
			Platform toCompare = (Platform) o;
			return this.getName().equals(toCompare.getName());
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return shortName;
	}
}
