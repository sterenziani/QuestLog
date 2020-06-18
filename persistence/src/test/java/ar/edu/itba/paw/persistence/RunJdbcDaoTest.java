/*package ar.edu.itba.paw.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Genre;
import ar.edu.itba.paw.model.entity.Platform;
import ar.edu.itba.paw.model.entity.Playstyle;
import ar.edu.itba.paw.model.entity.Review;
import ar.edu.itba.paw.model.entity.Run;
import ar.edu.itba.paw.model.entity.Score;
import ar.edu.itba.paw.model.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class RunJdbcDaoTest {
	private static final String PLAYSTYLE_TABLE = "playstyles";
	private	static final String PLATFORM_TABLE = "platforms";
	private static final String GAME_TABLE = "games";
	private	static final String USER_TABLE = "users";
	private static final String RUN_TABLE = "runs";
	private static final String ROLES_TABLE = "roles";
	private	static final String PLAYSTYLE_NAME = "100% completition";
	private	static final long TIME = 10000;
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

	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private RunJdbcDao runDao;
	private JdbcTemplate jdbcTemplate;
	
	private SimpleJdbcInsert runInsert;
	private SimpleJdbcInsert userInsert;
	private SimpleJdbcInsert gameInsert;
	private SimpleJdbcInsert playstyleInsert;
	private SimpleJdbcInsert platformInsert;
	private SimpleJdbcInsert roleInsert;



	@Before
	public void	setUp()
	{
		runDao = new RunJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		
		runInsert = new SimpleJdbcInsert(ds).withTableName(RUN_TABLE).usingGeneratedKeyColumns("run");
		JdbcTestUtils.deleteFromTables(jdbcTemplate, RUN_TABLE);
		
		gameInsert  = new SimpleJdbcInsert(ds).withTableName(GAME_TABLE).usingGeneratedKeyColumns("game");
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);

		userInsert  = new SimpleJdbcInsert(ds).withTableName(USER_TABLE).usingGeneratedKeyColumns("user");
		JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
		
		platformInsert = new SimpleJdbcInsert(ds).withTableName(PLATFORM_TABLE).usingGeneratedKeyColumns("platform");
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		
		playstyleInsert = new SimpleJdbcInsert(ds).withTableName(PLAYSTYLE_TABLE).usingGeneratedKeyColumns("playstyle");
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLAYSTYLE_TABLE);
		
		roleInsert = new SimpleJdbcInsert(ds).withTableName(ROLES_TABLE).usingGeneratedKeyColumns("role");
		JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLES_TABLE);

	}
	
	
	@Test
	public void	testFindRunByIdExists()
	{	
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		User user = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		Platform platform = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Playstyle playstyle = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		Run r = TestMethods.addRun(user, game, platform, playstyle, TIME, runInsert);
		Optional<Run> maybeRun = runDao.findRunById(r.getId());
		Assert.assertTrue(maybeRun.isPresent());
		Assert.assertEquals(game.getId(), maybeRun.get().getGame().getId());
		Assert.assertEquals(user.getId(), maybeRun.get().getUser().getId());
		Assert.assertEquals(platform.getId(), maybeRun.get().getPlatform().getId());
		Assert.assertEquals(r.getId(), maybeRun.get().getId());
	}
	
	@Test
	public void	testFindRunByIdNotExists()
	{	
		Optional<Run> maybeRun = runDao.findRunById(1);
		Assert.assertFalse(maybeRun.isPresent());
	}
	
	@Test
	public void testFindAllUserRuns()
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		User user1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		User user2 = TestMethods.addUser("user2", PASSWORD, "user2@gmail.com", LOCALE, userInsert);
		Platform platform = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Playstyle playstyle = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		Run r1 = TestMethods.addRun(user1, game, platform, playstyle, TIME, runInsert);
		Run r2 = TestMethods.addRun(user1, game, platform, playstyle, TIME, runInsert);
		Run r3 = TestMethods.addRun(user2, game, platform, playstyle, TIME, runInsert);
		
		List<Run> runs = runDao.findAllUserRuns(user1);
		List<Run> myList = new ArrayList<Run>();
		myList.add(r1);
		myList.add(r2);
		myList.add(r3);
		
		Assert.assertFalse(runs.isEmpty());
		Assert.assertEquals(2, runs.size());
		Assert.assertEquals(runs.get(0).getId(), myList.get(0).getId());
		Assert.assertEquals(runs.get(1).getId(), myList.get(1).getId());
	}
	
	@Test
	public void testFindRunsByUser()
	{
		Game game = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		User user1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		User user2 = TestMethods.addUser("user2", PASSWORD, "user2@gmail.com", LOCALE, userInsert);
		Platform platform = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Playstyle playstyle = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		Run r1 = TestMethods.addRun(user1, game, platform, playstyle, TIME, runInsert);
		Run r2 = TestMethods.addRun(user1, game, platform, playstyle, TIME, runInsert);
		Run r3 = TestMethods.addRun(user2, game, platform, playstyle, TIME, runInsert);
		Run r4 = TestMethods.addRun(user1, game, platform, playstyle, TIME, runInsert);

		
		List<Run> runs1 = runDao.findRunsByUser(user1, 1, 2);
		List<Run> runs2 = runDao.findRunsByUser(user1, 2, 2);
		List<Run> runs3 = runDao.findRunsByUser(user1, 1, 5);
	
		List<Run> myList = new ArrayList<Run>();
		myList.add(r1);
		myList.add(r2);
		myList.add(r3);
		myList.add(r4);

		Assert.assertFalse(runs1.isEmpty());
		Assert.assertFalse(runs2.isEmpty());
		Assert.assertFalse(runs3.isEmpty());

		Assert.assertEquals(2, runs1.size());
		Assert.assertEquals(1, runs2.size());
		Assert.assertEquals(3, runs3.size());

		Assert.assertEquals(runs1.get(0).getId(), myList.get(0).getId());
		Assert.assertEquals(runs1.get(1).getId(), myList.get(1).getId());
		
		Assert.assertEquals(runs2.get(0).getId(), myList.get(3).getId());
		
		Assert.assertEquals(runs3.get(0).getId(), myList.get(0).getId());
		Assert.assertEquals(runs3.get(1).getId(), myList.get(1).getId());
		Assert.assertEquals(runs3.get(2).getId(), myList.get(3).getId());
	}
	
	
	@Test
	public void testFindAllGameRuns() 
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, gameInsert);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		User u1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		Playstyle ps = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		Run r1 = TestMethods.addRun(u1, g1, p, ps, TIME, runInsert);
		Run r2 = TestMethods.addRun(u1, g1, p, ps, TIME, runInsert);
		Run r3 = TestMethods.addRun(u1, g2, p, ps, TIME, runInsert);
		
		List<Run> runs = runDao.findAllGameRuns(g1);
		List<Run> myList = new ArrayList<Run>();
		myList.add(r1);
		myList.add(r2);
		myList.add(r3);
		
		Assert.assertFalse(runs.isEmpty());
		Assert.assertEquals(2, runs.size());
		Assert.assertEquals(runs.get(0).getId(), myList.get(0).getId());
		Assert.assertEquals(runs.get(1).getId(), myList.get(1).getId());
	}
	
	@Test
	public void testGetAllRuns() 
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, gameInsert);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		User u1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		Playstyle ps = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		Run r1 = TestMethods.addRun(u1, g1, p, ps, TIME, runInsert);
		Run r2 = TestMethods.addRun(u1, g1, p, ps, TIME, runInsert);
		Run r3 = TestMethods.addRun(u1, g2, p, ps, TIME, runInsert);
		
		List<Run> runs = runDao.getAllRuns();
		List<Run> myList = new ArrayList<Run>();
		myList.add(r1);
		myList.add(r2);
		myList.add(r3);
		
		Assert.assertFalse(runs.isEmpty());
		Assert.assertEquals(3, runs.size());
		Assert.assertEquals(runs.get(0).getId(), myList.get(0).getId());
		Assert.assertEquals(runs.get(1).getId(), myList.get(1).getId());
		Assert.assertEquals(runs.get(2).getId(), myList.get(2).getId());

	}
	
	@Test
	public void testFindPlaystyleAndGameRun() 
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, gameInsert);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		User u1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		Playstyle ps1 = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		Playstyle ps2 = TestMethods.addPlaystyle("Main game", playstyleInsert);

		Run r1 = TestMethods.addRun(u1, g1, p, ps1, TIME, runInsert);
		Run r2 = TestMethods.addRun(u1, g1, p, ps2, TIME, runInsert);
		Run r3 = TestMethods.addRun(u1, g2, p, ps1, TIME, runInsert);
		Run r4 = TestMethods.addRun(u1, g1, p, ps1, TIME, runInsert);

		List<Run> runs = runDao.findPlaystyleAndGameRuns(g1, ps1);
		List<Run> myList = new ArrayList<Run>();
		myList.add(r1);
		myList.add(r2);
		myList.add(r3);
		myList.add(r4);

		Assert.assertFalse(runs.isEmpty());
		Assert.assertEquals(2, runs.size());
		Assert.assertEquals(runs.get(0).getId(), myList.get(0).getId());
		Assert.assertEquals(runs.get(1).getId(), myList.get(3).getId());

	}
	
	@Test
	public void testFindSpecificRuns() 
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, gameInsert);
		Platform p1 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform p2 = TestMethods.addPlatform("Nintendo DS", "NDS", PLATFORM_LOGO, platformInsert);
		User u1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		Playstyle ps1 = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		Playstyle ps2 = TestMethods.addPlaystyle("Main game", playstyleInsert);

		Run r1 = TestMethods.addRun(u1, g1, p1, ps1, TIME, runInsert);
		Run r2 = TestMethods.addRun(u1, g1, p1, ps2, TIME, runInsert);
		Run r3 = TestMethods.addRun(u1, g2, p1, ps1, TIME, runInsert);
		Run r4 = TestMethods.addRun(u1, g1, p2, ps1, TIME, runInsert);
		Run r5 = TestMethods.addRun(u1, g1, p1, ps1, TIME, runInsert);


		List<Run> runs = runDao.findSpecificRuns(g1, ps1, p1);
		List<Run> myList = new ArrayList<Run>();
		myList.add(r1);
		myList.add(r2);
		myList.add(r3);
		myList.add(r4);
		myList.add(r5);

		Assert.assertFalse(runs.isEmpty());
		Assert.assertEquals(2, runs.size());
		Assert.assertEquals(runs.get(0).getId(), myList.get(0).getId());
		Assert.assertEquals(runs.get(1).getId(), myList.get(4).getId());

	}
	
	@Test
	public void testRegisterRun()
	{	
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Playstyle ps = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		final Run r = runDao.register(u, g, p, ps, TIME);
		Assert.assertNotNull(r);
		Assert.assertEquals(u.getId(), r.getUser().getId());
		Assert.assertEquals(g.getId(), r.getGame().getId());
		Assert.assertEquals(p.getId(), r.getPlatform().getId());
		Assert.assertEquals(ps.getId(), r.getPlaystyle().getId());
		Assert.assertEquals(TIME, r.getTime());
	}
	
	@Test
	public void testFindGameRuns() 
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, gameInsert);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		User u1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, userInsert);
		User u2 = TestMethods.addUser("Juan1937", PASSWORD, EMAIL+"1", LOCALE, userInsert);
		Playstyle ps = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		Run r1 = TestMethods.addRun(u1, g1, p, ps, TIME, runInsert);
		Run r2 = TestMethods.addRun(u2, g1, p, ps, TIME, runInsert);
		Run r3 = TestMethods.addRun(u1, g2, p, ps, TIME, runInsert);
		Run r4 = TestMethods.addRun(u1, g1, p, ps, TIME, runInsert);
		
		List<Run> runs = runDao.findGameRuns(g1, u1);
		List<Run> myList = new ArrayList<Run>();
		myList.add(r1);
		myList.add(r2);
		myList.add(r3);
		myList.add(r4);
		
		Assert.assertFalse(runs.isEmpty());
		Assert.assertEquals(2, runs.size());
		Assert.assertEquals(runs.get(0).getUser().getId(), myList.get(0).getUser().getId());
		Assert.assertEquals(runs.get(0).getGame().getId(), myList.get(0).getGame().getId());
	}
	
	@Test
	public void testGetAveragePlatformPlaytime() 
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, gameInsert);
		Platform p1 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform p2 = TestMethods.addPlatform("Nintendo Switch", "Switch", PLATFORM_LOGO, platformInsert);
		User user1 = TestMethods.addUser("user 1", PASSWORD, EMAIL, LOCALE, userInsert);
		User user2 = TestMethods.addUser("user 2", PASSWORD, EMAIL+"1", LOCALE, userInsert);
		User user3 = TestMethods.addUser("user 3", PASSWORD, EMAIL+"2", LOCALE, userInsert);
		Playstyle ps = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		
		TestMethods.addRun(user1, g1, p1, ps, TIME, runInsert);
		TestMethods.addRun(user2, g1, p2, ps, TIME, runInsert);
		TestMethods.addRun(user3, g2, p1, ps, TIME, runInsert);
		TestMethods.addRun(user1, g1, p1, ps, TIME + 2, runInsert);
		
		long avTime = runDao.getAveragePlatformPlaytime(g1, p1);
		Assert.assertEquals(avTime, (TIME + TIME + 2)/2);
	}
	
	@Test
	public void testGetAveragePlatformPlaytimeNoRuns()
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Platform p1 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		
		long avTime = runDao.getAveragePlatformPlaytime(g1, p1);
		Assert.assertEquals(avTime, 0);



	}
	
	@Test
	public void testGetAveragePlaytime() 
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, gameInsert);
		Platform p1 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		User user1 = TestMethods.addUser("user 1", PASSWORD, EMAIL, LOCALE, userInsert);
		Playstyle ps = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		
		TestMethods.addRun(user1, g1, p1, ps, TIME, runInsert);
		TestMethods.addRun(user1, g1, p1, ps, TIME, runInsert);
		TestMethods.addRun(user1, g2, p1, ps, TIME, runInsert);
		TestMethods.addRun(user1, g1, p1, ps, TIME + 3, runInsert);
		
		long avTime = runDao.getAveragePlaytime(g1);
		Assert.assertEquals(avTime, (TIME + TIME + TIME + 3)/3);
	}
	
	@Test
	public void testGetAveragePlaytimeNoRuns() {
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		long avTime = runDao.getAveragePlaytime(g1);
		
		Assert.assertEquals(avTime, 0);
	}
	
	@Test
	public void testGetAveragePlaystylePlaytime() 
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, gameInsert);
		Platform p1 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		User user1 = TestMethods.addUser("user 1", PASSWORD, EMAIL, LOCALE, userInsert);
		Playstyle ps1 = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		Playstyle ps2 = TestMethods.addPlaystyle("Main Game", playstyleInsert);

		TestMethods.addRun(user1, g1, p1, ps1, TIME, runInsert);
		TestMethods.addRun(user1, g1, p1, ps2, TIME, runInsert);
		TestMethods.addRun(user1, g2, p1, ps1, TIME, runInsert);
		TestMethods.addRun(user1, g1, p1, ps1, TIME + 2, runInsert);
		
		long avTime = runDao.getAveragePlaystylePlaytime(g1, ps1);
		Assert.assertEquals(avTime, (TIME + TIME + 2)/2);
	}
	
	@Test
	public void testGetAveragePlaystylePlaytimeNoRuns() {
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Playstyle ps1 = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		long avTime = runDao.getAveragePlaystylePlaytime(g1, ps1);
		
		Assert.assertEquals(avTime, 0);
	}
	
	@Test
	public void testGetAverageSpecificPlaytime() 
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("Zelda 980", GAME_COVER, GAME_DESC, gameInsert);
		Platform p1 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform p2 = TestMethods.addPlatform("Nintendo Switch", "Switch", PLATFORM_LOGO, platformInsert);
		User user1 = TestMethods.addUser("user 1", PASSWORD, EMAIL, LOCALE, userInsert);
		Playstyle ps1 = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		Playstyle ps2 = TestMethods.addPlaystyle("Main Game", playstyleInsert);

		TestMethods.addRun(user1, g1, p1, ps1, TIME, runInsert);
		TestMethods.addRun(user1, g1, p1, ps2, TIME, runInsert);
		TestMethods.addRun(user1, g2, p1, ps1, TIME, runInsert);
		TestMethods.addRun(user1, g1, p1, ps1, TIME + 2, runInsert);
		TestMethods.addRun(user1, g1, p2, ps1, TIME, runInsert);
		TestMethods.addRun(user1, g1, p1, ps1, TIME + 2, runInsert);
		
		long avTime = runDao.getAverageSpecificPlaytime(g1, ps1, p1);
		Assert.assertEquals(avTime, (TIME + TIME + 2)/2);
	}
	
	@Test
	public void testGetAverageSpecificePlaytimeNoRuns() {
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Playstyle ps1 = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		long avTime = runDao.getAveragePlaystylePlaytime(g1, ps1);
		
		Assert.assertEquals(avTime, 0);
	}
	
	@Test
	public void testGetAverageSpecificPlatformPlaytimeNoRuns()
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Platform p1 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Playstyle ps1 = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		
		long avTime = runDao.getAverageSpecificPlaytime(g1, ps1, p1);
		
		Assert.assertEquals(avTime, 0);
	}
	
	@Test
	public void testCountRunsByUser() 
	{
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Platform p1 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		User user1 = TestMethods.addUser("user 1", PASSWORD, EMAIL, LOCALE, userInsert);
		User user2 = TestMethods.addUser("user 2", PASSWORD, EMAIL+"1", LOCALE, userInsert);
		Playstyle ps = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		
		TestMethods.addRun(user1, g1, p1, ps, TIME, runInsert);
		TestMethods.addRun(user1, g1, p1, ps, TIME, runInsert);
		TestMethods.addRun(user2, g1, p1, ps, TIME, runInsert);
		
		int count = runDao.countRunsByUser(user1);
		
		Assert.assertEquals(count, 2);
	}
	
	@Test
	public void testRegisterPlaystyle()
	{	
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLAYSTYLE_TABLE);
		
		final Playstyle ps = runDao.register(PLAYSTYLE_NAME);
		Assert.assertNotNull(ps);
		Assert.assertEquals(ps.getName(), PLAYSTYLE_NAME);
	}
	
	@Test
	public void testGetAllPlaystyles()
	{
		Playstyle ps1 = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		Playstyle ps2 = TestMethods.addPlaystyle("Main Story", playstyleInsert);
		
		List<Playstyle> plays = runDao.getAllPlaystyles();
		List<Playstyle> myList = new ArrayList<Playstyle>();
		myList.add(ps1);
		myList.add(ps2);

		Assert.assertFalse(plays.isEmpty());
		Assert.assertEquals(2, plays.size());
		Assert.assertEquals(plays.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(plays.get(0).getName(), myList.get(0).getName());
	}
	
	@Test
	public void	testChangePlaystyleName()
	{
		Playstyle ps = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		Optional<Playstyle> maybePs = runDao.changePlaystyleName("New Name", ps.getId());
		Assert.assertTrue(maybePs.isPresent());
		Assert.assertEquals("New Name", maybePs.get().getName());
	}
	
	@Test
	public void testFindPlaystyleById()
	{
		Playstyle ps = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		
		Optional<Playstyle> maybePs = runDao.findPlaystyleById(ps.getId());
		Assert.assertTrue(maybePs.isPresent());
		Assert.assertEquals(PLAYSTYLE_NAME, maybePs.get().getName());
		Assert.assertEquals(ps.getId(), maybePs.get().getId());
	}
	
	@Test
	public void testFindPlaystyleByName()
	{
		Playstyle ps = TestMethods.addPlaystyle(PLAYSTYLE_NAME, playstyleInsert);
		
		Optional<Playstyle> maybePs = runDao.findPlaystyleByName(ps.getName());
		Assert.assertTrue(maybePs.isPresent());
		Assert.assertEquals(PLAYSTYLE_NAME, maybePs.get().getName());
		Assert.assertEquals(ps.getId(), maybePs.get().getId());
	}
	
	
}
*/