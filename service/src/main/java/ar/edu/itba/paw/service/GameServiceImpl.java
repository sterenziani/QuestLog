package ar.edu.itba.paw.service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PublisherService;
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
	public Optional<Game> findById(long id, String backlog)
	{
		Optional<Game> g = gameDao.findById(id);
		if(g.isPresent())
		{
			g.get().setInBacklog(gameInBacklog(backlog, id));
		}
		return g;
	}
	
	@Override
	public Optional<Game> findByIdWithDetails(long id, String backlog)
	{
		Optional<Game> g = gameDao.findByIdWithDetails(id);
		if(g.isPresent())
		{
			g.get().setInBacklog(gameInBacklog(backlog, id));
		}
		return g;
	}

	@Override
	public Optional<Game> findByTitle(String title, String backlog)
	{
		Optional<Game> g = gameDao.findByTitle(title);
		if(g.isPresent())
		{
			g.get().setInBacklog(gameInBacklog(backlog, g.get().getId()));
		}
		return g;
	}
	
	@Override
	public Optional<Game> findByTitleWithDetails(String title, String backlog)
	{
		Optional<Game> g = gameDao.findByTitleWithDetails(title);
		if(g.isPresent())
		{
			g.get().setInBacklog(gameInBacklog(backlog, g.get().getId()));
		}
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
	public List<Game> getAllGames()
	{
		return gameDao.getAllGames();
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
	public List<Game> searchByTitleWithDetails(String search)
	{
		return gameDao.searchByTitleWithDetails(search);
	}
	
	@Override 
	public List<Game> getAllGamesWithDetails()
	{
		return gameDao.getAllGamesWithDetails();
	}
	
	@Override 
	public List<Game> getAllGames(String backlog)
	{
		List<Game> list = gameDao.getAllGames();
		for(Game g : list)
		{
			g.setInBacklog(gameInBacklog(backlog, g.getId()));
		}
		return list;
	}
	
	@Override 
	public List<Game> searchByTitle(String search)
	{
		return gameDao.searchByTitle(search);
	}
	
	@Override 
	public List<Game> searchByTitle(String search, String backlog)
	{
		List<Game> list = gameDao.searchByTitle(search);
		for(Game g : list)
		{
			g.setInBacklog(gameInBacklog(backlog, g.getId()));
		}
		return list;
	}

	@Override
	public List<Game> getUpcomingGames()
	{
		return gameDao.getUpcomingGames();
	}
	
	@Override
	public List<Game> getUpcomingGames(String backlog)
	{
		List<Game> list = gameDao.getUpcomingGames();
		for(Game g : list)
		{
			g.setInBacklog(gameInBacklog(backlog, g.getId()));
		}
		return list;
	}

	@Override
	public boolean gameInBacklog(String backlog, long gameId)
	{
		return backlog.contains("-" +gameId +"-");
	}
	
	@Override
	public List<Game> getGamesInBacklog(String backlog)
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
	
	@Override
	public String addToBacklog(String backlog, long gameId)
	{
		if(!gameInBacklog(backlog, gameId))
		{
			gameDao.findById(gameId).get().setInBacklog(true);
			return backlog +"-" +gameId +"-";
		}
		return backlog;
	}
	
	@Override
	public void addToUserBacklog(String backlog, User u)
	{
		List<Game> anonGames = getGamesInBacklog(backlog);
		for(Game g : anonGames)
		{
			gameDao.addToBacklog(u, g);
		}
	}
	
	@Override
	public String removeFromBacklog(String backlog, long gameId)
	{
		gameDao.findById(gameId).get().setInBacklog(false);
		return backlog.replaceAll("-"+gameId+"-", "");
	}
	
	@Override
	public String toggleBacklog(String backlog, long gameId)
	{
		if(gameInBacklog(backlog, gameId))
			return removeFromBacklog(backlog, gameId);
		else
			return addToBacklog(backlog, gameId);
	}
	
	@Override
	public void updateBacklogDetails(Collection<Game> games, String backlog)
	{
		for(Game g : games)
		{
			g.setInBacklog(gameInBacklog(backlog, g.getId()));
		}
	}
}
