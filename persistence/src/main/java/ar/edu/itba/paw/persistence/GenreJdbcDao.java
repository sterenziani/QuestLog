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
import ar.edu.itba.paw.model.Genre;

@Repository
public class GenreJdbcDao implements GenreDao{
	
	private	final SimpleJdbcInsert jdbcInsert;
	private JdbcTemplate jdbcTemplate;
	protected static final RowMapper<Genre> GENRE_MAPPER = new RowMapper<Genre>()
	{
		@Override
		public Genre mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new Genre(rs.getInt("genre"), rs.getString("genre_name"));
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
		return jdbcTemplate.query("SELECT * FROM genres WHERE genre = ?", GENRE_MAPPER, id).stream().findFirst();
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
	public Genre register(String name)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("name", name); 
		final Number genreId = jdbcInsert.executeAndReturnKey(args);
		return new Genre(genreId.longValue(), name);
	}

	@Override
	public List<Genre> getAllGenres()
	{
		return jdbcTemplate.query("SELECT * FROM genres", GENRE_MAPPER);
	}

}
