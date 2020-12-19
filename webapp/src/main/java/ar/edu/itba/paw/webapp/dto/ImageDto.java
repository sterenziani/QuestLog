package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Image;

public class ImageDto {
	private long id;
	private String name;
	private byte[] content;
	
	public static ImageDto fromImage(Image image, UriInfo uriInfo) {
		final ImageDto dto = new ImageDto();
		dto.content = image.getImageData();
		dto.name = image.getImageName();
		dto.id = image.getId();
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

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}
