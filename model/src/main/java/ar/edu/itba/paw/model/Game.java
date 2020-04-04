package ar.edu.itba.paw.model;

public class Game
{
	private final long game;
	private String title;
	private String cover;
	private String description;
	
	public Game(long game, String title, String cover, String description)
	{
		this.game = game;
		this.title = title;
		this.cover = cover;
		this.description = description;
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
}
