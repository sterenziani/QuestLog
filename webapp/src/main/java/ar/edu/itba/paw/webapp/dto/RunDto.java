package ar.edu.itba.paw.webapp.dto;
import javax.ws.rs.core.UriInfo;
import ar.edu.itba.paw.model.entity.Run;

public class RunDto
{
	private Long id;
	private UserDto user;
	private GameDto game;
	private PlatformDto platform;
	private PlaystyleDto playstyle;
	private long time;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public static RunDto fromRun(final Run run, UriInfo uriInfo)
	{
		final RunDto dto = new RunDto();
		dto.id = run.getId();
		dto.user = UserDto.fromUser(run.getUser(), uriInfo);
		dto.time = run.getTime();
		dto.game = GameDto.fromGame(run.getGame(), uriInfo);
		dto.platform = PlatformDto.fromPlatform(run.getPlatform(), uriInfo);
		dto.playstyle = PlaystyleDto.fromPlaystyle(run.getPlaystyle(), uriInfo);
		return dto;
	}

	public PlatformDto getPlatform() {
		return platform;
	}

	public void setPlatform(PlatformDto platform) {
		this.platform = platform;
	}

	public PlaystyleDto getPlaystyle() {
		return playstyle;
	}

	public void setPlaystyle(PlaystyleDto playstyle) {
		this.playstyle = playstyle;
	}
}
