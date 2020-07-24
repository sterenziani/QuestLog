package ar.edu.itba.paw.webapp.dto;

import java.net.URI;
import javax.ws.rs.core.UriInfo;
import ar.edu.itba.paw.model.entity.Game;

public class GameDto
{	
	private long gameId;
	private String title;
	private String cover;
	private String description;
	private String trailer;
	private int score;
	
	private URI platforms_url;
	private URI developers_url;
	private URI publishers_url;
	private URI genres_url;
	private URI releaseDates_url;
	
	private URI scores_url;
	private URI runs_url;
	private URI reviews_url;
	
	public static GameDto fromGame(Game game, UriInfo uriInfo)
	{
		final GameDto dto = new GameDto();
		dto.gameId = game.getId();
		dto.title = game.getTitle();
		dto.cover = game.getCover();
		dto.description = game.getDescription();
		dto.trailer = game.getTrailer();
		dto.score = game.getAverageScore();
		
		dto.platforms_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.gameId)).path("platforms").build();
		dto.developers_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.gameId)).path("developers").build();
		dto.publishers_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.gameId)).path("publishers").build();
		dto.genres_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.gameId)).path("genres").build();
		dto.releaseDates_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.gameId)).path("releaseDates").build();
		
		dto.scores_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.gameId)).path("scores").build();
		dto.runs_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.gameId)).path("runs").build();
		dto.reviews_url = uriInfo.getBaseUriBuilder().path("games").path(String.valueOf(dto.gameId)).path("reviews").build();
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

	public URI getReleaseDates_url() {
		return releaseDates_url;
	}

	public void setReleaseDates_url(URI releaseDates_url) {
		this.releaseDates_url = releaseDates_url;
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

	public URI getReviews_url() {
		return reviews_url;
	}

	public void setReviews_url(URI reviews_url) {
		this.reviews_url = reviews_url;
	}
}
