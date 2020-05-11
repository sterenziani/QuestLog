package ar.edu.itba.paw.webapp.component;

import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class PawBacklogCookieHandlerService implements BacklogCookieHandlerService {

    @Autowired
    UserService us;

    @Autowired
    GameService gs;

    private static final Logger LOGGER = LoggerFactory.getLogger(PawBacklogCookieHandlerService.class);

    public String toggleBacklog(long gameId, HttpServletResponse response, String backlog)
    {
        User u = us.getLoggedUser();
        if(u == null)
        {
            if(gameInBacklog(gameId, backlog))
                backlog = removeFromBacklog(gameId, backlog);
            else
                backlog = addToBacklog(gameId, backlog);
            updateBacklogCookie(response, backlog);
        }
        else
        {
            gs.toggleBacklog(gameId);
        }
        return backlog;
    }

    public void clearAnonBacklog(HttpServletResponse response)
    {
        updateBacklogCookie(response, "");
    }

    public void updateBacklogCookie(HttpServletResponse response, String backlog)
    {
        Cookie cookie = new Cookie("backlog", backlog);
        cookie.setPath("/");
        cookie.setMaxAge(600000);
        response.addCookie(cookie);
    }

    public List<Game> getGamesInBacklog(String backlog)
    {
        List<Game> list = new ArrayList<Game>();
        String[] ids = backlog.split("-");
        for(String id : ids)
        {
            if(!id.isEmpty())
            {
                Optional<Game> g = gs.findById(Long.parseLong(id));
                if(g.isPresent())
                {
                    list.add(g.get());
                    g.get().setInBacklog(true);
                }
            }
        }
        return list;
    }
    
    public List<Game> getGamesInBacklog(String backlog, int page, int pageSize)
    {
        List<Game> list = new ArrayList<Game>();
        String copy = backlog.replace("--", "-");
        String[] ids = copy.split("-");
        int base = 1 + pageSize*(page-1);
        if(base > ids.length-1)
        	return Collections.emptyList();
        int limit = pageSize*page;
        if(limit >= ids.length)
        	limit = ids.length-1;
        for(int i=base; i <= limit; i++)
        {
            Optional<Game> g = gs.findById(Long.parseLong(ids[i]));
            if(g.isPresent())
            {
                list.add(g.get());
                g.get().setInBacklog(true);
            }
        }
        return list;
    }
    
    public int countGamesInBacklog(String backlog)
    {
    	if(backlog.isEmpty())
    		return 0;
        String copy = backlog.replace("--", "-");
        String[] ids = copy.split("-");
        return ids.length - 1;
    }

    public String addToBacklog(long gameId, String backlog)
    {
        if(gameInBacklog(gameId, backlog))
        {
            return backlog;
        }
        return backlog +"-" +gameId +"-";
    }

    public String removeFromBacklog(long gameId, String backlog)
    {
        return backlog.replaceAll("-"+gameId+"-", "");
    }

    public void transferBacklog(HttpServletResponse response, String backlog, User u)
    {
        LOGGER.debug("Transferring anonymous backlog to user {}.", u.getUsername());
        List<Game> anonGames = getGamesInBacklog(backlog);
        for(Game g : anonGames)
            gs.addToBacklog(g.getId());
        clearAnonBacklog(response);
        LOGGER.debug("Backlog successfully transferred to user {}.", u.getUsername());
    }

    public void updateWithBacklogDetails(Collection<Game> games, String backlog)
    {
        for(Game g : games)
            g.setInBacklog(gameInBacklog(g.getId(), backlog));
    }

    public boolean gameInBacklog(long gameId, String backlog)
    {
        return backlog.contains("-" +gameId +"-");
    }
}
