package ar.edu.itba.paw.persistence;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Region;
import ar.edu.itba.paw.model.Release;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GameJdbcDao implements GameDao
{
	private	final SimpleJdbcInsert jdbcInsert;
	private JdbcTemplate jdbcTemplate;
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
	    jdbcTemplate = new JdbcTemplate(ds);
	    jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("games").usingGeneratedKeyColumns("game");
	}

	@Override
	public Optional<Game> findById(long id)
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
		Optional<Game> game = jdbcTemplate.query("SELECT * FROM games WHERE title LIKE '?'", GAME_MAPPER, title).parallelStream().findFirst();
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
	public Game register(String title, String cover, String description)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("title", title); 
		args.put("cover", cover); 
		args.put("description", description);
		final Number gameId = jdbcInsert.executeAndReturnKey(args);
		return new Game(gameId.longValue(), title, cover, description);
	}

	@Override
	public List<Game> getAllGames()
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
		Optional<Game> game = jdbcTemplate.query("INSERT INTO game_versions(game, platform) VALUES(?, ?) ON CONFLICT DO NOTHING", GAME_MAPPER, g.getId(), p.getId()).stream().findFirst();
		if(game.isPresent())
			game.get().addPlatform(p);
		return game;
	}

	@Override
	public Optional<Game> removePlatform(Game g, Platform p)
	{
		Optional<Game> game = jdbcTemplate.query("DELETE FROM game_versions WHERE game = ? AND platform = ?", GAME_MAPPER, g.getId(), p.getId()).stream().findFirst();
		if(game.isPresent())
			game.get().removePlatform(p);
		return game;
	}

	@Override
	public List<Platform> getAllPlatforms(Game g)
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
		Optional<Game> game = jdbcTemplate.query("INSERT INTO publishing(game, publisher) VALUES(?, ?) ON CONFLICT DO NOTHING", GAME_MAPPER, g.getId(), p.getId()).stream().findFirst();
		if(game.isPresent())
			game.get().addPublisher(p);
		return game;
	}

	@Override
	public Optional<Game> removePublisher(Game g, Publisher p)
	{
		Optional<Game> game = jdbcTemplate.query("DELETE FROM publishing WHERE game = ? AND publisher = ?", GAME_MAPPER, g.getId(), p.getId()).stream().findFirst();
		if(game.isPresent())
			game.get().removePublisher(p);
		return game;
	}

	@Override
	public List<Publisher> getAllPublishers(Game g)
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
		Optional<Game> game = jdbcTemplate.query("INSERT INTO development(game, developer) VALUES(?, ?) ON CONFLICT DO NOTHING", GAME_MAPPER, g.getId(), d.getId()).stream().findFirst();
		if(game.isPresent())
			game.get().addDeveloper(d);
		return game;
	}

	@Override
	public Optional<Game> removeDeveloper(Game g, Developer d)
	{
		Optional<Game> game = jdbcTemplate.query("DELETE FROM development WHERE game = ? AND developer = ?", GAME_MAPPER, g.getId(), d.getId()).stream().findFirst();
		if(game.isPresent())
			game.get().removeDeveloper(d);
		return game;
	}

	@Override
	public List<Developer> getAllDevelopers(Game g)
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
		Optional<Game> g = jdbcTemplate.query("INSERT INTO classifications(game, genre) VALUES(?, ?) ON CONFLICT DO NOTHING", GAME_MAPPER, game.getId(), genre.getId()).stream().findFirst();
		if(g.isPresent())
			g.get().addGenre(genre);
		return g;
	}

	@Override
	public Optional<Game> removeGenre(Game game, Genre genre)
	{
		Optional<Game> g = jdbcTemplate.query("DELETE FROM classifications WHERE game = ? AND genre = ?", GAME_MAPPER, game.getId(), genre.getId()).stream().findFirst();
		if(g.isPresent())
			g.get().removeGenre(genre);
		return g;
	}

	@Override
	public List<Genre> getAllGenres(Game g)
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
		Optional<Game> g = jdbcTemplate.query("INSERT INTO releases(game, region, release_date) VALUES(?, ?, ?) ON CONFLICT DO NOTHING", GAME_MAPPER, game.getId(), r.getRegion().getId(), r.getDate()).stream().findFirst();
		if(g.isPresent())
			g.get().addReleaseDate(r);;
		return g;
	}

	@Override
	public Optional<Game> removeReleaseDate(Game game, Release r)
	{
		Optional<Game> g = jdbcTemplate.query("DELETE FROM releases WHERE game = ? AND region = ?", GAME_MAPPER, game.getId(), r.getRegion().getId()).stream().findFirst();
		if(g.isPresent())
			g.get().removeReleaseDate(r);
		return g;
	}
	
	@Override
	public List<Release> getAllReleaseDates(Game g)
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
}