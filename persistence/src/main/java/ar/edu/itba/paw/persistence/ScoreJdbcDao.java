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
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.dao.ScoreDao;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Score;
import ar.edu.itba.paw.model.User;

//@Repository
public class ScoreJdbcDao implements ScoreDao{
	
	private JdbcTemplate jdbcScoreTemplate;
	
	protected static final RowMapper<Score> SCORE_MAPPER = new RowMapper<Score>() {
		
		@Override
		public Score mapRow(ResultSet rs, int rowNum) throws SQLException
		{	Game game = new Game(rs.getLong("game"), null, null, null);
			User user = new User(rs.getLong("user_id"), null, null, null, "en");
			return new Score(user, game, rs.getInt("score"));
		}
	};
	
	@Autowired
	public ScoreJdbcDao(final DataSource ds)
	{
	    jdbcScoreTemplate = new JdbcTemplate(ds);
	}
	

	@Override
	public Optional<Score> findScore(User user, Game game) {
		Optional <Score> score = jdbcScoreTemplate.query("SELECT * FROM scores WHERE user_id = ? AND game = ?", SCORE_MAPPER, user.getId(), game.getId()).stream().findFirst();
		if(score.isPresent()) {
			score.get().setGame(game);
			score.get().setUser(user);
			}
		return score;
	}

	@Override
	public Optional<Score> changeScore(int new_score, User user, Game game) {
		jdbcScoreTemplate.update("UPDATE scores SET score = ? WHERE user_id = ? AND game = ?", new_score, user.getId(), game.getId());
		return findScore(user, game);
	}
	
	@Override
	public Integer findAverageScore(Game game) {
		Integer average = jdbcScoreTemplate.queryForObject("SELECT AVG(score) FROM scores WHERE game = ?",Integer.class, game.getId());
		return average;
	}
	
	@Override
	public Score register(User user, Game game, int score) {
		final Map<String, Object> args = new HashMap<>();
		args.put("user_id", user.getId()); 
		args.put("game", game.getId()); 
		args.put("score", score);
		jdbcScoreTemplate.update("INSERT INTO scores (user_id,game,score) VALUES (?,?,?)", user.getId(), game.getId(), score);
		return new Score(user, game, score);
	}
	

	@Override
	public List<Score> getAllScores() {
		List<Score> score = jdbcScoreTemplate.query("SELECT * FROM scores", SCORE_MAPPER);
		for(Score s: score) {
			Optional <Game> game = getGame(s.getGame().getId());
			Optional <User> user = getUser(s.getUser().getId());
			if(game.isPresent() && user.isPresent()) {
				s.setGame(game.get());
				s.setUser(user.get());
			}
		}
		return score;
	}

	private Optional<Game> getGame(long id)
	{
		return jdbcScoreTemplate.query("SELECT * FROM (SELECT * FROM scores WHERE game = ?) AS g NATURAL JOIN games",
				new RowMapper<Game>()
				{
					@Override
					public Game mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						return new Game(rs.getInt("game"), rs.getString("title"), rs.getString("cover"), rs.getString("description"));
					}
				}, id).stream().findFirst();
	}
	
	private Optional<User> getUser(long id)
	{
		Optional <User> user = jdbcScoreTemplate.query("SELECT user_id, username, password, email, locale, exists(SELECT user_id FROM role_assignments NATURAL JOIN roles WHERE role_name LIKE 'Admin' AND user_id = ?) AS admin FROM users WHERE user_id = ?",
				new RowMapper<User>()
				{
					@Override
					public User mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						User u = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("locale"));
						u.setAdminStatus(rs.getBoolean("admin"));
						return u;
					}
				}, id, id).stream().findFirst();
		return user;
	}
	
	@Override
	public List<Score> findAllUserScores(User user)
	{
		List<Score> scores = jdbcScoreTemplate.query("SELECT * FROM scores WHERE user_id = ?", SCORE_MAPPER, user.getId());
		for(Score s : scores)
		{
			Optional <Game> game = getGame(s.getGame().getId());
			if(game.isPresent())
			{
				s.setGame(game.get());
				s.setUser(user);
			}
		}
		return scores;
	}

	@Override
	public List<Score> findAllUserScores(User user, int page, int pageSize)
	{
		List<Score> scores = jdbcScoreTemplate.query("SELECT * FROM scores WHERE user_id = ? LIMIT ? OFFSET ?", SCORE_MAPPER, user.getId(), pageSize, (page-1)*pageSize);
		for(Score s: scores)
		{
			Optional <Game> game = getGame(s.getGame().getId());
			if(game.isPresent())
			{
				s.setGame(game.get());
				s.setUser(user);
			}
		}
		return scores;
	}


	@Override
	public int countAllUserScores(User user)
	{
		return jdbcScoreTemplate.queryForObject("SELECT count(*) FROM scores WHERE user_id = ?", Integer.class, user.getId());
	}

}