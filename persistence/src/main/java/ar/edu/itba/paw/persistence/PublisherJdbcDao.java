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

import ar.edu.itba.paw.interfaces.PublisherDao;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Publisher;

@Repository
public class PublisherJdbcDao implements PublisherDao {
	private	final SimpleJdbcInsert jdbcInsert;
	private JdbcTemplate jdbcTemplate;
	protected static final RowMapper<Publisher> PUBLISHER_MAPPER = new RowMapper<Publisher>()
	{
		@Override
		public Publisher mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new Publisher(rs.getInt("publisher"), rs.getString("publisher_name"), rs.getString("publisher_logo"));
		}
	};
	
	@Autowired
	public PublisherJdbcDao(final DataSource ds)
	{
	    jdbcTemplate = new JdbcTemplate(ds);
	    jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("publishers").usingGeneratedKeyColumns("publisher");
	}
	
	@Override
	public Optional<Publisher> findById(long id)
	{
		Optional<Publisher> p = jdbcTemplate.query("SELECT * FROM publishers WHERE publisher = ?", PUBLISHER_MAPPER, id).stream().findFirst();
		if(p.isPresent())
		{
			List<Game> games = getAllGames(p.get());
			for(Game g : games)
			{
				p.get().addGame(g);
			}
		}
		
		return p;
	}

	@Override
	public Optional<Publisher> findByName(String name)
	{
		return jdbcTemplate.query("SELECT * FROM publishers WHERE publisher_name LIKE ?", PUBLISHER_MAPPER, name).stream().findFirst();
	}

	@Override
	public Optional<Publisher> changeName(long id, String new_name)
	{
		return jdbcTemplate.query("UPDATE TABLE publishers SET publisher_name LIKE ? WHERE publisher = ?", PUBLISHER_MAPPER, id, new_name).stream().findFirst();
	}

	@Override
	public Optional<Publisher> changeLogo(long id, String new_logo)
	{
		return jdbcTemplate.query("UPDATE TABLE publishers SET publisher_logo LIKE ? WHERE publisher = ?", PUBLISHER_MAPPER, id, new_logo).stream().findFirst();
	}

	@Override
	public Publisher register(String name, String logo)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("name", name); 
		args.put("logo", logo);
		final Number publisherId = jdbcInsert.executeAndReturnKey(args);
		return new Publisher(publisherId.longValue(), name, logo);
	}

	@Override
	public List<Publisher> getAllPublishers()
	{
		return jdbcTemplate.query("SELECT * FROM publishers", PUBLISHER_MAPPER);
	}

	@Override
	public List<Game> getAllGames(Publisher p)
	{
		List<Game> gameList = jdbcTemplate.query("SELECT DISTINCT * FROM (SELECT * FROM publishers WHERE publisher = ?) AS p NATURAL JOIN publishing NATURAL JOIN games",
				new Object[]{p.getId()},
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

