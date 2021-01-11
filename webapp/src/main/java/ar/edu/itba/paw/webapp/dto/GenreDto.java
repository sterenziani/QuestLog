package ar.edu.itba.paw.webapp.dto;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Genre;

public class GenreDto {
	private long id;
	private String name;
	private String logo;
	private URI games_url;
	
	public static GenreDto fromGenre(Genre g, UriInfo uriInfo)
	{
		final GenreDto dto = new GenreDto();
		dto.id = g.getId();
		dto.name = g.getName();
		dto.logo = g.getLogo();
		dto.games_url = uriInfo.getBaseUriBuilder().path("genres").path(String.valueOf(dto.id)).path("games").build();
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
