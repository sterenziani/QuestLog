package ar.edu.itba.paw.webapp.dto;
import javax.ws.rs.core.UriInfo;
import ar.edu.itba.paw.model.entity.Score;

public class ScoreDto
{
	private long game;
	private UserDto user;
	private int score;
	
	public static ScoreDto fromScore(final Score score, UriInfo uriInfo)
	{
		final ScoreDto dto = new ScoreDto();
		dto.game = score.getGame().getId();
		dto.user = UserDto.fromUser(score.getUser(), uriInfo);
		dto.score = score.getScore();
		return dto;
	}

	public long getGame() {
		return game;
	}

	public void setGame(long game) {
		this.game = game;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
