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
import ar.edu.itba.paw.interfaces.dao.GameDao;
import ar.edu.itba.paw.interfaces.dao.RunDao;
import ar.edu.itba.paw.interfaces.dao.ScoreDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.model.User;

@Repository
public class UserJdbcDao implements UserDao
{
	@Autowired
	private GameDao gameDao;
	
	@Autowired
	private ScoreDao scoreDao;
	
	@Autowired
	private RunDao runDao;
	
	private	final SimpleJdbcInsert jdbcInsert;
	private JdbcTemplate jdbcTemplate;
	protected static final RowMapper<User> USER_MAPPER = new RowMapper<User>()
	{
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			User u = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("email"));
			u.setAdminStatus(rs.getBoolean("admin"));
			return u;
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
		return jdbcTemplate.query("SELECT *, bool_and(users.user_id in (SELECT user_id FROM role_assignments NATURAL JOIN roles WHERE role_name LIKE 'Admin'))"
								+ "AS admin FROM users WHERE user_id = ? GROUP BY user_id", USER_MAPPER, id).stream().findFirst();
	}
	
	@Override
	public Optional<User> findByIdWithDetails(final long id)
	{
		Optional<User> opt = jdbcTemplate.query("SELECT *, bool_and(users.user_id in (SELECT user_id FROM role_assignments NATURAL JOIN roles WHERE role_name LIKE 'Admin'))"
												+ "AS admin FROM users WHERE user_id = ? GROUP BY user_id", USER_MAPPER, id).stream().findFirst();
		if(opt.isPresent())
		{
			User u = opt.get();
			u.setBacklog(gameDao.getGamesInBacklog(u));
			u.setScores(scoreDao.findAllUserScores(u));
			u.setRuns(runDao.findAllUserRuns(u));
		}
		return opt;
	}
	
	@Override
	public Optional<User> findByUsername(String username)
	{
		return jdbcTemplate.query("SELECT *, bool_and(users.user_id in (SELECT user_id FROM role_assignments NATURAL JOIN roles WHERE role_name LIKE 'Admin'))"
								+ "AS admin FROM users WHERE username LIKE ? GROUP BY user_id", USER_MAPPER, username).stream().findFirst();
	}
	
	@Override
	public Optional<User> findByUsernameWithDetails(String username)
	{
		Optional<User> opt = jdbcTemplate.query("SELECT *, bool_and(users.user_id in (SELECT user_id FROM role_assignments NATURAL JOIN roles WHERE role_name LIKE 'Admin'))"
												+ "AS admin FROM users WHERE username LIKE ? GROUP BY user_id", USER_MAPPER, username).stream().findFirst();
		if(opt.isPresent())
		{
			User u = opt.get();
			u.setBacklog(gameDao.getGamesInBacklog(u));
			u.setScores(scoreDao.findAllUserScores(u));
			u.setRuns(runDao.findAllUserRuns(u));
		}
		return opt;
	}
	
	@Override
	public Optional<User> findByEmail(String email)
	{
		return jdbcTemplate.query("SELECT *, bool_and(users.user_id in (SELECT user_id FROM role_assignments NATURAL JOIN roles WHERE role_name LIKE 'Admin'))"
								+ "AS admin FROM users WHERE email LIKE ? GROUP BY user_id", USER_MAPPER, email).stream().findFirst();
	}

	@Override
	public Optional<User> findByEmailWithDetails(String email)
	{
		Optional<User> opt = jdbcTemplate.query("SELECT *, bool_and(users.user_id in (SELECT user_id FROM role_assignments NATURAL JOIN roles WHERE role_name LIKE 'Admin'))"
												+ "AS admin FROM users WHERE email LIKE ? GROUP BY user_id", USER_MAPPER, email).stream().findFirst();
		if(opt.isPresent())
		{
			User u = opt.get();
			u.setBacklog(gameDao.getGamesInBacklog(u));
			u.setScores(scoreDao.findAllUserScores(u));
			u.setRuns(runDao.findAllUserRuns(u));
		}
		return opt;
	}

	@Override
	public User create(String username, String password, String email)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("username", username); 
		args.put("password", password);
		args.put("email", email);
		final Number userId = jdbcInsert.executeAndReturnKey(args);
		User u = new User(userId.longValue(), username, password, email);
		return u;
	}
	
	@Override
	public List<User> getAllUsers()
	{
		return jdbcTemplate.query("SELECT *, bool_and(users.user_id IN (SELECT user_id FROM role_assignments NATURAL JOIN roles WHERE role_name LIKE 'Admin')) AS admin FROM users GROUP BY user_id", USER_MAPPER);
	}
}
