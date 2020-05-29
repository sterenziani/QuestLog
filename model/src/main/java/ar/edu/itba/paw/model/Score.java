package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import ar.edu.itba.paw.model.compositekeys.ScoreKey;

@Entity
@Table(name="scores")
public class Score {
	
	@EmbeddedId
    private ScoreKey id;
	
    @ManyToOne
    @MapsId("game")
    @JoinColumn(name = "game")
    private Game game;

    @ManyToOne
    @MapsId("user_id")
	@JoinColumn(name = "user_id")
    private User user;

	
	@Column(name = "score", nullable = false)
	private int score;
	
	Score()
	{
		
	}
	
	public Score(User user, Game game, int score) {
		this.user = user;
		this.game = game;
		this.score = score;
		this.id = new ScoreKey(game.getId(), user.getId());
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
	
    public ScoreKey getId() {
        return id;
    }
 
    public void setId(Game game, User user) {
        this.id.setUserId(user.getId());
    	this.id.setGameId(game.getId());
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
