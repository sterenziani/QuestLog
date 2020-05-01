package ar.edu.itba.paw.model;

public class Image {

    private final String    imageName;
    private byte[]          base64CodedImage;

    public Image(String imageName, byte[] base64CodedImage){
        this.imageName          = imageName;
        this.base64CodedImage   = base64CodedImage;
    }

    public String   getImageName() {
        return imageName;
    }

    public byte[]   getBase64CodedImage() {
        return base64CodedImage;
    }

    public void     setBase64CodedImage(byte[] base64CodedImage) {
        this.base64CodedImage = base64CodedImage;
    }
}
