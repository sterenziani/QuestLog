package ar.edu.itba.paw.service;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.dao.GameDao;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.UserService;
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
	
	@Autowired
	private UserService us;

	@Override
	public Optional<Game> findById(long id)
	{
		Optional<Game> g = gameDao.findById(id);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(id));
		return g;
	}
	
	@Override
	public Optional<Game> findByIdWithDetails(long id)
	{
		Optional<Game> g = gameDao.findByIdWithDetails(id);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(id));
		return g;
	}

	@Override
	public Optional<Game> findByTitle(String title)
	{
		Optional<Game> g = gameDao.findByTitle(title);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(g.get().getId()));
		return g;
	}
	
	@Override
	public Optional<Game> findByTitleWithDetails(String title)
	{
		Optional<Game> g = gameDao.findByTitleWithDetails(title);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(g.get().getId()));
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
	public Game register(String title, String cover, String description, long[] platforms, long[] developers, long[] publishers, long[] genres, LocalDate[] releaseDates)
	{
		return gameDao.register(title, cover, description, platforms, developers, publishers, genres, releaseDates);
	}

	@Override 
	public List<Game> getAllGames()
	{
		List<Game> list = gameDao.getAllGames();
		updateBacklogDetails(list);
		return list;
	}
	
	@Override 
	public List<Game> getAllGamesWithDetails()
	{
		List<Game> list = gameDao.getAllGamesWithDetails();
		updateBacklogDetails(list);
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
	public List<Game> searchByTitle(String search, int page, int pageSize)
	{
		List<Game> list = gameDao.searchByTitle(search, page, pageSize);
		for(Game g : list)
			g.setInBacklog(gameInBacklog(g.getId()));
		return list;
	}
	
	@Override
	public List<Game> searchByTitleWithDetails(String search)
	{
		List<Game> list = gameDao.searchByTitleWithDetails(search);
		updateBacklogDetails(list);
		return list;
	}
	
	@Override
	public List<Game> getUpcomingGames()
	{
		List<Game> list = gameDao.getUpcomingGames();
		updateBacklogDetails(list);
		return list;
	}

	@Override
	public List<Game> getGamesReleasingTomorrow()
	{
		List<Game> list = gameDao.getGamesReleasingTomorrow();
		updateBacklogDetails(list);
		return list;
	}
	
	@Override
	public boolean gameInBacklog(long gameId)
	{
		User u = us.getLoggedUser();
		if(u == null)
			return false;
		return gameDao.isInBacklog(gameId, u);
	}
	
	@Override
	public List<Game> getGamesInBacklog()
	{
		User u = us.getLoggedUser();
		if(u == null)
			return Collections.emptyList();
		List<Game> games = gameDao.getGamesInBacklog(u);
		updateBacklogDetails(games);
		return games;
	}
	
	@Override
	public List<Game> getGamesInBacklog(User u)
	{
		if(u == null)
			return Collections.emptyList();
		List<Game> games = gameDao.getGamesInBacklog(u);
		updateBacklogDetails(games);
		return games;
	}	
	
	@Override
	public void addToBacklog(long gameId)
	{
		User u = us.getLoggedUser();
		if(u != null)
			gameDao.addToBacklog(gameId, u);
	}
	
	
	@Override
	public void removeFromBacklog(long gameId)
	{
		User u = us.getLoggedUser();
		if(u != null)
			gameDao.removeFromBacklog(gameId, u);
	}
	
	@Override
	public void toggleBacklog(long gameId)
	{
		User u = us.getLoggedUser();
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
	public void updateBacklogDetails(Collection<Game> games)
	{
		for(Game g : games)
			g.setInBacklog(gameInBacklog(g.getId()));
	}
	
	@Override
	public List<Game> getRecommendedGames()
	{
		User u = us.getLoggedUser();
		if(u == null)
			return Collections.emptyList();
		List<Game> games = gameDao.getSimilarToBacklog(u);
		updateBacklogDetails(games);
		return games;
	}
	
	@Override
	public List<Game> getPopularGames()
	{
		List<Game> games = gameDao.getMostBacklogged();
		updateBacklogDetails(games);
		return games;
	}
	
	@Override
	public List<Game> getFilteredGames(String searchTerm, List<String> genres, List <String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight, int page, int pageSize)
	{
		List<Game> results = gameDao.getFilteredGames(searchTerm, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, page, pageSize);
		updateBacklogDetails(results);
		return results;
	}

	public List<Game> getGamesInBacklogReleasingTomorrow(User u)
	{
		return gameDao.getGamesInBacklogReleasingTomorrow(u);

	}
	
	@Override
	public 	int countSearchResults(String searchTerm) {
		return gameDao.countSearchResults(searchTerm);
	}
	
	@Override
	public 	int countSearchResultsFiltered(String searchTerm, List<String> genres, List <String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight) {
		return gameDao.countSearchResultsFiltered(searchTerm, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight);
	}
}
