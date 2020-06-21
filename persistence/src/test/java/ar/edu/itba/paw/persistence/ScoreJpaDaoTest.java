package ar.edu.itba.paw.persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Score;
import ar.edu.itba.paw.model.entity.User;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ScoreJpaDaoTest {
	
	private static final String SCORE_TABLE = "scores";
	private static final String GAME_TABLE = "games";
	private	static final String USER_TABLE = "users";
	private static final String ROLES_TABLE = "roles";
	private static final int SCORE = 87;
	private static final String GAME_TITLE = "Example Game";
	private static final String GAME_COVER = "http://sega.com/game.jpg";
	private static final String GAME_DESC = "Explore the world!";
    private static final String GAME_TRAILER = "DpHDJRGuL7w";
	private	static final String USERNAME = "Username";
	private	static final String PASSWORD = "password";
	private	static final String EMAIL = "email@example.com";
	private static final String LOCALE = "en";
	
	@PersistenceContext
    private EntityManager em;
	
	@Autowired
	private DataSource ds;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ScoreJpaDao scoreDao;
	
	@Before
	public void	setUp()
	{
        jdbcTemplate = new JdbcTemplate(ds);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, SCORE_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
		TestMethods.addRole("Admin", em);
	}
	
	@Test
	public void testRegisterScore()
	{	Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, em);
		final Score s = scoreDao.register(u, g, SCORE);
		Assert.assertNotNull(s);
		Assert.assertEquals(u.getId(), s.getUser().getId());
		Assert.assertEquals(g.getId(), s.getGame().getId());
		Assert.assertEquals(SCORE, s.getScore());
	}
	
	@Test
	public void	testFindScoreExists()
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		User user = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, em);
		Score s = TestMethods.addScore(user, game, SCORE, em);
		Optional<Score> maybeScore = scoreDao.findScore(user, game);
		Assert.assertTrue(maybeScore.isPresent());
		Assert.assertEquals(game.getId(), maybeScore.get().getGame().getId());
		Assert.assertEquals(user.getId(), maybeScore.get().getUser().getId());
		Assert.assertEquals(s.getScore(), maybeScore.get().getScore());
	}
	
	@Test
	public void	testFindScoreNotExists()
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		User user = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, em);
		Optional<Score> maybeScore = scoreDao.findScore(user, game);
		Assert.assertFalse(maybeScore.isPresent());
	}
	
	@Test
	public void testFindAverageScore()
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		User user1 = TestMethods.addUser("user 1", PASSWORD, EMAIL, LOCALE, em);
		User user2 = TestMethods.addUser("user 2", PASSWORD, EMAIL+"1", LOCALE, em);
		User user3 = TestMethods.addUser("user 3", PASSWORD, EMAIL+"2", LOCALE, em);
		TestMethods.addScore(user1, game, 55, em);
		TestMethods.addScore(user2, game, 23, em);
		TestMethods.addScore(user3, game, 30, em);
		int avScore = scoreDao.findAverageScore(game);
		Assert.assertEquals(avScore, (55 + 23 + 30)/3);
	}
	
	@Test
	public void testGetAllScores() 
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		Game game2 = TestMethods.addGame("Zelda 27", GAME_COVER, GAME_DESC, GAME_TRAILER, em);		
		User user2 = TestMethods.addUser("pepito", PASSWORD, EMAIL, LOCALE, em);
		User user3 = TestMethods.addUser("miguelito", PASSWORD, EMAIL+"1", LOCALE, em);
		Score s2 = TestMethods.addScore(user2, game, 23, em);
		Score s3 = TestMethods.addScore(user3, game, 30, em);
		Score s4 = TestMethods.addScore(user2, game2, 30, em);
		List<Score> scores = scoreDao.getAllScores();
		List<Score> myList = new ArrayList<Score>();
		myList.add(s2);
		myList.add(s3);
		myList.add(s4);
		
		Assert.assertFalse(scores.isEmpty());
		Assert.assertEquals(3, scores.size());
		Assert.assertEquals(scores.get(0).getUser().getId(), myList.get(0).getUser().getId());
		Assert.assertEquals(scores.get(0).getGame().getTitle(), myList.get(0).getGame().getTitle());
		Assert.assertEquals(scores.get(2).getGame().getTitle(), myList.get(2).getGame().getTitle());
		Assert.assertNotEquals(scores.get(0).getGame().getTitle(), myList.get(2).getGame().getTitle());
		Assert.assertEquals(scores.get(2).getUser().getUsername(), myList.get(2).getUser().getUsername());
	}
	
	@Test
	public void testFindAllUserScoresPaginated()
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		Game game2 = TestMethods.addGame("Zelda 27", GAME_COVER, GAME_DESC, GAME_TRAILER, em);	
		Game game3 = TestMethods.addGame("Zelda 28", GAME_COVER, GAME_DESC, GAME_TRAILER, em);	
		User user = TestMethods.addUser("pepito", PASSWORD, EMAIL, LOCALE, em);
		Score s2 = TestMethods.addScore(user, game, 80, em);
		Score s3 = TestMethods.addScore(user, game2, 30, em);
		Score s4 = TestMethods.addScore(user, game3, 30, em);
		List<Score> scores = scoreDao.findAllUserScores(user, 1, 2);
		List<Score> myList = new ArrayList<Score>();
		myList.add(s2);
		myList.add(s3);
		
		Assert.assertFalse(scores.isEmpty());
		Assert.assertEquals(2, scores.size());
		Assert.assertEquals(scores.get(0).getUser().getId(), myList.get(0).getUser().getId());
		Assert.assertEquals(scores.get(0).getGame().getTitle(), myList.get(0).getGame().getTitle());
		Assert.assertEquals(scores.get(1).getGame().getTitle(), myList.get(1).getGame().getTitle());
		Assert.assertNotEquals(scores.get(0).getGame().getTitle(), myList.get(1).getGame().getTitle());
		Assert.assertFalse(scores.contains(s4));
	}
	
	@Test
	public void testCountAllUserScores()
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		Game game2 = TestMethods.addGame("Zelda 27", GAME_COVER, GAME_DESC, GAME_TRAILER, em);	
		Game game3 = TestMethods.addGame("Zelda 28", GAME_COVER, GAME_DESC, GAME_TRAILER, em);	
		User user = TestMethods.addUser("pepito", PASSWORD, EMAIL, LOCALE, em);
		Score s2 = TestMethods.addScore(user, game, 80, em);
		Score s3 = TestMethods.addScore(user, game2, 30, em);
		Score s4 = TestMethods.addScore(user, game3, 30, em);
		List<Score> myList = new ArrayList<Score>();
		myList.add(s2);
		myList.add(s3);
		myList.add(s4);
		int count = scoreDao.countAllUserScores(user);
		Assert.assertEquals(count, myList.size());
	}
}