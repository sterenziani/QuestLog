package ar.edu.itba.paw.persistence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class PlatformJdbcDaoTest
{
	private	static final String GAME_TABLE = "games";
	private	static final String PLATFORM_TABLE = "platforms";
	private	static final String PLATFORM_NAME = "PlayStation 4";
	private	static final String PLATFORM_SHORT_NAME = "PS4";
	private	static final String PLATFORM_LOGO = "https://i.ytimg.com/vi/56NxUWEFpL0/maxresdefault.jpg";

	@Autowired
	private DataSource ds;
	
	@Autowired
	private PlatformJdbcDao platformDao;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	
	@Before
	public void	setUp()
	{
		platformDao = new PlatformJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(ds).withTableName(PLATFORM_TABLE).usingGeneratedKeyColumns("platform");
	}
	
	@Test
	public void	testRegisterPlatform()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		final Platform p = platformDao.register(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO);
        Assert.assertNotNull(p);
        Assert.assertEquals(PLATFORM_NAME, p.getName());
        Assert.assertEquals(PLATFORM_SHORT_NAME, p.getShortName());
        Assert.assertEquals(PLATFORM_LOGO, p.getLogo());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PLATFORM_TABLE));
	}
	
	@Test
	public void	testFindPlatformByIdDoesntExist()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Optional<Platform> maybePlatform = platformDao.findById(1);
		Assert.assertFalse(maybePlatform.isPresent());
	}
	
	@Test
	public void	testFindPlatformByIdExists()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("platform_name", PLATFORM_NAME);
		args.put("platform_name_short", PLATFORM_SHORT_NAME);
		args.put("platform_logo", PLATFORM_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		
		Optional<Platform> maybePlatform = platformDao.findById(key.longValue());
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testFindPlatformByNameDoesntExist()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Optional<Platform> maybePlatform = platformDao.findByName(PLATFORM_NAME);
		Assert.assertFalse(maybePlatform.isPresent());
	}
	
	@Test
	public void	testFindPlatformByNameExists()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("platform_name", PLATFORM_NAME);
		args.put("platform_name_short", PLATFORM_SHORT_NAME);
		args.put("platform_logo", PLATFORM_LOGO);
		jdbcInsert.execute(args);
		
		Optional<Platform> maybePlatform = platformDao.findByName(PLATFORM_NAME);
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testChangePlatformName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("platform_name", PLATFORM_NAME);
		args.put("platform_name_short", PLATFORM_SHORT_NAME);
		args.put("platform_logo", PLATFORM_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		
		Optional<Platform> maybePlatform = platformDao.changeName(key.longValue(), "La Plei Cuatro");
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals("La Plei Cuatro", maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testChangePlatformShortName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("platform_name", PLATFORM_NAME);
		args.put("platform_name_short", PLATFORM_SHORT_NAME);
		args.put("platform_logo", PLATFORM_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		
		Optional<Platform> maybePlatform = platformDao.changeShortName(key.longValue(), "Play4");
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals("Play4", maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testChangeLogo()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("platform_name", PLATFORM_NAME);
		args.put("platform_name_short", PLATFORM_SHORT_NAME);
		args.put("platform_logo", PLATFORM_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		
		Optional<Platform> maybePlatform = platformDao.changeLogo(key.longValue(), "http://ps4.com/logo.png");
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals("http://ps4.com/logo.png", maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testGetAllPlatforms()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		final Map<String, Object> args1 = new HashMap<>();
		args1.put("platform_name", PLATFORM_NAME);
		args1.put("platform_name_short", PLATFORM_SHORT_NAME);
		args1.put("platform_logo", PLATFORM_LOGO);
		Number key1 = jdbcInsert.executeAndReturnKey(args1);
		final Map<String, Object> args2 = new HashMap<>();
		args2.put("platform_name", "Nintendo Switch");
		args2.put("platform_name_short", "NS");
		args2.put("platform_logo", "http://nintendo.com/switch.png");
		Number key2 = jdbcInsert.executeAndReturnKey(args2);
		
		Platform ps4 = new Platform(key1.longValue(), PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO);
		Platform ns = new Platform(key2.longValue(), "Nintendo Switch", "NS", "http://nintendo.com/switch.png");
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ps4);
		myList.add(ns);
		
		List<Platform> platformList = platformDao.getAllPlatforms();
		Assert.assertFalse(platformList.isEmpty());
		Assert.assertEquals(2, platformList.size());
		Assert.assertEquals(platformList.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList.get(1).getName(), myList.get(1).getName());
	}
	
	@Test
	public void	testGetAllGamesByPlatform()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("platform_name", PLATFORM_NAME);
		args.put("platform_name_short", PLATFORM_SHORT_NAME);
		args.put("platform_logo", PLATFORM_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		Platform p = new Platform(key.longValue(), PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO);
		SimpleJdbcInsert jdbcInsertGames = new SimpleJdbcInsert(ds).withTableName(GAME_TABLE).usingGeneratedKeyColumns("game");
		SimpleJdbcInsert jdbcInsertVersions = new SimpleJdbcInsert(ds).withTableName("game_versions");
		
		// Insert Mario
		final Map<String, Object> gameArgs = new HashMap<>();
		gameArgs.put("title", "Mario");
		gameArgs.put("cover", "http://nintendo.com/mario.png");
		gameArgs.put("description", "A game with Mario");
		Number gameKey = jdbcInsertGames.executeAndReturnKey(gameArgs);
		Game g1 = new Game(gameKey.longValue(), "Mario", "http://nintendo.com/mario.png", "A game with Mario");
		// Say Mario is on the platform
		final Map<String, Object> versionArgs = new HashMap<>();
		versionArgs.put("game", gameKey.longValue());
		versionArgs.put("platform", key.longValue());
		jdbcInsertVersions.execute(versionArgs);
		
		final Map<String, Object> gameArgs2 = new HashMap<>();
		gameArgs2.put("title", "Zelda");
		gameArgs2.put("cover", "http://nintendo.com/zelda.png");
		gameArgs2.put("description", "A game with Link");
		Number gameKey2 = jdbcInsertGames.executeAndReturnKey(gameArgs2);
		Game g2 = new Game(gameKey2.longValue(), "Zelda", "http://nintendo.com/zelda.png", "A game with Link");
		final Map<String, Object> versionArgs2 = new HashMap<>();
		versionArgs2.put("game", gameKey2.longValue());
		versionArgs2.put("platform", key.longValue());
		jdbcInsertVersions.execute(versionArgs2);
		
		final Map<String, Object> gameArgs3 = new HashMap<>();
		gameArgs3.put("title", "Sonic");
		gameArgs3.put("cover", "http://sega.com/sonic.png");
		gameArgs3.put("description", "A game with Sonic");
		jdbcInsertGames.execute(gameArgs3);
		// Let's say this one is not on this platform
		
		List<Game> gamesList = platformDao.getAllGames(p);
		List<Game> myList = new ArrayList<Game>();
		myList.add(g1);
		myList.add(g2);
		
		Assert.assertNotNull(gamesList);
		Assert.assertEquals(myList.size(), gamesList.size());
		Assert.assertEquals(myList.get(0).getTitle(), gamesList.get(0).getTitle());
		Assert.assertEquals(myList.get(1).getTitle(), gamesList.get(1).getTitle());
	}
}
