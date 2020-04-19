package ar.edu.itba.paw.service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Release;
import ar.edu.itba.paw.model.User;

@Service
public class GameServiceImpl implements GameService
{
	@Autowired
	private GameDao gameDao;
	
	@Override
	public Optional<Game> findById(long id, String backlog, User u)
	{
		Optional<Game> g = gameDao.findById(id);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(id, backlog, u));
		return g;
	}
	
	@Override
	public Optional<Game> findByIdWithDetails(long id, String backlog, User u)
	{
		Optional<Game> g = gameDao.findByIdWithDetails(id);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(id, backlog, u));
		return g;
	}

	@Override
	public Optional<Game> findByTitle(String title, String backlog, User u)
	{
		Optional<Game> g = gameDao.findByTitle(title);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(g.get().getId(), backlog, u));
		return g;
	}
	
	@Override
	public Optional<Game> findByTitleWithDetails(String title, String backlog, User u)
	{
		Optional<Game> g = gameDao.findByTitleWithDetails(title);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(g.get().getId(), backlog, u));
		return g;
	}

	@Override
	public Optional<Game> changeTitle(long id, String new_title)
	{
		return gameDao.changeTitle(id, new_title);
	}

	@Override
	public Optional<Game> changeCover(long id, String new_cover)
	{
		return gameDao.changeCover(id, new_cover);
	}

	@Override
	public Optional<Game> changeDescription(long id, String new_desc)
	{
		return gameDao.changeDescription(id, new_desc);
	}

	@Override
	public Game register(String title, String cover, String description)
	{
		return gameDao.register(title, cover, description);
	}

	@Override 
	public List<Game> getAllGames(String backlog, User u)
	{
		List<Game> list = gameDao.getAllGames();
		updateBacklogDetails(list, backlog, u);
		return list;
	}
	
	@Override 
	public List<Game> getAllGamesWithDetails(String backlog, User u)
	{
		List<Game> list = gameDao.getAllGamesWithDetails();
		updateBacklogDetails(list, backlog, u);
		return list;
	}

	@Override
	public Optional<Game> addPlatform(Game g, Platform p)
	{
		return gameDao.addPlatform(g, p);		
	}

	@Override
	public Optional<Game> removePlatform(Game g, Platform p)
	{
		return gameDao.removePlatform(g, p);
	}

	@Override
	public List<Platform> getAllPlatforms(Game g)
	{
		return gameDao.getAllPlatforms(g);
	}
	
	@Override
	public Optional<Game> addDeveloper(Game g, Developer p)
	{
		return gameDao.addDeveloper(g, p);		
	}

	@Override
	public Optional<Game> removeDeveloper(Game g, Developer p)
	{
		return gameDao.removeDeveloper(g, p);
	}

	@Override
	public List<Developer> getAllDevelopers(Game g)
	{
		return gameDao.getAllDevelopers(g);
	}
	
	@Override
	public Optional<Game> addPublisher(Game g, Publisher p)
	{
		return gameDao.addPublisher(g, p);		
	}

	@Override
	public Optional<Game> removePublisher(Game g, Publisher p)
	{
		return gameDao.removePublisher(g, p);
	}

	@Override
	public List<Publisher> getAllPublishers(Game g)
	{
		return gameDao.getAllPublishers(g);
	}

	@Override
	public Optional<Game> addGenre(Game game, Genre genre)
	{
		return gameDao.addGenre(game, genre);
	}

	@Override
	public Optional<Game> removeGenre(Game game, Genre genre)
	{
		return gameDao.removeGenre(game, genre);
	}

	@Override
	public List<Genre> getAllGenres(Game g)
	{
		return gameDao.getAllGenres(g);
	}
	
	@Override
	public Optional<Game> addReleaseDate(Game game, Release r)
	{
		return gameDao.addReleaseDate(game, r);
	}

	@Override
	public Optional<Game> removeReleaseDate(Game game, Release r)
	{
		return gameDao.removeReleaseDate(game, r);
	}
	
	@Override
	public List<Release> getAllReleaseDates(Game g)
	{
		return gameDao.getAllReleaseDates(g);
	}
	
	@Override 
	public List<Game> searchByTitle(String search, String backlog, User u)
	{
		List<Game> list = gameDao.searchByTitle(search);
		for(Game g : list)
			g.setInBacklog(gameInBacklog(g.getId(), backlog, u));
		return list;
	}
	
	@Override
	public List<Game> searchByTitleWithDetails(String search, String backlog, User u)
	{
		List<Game> list = gameDao.searchByTitleWithDetails(search);
		updateBacklogDetails(list, backlog, u);
		return list;
	}
	
	@Override
	public List<Game> getUpcomingGames(String backlog, User u)
	{
		List<Game> list = gameDao.getUpcomingGames();
		updateBacklogDetails(list, backlog, u);
		return list;
	}

	
	@Override
	public boolean gameInBacklog(long gameId, String backlog, User u)
	{
		if(u == null)
			return backlog.contains("-" +gameId +"-");
		return gameDao.isInBacklog(gameId, u);
	}

	
	@Override
	public List<Game> getGamesInBacklog(String backlog, User u)
	{
		if(u == null)
			return getGamesInBacklog(backlog);
		List<Game> games = gameDao.getGamesInBacklog(u);
		updateBacklogDetails(games, backlog, u);
		return games;
	}
	
	
	@Override
	public String addToBacklog(long gameId, String backlog, User u)
	{
		if(u == null)
			return addToBacklog(gameId, backlog);
		gameDao.addToBacklog(gameId, u);
		return backlog;
	}
	
	
	@Override
	public String removeFromBacklog(long gameId, String backlog, User u)
	{
		if(u == null)
			return removeFromBacklog(gameId, backlog);
		gameDao.removeFromBacklog(gameId, u);
		return backlog;
	}
	
	@Override
	public String transferBacklog(String backlog, User u)
	{
		List<Game> anonGames = getGamesInBacklog(backlog);
		for(Game g : anonGames)
			gameDao.addToBacklog(g.getId(), u);
		return "";
	}
	
	
	@Override
	public String toggleBacklog(long gameId, String backlog, User u)
	{
		if(u == null)
			return toggleBacklog(gameId, backlog);
		if(gameDao.isInBacklog(gameId, u))
			gameDao.removeFromBacklog(gameId, u);
		else
			gameDao.addToBacklog(gameId, u);
		return backlog;
	}
	
	@Override
	public void updateBacklogDetails(Collection<Game> games, String backlog, User u)
	{
		for(Game g : games)
			g.setInBacklog(gameInBacklog(g.getId(), backlog, u));
	}
	
	///////////////////// METHODS FOR COOKIE BACKLOG ONLY
	
	private boolean gameInBacklog(long gameId, String backlog)
	{
		return backlog.contains("-" +gameId +"-");
	}
	
	private List<Game> getGamesInBacklog(String backlog)
	{
		List<Game> list = new ArrayList<Game>();
		String[] ids = backlog.split("-");
		for(String id : ids)
		{
			if(!id.isEmpty())
			{
				Optional<Game> g = gameDao.findById(Long.parseLong(id));
				if(g.isPresent())
				{
					list.add(g.get());
					g.get().setInBacklog(true);
				}
			}
		}
		return list;
	}
	
	private String addToBacklog(long gameId, String backlog)
	{
		if(!gameInBacklog(gameId, backlog))
		{
			gameDao.findById(gameId).get().setInBacklog(true);
			return backlog +"-" +gameId +"-";
		}
		return backlog;
	}
	
	private String removeFromBacklog(long gameId, String backlog)
	{
		gameDao.findById(gameId).get().setInBacklog(false);
		return backlog.replaceAll("-"+gameId+"-", "");
	}
	
	private String toggleBacklog(long gameId, String backlog)
	{
		if(gameInBacklog(gameId, backlog))
			return removeFromBacklog(gameId, backlog);
		else
			return addToBacklog(gameId, backlog);
	}
}
