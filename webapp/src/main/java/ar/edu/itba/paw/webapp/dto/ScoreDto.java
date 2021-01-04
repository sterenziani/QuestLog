package ar.edu.itba.paw.webapp.dto;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import ar.edu.itba.paw.model.entity.Score;

public class ScoreDto
{
	@NotNull
	private GameDto game;

	@NotNull
	private UserDto user;

	@Max(100)
	@Min(0)
	private int score;
	
	public static ScoreDto fromScore(final Score score, UriInfo uriInfo)
	{
		final ScoreDto dto = new ScoreDto();
		dto.game = GameDto.fromGame(score.getGame(), uriInfo);
		dto.user = UserDto.fromUser(score.getUser(), uriInfo);
		dto.score = score.getScore();
		return dto;
	}

	public GameDto getGame() {
		return game;
	}

	public void setGame(GameDto game) {
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
