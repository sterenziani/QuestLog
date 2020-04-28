package ar.edu.itba.paw.service;
import java.util.Collection;
import java.util.Collections;
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
	public Optional<Game> findById(long id, User u)
	{
		Optional<Game> g = gameDao.findById(id);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(id, u));
		return g;
	}
	
	@Override
	public Optional<Game> findByIdWithDetails(long id, User u)
	{
		Optional<Game> g = gameDao.findByIdWithDetails(id);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(id, u));
		return g;
	}

	@Override
	public Optional<Game> findByTitle(String title, User u)
	{
		Optional<Game> g = gameDao.findByTitle(title);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(g.get().getId(), u));
		return g;
	}
	
	@Override
	public Optional<Game> findByTitleWithDetails(String title, User u)
	{
		Optional<Game> g = gameDao.findByTitleWithDetails(title);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(g.get().getId(), u));
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
	public List<Game> getAllGames(User u)
	{
		List<Game> list = gameDao.getAllGames();
		updateBacklogDetails(list, u);
		return list;
	}
	
	@Override 
	public List<Game> getAllGamesWithDetails(User u)
	{
		List<Game> list = gameDao.getAllGamesWithDetails();
		updateBacklogDetails(list, u);
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
	public List<Game> searchByTitle(String search, User u)
	{
		List<Game> list = gameDao.searchByTitle(search);
		for(Game g : list)
			g.setInBacklog(gameInBacklog(g.getId(), u));
		return list;
	}
	
	@Override
	public List<Game> searchByTitleWithDetails(String search, User u)
	{
		List<Game> list = gameDao.searchByTitleWithDetails(search);
		updateBacklogDetails(list, u);
		return list;
	}
	
	@Override
	public List<Game> getUpcomingGames(User u)
	{
		List<Game> list = gameDao.getUpcomingGames();
		updateBacklogDetails(list, u);
		return list;
	}

	
	@Override
	public boolean gameInBacklog(long gameId, User u)
	{
		if(u == null)
			return false;
		return gameDao.isInBacklog(gameId, u);
	}

	
	@Override
	public List<Game> getGamesInBacklog(User u)
	{
		if(u == null)
			return Collections.emptyList();
		List<Game> games = gameDao.getGamesInBacklog(u);
		updateBacklogDetails(games, u);
		return games;
	}
	
	
	@Override
	public void addToBacklog(long gameId, User u)
	{
		if(u != null)
			gameDao.addToBacklog(gameId, u);
	}
	
	
	@Override
	public void removeFromBacklog(long gameId, User u)
	{
		if(u != null)
			gameDao.removeFromBacklog(gameId, u);
	}
	
	@Override
	public void toggleBacklog(long gameId, User u)
	{
		if(u != null)
		{
			if(gameDao.isInBacklog(gameId, u))
				gameDao.removeFromBacklog(gameId, u);
			else
				gameDao.addToBacklog(gameId, u);
		}
		return;
	}
	
	@Override
	public void updateBacklogDetails(Collection<Game> games, User u)
	{
		for(Game g : games)
			g.setInBacklog(gameInBacklog(g.getId(), u));
	}
	
	@Override
	public List<Game> getRecommendedGames(User u)
	{
		if(u == null)
			return Collections.emptyList();
		List<Game> games = gameDao.getSimilarToBacklog(u);
		updateBacklogDetails(games, u);
		return games;
	}
	
	@Override
	public List<Game> getPopularGames(User u)
	{
		List<Game> games = gameDao.getMostBacklogged();
		if(u == null)
			return games;
		updateBacklogDetails(games, u);
		return games;
	}
}
