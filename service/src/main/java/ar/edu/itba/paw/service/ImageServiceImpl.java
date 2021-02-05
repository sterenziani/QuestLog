package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.model.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    public static final String MD5 = "md5";

    @Autowired
    private ImageDao imageDao;

    @Transactional
    @Override
    public Optional<Image> findByImageName(String image_name) {
        return imageDao.findByImageName(image_name);
    }

    @Transactional
    @Override
    public Image uploadImage(String image_name, byte[] image_data) {
        return imageDao.uploadImage(image_name, image_data);
    }

    @Transactional
    @Override
    public void removeByName(String image_name) {
        imageDao.removeByName(image_name);
    }

    private final static String DELIMS                          = "[,]";
    private final static String DELIMITER                       = "[/]";
    private final static String IMAGE_CONTENT_TYPE_FROM_MIME    = "image";

    @Override
    public boolean isImage(String base64image) {
        if(base64image == null || base64image.isEmpty())
            return false;
        Optional<InputStream> image = getBase64ImageAsInputStream(base64image);
        if(image.isPresent()){
            try {
                String mimeType = URLConnection
                        .guessContentTypeFromStream(image.get());
                if(mimeType == null || mimeType.isEmpty()){
                    return false;
                }
                String[] splitMimeType = mimeType.split(DELIMITER);
                return IMAGE_CONTENT_TYPE_FROM_MIME.equalsIgnoreCase(splitMimeType[0]);
            } catch (IOException e){
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Optional<String> getImageExtension(String base64image) {
        if(base64image == null || base64image.isEmpty())
            return Optional.empty();
        Optional<InputStream> image = getBase64ImageAsInputStream(base64image);
        if(image.isPresent()) {
            try {
                String[] mimeType = URLConnection
                        .guessContentTypeFromStream(image.get())
                        .split(DELIMITER);
                return Optional.of(mimeType[1]);
            } catch (IOException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public byte[] getImage(String base64image) {
        if(base64image == null || base64image.isEmpty()){
            return null;
        }
        String[] parts = base64image.split(DELIMS);
        if(parts.length < 2){
            return null;
        }
        String imageBase64 = parts[1];
        return Base64.getDecoder().decode(imageBase64);
    }

    @Override
    public Optional<String> getImageContentHash(String base64image) {
        Optional<InputStream> imageStream = getBase64ImageAsInputStream(base64image);
        if(!imageStream.isPresent()){
            return Optional.empty();
        }
        byte[] bytesBuffer = new byte[1024];
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
            int bytesRead = -1;
            while ((bytesRead = imageStream.get().read(bytesBuffer)) != -1) {
                messageDigest.update(bytesBuffer, 0, bytesRead);
            }
            byte[] digest = messageDigest.digest();
            String contentHash = getHexaString(digest);
            return Optional.of(contentHash);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    private static String getHexaString(byte[] data) {
        String result = new BigInteger(1, data).toString(16);
        return result;
    }

    private Optional<InputStream> getBase64ImageAsInputStream(String base64image){
        byte[] image = getImage(base64image);
        if(image == null){
            return Optional.empty();
        }
        return Optional.of(new ByteArrayInputStream(image));
    }
}
