package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Image;

import java.util.Optional;

public interface ImageService {
    /**
     * Finds a image given its image_name
     * @param image_name The unique image_name for the image.
     * @return The matched image, or null otherwise.
     */

    Optional<Image> findByImageName(String image_name);

    /**
     * Uploads an image.
     * @param image_name	The name of the image.
     * @param image_data	The InputStream of the image.
     * @return              The uploaded image.
     */

    Image           uploadImage(String image_name, byte[] image_data);

    /**
     * Removes an image.
     * @param image_name	The name of the image.
     */

    void            removeByName(String image_name);
}
