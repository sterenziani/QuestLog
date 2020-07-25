package ar.edu.itba.paw.webapp.dto;

import java.time.LocalDate;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Release;

public class ReleaseDto
{
	private GameDto game;
	private RegionDto region;
	private LocalDate date;
	
	public static ReleaseDto fromRelease(final Release release, UriInfo uriInfo)
	{
		final ReleaseDto dto = new ReleaseDto();
		dto.game = GameDto.fromGame(release.getGame(), uriInfo);
		dto.region = RegionDto.fromRegion(release.getRegion(), uriInfo);
		dto.date = release.getDate();
		return dto;
	}

	public GameDto getGame() {
		return game;
	}

	public void setGame(GameDto game) {
		this.game = game;
	}

	public RegionDto getRegion() {
		return region;
	}

	public void setRegion(RegionDto region) {
		this.region = region;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
