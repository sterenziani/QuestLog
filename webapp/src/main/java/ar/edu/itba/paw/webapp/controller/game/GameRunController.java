package ar.edu.itba.paw.webapp.controller.game;

import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.PlatformService;
import ar.edu.itba.paw.interfaces.service.RunService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Playstyle;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class GameRunController {

    @Autowired
    private UserService         us;

    @Autowired
    private GameService         gs;

    @Autowired
    private RunService          runs;

    @Autowired
    private PlatformService     ps;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameRunController.class);

    @RequestMapping(value = "/createRun/run/{gameId}", method = { RequestMethod.POST })
    public ModelAndView register(@RequestParam("hours") int hours, @RequestParam("mins") int mins, @RequestParam("secs") int secs,
                                 @RequestParam("game") long gameId, @RequestParam("platforms") String platform, @RequestParam("playstyles") String playst)
    {
        User user = us.getLoggedUser();
        if(user == null)
            return new ModelAndView("redirect:/games/{gameId}");
        Optional<Game> game = gs.findByIdWithDetails(gameId);
        Optional <Platform> plat = ps.findByName(platform);
        long time = hours*3600 + mins*60 + secs;
        Optional <Playstyle> play = runs.findPlaystyleByName(playst);
        if(game.isPresent() && plat.isPresent() && play.isPresent())
        {
            LOGGER.debug("Registering run of {} seconds in style {} from user {} for game {} on platform {}.", time, play.get().getName(), user.getUsername(), game.get().getTitle(), plat.get().getShortName());
            runs.register(user, game.get(), plat.get(), play.get(), time);
            LOGGER.debug("Registration of run of {} by user {} successful.", game.get().getTitle(), user.getUsername());
        }
        return new ModelAndView("redirect:/games/{gameId}");
    }

    @RequestMapping("/createRun/{gameId}")
    public ModelAndView createRun(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        User u = us.getLoggedUser();
        if(u == null)
            return new ModelAndView("redirect:/games/{gameId}");
        final ModelAndView mav = new ModelAndView("createRun");
        Game g = gs.findByIdWithDetails(gameId).orElseThrow(GameNotFoundException::new);
        mav.addObject("game", g);
        mav.addObject("playstyles",runs.getAllPlaystyles());
        return mav;
    }
}
