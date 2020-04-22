package ar.edu.itba.paw.model;

public class Score {
	
	private User user;
	private Game game;
	private int score;
	
	public Score(User user, Game game, int score) {
		this.user = user;
		this.game = game;
		this.score = score;
	}
	
	public User getUser() {
		return user;
	}
	
	public Game getGame() {
		return game;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setUser(User u) {
		user = u;
	}
	
	public void setGame(Game g) {
		game = g;
	}
	
	public void setScore(int s) {
		score = s;
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = (int) (31 * hashCode + user.hashCode());
		hashCode = (int) (31 * hashCode + game.hashCode());
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Score)
		{
			Score toCompare = (Score) o;
			return this.game.equals(toCompare.getGame()) && this.user.equals(toCompare.getUser());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return Integer.toString(score);
		
	}
}
