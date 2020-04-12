package ar.edu.itba.paw.service;
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

@Service
public class GameServiceImpl implements GameService
{
	@Autowired
	private GameDao gameDao;
	
	@Override
	public Optional<Game> findById(long id)
	{
		return gameDao.findById(id);
	}

	@Override
	public Optional<Game> findByTitle(String title)
	{
		return gameDao.findByTitle(title);
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
	public List<Game> searchByTitle(String search)
	{
		return gameDao.searchByTitle(search);
	}
	
	@Override 
	public List<Game> getAllGamesSimplified(){
		return gameDao.getAllGamesSimplified();
	}
	
	@Override 
	public List<Game> searchByTitleSimplified(String search){
		return gameDao.searchByTitleSimplified(search);
	}

}
