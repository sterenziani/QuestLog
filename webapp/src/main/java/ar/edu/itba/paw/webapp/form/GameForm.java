package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.anotation.ImageUnique;
import org.springframework.web.multipart.MultipartFile;

public class GameForm {

    @ImageUnique
    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
