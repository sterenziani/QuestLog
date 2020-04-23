package ar.edu.itba.paw.persistence;

import java.sql.Date;
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

import ar.edu.itba.paw.interfaces.ReviewDao;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Review;
import ar.edu.itba.paw.model.User;

@Repository
public class ReviewJdbcDao implements ReviewDao {


	private	final SimpleJdbcInsert jdbcReviewInsert;
	private JdbcTemplate jdbcReviewTemplate;

	protected static final RowMapper<Review> REVIEW_MAPPER = new RowMapper<Review>() {
		
		@Override
		public Review mapRow(ResultSet rs, int rowNum) throws SQLException
		{	Game game = new Game(rs.getLong("game"), null, null, null);
			User user = new User(rs.getLong("user_id"), null, null, null);
			Platform platform = new Platform(rs.getLong("platform"),null, null, null);
			return new Review(rs.getInt("review"), user, game, platform, rs.getInt("score"), rs.getString("body"), rs.getDate("post_date"));
		}
	};


	
	@Autowired
	public ReviewJdbcDao(final DataSource ds)
	{
	    jdbcReviewTemplate = new JdbcTemplate(ds);
	    jdbcReviewInsert = new SimpleJdbcInsert(jdbcReviewTemplate).withTableName("reviews").usingGeneratedKeyColumns("review");
	}


	@Override
	public Optional<Review> findReviewById(long reviewId) {
		Optional<Review> review = jdbcReviewTemplate.query("SELECT * FROM reviews WHERE review = ?", REVIEW_MAPPER, reviewId).stream().findFirst();
		if(review.isPresent()) {
			Optional <Game> game = getGame(review.get().getGame().getId());
			Optional <User> user = getUser(review.get().getUser().getId());
			Optional <Platform> platform = getPlatform(review.get().getPlatform().getId());
			if(game.isPresent() && user.isPresent() && platform.isPresent()) {
				review.get().setGame(game.get());
				review.get().setUser(user.get());
				review.get().setPlatform(platform.get());
			}
		}
		return review;
	}

	@Override
	public List<Review> findGameReviews(Game game) {
		List<Review> reviews = jdbcReviewTemplate.query("SELECT * FROM reviews WHERE game = ?", REVIEW_MAPPER, game.getId());
		for(Review r: reviews) {
			Optional <User> user = getUser(r.getUser().getId());
			Optional <Platform> platform = getPlatform(r.getPlatform().getId());
			if(user.isPresent() && platform.isPresent()) {
				r.setGame(game);
				r.setUser(user.get());
				r.setPlatform(platform.get());
			}
		}
		return reviews;
		
	}
	
	@Override
	public List<Review> findGameAndPlatformReviews(Game game, Platform platform) {
		List<Review> reviews = jdbcReviewTemplate.query("SELECT * FROM reviews WHERE platform = ? AND game = ?", REVIEW_MAPPER, platform.getId(), game.getId());
		for(Review r: reviews) {
			Optional <User> user = getUser(r.getUser().getId());
			if(user.isPresent()) {
				r.setGame(game);
				r.setUser(user.get());
				r.setPlatform(platform);
			}
		}
		return reviews;	
	}


	@Override
	public List<Review> findUserReviews(User user) {
		List<Review> reviews = jdbcReviewTemplate.query("SELECT * FROM reviews WHERE user_id = ?", REVIEW_MAPPER, user.getId());
		for(Review r: reviews) {
			Optional <Game> game = getGame(r.getGame().getId());
			Optional <Platform> platform = getPlatform(r.getPlatform().getId());
			if(game.isPresent() && platform.isPresent()) {
				r.setGame(game.get());
				r.setUser(user);
				r.setPlatform(platform.get());
			}
		}
		return reviews;
		
	}


	@Override
	public List<Review> findUserAndGameReviews(User user, Game game) {
		List<Review> reviews = jdbcReviewTemplate.query("SELECT * FROM reviews WHERE user_id = ? AND game = ?", REVIEW_MAPPER, user.getId(), game.getId());
		for(Review r: reviews) {
			Optional <Platform> platform = getPlatform(r.getPlatform().getId());
			if(platform.isPresent()) {
				r.setGame(game);
				r.setUser(user);
				r.setPlatform(platform.get());
			}
		}
		return reviews;
	}


	@Override
	public Optional<Review> changeReviewBody(long review, String body) {
		jdbcReviewTemplate.update("UPDATE reviews SET body = ? WHERE review = ?", body, review);
		return findReviewById(review);
	}


	@Override
	public List<Review> getAllReviews() {
		List<Review> reviews = jdbcReviewTemplate.query("SELECT * FROM reviews", REVIEW_MAPPER);
		for(Review r: reviews) {
			Optional <Game> game = getGame(r.getGame().getId());
			Optional <User> user = getUser(r.getUser().getId());
			Optional <Platform> platform = getPlatform(r.getPlatform().getId());
			if(game.isPresent() && user.isPresent() && platform.isPresent()) {
				r.setGame(game.get());
				r.setUser(user.get());
				r.setPlatform(platform.get());
			}
		}
		return reviews;
		
	}
	
	@Override
	public Review register(User user, Game game, Platform platform, int score, String body, Date date) {
		final Map<String, Object> args = new HashMap<>();
		args.put("user_id", user.getId()); 
		args.put("game", game.getId()); 
		args.put("platform", platform.getId());
		args.put("score", score);
		args.put("body", body);
		args.put("post_date", date);
		final Number reviewId = jdbcReviewInsert.executeAndReturnKey(args);
		return new Review(reviewId.longValue(), user, game, platform, score, body, date);

	}
	
	@Override
	public Optional<Game> getGame(long id)
	{
		return jdbcReviewTemplate.query("SELECT * FROM (SELECT * FROM reviews WHERE game = ?) AS g NATURAL JOIN games",
				new RowMapper<Game>()
				{
					@Override
					public Game mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						return new Game(rs.getInt("game"), rs.getString("title"), rs.getString("cover"), rs.getString("description"));
					}
				}, id).stream().findFirst();
	}
	
	@Override
	public Optional<User> getUser(long id)
	{
		Optional <User> user = jdbcReviewTemplate.query("SELECT * FROM (SELECT * FROM reviews WHERE user_id = ?) AS g NATURAL JOIN users",
				new RowMapper<User>()
				{
					@Override
					public User mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("email"));
					}
				}, id).stream().findFirst();
		return user;
	}
	
	@Override
	public Optional<Platform> getPlatform(long id)
	{
		Optional <Platform> platform = jdbcReviewTemplate.query("SELECT * FROM (SELECT * FROM reviews WHERE platform = ?) AS g NATURAL JOIN platforms",
				new RowMapper<Platform>()
				{
					@Override
					public Platform mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						return new Platform(rs.getInt("platform"), rs.getString("platform_name"), rs.getString("platform_name_short"), rs.getString("platform_logo"));
					}
				}, id).stream().findFirst();
		return platform;
	}

}
