package ar.edu.itba.paw.webapp.form;
import ar.edu.itba.paw.webapp.validators.anotation.ImageSize;
import ar.edu.itba.paw.webapp.validators.anotation.ImageUnique;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class GameForm {

    @Size(min = 1)
    private String              title;

    @Size(max = 15000)
    private String              description;

    @ImageUnique
    @ImageSize(max=256000)
    private MultipartFile       cover;

    private long[]              platforms;

    private long[]              developers;

    private long[]              publishers;

    private long[]              genres;

    private LocalDate[]         releaseDates;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public MultipartFile getCover() {
        return cover;
    }

    public long[] getPlatforms() {
        return platforms;
    }

    public long[] getDevelopers() {
        return developers;
    }

    public long[] getPublishers() {
        return publishers;
    }

    public long[] getGenres() {
        return genres;
    }

    public LocalDate[] getReleaseDates() {
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

    public void setPlatforms(long[] platforms) {
        this.platforms = platforms;
    }

    public void setDevelopers(long[] developers) {
        this.developers = developers;
    }

    public void setPublishers(long[] publishers) {
        this.publishers = publishers;
    }

    public void setGenres(long[] genres) {
        this.genres = genres;
    }

    public void setReleaseDates(LocalDate[] releaseDates) {
        this.releaseDates = releaseDates;
    }
}
