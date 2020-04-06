package ar.edu.itba.paw.persistence;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.GenreDao;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;

@Repository
public class GenreJdbcDao implements GenreDao
{	
	private	final SimpleJdbcInsert jdbcInsert;
	private JdbcTemplate jdbcTemplate;
	protected static final RowMapper<Genre> GENRE_MAPPER = new RowMapper<Genre>()
	{
		@Override
		public Genre mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new Genre(rs.getInt("genre"), rs.getString("genre_name"),rs.getString("genre_logo"));
		}
	};
	
	@Autowired
	public GenreJdbcDao(final DataSource ds)
	{
	    jdbcTemplate = new JdbcTemplate(ds);
	    jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("genres").usingGeneratedKeyColumns("genre");
	}
	
	@Override
	public Optional<Genre> findById(long id)
	{
		Optional<Genre> genre = jdbcTemplate.query("SELECT * FROM genres WHERE genre = ?", GENRE_MAPPER, id).stream().findFirst();
		if(genre.isPresent())
		{
			List<Game> games = getAllGames(genre.get());
			for(Game g : games)
			{
				genre.get().addGame(g);
			}
		}
		return genre;
	}

	@Override
	public Optional<Genre> findByName(String name)
	{
		return jdbcTemplate.query("SELECT * FROM genres WHERE genre_name LIKE ?", GENRE_MAPPER, name).stream().findFirst();
	}

	@Override
	public Optional<Genre> changeName(long id, String new_name)
	{
		return jdbcTemplate.query("UPDATE TABLE genres SET genre_name LIKE ? WHERE genre = ?", GENRE_MAPPER, id, new_name).stream().findFirst();
	}
	
	@Override
	public Optional<Genre> changeLogo(long id, String new_logo)
	{
		return jdbcTemplate.query("UPDATE TABLE genres SET genre_logo LIKE ? WHERE genre = ?", GENRE_MAPPER, id, new_logo).stream().findFirst();
	}

	@Override
	public Genre register(String name, String logo)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("name", name); 
		args.put("logo", logo);
		final Number genreId = jdbcInsert.executeAndReturnKey(args);
		return new Genre(genreId.longValue(), name, logo);
	}

	@Override
	public List<Genre> getAllGenres()
	{
		return jdbcTemplate.query("SELECT * FROM genres", GENRE_MAPPER);
	}

	@Override
	public List<Game> getAllGames(Genre g)
	{
		List<Game> gameList = jdbcTemplate.query("SELECT * FROM (SELECT * FROM genres WHERE genre = ?) AS g NATURAL JOIN classifications NATURAL JOIN games",
				new Object[]{g.getId()},
				new RowMapper<Game>()
				{
					@Override
					public Game mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						return new Game(rs.getInt("game"), rs.getString("title"), rs.getString("cover"), rs.getString("description"));
					}
				});
		return gameList;
	}

}
