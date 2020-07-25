package ar.edu.itba.paw.webapp.dto;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Publisher;

public class PublisherDto
{
	private long publisherId;
	private String name;
	private String logo;
	private URI games_url;
	
	public static PublisherDto fromPublisher(Publisher p, UriInfo uriInfo)
	{
		final PublisherDto dto = new PublisherDto();
		dto.publisherId = p.getId();
		dto.name = p.getName();
		dto.logo = p.getLogo();
		dto.games_url = uriInfo.getBaseUriBuilder().path("publishers").path(String.valueOf(dto.publisherId)).path("games").build();
		return dto;
	}

	public long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
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
