package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

public interface BacklogCookieHandlerService {
    String toggleBacklog(long gameId, HttpServletResponse response, String backlog);

    void clearAnonBacklog(HttpServletResponse response);

    void updateBacklogCookie(HttpServletResponse response, String backlog);

    List<Game> getGamesInBacklog(String backlog);
    
    List<Game> getGamesInBacklog(String backlog, int page, int pageSize);

    String addToBacklog(long gameId, String backlog);

    String removeFromBacklog(long gameId, String backlog);

    void transferBacklog(HttpServletResponse response, String backlog, User u);

    void updateWithBacklogDetails(Collection<Game> games, String backlog);

    boolean gameInBacklog(long gameId, String backlog);
    
    public int countGamesInBacklog(String backlog);
}
