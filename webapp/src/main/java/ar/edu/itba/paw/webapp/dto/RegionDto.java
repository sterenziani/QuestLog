package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Region;

public class RegionDto {
	private Long id;
	private String name;
	private String shortName;
	
	public static RegionDto fromRegion(Region region, UriInfo uriInfo)
	{   
		final RegionDto dto = new RegionDto();
		dto.id = region.getId();
		dto.name = region.getName();
		dto.shortName = region.getShortName();
		return dto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
}
