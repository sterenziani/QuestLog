package ar.edu.itba.paw.model.entity;

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
@Table(name="runs")
public class Run {
	
	@Id
	@Column(name = "run")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "runs_run_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "runs_run_seq", name = "runs_run_seq")
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
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "playstyle", referencedColumnName = "playstyle")
	private Playstyle playstyle;
	
	@Column(name = "time", nullable = false)
	private long time;
	
	public Run()
	{
		
	}
	
	@Deprecated
	public Run(long run, User user_id, Game game, Platform platform, Playstyle playstle, long time) {
		this.id = run;
		this.user = user_id;
		this.game = game;
		this.platform = platform;
		this.playstyle = playstle;
		this.time = time;
	}
	
	public Run(User user_id, Game game, Platform platform, Playstyle playstle, long time) {
		this.user = user_id;
		this.game = game;
		this.platform = platform;
		this.playstyle = playstle;
		this.time = time;
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
		hashCode = (int) (31 * hashCode + id);
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
			if(this.id == toCompare.getId())
				return this.game.equals(toCompare.getGame()) && this.user.equals(toCompare.getUser()) 
				&& this.platform.equals(toCompare.getPlatform()) && this.playstyle.equals(toCompare.getPlaystyle());
		}
		return false;
	}
	
	@Override
	public String toString() {
		int hours = (int) time/3600;
		int minutes = (int) time/60 - hours*60;
		int seconds = (int) (time - hours*3600 - minutes*60);
		String timestamp = Integer.toString(hours) + " : ";
		if(minutes < 10)
			timestamp += "0";
		timestamp += Integer.toString(minutes) + " : ";
		if(seconds < 10)
			timestamp += "0";
		timestamp += Integer.toString(seconds);
		return timestamp;
	}	
}
