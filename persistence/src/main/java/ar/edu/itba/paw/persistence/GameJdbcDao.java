package ar.edu.itba.paw.persistence;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.dao.GameDao;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Region;
import ar.edu.itba.paw.model.Release;
import ar.edu.itba.paw.model.User;

@Repository
public class GameJdbcDao implements GameDao
{
	private	final SimpleJdbcInsert jdbcInsert;
	private final SimpleJdbcInsert gameVersionsJdbcInsert;
	private final SimpleJdbcInsert developmentJdbcInsert;
	private final SimpleJdbcInsert publishingJdbcInsert;
	private final SimpleJdbcInsert classificationJdbcInsert;
	private final SimpleJdbcInsert releasesJdbcInsert;
	private JdbcTemplate 		   jdbcTemplate;
	private static final int MIN_AMOUNT_FOR_OVERLAP = 3;
	private static final int MIN_AMOUNT_FOR_POPULAR = 3;
	protected static final RowMapper<Game> GAME_MAPPER = new RowMapper<Game>()
	{
		@Override
		public Game mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new Game(rs.getInt("game"), rs.getString("title"), rs.getString("cover"), rs.getString("description"));
		}
	};
	
	@Autowired
	public GameJdbcDao(final DataSource ds)
	{
	    jdbcTemplate 			 = new JdbcTemplate(ds);
	    jdbcInsert 				 = new SimpleJdbcInsert(jdbcTemplate).withTableName("games").usingGeneratedKeyColumns("game");
	    gameVersionsJdbcInsert 	 = new SimpleJdbcInsert(jdbcTemplate).withTableName("game_versions");
	    developmentJdbcInsert	 = new SimpleJdbcInsert(jdbcTemplate).withTableName("development");
	    publishingJdbcInsert	 = new SimpleJdbcInsert(jdbcTemplate).withTableName("publishing");
	    classificationJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("classifications");
	    releasesJdbcInsert		 = new SimpleJdbcInsert(jdbcTemplate).withTableName("releases");
	}

	@Override
	public Optional<Game> findById(long id)
	{
		Optional<Game> g = jdbcTemplate.query("SELECT * FROM games WHERE game = ?", GAME_MAPPER, id).stream().findFirst();
		return g;
	}
	
	@Override
	public Optional<Game> findByIdWithDetails(long id)
	{
		Optional<Game> g = jdbcTemplate.query("SELECT * FROM games WHERE game = ?", GAME_MAPPER, id).stream().findFirst();
		if(g.isPresent())
		{
			List<Platform> platforms = getAllPlatforms(g.get());
			for(Platform p : platforms)
				g.get().addPlatform(p);

			List<Developer> developers = getAllDevelopers(g.get());
			for(Developer d : developers)
				g.get().addDeveloper(d);

			List<Publisher> publishers = getAllPublishers(g.get());
			for(Publisher pub: publishers)
				g.get().addPublisher(pub);

			List<Genre> genres = getAllGenres(g.get());
			for(Genre genre : genres)
				g.get().addGenre(genre);
			
			List<Release> releases = getAllReleaseDates(g.get());
			for(Release r: releases)
				g.get().addReleaseDate(r);
		}
		return g;
	}

	@Override
	public Optional<Game> findByTitle(String title)
	{
		Optional<Game> game = jdbcTemplate.query("SELECT * FROM games WHERE title LIKE ?", GAME_MAPPER, title).parallelStream().findFirst();
		return game;
	}
	
	@Override
	public Optional<Game> findByTitleWithDetails(String title)
	{
		Optional<Game> game = jdbcTemplate.query("SELECT * FROM games WHERE title LIKE ?", GAME_MAPPER, title).parallelStream().findFirst();
		if(game.isPresent())
		{
			List<Platform> platforms = getAllPlatforms(game.get());
			for(Platform p : platforms)
				game.get().addPlatform(p);

			List<Developer> developers = getAllDevelopers(game.get());
			for(Developer d : developers)
				game.get().addDeveloper(d);

			List<Publisher> publishers = getAllPublishers(game.get());
			for(Publisher pub: publishers)
				game.get().addPublisher(pub);

			List<Genre> genres = getAllGenres(game.get());
			for(Genre genre : genres)
				game.get().addGenre(genre);
			
			List<Release> releases = getAllReleaseDates(game.get());
			for(Release r: releases)
				game.get().addReleaseDate(r);
		}
		return game;
	}

	@Override
	public Optional<Game> changeTitle(long id, String new_title)
	{
		jdbcTemplate.update("UPDATE games SET title = ? WHERE game = ?", new_title, id);
		return findById(id);
	}

	@Override
	public Optional<Game> changeCover(long id, String new_cover)
	{
		jdbcTemplate.update("UPDATE games SET cover = ? WHERE game = ?", new_cover, id);
		return findById(id);
	}

	@Override
	public Optional<Game> changeDescription(long id, String new_desc)
	{
		jdbcTemplate.update("UPDATE games SET description = ? WHERE game = ?", new_desc, id);
		return findById(id);
	}

	@Override
	public Game register(String title, String cover, String description, long[] platforms, long[] developers, long[] publishers, long[] genres, Map<Long, LocalDate> releaseDates)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("title", title); 
		args.put("cover", cover); 
		args.put("description", description);
		final Number gameId = jdbcInsert.executeAndReturnKey(args);
		if(gameId == null)
			return null;
		final long gameIdLong = gameId.longValue();
		addPlatforms(gameIdLong, platforms);
		addDevelopers(gameIdLong, developers);
		addPublishers(gameIdLong, publishers);
		addGenres(gameIdLong, genres);
		addReleaseDates(gameIdLong, releaseDates);
		return new Game(gameId.longValue(), title, cover, description);
	}

	@Override 
	public List<Game> getAllGames(){
		return jdbcTemplate.query("SELECT * FROM games", GAME_MAPPER);
	}
	
	@Override
	public List<Game> getAllGamesWithDetails()
	{
		List<Game> games = jdbcTemplate.query("SELECT * FROM games", GAME_MAPPER);
		for(Game g : games)
		{
			List<Platform> platforms = getAllPlatforms(g);
			for(Platform p : platforms)
				g.addPlatform(p);

			List<Developer> developers = getAllDevelopers(g);
			for(Developer d : developers)
				g.addDeveloper(d);

			List<Publisher> publishers = getAllPublishers(g);
			for(Publisher pub: publishers)
				g.addPublisher(pub);

			List<Genre> genres = getAllGenres(g);
			for(Genre genre : genres)
				g.addGenre(genre);
			
			List<Release> releases = getAllReleaseDates(g);
			for(Release r: releases)
				g.addReleaseDate(r);
		}
		return games;
	}
	
	@Override
	public Optional<Game> addPlatform(Game g, Platform p)
	{
		jdbcTemplate.update("INSERT INTO game_versions(game, platform) VALUES(?, ?)", g.getId(), p.getId());
		return findById(g.getId());
	}

	private void addPlatforms(long g, long[] platforms_ids)
	{
		MapSqlParameterSource[] gameVersionRows = new MapSqlParameterSource[platforms_ids.length];
		for(int i = 0; i < platforms_ids.length; i++){
			gameVersionRows[i] = new MapSqlParameterSource().addValue("game", g).addValue("platform", platforms_ids[i]);
		}
		gameVersionsJdbcInsert.executeBatch(gameVersionRows);
	}

	@Override
	public Optional<Game> removePlatform(Game g, Platform p)
	{
		jdbcTemplate.update("DELETE FROM game_versions WHERE game = ? AND platform = ?", g.getId(), p.getId());
		return findById(g.getId());
	}

	private List<Platform> getAllPlatforms(Game g)
	{
		List<Platform> platformList = jdbcTemplate.query("SELECT * FROM (SELECT * FROM games WHERE game = ?) AS g NATURAL JOIN game_versions NATURAL JOIN platforms",
				new RowMapper<Platform>()
				{
					@Override
					public Platform mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						return new Platform(rs.getInt("platform"), rs.getString("platform_name"), rs.getString("platform_name_short"), rs.getString("platform_logo"));
					}
				}, g.getId());
		return platformList;
	}
	
	@Override
	public Optional<Game> addPublisher(Game g, Publisher p)
	{
		jdbcTemplate.update("INSERT INTO publishing(game, publisher) VALUES(?, ?)", g.getId(), p.getId());
		return findById(g.getId());
	}

	private void addPublishers(long g, long[] publisher_ids)
	{
		MapSqlParameterSource[] publishingRows = new MapSqlParameterSource[publisher_ids.length];
		for(int i = 0; i < publisher_ids.length; i++){
			publishingRows[i] = new MapSqlParameterSource().addValue("game", g).addValue("publisher", publisher_ids[i]);
		}
		publishingJdbcInsert.executeBatch(publishingRows);
	}

	@Override
	public Optional<Game> removePublisher(Game g, Publisher p)
	{
		jdbcTemplate.update("DELETE FROM publishing WHERE game = ? AND publisher = ?", g.getId(), p.getId());
		return findById(g.getId());
	}

	private List<Publisher> getAllPublishers(Game g)
	{
		List<Publisher> publisherList = jdbcTemplate.query("SELECT * FROM (SELECT * FROM games WHERE game = ?) AS g NATURAL JOIN publishing NATURAL JOIN publishers",
				new RowMapper<Publisher>()
				{
					@Override
					public Publisher mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						return new Publisher(rs.getInt("publisher"), rs.getString("publisher_name"), rs.getString("publisher_logo"));
					}
				}, g.getId());
		return publisherList;
	}
	
	@Override
	public Optional<Game> addDeveloper(Game g, Developer d)
	{
		jdbcTemplate.update("INSERT INTO development(game, developer) VALUES(?, ?)", g.getId(), d.getId());
		return findById(g.getId());
	}

	private void addDevelopers(long g, long[] devs_ids)
	{
		MapSqlParameterSource[] developmentRows = new MapSqlParameterSource[devs_ids.length];
		for(int i = 0; i < devs_ids.length; i++){
			developmentRows[i] = new MapSqlParameterSource().addValue("game", g).addValue("developer", devs_ids[i]);
		}
		developmentJdbcInsert.executeBatch(developmentRows);
	}

	@Override
	public Optional<Game> removeDeveloper(Game g, Developer d)
	{
		jdbcTemplate.update("DELETE FROM development WHERE game = ? AND developer = ?", g.getId(), d.getId());
		return findById(g.getId());
	}

	private List<Developer> getAllDevelopers(Game g)
	{
		List<Developer> developerList = jdbcTemplate.query("SELECT * FROM (SELECT * FROM games WHERE game = ?) AS g NATURAL JOIN development NATURAL JOIN developers",
				new RowMapper<Developer>()
				{
					@Override
					public Developer mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						return new Developer(rs.getInt("developer"), rs.getString("developer_name"), rs.getString("developer_logo"));
					}
				}, g.getId());
		return developerList;
	}

	@Override
	public Optional<Game> addGenre(Game game, Genre genre)
	{
		jdbcTemplate.update("INSERT INTO classifications(game, genre) VALUES(?, ?)", game.getId(), genre.getId());
		return findById(game.getId());
	}

	private void addGenres(long g, long[] genres_ids)
	{
		MapSqlParameterSource[] classificationRows = new MapSqlParameterSource[genres_ids.length];
		for(int i = 0; i < genres_ids.length; i++){
			classificationRows[i] = new MapSqlParameterSource().addValue("game", g).addValue("genre", genres_ids[i]);
		}
		classificationJdbcInsert.executeBatch(classificationRows);
	}

	@Override
	public Optional<Game> removeGenre(Game game, Genre genre)
	{
		jdbcTemplate.update("DELETE FROM classifications WHERE game = ? AND genre = ?", game.getId(), genre.getId());
		return findById(game.getId());
	}

	
	private List<Genre> getAllGenres(Game g)
	{
		List<Genre> genreList = jdbcTemplate.query("SELECT * FROM (SELECT * FROM games WHERE game = ?) AS g NATURAL JOIN classifications NATURAL JOIN genres",
				new RowMapper<Genre>()
				{
					@Override
					public Genre mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						return new Genre(rs.getInt("genre"), rs.getString("genre_name"), rs.getString("genre_logo"));
					}
				}, g.getId());
		return genreList;
	}

	@Override
	public Optional<Game> addReleaseDate(Game game, Release r)
	{
		jdbcTemplate.update("INSERT INTO releases(game, region, release_date) VALUES(?, ?, ?)", game.getId(), r.getRegion().getId(), r.getDate());
		return findById(game.getId());
	}

	private void addReleaseDates(long g, Map<Long, LocalDate> releaseDates)
	{
		MapSqlParameterSource[] releasesRows = new MapSqlParameterSource[releaseDates.size()];
		int i = 0;
		for(Map.Entry<Long, LocalDate> releaseDate : releaseDates.entrySet()){
			releasesRows[i] = new MapSqlParameterSource().addValue("game", g).addValue("region", releaseDate.getKey()).addValue("release_date", releaseDate.getValue());
		}
		releasesJdbcInsert.executeBatch(releasesRows);
	}

	@Override
	public Optional<Game> removeReleaseDate(Game game, Release r)
	{
		jdbcTemplate.update("DELETE FROM releases WHERE game = ? AND region = ?", game.getId(), r.getRegion().getId());
		return findById(game.getId());
	}
	
	private List<Release> getAllReleaseDates(Game g)
	{
		List<Release> dates = jdbcTemplate.query("SELECT * FROM releases NATURAL JOIN regions WHERE game = ?",
				new RowMapper<Release>()
				{
					@Override
					public Release mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						Region r = new Region(rs.getLong("region"), rs.getString("region_name"), rs.getString("region_short"));
						return new Release(r, rs.getDate("release_date"));
					}
				}, g.getId());
		return dates;
	}
	
	@Override
	public List<Game> searchByTitle(String search, int page, int pageSize){
		return jdbcTemplate.query("SELECT DISTINCT * FROM games WHERE LOWER(title) LIKE LOWER(CONCAT('%',?,'%')) ORDER BY game LIMIT ? OFFSET ?", GAME_MAPPER, search, pageSize, (page-1)*pageSize);
	}
	
	public List<Game> searchByTitleWithDetails(String search){
		List<Game> searchGames = jdbcTemplate.query("SELECT DISTINCT * FROM games WHERE LOWER(title) LIKE LOWER(CONCAT('%',?,'%')) ", GAME_MAPPER, search);
		for(Game g : searchGames)
		{	
			List<Platform> platforms = getAllPlatforms(g);
			for(Platform p : platforms)
				g.addPlatform(p);

			List<Developer> developers = getAllDevelopers(g);
			for(Developer d : developers)
				g.addDeveloper(d);

			List<Publisher> publishers = getAllPublishers(g);
			for(Publisher pub: publishers)
				g.addPublisher(pub);

			List<Genre> genres = getAllGenres(g);
			for(Genre genre : genres)
				g.addGenre(genre);
			
			List<Release> releases = getAllReleaseDates(g);
			for(Release r: releases)
				g.addReleaseDate(r);
		}
		return searchGames;
	}
	
	@Override
	public List<Game> getUpcomingGames()
	{
		return jdbcTemplate.query("SELECT game, title, cover, description, min(release_date) AS next_date\n"
								+ "FROM games NATURAL JOIN releases WHERE release_date > CURRENT_DATE AND release_date < CURRENT_DATE + 250\n"
								+ "GROUP BY game ORDER BY next_date LIMIT 10", GAME_MAPPER);
	}
	
	@Override
	public List<Game> getGamesReleasingTomorrow()
	{
		return jdbcTemplate.query("SELECT DISTINCT game, title, cover, description FROM games NATURAL JOIN releases WHERE release_date = CURRENT_DATE + 1", GAME_MAPPER);
	}
	
	@Override
	public List<Game> getGamesInBacklogReleasingTomorrow(User u)
	{
		return jdbcTemplate.query("SELECT DISTINCT game, title, cover, description FROM games NATURAL JOIN releases WHERE release_date = CURRENT_DATE + 1 "
									+"AND game in (SELECT game FROM backlogs WHERE user_id = ?)) AS g NATURAL JOIN games", GAME_MAPPER, u.getId());
	}
	
	@Override
	public boolean isInBacklog(long gameId, User u)
	{
		Optional<Game> g = jdbcTemplate.query("SELECT * FROM (SELECT * FROM backlogs WHERE game = ? AND user_id = ?) AS g NATURAL JOIN games", GAME_MAPPER, gameId, u.getId()).stream().findFirst();
		return g.isPresent();
	}
	
	@Override
	public void addToBacklog(long gameId, User u)
	{
		jdbcTemplate.update("INSERT INTO backlogs(user_id, game) VALUES(?, ?) ON CONFLICT DO NOTHING", u.getId(), gameId);
	}
	
	@Override
	public void removeFromBacklog(long gameId, User u)
	{
		jdbcTemplate.update("DELETE FROM backlogs WHERE user_id = ? AND game = ?", u.getId(), gameId);
	}

	@Override
	public List<Game> getGamesInBacklog(User u)
	{
		List<Game> games = jdbcTemplate.query("SELECT * FROM (SELECT * FROM backlogs WHERE user_id = ?) AS g NATURAL JOIN games", GAME_MAPPER, u.getId());
		return games;
	}
	
	@Override
	public List<Game> getSimilarToBacklog(User u)
	{
		long id = u.getId();
		return jdbcTemplate.query("SELECT * FROM (SELECT t2.game AS game FROM backlogs AS t1 JOIN backlogs AS t2 ON t1.user_id = t2.user_id AND t1.user_id != ?"
												+ "AND t1.game IN (SELECT game FROM backlogs WHERE user_id = ?) AND t2.game NOT IN (SELECT game FROM backlogs WHERE user_id = ?)"
												+ "GROUP BY t2.game HAVING count(*) >= ? ORDER BY count(*) DESC) AS a NATURAL JOIN games LIMIT 10", GAME_MAPPER, id, id, id, MIN_AMOUNT_FOR_OVERLAP);
	}
	
	@Override
	public List<Game> getMostBacklogged()
	{
		return jdbcTemplate.query("SELECT * FROM (SELECT game FROM backlogs GROUP BY game HAVING count(*) >= ? ORDER BY count(*) DESC) AS a NATURAL JOIN games LIMIT 10", GAME_MAPPER, MIN_AMOUNT_FOR_POPULAR);
	}
	
	@Override
	public List<Game> getFilteredGames(String searchTerm, List<String> genres, List <String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight, int page, int pageSize)
	{			
		String genreFilter = "";
		if(genres.size()>0) 
			genreFilter =  " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT * FROM genres WHERE genre IN (" + String.join(", ", genres) + ")) AS gnrs NATURAL JOIN classifications) AS a";		
		
		String platformFilter = "";
		if(platforms.size()>0)
			platformFilter = " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT * FROM platforms WHERE platform IN (" + String.join(", ", platforms) + ")) AS plats NATURAL JOIN game_versions) AS b";		
		
		String scoreFilter = "";
		if(scoreLeft != 0 || scoreRight != 100)
			scoreFilter = " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT game FROM scores GROUP BY game HAVING AVG(score) >= " + scoreLeft + " AND AVG(score) <= " + scoreRight + ") AS sc) AS c";		
		String timeFilter = "";
		if(timeLeft != 0 || timeRight != 35999999)
			timeFilter = " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT game FROM runs WHERE playstyle = 1 GROUP BY game HAVING AVG(time) >= " + timeLeft + " AND AVG(time) <= " + timeRight + ") AS a) AS d";
		
		return jdbcTemplate.query("SELECT * FROM (SELECT * FROM games WHERE LOWER(title) LIKE LOWER(CONCAT('%',?,'%'))) as z" + genreFilter + platformFilter + scoreFilter + timeFilter
				+ " ORDER BY game LIMIT ? OFFSET ?", GAME_MAPPER, searchTerm, pageSize, (page-1)*pageSize);
	}

	@Override
	public int countSearchResults(String searchTerm)
	{
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM games WHERE LOWER(title) LIKE LOWER(CONCAT('%',?,'%'))", Integer.class, searchTerm);
	}

	@Override
	public int countSearchResultsFiltered(String searchTerm, List<String> genres, List<String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight)
	{
		String genreFilter = "";
		if(genres.size()>0) 
			genreFilter =  " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT * FROM genres WHERE genre IN (" + String.join(", ", genres) + ")) AS gnrs NATURAL JOIN classifications) AS a";		
		
		System.out.println("COUNTER");
		System.out.println(genres.size());
		System.out.println(genres);
		System.out.println("");
		
		String platformFilter = "";
		if(platforms.size()>0)
			platformFilter = " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT * FROM platforms WHERE platform IN (" + String.join(", ", platforms) + ")) AS plats NATURAL JOIN game_versions) AS b";		
		String scoreFilter = "";
		if(scoreLeft != 0 || scoreRight != 100)
			scoreFilter = " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT game FROM scores GROUP BY game HAVING AVG(score) >= " + scoreLeft + " AND AVG(score) <= " + scoreRight + ") AS sc) AS c";	
		
		String timeFilter = "";
		if(timeLeft != 0 || timeRight != 35999999)
			timeFilter = " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT game FROM runs WHERE playstyle = 1 GROUP BY game HAVING AVG(time) >= " + timeLeft + " AND AVG(time) <= " + timeRight + ") AS a) AS d";
		
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM (SELECT * FROM (SELECT * FROM games WHERE LOWER(title) LIKE LOWER(CONCAT('%',?,'%'))) as z" + genreFilter + platformFilter
				+ scoreFilter + timeFilter + ") AS x", Integer.class, searchTerm);
	}

	@Override
	public List<Game> getGamesForPlatform(Platform p, int page, int pageSize)
	{
		return jdbcTemplate.query("SELECT * FROM games NATURAL JOIN game_versions WHERE platform = ? ORDER BY title LIMIT ? OFFSET ?", GAME_MAPPER, p.getId(), pageSize, (page-1)*pageSize);
	}

	@Override
	public int countGamesForPlatform(Platform p)
	{
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM games NATURAL JOIN game_versions WHERE platform = ?", Integer.class, p.getId());
	}

	@Override
	public List<Game> getGamesForGenre(Genre g, int page, int pageSize)
	{
		return jdbcTemplate.query("SELECT * FROM games NATURAL JOIN classifications WHERE genre = ? ORDER BY title LIMIT ? OFFSET ?", GAME_MAPPER, g.getId(), pageSize, (page-1)*pageSize);

	}

	@Override
	public int countGamesForGenre(Genre g)
	{
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM games NATURAL JOIN classifications WHERE genre = ?", Integer.class, g.getId());
	}

	@Override
	public List<Game> getGamesForDeveloper(Developer d, int page, int pageSize)
	{
		return jdbcTemplate.query("SELECT * FROM games NATURAL JOIN development WHERE developer = ? ORDER BY title LIMIT ? OFFSET ?", GAME_MAPPER, d.getId(), pageSize, (page-1)*pageSize);
	}

	@Override
	public int countGamesForDeveloper(Developer d)
	{
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM games NATURAL JOIN development WHERE developer = ?", Integer.class, d.getId());
	}

	@Override
	public List<Game> getGamesForPublisher(Publisher p, int page, int pageSize)
	{
		return jdbcTemplate.query("SELECT * FROM games NATURAL JOIN publishing WHERE publisher = ? ORDER BY title LIMIT ? OFFSET ?", GAME_MAPPER, p.getId(), pageSize, (page-1)*pageSize);
	}

	@Override
	public int countGamesForPublisher(Publisher p)
	{
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM games NATURAL JOIN publishing WHERE publisher = ?", Integer.class, p.getId());
	}

	@Override
	public List<Game> getGamesInBacklog(User u, int page, int pageSize)
	{
		List<Game> games = jdbcTemplate.query("SELECT * FROM (SELECT * FROM backlogs WHERE user_id = ?) AS g NATURAL JOIN games LIMIT ? OFFSET ?", GAME_MAPPER, u.getId(), pageSize, (page-1)*pageSize);
		return games;
	}

	@Override
	public int countGamesInBacklog(User u)
	{
		return jdbcTemplate.queryForObject("SELECT count(*) FROM (SELECT * FROM backlogs WHERE user_id = ?) AS g NATURAL JOIN games", Integer.class, u.getId());
	}	
}