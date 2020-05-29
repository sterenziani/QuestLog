package ar.edu.itba.paw.model;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ar.edu.itba.paw.model.relations.Development;

@Entity
@Table(name="developers")
public class Developer 
{
	
	@Id
	@Column(name = "developer")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "developers_developer_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "developers_developer_seq", name = "developers_developer_seq")
	private Long developer;
	
	@Column(name = "developer_name", length = 75, nullable = false, unique = true)
	private String name;
	
	@Column(name = "developer_logo")
	private String logo;
	
	@OneToMany(mappedBy = "developer")
	private Set<Development> developments;
	
	Developer()
	{
		
	}
	
	@Deprecated
	public Developer(long developer, String name, String logo)
	{
		this.developer = developer;
		this.name = name;
		this.logo = logo;
	}
	
	public Developer(String name, String logo)
	{
		this.name = name;
		this.logo = logo;
	}
	
	public Long getId()
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
	
	@Override
	public int hashCode()
	{
	    return getName().hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Developer)
		{
			Developer toCompare = (Developer) o;
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
