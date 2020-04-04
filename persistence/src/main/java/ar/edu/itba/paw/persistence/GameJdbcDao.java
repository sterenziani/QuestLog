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
import ar.edu.itba.paw.model.Game;
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
		return jdbcTemplate.query("SELECT * FROM games WHERE game = ?", GAME_MAPPER, id).stream().findFirst();
	}

	@Override
	public List<Game> findByTitle(String title)
	{
		return jdbcTemplate.query("SELECT * FROM games WHERE title LIKE '?'", GAME_MAPPER, title);
	}

	@Override
	public Optional<Game> changeTitle(long id, String new_title)
	{
		return jdbcTemplate.query("UPDATE TABLE games SET title = ? WHERE id = ?", GAME_MAPPER, id, new_title).stream().findFirst();
	}

	@Override
	public Optional<Game> changeCover(long id, String new_cover)
	{
		return jdbcTemplate.query("UPDATE TABLE games SET cover = ? WHERE id = ?", GAME_MAPPER, id, new_cover).stream().findFirst();
	}

	@Override
	public Optional<Game> changeDescription(long id, String new_desc)
	{
		return jdbcTemplate.query("UPDATE TABLE games SET description = ? WHERE id = ?", GAME_MAPPER, id, new_desc).stream().findFirst();
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
		return jdbcTemplate.query("SELECT * FROM games", GAME_MAPPER);
	}
}
