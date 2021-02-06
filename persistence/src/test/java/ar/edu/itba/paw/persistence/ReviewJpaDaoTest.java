package ar.edu.itba.paw.persistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Platform;
import ar.edu.itba.paw.model.entity.Review;
import ar.edu.itba.paw.model.entity.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ReviewJpaDaoTest {
	
	private static final String REVIEW_TABLE = "reviews";
	private	static final String PLATFORM_TABLE = "platforms";
	private static final String GAME_TABLE = "games";
	private	static final String USER_TABLE = "users";
	private static final int SCORE = 87;
	private static final String GAME_TITLE = "Example Game";
	private static final String GAME_COVER = "http://sega.com/game.jpg";
	private static final String GAME_DESC = "Explore the world!";
    private static final String GAME_TRAILER = "DpHDJRGuL7w";
	private	static final String PLATFORM_NAME = "Wii";
	private	static final String PLATFORM_SHORT_NAME = "Wii";
	private	static final String PLATFORM_LOGO = "https://nintendo.com/wii.jpg";
	private	static final String USERNAME = "Username";
	private	static final String PASSWORD = "password";
	private	static final String EMAIL = "email@example.com";
	private static final String LOCALE = "en";
	private static final List<String> BODY = Arrays.asList("This game is pathetic, terrible", "Worst game ever!");
	private static final LocalDate DATE_STAMP = LocalDate.of(1970, 8, 3);
	
	@PersistenceContext
    private EntityManager em;
	
	@Autowired
	private ReviewJpaDao reviewDao;

    @Before
	public void	setUp()
	{
		TestMethods.deleteFromTable(REVIEW_TABLE, em);
		TestMethods.deleteFromTable(GAME_TABLE, em);
		TestMethods.deleteFromTable(USER_TABLE, em);
		TestMethods.deleteFromTable(PLATFORM_TABLE, em);
	}
	
	@Test
	public void	testFindReviewByIdExists()
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		User user = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, em);
		Platform platform = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Review r = TestMethods.addReview(user, game, platform, SCORE, BODY, DATE_STAMP, em);
		Optional<Review> maybeRev = reviewDao.findReviewById(r.getId());
		Assert.assertTrue(maybeRev.isPresent());
		Assert.assertEquals(game.getId(), maybeRev.get().getGame().getId());
		Assert.assertEquals(user.getId(), maybeRev.get().getUser().getId());
		Assert.assertEquals(platform.getId(), maybeRev.get().getPlatform().getId());
		Assert.assertEquals(r.getId(), maybeRev.get().getId());
	}
	
	
	@Test
	public void testRegisterReview()
	{	Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, em);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
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
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		User u1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, em);
		User u2 = TestMethods.addUser("Juan1937", PASSWORD, EMAIL+"1", LOCALE, em);
		Platform p1 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Platform p2 = TestMethods.addPlatform("Nintendo Switch", "Switch", PLATFORM_LOGO, em);
		Review r1 = TestMethods.addReview(u1, g1, p1, SCORE, BODY, DATE_STAMP, em);
		Review r2 = TestMethods.addReview(u2, g2, p2, SCORE, BODY, DATE_STAMP, em);
		
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
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		Platform p1 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Platform p2 = TestMethods.addPlatform("Nintendo Switch", "Switch", PLATFORM_LOGO, em);
		User u1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, em);
		User u2 = TestMethods.addUser("Juan1937", PASSWORD, EMAIL+"1", LOCALE, em);
		Review r1 = TestMethods.addReview(u1, g1, p1, SCORE, BODY, DATE_STAMP, em);
		Review r2 = TestMethods.addReview(u1, g2, p2, SCORE, BODY, DATE_STAMP, em);
		Review r3 = TestMethods.addReview(u2, g2, p2, SCORE, BODY, DATE_STAMP, em);

		
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