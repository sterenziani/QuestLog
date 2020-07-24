package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Playstyle;

public class PlaystyleDto
{
	private long playstyleId;
	private String name;
	
	public static PlaystyleDto fromPlaystyle(Playstyle style, UriInfo uriInfo)
	{
		final PlaystyleDto dto = new PlaystyleDto();
		dto.playstyleId = style.getId();
		dto.name = style.getName();
		return dto;
	}

	public long getPlaystyleId() {
		return playstyleId;
	}

	public void setPlaystyleId(long playstyleId) {
		this.playstyleId = playstyleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
