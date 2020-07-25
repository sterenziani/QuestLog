package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Region;

public class RegionDto {
	private Long regionId;
	private String name;
	private String shortName;
	
	public static RegionDto fromRegion(Region region, UriInfo uriInfo)
	{   
		final RegionDto dto = new RegionDto();
		dto.regionId = region.getId();
		dto.name = region.getName();
		dto.shortName = region.getShortName();
		return dto;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
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
