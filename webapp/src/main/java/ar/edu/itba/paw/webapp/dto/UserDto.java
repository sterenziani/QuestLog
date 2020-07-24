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
	private URI scores;
	private int score_total;
	private int score_average;
	private URI runs;
	private int runs_total;
	private long runs_hours_played;
	
	public static UserDto fromUser(User user, UriInfo uriInfo)
	{
		final UserDto dto = new UserDto();
		dto.id = user.getId();
		dto.username = user.getUsername();
		dto.email = user.getEmail();
		dto.locale = user.getLocale().toString();
		
		dto.score_total = user.getScoreCount();
		dto.score_average = user.getScoreAverage();
		dto.scores = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.id)).path("scores").build();
		
		dto.runs_total = user.getRunCount();
		dto.runs_hours_played = user.getTotalHoursPlayed();
		dto.runs = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.id)).path("runs").build();
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

	public URI getRuns() {
		return runs;
	}

	public void setRuns(URI runs) {
		this.runs = runs;
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

	public URI getScores() {
		return scores;
	}

	public void setScores(URI scores) {
		this.scores = scores;
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
}
