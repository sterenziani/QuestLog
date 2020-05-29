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
import ar.edu.itba.paw.model.relations.Publishing;


@Entity
@Table(name="publishers")
public class Publisher 
{
	
	@Id
	@Column(name = "publisher")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publishers_publisher_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "publishers_publisher_seq", name = "publishers_publisher_seq")
	private Long publisher;
	
	@Column(name = "publisher_name", length = 75, nullable = false, unique = true)
	private String name;
	
	@Column(name = "publisher_logo")
	private String logo;
	
	@OneToMany(mappedBy = "publisher")
	private Set<Publishing> publishings;
	
	Publisher()
	{
		
	}
	
	@Deprecated
	public Publisher(long publisher, String name, String logo)
	{
		this.publisher = publisher;
		this.name = name;
		this.logo = logo;
	}
	
	public Publisher(String name, String logo)
	{
		this.name = name;
		this.logo = logo;
	}
	
	public Long getId()
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
