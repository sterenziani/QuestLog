package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.relations.Classification;
import ar.edu.itba.paw.model.relations.Development;
import ar.edu.itba.paw.model.relations.GameVersion;
import ar.edu.itba.paw.model.relations.Publishing;

import javax.persistence.*;
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

	@OneToMany(mappedBy = "game")
	private Set<GameVersion> game_versions;

	@OneToMany(mappedBy = "game")
	private Set<Development> developments;

	@OneToMany(mappedBy = "game")
	private Set<Publishing> publishings;

	@OneToMany(mappedBy = "game")
	private Set<Classification> classifications;
	
	@OneToMany(mappedBy = "game")
	private Set<Score> scores;
	
	@OneToMany(mappedBy = "game")
	private Set<Run> runs;

	/*
	private Set<Platform> platforms;
	private Set<Developer> developers;
	private Set<Publisher> publishers;
	private Set<Genre> genres;
	private Set<Release> releaseDates;*/

	public Game(){
		//Just for Hibernate
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
