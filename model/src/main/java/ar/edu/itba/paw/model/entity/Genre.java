package ar.edu.itba.paw.model.entity;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="genres")
public class Genre
{

	@Id
	@Column(name = "genre")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genres_genre_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "genres_genre_seq", name = "genres_genre_seq")
	private Long id;
	
	@Column(name = "genre_name", length = 15, nullable = false, unique = true)
	private String name;
	
	@Column(name = "genre_logo")
	private String logo;

	@ManyToMany(mappedBy = "genres", cascade = CascadeType.PERSIST)
	private Set<Game> games = new HashSet<>();
	
	public Genre()
	{
		
	}
	
	@Deprecated
	public Genre(long genre, String name, String logo)
	{
		this.id = genre;
		this.name = name;
		this.logo = logo;
	}
	
	public Genre(String name, String logo)
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
	
	public void setLogo(String link)
	{
		logo = link;
	}
	
	public String getLogo()
	{
		return logo;
	}
	
	public String getName()
	{
		return name;
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
		if(o instanceof Genre)
		{
			Genre toCompare = (Genre) o;
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
