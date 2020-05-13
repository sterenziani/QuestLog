package ar.edu.itba.paw.model;
import java.util.HashSet;
import java.util.Set;

public class GameDetail extends Game
{
	private Set<Platform> platforms;
	private Set<Developer> developers;
	private Set<Publisher> publishers;
	private Set<Genre> genres;
	private Set<Release> releaseDates;
	private boolean inBacklog;
	
	public GameDetail(long game, String title, String cover, String description)
	{
		super(game, title, cover, description);
		this.platforms = new HashSet<Platform>();
		this.developers = new HashSet<Developer>();
		this.publishers = new HashSet<Publisher>();
		this.genres = new HashSet<Genre>();
		this.releaseDates = new HashSet<Release>();
		this.inBacklog = false;
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
	
	public boolean getInBacklog()
	{
		return inBacklog;
	}
	
	public void setInBacklog(boolean val)
	{
		inBacklog = val;
	}
}
