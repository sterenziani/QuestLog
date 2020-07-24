package ar.edu.itba.paw.webapp.dto;
import java.net.URI;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.User;

public class UserDto
{
	private long id;
	private String username;
	private String password;
	private String email;
	private String locale;
	private URI scores_url;
	private int score_total;
	private int score_average;
	private URI runs_url;
	private int runs_total;
	private long runs_hours_played;
	private URI reviews_url;
	private int reviews_total;
	
	public static UserDto fromUser(User user, UriInfo uriInfo)
	{
		final UserDto dto = new UserDto();
		dto.id = user.getId();
		dto.username = user.getUsername();
		dto.email = user.getEmail();
		dto.locale = user.getLocale().toString();
		
		dto.score_total = user.getScoreCount();
		dto.score_average = user.getScoreAverage();
		dto.scores_url = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.id)).path("scores").build();
		
		dto.runs_total = user.getRunCount();
		dto.runs_hours_played = user.getTotalHoursPlayed();
		dto.runs_url = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.id)).path("runs").build();
		
		dto.reviews_total = user.getReviewCount();
		dto.reviews_url = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.id)).path("reviews").build();
		return dto;
	}
	
	public String getPassword() {
		return password;
	}

	public int getScore_total() {
		return score_total;
	}

	public void setScore_total(int score_total) {
		this.score_total = score_total;
	}

	public int getScore_average() {
		return score_average;
	}

	public void setScore_average(int score_average) {
		this.score_average = score_average;
	}

	public int getRuns_total() {
		return runs_total;
	}

	public void setRuns_total(int runs_total) {
		this.runs_total = runs_total;
	}

	public long getRuns_hours_played() {
		return runs_hours_played;
	}

	public void setRuns_hours_played(long runs_hours_played) {
		this.runs_hours_played = runs_hours_played;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public int getReviews_total() {
		return reviews_total;
	}

	public void setReviews_total(int reviews_total) {
		this.reviews_total = reviews_total;
	}
}
