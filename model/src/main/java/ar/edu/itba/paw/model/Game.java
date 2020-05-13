package ar.edu.itba.paw.model;

public class Game
{
	private final long game;
	private String title;
	private String cover;
	private String description;
	private boolean inBacklog;
	
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
	
	public boolean getInBacklog()
	{
		return inBacklog;
	}
	
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
