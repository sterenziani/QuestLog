package ar.edu.itba.paw.webapp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.webapp.exception.ImageNotFoundException;

@Controller
public class ImageController {

    @Autowired
    private ImageService is;

    @RequestMapping(value = "/images/{image_name:.+}")
    public @ResponseBody byte[] retrieveImage(@PathVariable("image_name") String image_name)
    {
        return is.findByImageName(image_name).orElseThrow(ImageNotFoundException::new).getImageData();
    }
}
