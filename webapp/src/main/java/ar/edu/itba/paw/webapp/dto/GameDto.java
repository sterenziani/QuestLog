package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Game;

public class GameDto
{	
	private long gameId;
	private String title;
	private String cover;
	private String description;
	private String trailer;
	private boolean inBacklog;
	private int score;
	//private Set<Platform> platforms = new HashSet<>();
	//private Set<Developer> developers = new HashSet<>();
	//private Set<Publisher> publishers = new HashSet<>();
	//private Set<Genre> genres = new HashSet<>();
	//private Set<User> backlog = new HashSet<>();
	//private Set<Release> releaseDates = new HashSet<>();
	//private List<ScoreDto> scores;
	//private List<RunDto> runs;
	//private Set<Review> reviews = new HashSet<>();
	
	public static GameDto fromGame(final Game game, UriInfo uriInfo)
	{
		final GameDto dto = new GameDto();
		dto.gameId = game.getId();
		dto.title = game.getTitle();
		dto.cover = game.getCover();
		dto.description = game.getDescription();
		dto.trailer = game.getTrailer();
		dto.inBacklog = game.getInBacklog();
		dto.score = game.getAverageScore();
		return dto;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}

	public boolean isInBacklog() {
		return inBacklog;
	}

	public void setInBacklog(boolean inBacklog) {
		this.inBacklog = inBacklog;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
