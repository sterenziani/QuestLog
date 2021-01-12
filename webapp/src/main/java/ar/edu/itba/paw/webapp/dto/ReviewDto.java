package ar.edu.itba.paw.webapp.dto;

import java.time.LocalDate;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Review;

public class ReviewDto {
	private long id;
	private UserDto user;
	private GameDto game;
	private PlatformDto platform;
	private int score;
	private List<String> body;
	private LocalDate postDate;
	private boolean enabled;
	
	public static ReviewDto fromReview(Review r, UriInfo uriInfo)
	{
		final ReviewDto dto = new ReviewDto();
		dto.id = r.getId();
		dto.user = UserDto.fromUser(r.getUser(), uriInfo);
		dto.game = GameDto.fromGame(r.getGame(), uriInfo);
		dto.platform = PlatformDto.fromPlatform(r.getPlatform(), uriInfo);
		dto.score = r.getScore();
		dto.body = r.getBody();
		dto.postDate = r.getPostDate();
		dto.enabled = r.isEnabled();
		return dto;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public GameDto getGame() {
		return game;
	}

	public void setGame(GameDto game) {
		this.game = game;
	}

	public PlatformDto getPlatform() {
		return platform;
	}

	public void setPlatform(PlatformDto platform) {
		this.platform = platform;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public List<String> getBody() {
		return body;
	}

	public void setBody(List<String> body) {
		this.body = body;
	}

	public LocalDate getPostDate() {
		return postDate;
	}

	public void setPostDate(LocalDate postDate) {
		this.postDate = postDate;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
