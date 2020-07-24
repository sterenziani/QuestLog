package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Genre;

public class GenreDto {
	private long genreId;
	private String name;
	private String logo;
	
	public static GenreDto fromGenre(Genre g, UriInfo uriInfo)
	{
		final GenreDto dto = new GenreDto();
		dto.genreId = g.getId();
		dto.name = g.getName();
		dto.logo = g.getLogo();
		return dto;
	}

	public long getGenreId() {
		return genreId;
	}

	public void setGenreId(long genreId) {
		this.genreId = genreId;
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
}
