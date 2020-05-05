package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.webapp.exception.BadImageException;
import ar.edu.itba.paw.webapp.exception.ImageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Controller
public class ImageController {

    @Autowired
    private ImageService is;

    @RequestMapping(
            value = "/images/{image_name:.+}"
    )
    public @ResponseBody byte[] retrieveImage(@PathVariable("image_name") String image_name) {
        InputStream image_data = is.findByImageName(image_name).orElseThrow(ImageNotFoundException::new).getBase64CodedImage();
        try {
            return IOUtils.toByteArray(image_data);
        } catch (IOException e){
            throw new BadImageException();
        }
    }
}
