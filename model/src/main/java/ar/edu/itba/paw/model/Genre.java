package ar.edu.itba.paw.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="genres")
public class Genre
{

	@Id
	@Column(name = "genre")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genres_genre_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "genres_genre_seq", name = "genres_genre_seq")
	private Long genre;
	
	@Column(name = "genre_name", length = 15, nullable = false, unique = true)
	private String name;
	
	@Column(name = "genre_logo")
	private String logo;
	
	Genre()
	{
		
	}
	
	@Deprecated
	public Genre(long genre, String name, String logo)
	{
		this.genre = genre;
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
		return genre;
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
