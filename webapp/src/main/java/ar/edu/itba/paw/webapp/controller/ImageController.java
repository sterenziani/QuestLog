package ar.edu.itba.paw.webapp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.webapp.exception.ImageNotFoundException;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/images")
@Controller
public class ImageController {

    @Autowired
    private ImageService is;

    @RequestMapping(value = "/**")
    public @ResponseBody byte[] retrieveImage(HttpServletRequest request)
    {
        String[] image_name = request.getRequestURI().split(request.getContextPath() + "/images/");
        if(image_name.length != 2){
            throw new ImageNotFoundException();
        }
        return is.findByImageName(image_name[1]).orElseThrow(ImageNotFoundException::new).getImageData();
    }
}
