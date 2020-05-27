package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="scores")
public class Score {
	
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User user_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Game game;
	
	@Column(name = "score", nullable = false)
	private int score;
	
	public Score(User user, Game game, int score) {
		this.user_id = user;
		this.game = game;
		this.score = score;
	}
	
	public User getUser() {
		return user_id;
	}
	
	public Game getGame() {
		return game;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setUser(User u) {
		user_id = u;
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
		hashCode = (int) (31 * hashCode + user_id.hashCode());
		hashCode = (int) (31 * hashCode + game.hashCode());
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Score)
		{
			Score toCompare = (Score) o;
			return this.game.equals(toCompare.getGame()) && this.user_id.equals(toCompare.getUser());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return Integer.toString(score);
		
	}
}
