package ar.edu.itba.paw.model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="reviews")
public class Review {
	
	@Id
	@Column(name = "review")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_review_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "reviews_review_seq", name = "reviews_review_seq")
	private Long id;
	
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

	@ElementCollection(fetch = FetchType.EAGER)
	@Column(length = 15000)
	private List<String> body;
	
	@Column
	private LocalDate post_date;
	
	@Column(nullable = false)
	private boolean enabled;
	
	
	public Review()
	{
		
	}
	
	@Deprecated
	public Review (long review, User user, Game game, Platform platform, int score, List<String> body, LocalDate post_date)
	{
		this.id = review;
		this.user = user;
		this.game = game;
		this.platform = platform;
		this.score = score;
		this.body = body;
		this.post_date = post_date;
		this.enabled = true;
	}
	
	public Review (User user, Game game, Platform platform, int score, List<String> body, LocalDate post_date)
	{
		this.user = user;
		this.game = game;
		this.platform = platform;
		this.score = score;
		this.body = body;
		this.post_date = post_date;
		this.enabled = true;
	}
	
	
	public Long getId() {
		return id;
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

	public List<String> getBody() {
		return body;
	}

	public LocalDate getPostDate() {
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

	public void setBody(List<String> body) {
		this.body = body;
	}

	public void setPostDate(LocalDate pd) {
		post_date = pd;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void enable()
	{
		enabled = true;
	}
	
	public void disable()
	{
		enabled = false;
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = (int) (31 * hashCode + id);
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
			if(this.id == toCompare.getId())
				return this.game.equals(toCompare.getGame()) && this.user.equals(toCompare.getUser())
				&& this.platform.equals(toCompare.getPlatform());
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(String line : body){
			str.append(line).append("\r\n");
		}
		return str.toString();
	}
}
