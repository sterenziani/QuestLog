/*package ar.edu.itba.paw.persistence;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Review;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class ReviewJdbcDaoTest {
	
	private static final String REVIEW_TABLE = "reviews";
	private	static final String PLATFORM_TABLE = "platforms";
	private static final String GAME_TABLE = "games";
	private	static final String USER_TABLE = "users";
	private static final int SCORE = 87;
	private static final String GAME_TITLE = "Example Game";
	private static final String GAME_COVER = "http://sega.com/game.jpg";
	private static final String GAME_DESC = "Explore the world!";
	private	static final String PLATFORM_NAME = "Wii";
	private	static final String PLATFORM_SHORT_NAME = "Wii";
	private	static final String PLATFORM_LOGO = "https://nintendo.com/wii.jpg";
	private	static final String USERNAME = "Username";
	private	static final String PASSWORD = "password";
	private	static final String EMAIL = "email@example.com";
	private static final String LOCALE = "en";
	private static final String BODY = "This game is pathetic, terrible";
	private static final Date DATE_STAMP = new Date(20101003);
	
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private ReviewJdbcDao reviewDao;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert reviewInsert;
	private SimpleJdbcInsert userInsert;
	private SimpleJdbcInsert gameInsert;
	private SimpleJdbcInsert platformInsert;


	@Before
	public void	setUp()
	{
		reviewDao = new ReviewJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		reviewInsert = new SimpleJdbcInsert(ds).withTableName(REVIEW_TABLE).usingGeneratedKeyColumns("review");
		JdbcTestUtils.deleteFromTables(jdbcTemplate, REVIEW_TABLE);

		gameInsert  = new SimpleJdbcInsert(ds).withTableName(GAME_TABLE).usingGeneratedKeyColumns("game");
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);

		userInsert  = new SimpleJdbcInsert(ds).withTableName(USER_TABLE).usingGeneratedKeyColumns("user");
		JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
		
		platformInsert = new SimpleJdbcInsert(ds).withTableName(PLATFORM_TABLE).usingGeneratedKeyColumns("platform");
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
	}
	
	@Test
	public void	testFindReviewByIdExists()
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		User user = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		Platform platform = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Review r = TestMethods.addReview(user, game, platform, SCORE, BODY, DATE_STAMP, reviewInsert);
		Optional<Review> maybeRev = reviewDao.findReviewById(r.getId());
		Assert.assertTrue(maybeRev.isPresent());
		Assert.assertEquals(game.getId(), maybeRev.get().getGame().getId());
		Assert.assertEquals(user.getId(), maybeRev.get().getUser().getId());
		Assert.assertEquals(platform.getId(), maybeRev.get().getPlatform().getId());
		Assert.assertEquals(r.getId(), maybeRev.get().getId());
	}
	
	
	@Test
	public void testRegisterReview()
	{	Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		final Review r = reviewDao.register(u, g, p, SCORE, BODY, DATE_STAMP);
		Assert.assertNotNull(r);
		Assert.assertEquals(u.getId(), r.getUser().getId());
		Assert.assertEquals(g.getId(), r.getGame().getId());
		Assert.assertEquals(p.getId(), r.getPlatform().getId());
		Assert.assertEquals(SCORE, r.getScore());
		Assert.assertEquals(BODY, r.getBody());
		Assert.assertEquals(DATE_STAMP, r.getPostDate());
	}
	
	@Test
	public void testFindAllReviews()
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, gameInsert);
		User u1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		User u2 = TestMethods.addUser("Juan1937", PASSWORD, EMAIL+"1", LOCALE, userInsert);
		Platform p1 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform p2 = TestMethods.addPlatform("Nintendo Switch", "Switch", PLATFORM_LOGO, platformInsert);
		Review r1 = TestMethods.addReview(u1, g1, p1, SCORE, BODY, DATE_STAMP, reviewInsert);
		Review r2 = TestMethods.addReview(u2, g2, p2, SCORE, BODY, DATE_STAMP, reviewInsert);
		
		List<Review> revs = reviewDao.getAllReviews();
		List<Review> myList = new ArrayList<Review>();
		myList.add(r1);
		myList.add(r2);
		
		Assert.assertFalse(revs.isEmpty());
		Assert.assertEquals(2, revs.size());
		Assert.assertEquals(revs.get(0).getUser().getId(), myList.get(0).getUser().getId());
		Assert.assertEquals(revs.get(0).getGame().getTitle(), myList.get(0).getGame().getTitle());
		Assert.assertEquals(revs.get(0).getPlatform().getId(), myList.get(0).getPlatform().getId());
		Assert.assertNotEquals(revs.get(0).getGame().getTitle(), myList.get(1).getGame().getTitle());
		Assert.assertNotEquals(revs.get(0).getPlatform().getName(), myList.get(1).getPlatform().getName());
		Assert.assertNotEquals(revs.get(0).getUser().getUsername(), myList.get(1).getUser().getUsername());
	}
	
	@Test
	public void findUserReviews() {
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, gameInsert);
		Platform p1 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform p2 = TestMethods.addPlatform("Nintendo Switch", "Switch", PLATFORM_LOGO, platformInsert);
		User u1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		User u2 = TestMethods.addUser("Juan1937", PASSWORD, EMAIL+"1", LOCALE, userInsert);
		Review r1 = TestMethods.addReview(u1, g1, p1, SCORE, BODY, DATE_STAMP, reviewInsert);
		Review r2 = TestMethods.addReview(u1, g2, p2, SCORE, BODY, DATE_STAMP, reviewInsert);
		Review r3 = TestMethods.addReview(u2, g2, p2, SCORE, BODY, DATE_STAMP, reviewInsert);

		
		List<Review> revs = reviewDao.findUserReviews(u1);
		List<Review> myList = new ArrayList<Review>();
		myList.add(r1);
		myList.add(r2);
		myList.add(r3);
		
		Assert.assertFalse(revs.isEmpty());
		Assert.assertEquals(2, revs.size());
		Assert.assertEquals(revs.get(0).getUser().getId(), myList.get(0).getUser().getId());
		Assert.assertEquals(revs.get(0).getUser().getId(), revs.get(1).getUser().getId());
		Assert.assertEquals(revs.get(0).getGame().getTitle(), myList.get(0).getGame().getTitle());
		Assert.assertEquals(revs.get(0).getPlatform().getId(), myList.get(0).getPlatform().getId());
		Assert.assertNotEquals(revs.get(0).getGame().getTitle(), myList.get(1).getGame().getTitle());

	}
	
	
}
*/