package ar.edu.itba.paw.webapp.form;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.validators.anotation.ImageExists;
import ar.edu.itba.paw.webapp.validators.anotation.ImageSize;
import ar.edu.itba.paw.webapp.validators.anotation.ImageUnique;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GameForm {

    @Size(min = 1)
    private String              title;

    @Size(max = 15000)
    private String              description;

    @ImageExists
    @ImageUnique
    @ImageSize(max=256000)
    private MultipartFile       cover;

    @NotNull
    private List<Long>          platforms;

    @NotNull
    private List<Long>          developers;

    @NotNull
    private List<Long>          publishers;

    @NotNull
    private List<Long>          genres;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Map<Long, LocalDate> releaseDates;

    public GameForm(){

    }

    public GameForm(Game g){
        this.title        = g.getTitle();
        this.description  = g.getDescription();
        this.platforms    = g.getPlatforms().stream().map(Platform::getId).collect(Collectors.toList());
        this.developers   = g.getDevelopers().stream().map(Developer::getId).collect(Collectors.toList());
        this.publishers   = g.getPublishers().stream().map(Publisher::getId).collect(Collectors.toList());
        this.genres       = g.getGenres().stream().map(Genre::getId).collect(Collectors.toList());
        this.releaseDates = g.getReleaseDates().stream().collect(Collectors.toMap(r -> r.getRegion().getId(), r -> r.getDate().toLocalDate()));
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public MultipartFile getCover() {
        return cover;
    }

    public List<Long> getPlatforms() {
        return platforms;
    }

    public List<Long> getDevelopers() {
        return developers;
    }

    public List<Long> getPublishers() {
        return publishers;
    }

    public List<Long> getGenres() {
        return genres;
    }

    public Map<Long, LocalDate> getReleaseDates() {
        return releaseDates;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCover(MultipartFile cover) {
        this.cover = cover;
    }

    public void setPlatforms(List<Long> platforms) {
        this.platforms = platforms;
    }

    public void setDevelopers(List<Long> developers) {
        this.developers = developers;
    }

    public void setPublishers(List<Long> publishers) {
        this.publishers = publishers;
    }

    public void setGenres(List<Long> genres) {
        this.genres = genres;
    }

    public void setReleaseDates(Map<Long, LocalDate> releaseDates) {
        this.releaseDates = releaseDates;
    }
}
