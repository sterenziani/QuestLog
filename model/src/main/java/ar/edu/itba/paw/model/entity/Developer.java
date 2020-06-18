package ar.edu.itba.paw.model.entity;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="developers")
public class Developer 
{
	
	@Id
	@Column(name = "developer")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "developers_developer_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "developers_developer_seq", name = "developers_developer_seq")
	private Long id;
	
	@Column(name = "developer_name", length = 75, nullable = false, unique = true)
	private String name;
	
	@Column(name = "developer_logo")
	private String logo;

	@ManyToMany(mappedBy = "developers", cascade = CascadeType.PERSIST)
	private Set<Game> games = new HashSet<>();
	
	public Developer()
	{
		
	}
	
	@Deprecated
	public Developer(long developer, String name, String logo)
	{
		this.id = developer;
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
