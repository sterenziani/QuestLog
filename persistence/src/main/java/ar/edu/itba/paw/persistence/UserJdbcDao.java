package ar.edu.itba.paw.persistence;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.model.PasswordResetToken;
import ar.edu.itba.paw.model.User;

//@Repository
public class UserJdbcDao implements UserDao
{
	
	private	final SimpleJdbcInsert jdbcInsert;
	private JdbcTemplate jdbcTemplate;
	protected static final RowMapper<User> USER_MAPPER = new RowMapper<User>()
	{
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			User u = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("locale"));
			u.setAdminStatus(rs.getBoolean("admin"));
			return u;
		}
	};
	protected static final RowMapper<PasswordResetToken> TOKEN_MAPPER = new RowMapper<PasswordResetToken>()
	{
		@Override
		public PasswordResetToken mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			PasswordResetToken t = new PasswordResetToken(rs.getString("token"), new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("locale")), rs.getDate("expiration"));
			return t;
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
		return jdbcTemplate.query("SELECT user_id, username, password, email, locale, exists(SELECT user_id FROM role_assignments NATURAL JOIN roles WHERE role_name LIKE 'Admin' AND user_id = ?) "
				+ "AS admin FROM users WHERE user_id = ?", USER_MAPPER, id, id).stream().findFirst();
	}
	
	@Override
	public Optional<User> findByUsername(String username)
	{
		return jdbcTemplate.query("SELECT user_id, username, password, email, locale, exists(SELECT user_id FROM role_assignments NATURAL JOIN roles NATURAL JOIN users WHERE role_name LIKE 'Admin'  AND username = ?)"
								+" AS admin FROM users WHERE username = ?", USER_MAPPER, username, username).stream().findFirst();
	}
	
	@Override
	public Optional<User> findByEmail(String email)
	{
		return jdbcTemplate.query("SELECT user_id, username, password, email, locale, exists(SELECT user_id FROM role_assignments NATURAL JOIN roles NATURAL JOIN users WHERE role_name LIKE 'Admin'  AND email = ?)" 
								+" AS admin FROM users WHERE email = ?", USER_MAPPER, email, email).stream().findFirst();
	}

	@Override
	public User create(String username, String password, String email, Locale locale)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("username", username); 
		args.put("password", password);
		args.put("email", email);
		args.put("locale", locale.toLanguageTag());
		final Number userId = jdbcInsert.executeAndReturnKey(args);
		User u = new User(userId.longValue(), username, password, email, locale.toLanguageTag());
		return u;
	}
	
	@Override
	public List<User> getAllUsers()
	{
		return jdbcTemplate.query("SELECT user_id, username, password, email, locale, exists(SELECT user_id FROM role_assignments NATURAL JOIN roles WHERE role_name LIKE 'Admin' AND user_id = users.user_id) AS admin FROM users", USER_MAPPER);
	}

	@Override
	public void saveToken(PasswordResetToken token)
	{
		jdbcTemplate.update("DELETE FROM tokens WHERE user_id = ?", token.getUser().getId());
		jdbcTemplate.update("INSERT INTO tokens(user_id, token, expiration) VALUES(?, ?, CURRENT_DATE + 2)", token.getUser().getId(), token.getToken());
	}
	
	@Override
	public Optional<PasswordResetToken> findTokenByToken(String token)
	{
		return jdbcTemplate.query("SELECT * FROM tokens NATURAL JOIN users WHERE token = ?", TOKEN_MAPPER, token).stream().findFirst();
	}
	
	@Override
	public Optional<User> findUserByToken(String token)
	{
		return jdbcTemplate.query("SELECT user_id, username, password, email, locale, exists(SELECT user_id FROM role_assignments NATURAL JOIN roles WHERE role_name LIKE 'Admin' AND user_id = a.user_id) AS admin FROM "
									+"(SELECT * FROM tokens NATURAL JOIN users WHERE token = ?) AS a", USER_MAPPER, token).stream().findFirst();
	}

	@Override
	public void changePassword(User user, String password)
	{
		jdbcTemplate.update("UPDATE users SET password = ? WHERE user_id = ?", password, user.getId());
	}
	
	@Override
	public void deleteTokenForUser(User u)
	{
		jdbcTemplate.update("DELETE FROM tokens WHERE user_id = ?", u.getId());
	}

	@Override
	public void updateLocale(User user, Locale locale)
	{
		jdbcTemplate.update("UPDATE users SET locale = ? WHERE user_id = ?", locale.toLanguageTag(), user.getId());
	}
	
	@Override
	public int countUserSearchResults(String searchTerm)
	{
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE LOWER(username) LIKE LOWER(CONCAT('%',?,'%'))", Integer.class, searchTerm);
	}

	@Override
	public List<User> searchByUsernamePaged(String searchTerm, int page, int pageSize) {
		return jdbcTemplate.query("SELECT user_id, username, password, email, locale, exists(SELECT user_id FROM role_assignments NATURAL JOIN roles WHERE role_name LIKE 'Admin' AND user_id = users.user_id) AS admin FROM users WHERE LOWER(username) LIKE LOWER(CONCAT('%',?,'%')) LIMIT ? OFFSET ?", 
				USER_MAPPER, searchTerm, pageSize, (page-1)*pageSize);
	}

	@Override
	public void addAdmin(User u) {
		jdbcTemplate.update("INSERT INTO role_assignments(user_id,role) VALUES(?, (SELECT role FROM roles WHERE role_name = 'Admin'))", u.getId());	
	}

	@Override
	public void removeAdmin(User u) {
		jdbcTemplate.update("DELETE FROM role_assignments WHERE user_id = ?", u.getId());	
	}
}