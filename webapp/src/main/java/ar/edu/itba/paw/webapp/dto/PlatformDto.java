package ar.edu.itba.paw.webapp.dto;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Platform;

public class PlatformDto
{
	private long id;
	private String name;
	private String shortName;
	private String logo;
	private URI games_url;
	
	public static PlatformDto fromPlatform(Platform p, UriInfo uriInfo)
	{
		final PlatformDto dto = new PlatformDto();
		dto.id = p.getId();
		dto.name = p.getName();
		dto.shortName = p.getShortName();
		dto.logo = p.getLogo();
		dto.games_url = uriInfo.getBaseUriBuilder().path("platforms").path(String.valueOf(dto.id)).path("games").build();
		return dto;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public URI getGames_url() {
		return games_url;
	}

	public void setGames_url(URI games_url) {
		this.games_url = games_url;
	}
}
