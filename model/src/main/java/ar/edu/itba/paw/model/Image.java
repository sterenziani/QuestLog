package ar.edu.itba.paw.model;

import java.io.InputStream;

public class Image {

    private final long      id;
    private final String    imageName;
    private InputStream     base64CodedImage;

    public Image(long id, String imageName, InputStream base64CodedImage){
        this.id                 = id;
        this.imageName          = imageName;
        this.base64CodedImage   = base64CodedImage;
    }

    public long getId() {
        return id;
    }

    public String   getImageName() {
        return imageName;
    }

    public InputStream getBase64CodedImage() {
        return base64CodedImage;
    }

    public void setBase64CodedImage(InputStream base64CodedImage) {
        this.base64CodedImage = base64CodedImage;
    }
}
