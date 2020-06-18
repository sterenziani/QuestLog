package ar.edu.itba.paw.model.entity;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name="publishers")
public class Publisher 
{
	@Id
	@Column(name = "publisher")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publishers_publisher_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "publishers_publisher_seq", name = "publishers_publisher_seq")
	private Long id;
	
	@Column(name = "publisher_name", length = 75, nullable = false, unique = true)
	private String name;
	
	@Column(name = "publisher_logo")
	private String logo;

	@ManyToMany(mappedBy = "publishers", cascade = CascadeType.PERSIST)
	private Set<Game> games = new HashSet<>();
	
	public Publisher()
	{
		
	}
	
	@Deprecated
	public Publisher(long publisher, String name, String logo)
	{
		this.id = publisher;
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

	public Set<Game> getGames() {
		return games;
	}

	public void addGame(Game g){
		games.add(g);
	}

	public void removeGame(Game g){
		games.remove(g);
	}

	public boolean hasGame(Game g){
		return games.contains(g);
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
