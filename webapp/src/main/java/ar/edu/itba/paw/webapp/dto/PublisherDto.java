package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Publisher;

public class PublisherDto
{
	private long publisherId;
	private String name;
	private String logo;
	
	public static PublisherDto fromPublisher(Publisher p, UriInfo uriInfo)
	{
		final PublisherDto dto = new PublisherDto();
		dto.publisherId = p.getId();
		dto.name = p.getName();
		dto.logo = p.getLogo();
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
}
