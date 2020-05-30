package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "games")
public class Game
{
	@Id
	@Column(name = "game")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "games_game_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "games_game_seq", name = "games_game_seq")
	private long game;

	@Column(name = "title")
	private String title;

	@Column(name = "cover")
	private String cover;

	@Column(name = "description")
	private String description;

	@Transient
	private boolean inBacklog;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "game_versions",
		joinColumns = @JoinColumn(name = "game"),
		inverseJoinColumns = @JoinColumn(name = "platform"))
	private Set<Platform> platforms = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "development",
			joinColumns = @JoinColumn(name = "game"),
			inverseJoinColumns = @JoinColumn(name = "developer"))
	private Set<Developer> developers = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "publishing",
			joinColumns = @JoinColumn(name = "game"),
			inverseJoinColumns = @JoinColumn(name = "publisher"))
	private Set<Publisher> publishers = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "classifications",
			joinColumns = @JoinColumn(name = "game"),
			inverseJoinColumns = @JoinColumn(name = "genre"))
	private Set<Genre> genres = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "backlogs",
			joinColumns = @JoinColumn(name = "game"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> backlog = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
	private Set<Release> releaseDates = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
	private Set<Score> scores = new HashSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
	private Set<Run> runs = new HashSet<>();

	/*
	private Set<Platform> platforms;
	private Set<Developer> developers;
	private Set<Publisher> publishers;
	private Set<Genre> genres;
	private Set<Release> releaseDates;*/

	public Game(){
		//Just for Hibernate
	}

	public Game(String title, String cover, String description)
	{
		this.title = title;
		this.cover = cover;
		this.description = description;
		this.inBacklog = false;
	}
	
	public Game(long game, String title, String cover, String description)
	{
		this.game = game;
		this.title = title;
		this.cover = cover;
		this.description = description;
		this.inBacklog = false;
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
	
	public void addScore(Score score)
	{
		scores.add(score);
	}
	
	public void removeRole(Score score)
	{
		scores.remove(score);
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

	@Transient
	public boolean getInBacklog()
	{
		return inBacklog;
	}

	@Transient
	public void setInBacklog(boolean val)
	{
		inBacklog = val;
	}

	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = (int) (31 * hashCode + game);
		hashCode = 31 * hashCode + title.hashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Game)
		{
			Game toCompare = (Game) o;
			return this.game == toCompare.getId() && this.title.equals(toCompare.getTitle());
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return title;
	}
}
