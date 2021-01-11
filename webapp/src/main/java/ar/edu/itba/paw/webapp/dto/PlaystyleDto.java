package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Playstyle;

public class PlaystyleDto
{
	private long id;
	private String name;
	
	public static PlaystyleDto fromPlaystyle(Playstyle style, UriInfo uriInfo)
	{
		final PlaystyleDto dto = new PlaystyleDto();
		dto.id = style.getId();
		dto.name = style.getName();
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
}
