package ar.edu.itba.paw.model;


public class Run {
	private final long run;
	private User user;
	private Game game;
	private Platform platform;
	private Playstyle playstyle;
	private long time;
	
	public Run(long run, User user, Game game, Platform platform, Playstyle playstle, long time) {
		this.run = run;
		this.user = user;
		this.game = game;
		this.platform = platform;
		this.playstyle = playstle;
		this.time = time;
	}
	
	public long getId() {
		return run;
	}
	
	public User getUser() {
		return user;
	}
	
	public Game getGame() {
		return game;
	}
	
	public Platform getPlatform() {
		return platform;
	}
	
	public Playstyle getPlaystyle() {
		return playstyle;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setUser(User u) {
		user = u;
	}
	
	public void setGame(Game g) {
		game = g;
	}
	
	public void setPlatform(Platform p) {
		platform = p;
	}
	
	public void setPlaystyle(Playstyle p) {
		playstyle = p;
	}
	
	public void setTime(long t) {
		time = t;
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = (int) (31 * hashCode + run);
		hashCode = (int) (31 * hashCode + user.hashCode());
		hashCode = (int) (31 * hashCode + game.hashCode());
		hashCode = (int) (31 * hashCode + platform.hashCode());
		hashCode = (int) (31 * hashCode + playstyle.hashCode());
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Run){
			Run toCompare = (Run) o;
			if(this.run == toCompare.getId())
				return this.game.equals(toCompare.getGame()) && this.user.equals(toCompare.getUser()) 
				&& this.platform.equals(toCompare.getPlatform()) && this.playstyle.equals(toCompare.getPlaystyle());
		}
		return false;
	}
	
	@Override
	public String toString() {
		int hours = (int) time/3600;
		int minutes = (int) time/60 - hours*60;
		int seconds = (int) time - hours*3600 - minutes*60;
		return Integer.toString(hours) + ":" + Integer.toString(minutes) + ":" +
				Integer.toString(seconds);	
	}
	
	
}
