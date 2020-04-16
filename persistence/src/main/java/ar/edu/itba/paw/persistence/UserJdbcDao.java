package ar.edu.itba.paw.persistence;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.User;

@Repository
public class UserJdbcDao implements UserDao
{
	private	final SimpleJdbcInsert jdbcInsert;
	private JdbcTemplate jdbcTemplate;
	protected static final RowMapper<User> USER_MAPPER = new RowMapper<User>()
	{
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"));
		}
	};
	
	@Autowired
	public UserJdbcDao(final DataSource ds)
	{
	    jdbcTemplate = new JdbcTemplate(ds);
	    jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("user_id");
	}
	
	@Override
	public Optional<User> findById(final long id)
	{
		return jdbcTemplate.query("SELECT * FROM users WHERE user_id = ?", USER_MAPPER, id).stream().findFirst();
	}
	
	@Override
	public Optional<User> findByUsername(String username)
	{
		return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", USER_MAPPER, username).stream().findFirst();
	}

	@Override
	public User create(String username, String password)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("username", username); 
		args.put("password", password);
		final Number userId = jdbcInsert.executeAndReturnKey(args);
		return new User(userId.longValue(), username, password);
	}
}
