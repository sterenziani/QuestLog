package ar.edu.itba.paw.webapp.dto;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Game;


public class GameDto
{    
	private long id;
	private String title;
	private String cover;
	private String description;
	private String trailer;
	private int score;
	private boolean in_backlog;
	
	private URI platforms_url;
	private URI developers_url;
	private URI publishers_url;
	private URI genres_url;
	private URI release_dates_url;
	private URI scores_url;
	private URI runs_url;
	private URI reviews_url;
	private URI average_times_url;
	private URI top_runs_url;
	
	private boolean released;
	private int votes;
	
	public static GameDto fromGame(Game game, UriInfo uriInfo)
	{   
		if(game == null)
			return null;
		final GameDto dto = new GameDto();
		dto.id = game.getId();
		dto.title = game.getTitle();
		dto.cover = game.getCover();
		dto.description = game.getDescription();
		dto.trailer = game.getTrailer();
		dto.score = game.getAverageScore();
		dto.setIn_backlog(game.getInBacklog());

		dto.platforms_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.id)).path("platforms").build();
		dto.developers_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.id)).path("developers").build();
		dto.publishers_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.id)).path("publishers").build();
		dto.genres_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.id)).path("genres").build();
		dto.release_dates_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.id)).path("release_dates").build();
		dto.scores_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.id)).path("scores").build();
		dto.runs_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.id)).path("runs").build();
		dto.reviews_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.id)).path("reviews").build();
		dto.average_times_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.id)).path("average_times").build();
		dto.top_runs_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.id)).path("top_runs").build();
		
		dto.released = game.hasReleased();
		dto.setVotes(game.getScores().size());
		return dto;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public URI getPlatforms_url() {
		return platforms_url;
	}

	public void setPlatforms_url(URI platforms_url) {
		this.platforms_url = platforms_url;
	}

	public URI getDevelopers_url() {
		return developers_url;
	}

	public void setDevelopers_url(URI developers_url) {
		this.developers_url = developers_url;
	}

	public URI getPublishers_url() {
		return publishers_url;
	}

	public void setPublishers_url(URI publishers_url) {
		this.publishers_url = publishers_url;
	}

	public URI getGenres_url() {
		return genres_url;
	}

	public void setGenres_url(URI genres_url) {
		this.genres_url = genres_url;
	}

	public URI getRelease_dates_url() {
		return release_dates_url;
	}

	public void setRelease_dates_url(URI releaseDates_url) {
		this.release_dates_url = releaseDates_url;
	}

	public URI getReviews_url() {
		return reviews_url;
	}

	public void setReviews_url(URI reviews_url) {
		this.reviews_url = reviews_url;
	}

	public URI getAverage_times_url() {
		return average_times_url;
	}

	public void setAverage_times_url(URI average_times_url) {
		this.average_times_url = average_times_url;
	}

	public URI getTop_runs_url() {
		return top_runs_url;
	}

	public void setTop_runs_url(URI top_runs_url) {
		this.top_runs_url = top_runs_url;
	}

	public URI getScores_url() {
		return scores_url;
	}

	public void setScores_url(URI scores_url) {
		this.scores_url = scores_url;
	}

	public URI getRuns_url() {
		return runs_url;
	}

	public void setRuns_url(URI runs_url) {
		this.runs_url = runs_url;
	}

	public boolean isReleased() {
		return released;
	}

	public void setReleased(boolean released) {
		this.released = released;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public boolean isIn_backlog() {
		return in_backlog;
	}

	public void setIn_backlog(boolean in_backlog) {
		this.in_backlog = in_backlog;
	}
	
}
