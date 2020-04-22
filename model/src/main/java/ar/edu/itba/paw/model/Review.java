package ar.edu.itba.paw.model;

import java.sql.Date;

public class Review {
	private final long review;
	private User user;
	private Game game;
	private Platform platform;
	private final int score;
	private String body;
	private Date post_date;
	
	public Review (long review, User user, Game game, Platform platform, int score, String body, Date post_date)
	{
		this.review = review;
		this.user = user;
		this.game = game;
		this.platform = platform;
		this.score = score;
		this.body = body;
		this.post_date = post_date;
	}
	
	public long getId() {
		return review;
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
	
	public int getScore() {
		return score;
	}
	
	public String getBody() {
		return body;
	}
	
	public Date getPostDate() {
		return post_date;
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
	
	public void setBody(String b) {
		body = b;
	}
	
	public void setPostDate(Date pd) {
		post_date = pd;
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = (int) (31 * hashCode + review);
		hashCode = (int) (31 * hashCode + user.hashCode());
		hashCode = (int) (31 * hashCode + game.hashCode());
		hashCode = (int) (31 * hashCode + platform.hashCode());
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Review){
			Review toCompare = (Review) o;
			if(this.review == toCompare.getId())
				return this.game.equals(toCompare.getGame()) && this.user.equals(toCompare.getUser())
				&& this.platform.equals(toCompare.getPlatform());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return body;	
	}
}
