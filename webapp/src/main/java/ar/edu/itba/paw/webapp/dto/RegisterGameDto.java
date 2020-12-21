package ar.edu.itba.paw.webapp.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import ar.edu.itba.paw.webapp.validators.anotation.ImageSize;

public class RegisterGameDto {
	
	public RegisterGameDto() {}
	
    @Size(min = 1)
    private String              title;

    @Size(max = 15000)
    private String              description;

    //@ImageSize(max=256000)
    //private MultipartFile       cover;
    
    private Map<Long, String> releaseDates;

    private String              trailer;
    private List<Long>          platforms;
    private List<Long>          developers;
    private List<Long>          publishers;
    private List<Long>          genres;
    
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	/*
	public MultipartFile getCover() {
		return cover;
	}
	public void setCover(MultipartFile cover) {
		this.cover = cover;
	}
	
	*/
	public Map<Long, String> getReleaseDates() {
		return releaseDates;
	}
	
	public void setReleaseDates(Map<Long, String> releaseDates) {
		this.releaseDates = releaseDates;
	}
	
	public String getTrailer() {
		return trailer;
	}
	
	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}
	
	public List<Long> getPlatforms() {
		return platforms;
	}
	
	public void setPlatforms(List<Long> platforms) {
		this.platforms = platforms;
	}
	
	public List<Long> getDevelopers() {
		return developers;
	}
	
	public void setDevelopers(List<Long> developers) {
		this.developers = developers;
	}
	
	public List<Long> getPublishers() {
		return publishers;
	}
	
	public void setPublishers(List<Long> publishers) {
		this.publishers = publishers;
	}
	
	public List<Long> getGenres() {
		return genres;
	}
	
	public void setGenres(List<Long> genres) {
		this.genres = genres;
	}
}
