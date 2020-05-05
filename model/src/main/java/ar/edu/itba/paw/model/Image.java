package ar.edu.itba.paw.model;

import java.io.InputStream;

public class Image {

    private final long      id;
    private final String    imageName;
    private InputStream     imageData;

    public Image(long id, String imageName, InputStream imageData){
        this.id         = id;
        this.imageName  = imageName;
        this.imageData  = imageData;
    }

    public long getId() {
        return id;
    }

    public String   getImageName() {
        return imageName;
    }

    public InputStream getImageData() {
        return imageData;
    }

    public void setImageData(InputStream base64CodedImage) {
        this.imageData = base64CodedImage;
    }
}
