package ar.edu.itba.paw.model;
import java.util.HashSet;
import java.util.Set;

public class Game
{
	private final long game;
	private String title;
	private String cover;
	private String description;
	private Set<Platform> platforms;
	private Set<Developer> developers;
	private Set<Publisher> publishers;
	private Set<Genre> genres;
	private Set<Release> releaseDates;
	
	public Game(long game, String title, String cover, String description)
	{
		this.game = game;
		this.title = title;
		this.cover = cover;
		this.description = description;
		this.platforms = new HashSet<Platform>();
		this.developers = new HashSet<Developer>();
		this.publishers = new HashSet<Publisher>();
		this.genres = new HashSet<Genre>();
		this.releaseDates = new HashSet<Release>();
	}

	public long getId()
	{
		return game;
	}

	public void setTitle(String s)
	{
		title = s;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setCover(String link)
	{
		cover = link;
	}
	
	public String getCover()
	{
		return cover;
	}
	
	public void setDescription(String s)
	{
		description = s;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void addPlatform(Platform p)
	{
		platforms.add(p);
	}
	
	public void removePlatform(Platform p)
	{
		platforms.remove(p);
	}
	
	public Set<Platform> getPlatforms()
	{
		return platforms;
	}

	public void addPublisher(Publisher pub)
	{
		publishers.add(pub);
	}
	
	public void removePublisher(Publisher pub)
	{
		publishers.remove(pub);
	}
	
	public Set<Publisher> getPublishers()
	{
		return publishers;
	}
	public void addDeveloper(Developer d)
	{
		developers.add(d);
	}
	
	public void removeDeveloper(Developer d)
	{
		developers.remove(d);
	}
	
	public Set<Developer> getDevelopers()
	{
		return developers;
	}

	public void addGenre(Genre g)
	{
		genres.add(g);
	}
	
	public void removeGenre(Genre g)
	{
		genres.remove(g);
	}
	
	public Set<Genre> getGenres()
	{
		return genres;
	}
	
	public void addReleaseDate(Release r)
	{
		releaseDates.add(r);
	}
	
	public void removeReleaseDate(Release r)
	{
		releaseDates.remove(r);
	}
	
	public Set<Release> getReleaseDates()
	{
		return releaseDates;
	}
}
