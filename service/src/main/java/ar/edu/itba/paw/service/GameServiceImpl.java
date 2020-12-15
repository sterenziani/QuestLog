package ar.edu.itba.paw.service;
import java.io.IOException;
import java.time.LocalDate;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.model.exception.BadFormatException;
import org.apache.commons.io.FilenameUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.itba.paw.interfaces.dao.GameDao;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.UserService;
import org.springframework.web.multipart.MultipartFile;
import ar.edu.itba.paw.model.entity.Developer;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Genre;
import ar.edu.itba.paw.model.entity.Platform;
import ar.edu.itba.paw.model.entity.Publisher;
import ar.edu.itba.paw.model.entity.Release;
import ar.edu.itba.paw.model.entity.User;

@Service
public class GameServiceImpl implements GameService
{


	@Autowired
	private GameDao gameDao;

	@Autowired
	private UserService us;

	@Autowired
	private ImageService is;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GameServiceImpl.class);

	@Transactional
	@Override
	public Optional<Game> findById(long id)
	{
		Optional<Game> g = gameDao.findById(id);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(id));
		return g;
	}

	@Transactional
	@Override
	public Optional<Game> findByIdWithDetails(long id)
	{
		Optional<Game> g = gameDao.findByIdWithDetails(id);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(id));
		return g;
	}

	@Transactional
	@Override
	public Optional<Game> findByTitle(String title)
	{
		Optional<Game> g = gameDao.findByTitle(title);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(g.get().getId()));
		return g;
	}

	@Transactional
	@Override
	public Optional<Game> findByTitleWithDetails(String title)
	{
		Optional<Game> g = gameDao.findByTitleWithDetails(title);
		if(g.isPresent())
			g.get().setInBacklog(gameInBacklog(g.get().getId()));
		return g;
	}

	@Transactional
	@Override
	public Optional<Game> changeTitle(long id, String new_title)
	{
		LOGGER.debug("Changing title of game of ID {} into {}", id, new_title);
		return gameDao.changeTitle(id, new_title);
	}

	@Transactional
	@Override
	public Optional<Game> changeCover(long id, String new_cover)
	{
		LOGGER.debug("Changing cover of game of ID {} into {}", id, new_cover);
		return gameDao.changeCover(id, new_cover);
	}

	@Transactional
	@Override
	public Optional<Game> changeDescription(long id, String new_desc)
	{
		LOGGER.debug("Changing description of game of ID {} into {}", id, new_desc);
		return gameDao.changeDescription(id, new_desc);
	}

	@Transactional
	@Override
	public Game register(String title, MultipartFile cover, String description, String trailer, List<Long> platforms, List<Long> developers, List<Long> publishers, List<Long> genres, Map<Long, LocalDate> releaseDates) throws BadFormatException
	{
		LOGGER.debug("Registering new game {} with cover {}, platforms {}", title, cover, platforms);
		Game g;
		if(trailer != null && !trailer.isEmpty()) {
			g = gameDao.register(title, null, description, trailer);
		} else {
			g = gameDao.register(title, null, description, null);
		}
		gameDao.addPlatforms(g.getId(), platforms);
		gameDao.addDevelopers(g.getId(), developers);
		gameDao.addPublishers(g.getId(), publishers);
		gameDao.addGenres(g.getId(), genres);
		gameDao.addReleaseDates(g.getId(), releaseDates);
		if(!cover.isEmpty()) {
			g.setCover(getCoverName(g.getId(), cover));
			try {
				is.uploadImage(g.getCover(), cover.getBytes());
			} catch (IOException e){
				throw new BadFormatException();
			}
		} else {
			g.setCover(null);
		}
		gameDao.update(g);
		return g;
	}

	@Transactional
	@Override
	public List<Game> getAllGames()
	{
		List<Game> list = gameDao.getAllGames();
		updateBacklogDetails(list);
		return list;
	}

	@Transactional
	@Override
	public Optional<Game> addPlatform(Game g, Platform p)
	{
		return gameDao.addPlatform(g, p);
	}

	@Transactional
	@Override
	public Optional<Game> removePlatform(Game g, Platform p)
	{
		return gameDao.removePlatform(g, p);
	}

	@Transactional
	@Override
	public Optional<Game> addDeveloper(Game g, Developer p)
	{
		return gameDao.addDeveloper(g, p);
	}

	@Transactional
	@Override
	public Optional<Game> removeDeveloper(Game g, Developer p)
	{
		return gameDao.removeDeveloper(g, p);
	}

	@Transactional
	@Override
	public Optional<Game> addPublisher(Game g, Publisher p)
	{
		return gameDao.addPublisher(g, p);
	}

	@Transactional
	@Override
	public Optional<Game> removePublisher(Game g, Publisher p)
	{
		return gameDao.removePublisher(g, p);
	}

	@Transactional
	@Override
	public Optional<Game> addGenre(Game game, Genre genre)
	{
		return gameDao.addGenre(game, genre);
	}

	@Transactional
	@Override
	public Optional<Game> removeGenre(Game game, Genre genre)
	{
		return gameDao.removeGenre(game, genre);
	}

	@Transactional
	@Override
	public Optional<Game> addReleaseDate(Game game, Release r)
	{
		return gameDao.addReleaseDate(game, r);
	}

	@Transactional
	@Override
	public Optional<Game> removeReleaseDate(Game game, Release r)
	{
		return gameDao.removeReleaseDate(game, r);
	}

	@Transactional
	@Override
	public List<Game> searchByTitle(String search, int page, int pageSize)
	{
		List<Game> list = gameDao.searchByTitle(search, page, pageSize);
		for(Game g : list)
			g.setInBacklog(gameInBacklog(g.getId()));
		return list;
	}

	@Transactional
	@Override
	public List<Game> getUpcomingGames()
	{
		List<Game> list = gameDao.getUpcomingGames();
		updateBacklogDetails(list);
		return list;
	}

	@Transactional
	@Override
	public List<Game> getGamesReleasingTomorrow()
	{
		List<Game> list = gameDao.getGamesReleasingTomorrow();
		updateBacklogDetails(list);
		return list;
	}

	@Transactional
	@Override
	public boolean gameInBacklog(long gameId)
	{
		User u = us.getLoggedUser();
		if(u == null)
			return false;
		return gameDao.isInBacklog(gameId, u);
	}

	@Transactional
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

	@Transactional
	@Override
	public List<Game> getGamesInBacklog(User u)
	{
		if(u == null)
			return Collections.emptyList();
		List<Game> games = gameDao.getGamesInBacklog(u);
		updateBacklogDetails(games);
		return games;
	}

	@Transactional
	@Override
	public void addToBacklog(long gameId)
	{
		User u = us.getLoggedUser();
		if(u != null)
			gameDao.addToBacklog(gameId, u);
	}

	@Transactional
	@Override
	public void removeFromBacklog(long gameId)
	{
		User u = us.getLoggedUser();
		if(u != null)
			gameDao.removeFromBacklog(gameId, u);
	}

	@Transactional
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

	@Transactional
	@Override
	public void updateBacklogDetails(Collection<Game> games)
	{
		for(Game g : games)
			g.setInBacklog(gameInBacklog(g.getId()));
	}

	@Transactional
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

	@Transactional
	@Override
	public List<Game> getPopularGames()
	{
		List<Game> games = gameDao.getMostBacklogged();
		updateBacklogDetails(games);
		return games;
	}

	@Transactional
	@Override
	public List<Game> getFilteredGames(String searchTerm, List<String> genres, List<String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight, int page, int pageSize)
	{
		List<Game> results = gameDao.getFilteredGames(searchTerm, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, page, pageSize);
		updateBacklogDetails(results);
		return results;
	}

	@Transactional
	@Override
	public List<Game> getGamesInBacklogReleasingTomorrow(User u)
	{
		return gameDao.getGamesInBacklogReleasingTomorrow(u);

	}

	@Transactional
	@Override
	public 	int countSearchResults(String searchTerm) {
		return gameDao.countSearchResults(searchTerm);
	}

	@Transactional
	@Override
	public 	int countSearchResultsFiltered(String searchTerm, List<String> genres, List <String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight) {
		return gameDao.countSearchResultsFiltered(searchTerm, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight);
	}

	@Transactional
	@Override
	public List<Game> getGamesForPlatform(Platform p, int page, int pageSize)
	{
		List<Game> results = gameDao.getGamesForPlatform(p, page, pageSize);
		updateBacklogDetails(results);
		return results;
	}

	@Transactional
	@Override
	public int countGamesForPlatform(Platform p)
	{
		return gameDao.countGamesForPlatform(p);
	}

	@Transactional
	@Override
	public List<Game> getGamesForGenre(Genre g, int page, int pageSize) {
		List<Game> results = gameDao.getGamesForGenre(g, page, pageSize);
		updateBacklogDetails(results);
		return results;
	}

	@Transactional
	@Override
	public List<Game> getGamesForDeveloper(Developer d, int page, int pageSize)
	{
		List<Game> results = gameDao.getGamesForDeveloper(d, page, pageSize);
		updateBacklogDetails(results);
		return results;
	}

	@Transactional
	@Override
	public int countGamesForGenre(Genre g)
	{
		return gameDao.countGamesForGenre(g);
	}

	@Transactional
	@Override
	public int countGamesForDeveloper(Developer d)
	{
		return gameDao.countGamesForDeveloper(d);
	}

	@Transactional
	@Override
	public List<Game> getGamesForPublisher(Publisher p, int page, int pageSize)
	{
		List<Game> results = gameDao.getGamesForPublisher(p, page, pageSize);
		updateBacklogDetails(results);
		return results;
	}

	@Transactional
	@Override
	public int countGamesForPublisher(Publisher p)
	{
		return gameDao.countGamesForPublisher(p);
	}

	@Transactional
	@Override
	public List<Game> getGamesInBacklog(int page, int pageSize)
	{
		User u = us.getLoggedUser();
		if(u == null)
			return Collections.emptyList();
		List<Game> games = gameDao.getGamesInBacklog(u, page, pageSize);
		updateBacklogDetails(games);
		return games;
	}

	@Transactional
	@Override
	public List<Game> getGamesInBacklog(User u, int page, int pageSize)
	{
		if(u == null)
			return Collections.emptyList();
		List<Game> games = gameDao.getGamesInBacklog(u, page, pageSize);
		updateBacklogDetails(games);
		return games;
	}

	@Transactional
	@Override
	public int countGamesInBacklog()
	{
		User u = us.getLoggedUser();
		if(u == null)
			return 0;
		return gameDao.countGamesInBacklog(u);
	}

	@Transactional
	@Override
	public int countGamesInBacklog(User u)
	{
		return gameDao.countGamesInBacklog(u);
	}

	@Transactional
	@Override
	public void remove(Game g){
		gameDao.removeAllPlatforms(g);
		gameDao.removeAllDevelopers(g);
		gameDao.removeAllPublishers(g);
		gameDao.removeAllGenres(g);
		gameDao.removeAllReleaseDates(g);
		gameDao.remove(g);
		is.removeByName(g.getCover());
	}

	@Transactional
	@Override
	public void removeById(long id){
		Optional<Game> g = gameDao.findById(id);
		remove(g.get());
	}

	@Transactional
	@Override
	public void update(long id, String title, MultipartFile cover, String description, String trailer, List<Long> platforms, List<Long> developers, List<Long> publishers, List<Long> genres, Map<Long, LocalDate> releaseDates) throws BadFormatException {
		Optional<Game> optg = gameDao.findById(id);
		Game		   g    = optg.get();
		g.setTitle(title);
		g.setDescription(description);
		if(trailer != null && !trailer.isEmpty()) {
			g.setTrailer(trailer);
		} else {
			g.setTrailer(null);
		}
		gameDao.removeAllPlatforms(g);
		gameDao.removeAllDevelopers(g);
		gameDao.removeAllPublishers(g);
		gameDao.removeAllGenres(g);
		System.out.println("Whole new world");
		gameDao.removeAllReleaseDates(g);
		System.out.println("Whole new place");
		gameDao.addPlatforms(id, platforms);
		gameDao.addDevelopers(id, developers);
		gameDao.addPublishers(id, publishers);
		gameDao.addGenres(id, genres);
		gameDao.addReleaseDates(id, releaseDates);
		if(cover != null && !cover.isEmpty()) {
			String coverName = getCoverName(id, cover);
			is.removeByName(g.getCover());
			try {
				is.uploadImage(coverName, cover.getBytes());
			} catch (IOException e){
				throw new BadFormatException();
			}
			g.setCover(coverName);
		}
		gameDao.update(g);
	}

	private String getCoverName(long id, MultipartFile cover){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("games/").append("cover-game-").append(id).append('.').append(FilenameUtils.getExtension(cover.getOriginalFilename()));
		return stringBuilder.toString();
	}
}
