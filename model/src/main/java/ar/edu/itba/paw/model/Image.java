package ar.edu.itba.paw.model;

import java.io.InputStream;

public class Image {

    private final long      id;
    private final String    imageName;
    private byte[]          imageData;

    public Image(long id, String imageName, byte[] imageData){
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

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
