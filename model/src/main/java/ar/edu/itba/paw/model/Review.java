package ar.edu.itba.paw.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="reviews")
public class Review {
	
	@Id
	@Column(name = "review")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_review_id_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "reviews_review_id_seq", name = "reviews_review_id_seq")
	private Long review;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game", referencedColumnName = "game")
	private Game game;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "platform", referencedColumnName = "platform")
	private Platform platform;
	
	@Column(nullable = false)
	private int score;
	
	@Column(length = 2000)
	private String body;
	
	@Column
	private Date post_date;
	
	
	Review()
	{
		
	}
	
	@Deprecated
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
	
	public Review (User user, Game game, Platform platform, int score, String body, Date post_date)
	{
		this.user = user;
		this.game = game;
		this.platform = platform;
		this.score = score;
		this.body = body;
		this.post_date = post_date;
	}
	
	
	public Long getId() {
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
