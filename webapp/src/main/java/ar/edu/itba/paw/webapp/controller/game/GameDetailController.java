package ar.edu.itba.paw.webapp.controller.game;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Score;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
public class GameDetailController {

    @Autowired
    private UserService                 us;

    @Autowired
    private GameService                 gs;

    @Autowired
    private RunService                  runs;

    @Autowired
    private ScoreService                scors;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;

    private static final Logger         LOGGER = LoggerFactory.getLogger(GameDetailController.class);

    @RequestMapping("/games/{gameId}")
    public ModelAndView gameProfile(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        final ModelAndView mav = new ModelAndView("game");
        User u = us.getLoggedUser();
        Game g = gs.findByIdWithDetails(gameId).orElseThrow(GameNotFoundException::new);
        mav.addObject("playAverage", runs.getAverageAllPlayStyles(g));
        mav.addObject("averageScore", scors.findAverageScore(g));
        if(u == null)
        {
            g.setInBacklog(backlogCookieHandlerService.gameInBacklog(gameId, backlog));
            mav.addObject("game", g);
        }
        else
        {
            mav.addObject("game", g);
            Optional<Score> sc = scors.findScore(u,g);
            if(sc.isPresent())
                mav.addObject("user_score",sc.get());
            else
                mav.addObject("user_score",null);
            mav.addObject("user_runs", runs.findGameRuns(g, u));
        }
        return mav;
    }

    @RequestMapping(value = "/games/{gameId}", method = RequestMethod.POST)
    public ModelAndView addToBacklogAndShowGameProfile(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/games/{gameId}");
    }

    @RequestMapping(value = "/games/scores/{gameId}", method = { RequestMethod.POST })
    public ModelAndView register(@RequestParam("score") int scoreInput, @RequestParam("game") long gameId)
    {
        User user = us.getLoggedUser();
        if(user == null)
            return new ModelAndView("redirect:/games/{gameId}");
        Game game = gs.findByIdWithDetails(gameId).orElseThrow(GameNotFoundException::new);
        Optional<Score> score = scors.findScore(user, game);
        LOGGER.debug("Registering score {} from user {} for game {}.", scoreInput, user.getUsername(), game.getTitle());
        if (score.isPresent())
            scors.changeScore(scoreInput, user, game);
        else
            scors.register(user, game, scoreInput);
        LOGGER.debug("{}'s score for game {} successfully registered.", user.getUsername(), game.getTitle());
        return new ModelAndView("redirect:/games/{gameId}");
    }
}
