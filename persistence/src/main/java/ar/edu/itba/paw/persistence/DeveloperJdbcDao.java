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

import ar.edu.itba.paw.interfaces.DeveloperDao;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;

@Repository
public class DeveloperJdbcDao implements DeveloperDao {

	private	final SimpleJdbcInsert jdbcInsert;
	private JdbcTemplate jdbcTemplate;
	protected static final RowMapper<Developer> DEVELOPER_MAPPER = new RowMapper<Developer>()
	{
		@Override
		public Developer mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new Developer(rs.getInt("developer"), rs.getString("developer_name"), rs.getString("developer_logo"));
		}
	};
	
	@Autowired
	public DeveloperJdbcDao(final DataSource ds)
	{
	    jdbcTemplate = new JdbcTemplate(ds);
	    jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("developers").usingGeneratedKeyColumns("developer");
	}
	
	@Override
	public Optional<Developer> findById(long id)
	{
		Optional<Developer> d = jdbcTemplate.query("SELECT * FROM developers WHERE developer = ?", DEVELOPER_MAPPER, id).stream().findFirst();
		if(d.isPresent())
		{
			List<Game> games = getAllGames(d.get());
			for(Game g : games)
			{
				d.get().addGame(g);
			}
		}
		
		return d;
	}

	@Override
	public Optional<Developer> findByName(String name)
	{
		return jdbcTemplate.query("SELECT * FROM developers WHERE developer_name LIKE ?", DEVELOPER_MAPPER, name).stream().findFirst();
	}

	@Override
	public Optional<Developer> changeName(long id, String new_name)
	{
		return jdbcTemplate.query("UPDATE TABLE developers SET developer_name LIKE ? WHERE developer = ?", DEVELOPER_MAPPER, id, new_name).stream().findFirst();
	}

	@Override
	public Optional<Developer> changeLogo(long id, String new_logo)
	{
		return jdbcTemplate.query("UPDATE TABLE developers SET developer_logo LIKE ? WHERE developer = ?", DEVELOPER_MAPPER, id, new_logo).stream().findFirst();
	}

	@Override
	public Developer register(String name, String logo)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("name", name); 
		args.put("logo", logo);
		final Number developerId = jdbcInsert.executeAndReturnKey(args);
		return new Developer(developerId.longValue(), name, logo);
	}

	@Override
	public List<Developer> getAllDevelopers()
	{
		return jdbcTemplate.query("SELECT * FROM developers", DEVELOPER_MAPPER);
	}
	
	@Override
	public List<Game> getAllGames(Developer d)
	{
		List<Game> gameList = jdbcTemplate.query("SELECT DISTINCT * FROM (SELECT * FROM developers WHERE developer = ?) AS d NATURAL JOIN development NATURAL JOIN games",
				new Object[]{d.getId()},
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
