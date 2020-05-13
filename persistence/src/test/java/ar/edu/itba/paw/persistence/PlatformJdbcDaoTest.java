package ar.edu.itba.paw.persistence;
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

import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Game;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class PlatformJdbcDaoTest
{
	private	static final String GAME_TABLE = "games";
	private static final String GAME_TITLE = "Example Game";
	private static final String GAME_COVER = "http://sega.com/game.jpg";
	private static final String GAME_DESC = "Explore the world!";
	private	static final String PLATFORM_TABLE = "platforms";
	private	static final String PLATFORM_NAME = "PlayStation 4";
	private	static final String PLATFORM_SHORT_NAME = "PS4";
	private	static final String PLATFORM_LOGO = "https://i.ytimg.com/vi/56NxUWEFpL0/maxresdefault.jpg";
	private static final String VERSION_TABLE = "game_versions";


	@Autowired
	private DataSource ds;
	
	@Autowired
	private PlatformJdbcDao platformDao;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert platformInsert;
	private SimpleJdbcInsert gameInsert;
	private SimpleJdbcInsert versionInsert;

	
	@Before
	public void	setUp()
	{
		platformDao = new PlatformJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		platformInsert = new SimpleJdbcInsert(ds).withTableName(PLATFORM_TABLE).usingGeneratedKeyColumns("platform");
		gameInsert = new SimpleJdbcInsert(ds).withTableName(GAME_TABLE).usingGeneratedKeyColumns("game");
		versionInsert = new SimpleJdbcInsert(ds).withTableName(VERSION_TABLE);
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
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Optional<Platform> maybePlatform = platformDao.findById(p.getId());
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
		TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
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
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Optional<Platform> maybePlatform = platformDao.changeName(p.getId(), "La Plei Cuatro");
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals("La Plei Cuatro", maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testChangePlatformShortName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Optional<Platform> maybePlatform = platformDao.changeShortName(p.getId(), "Play4");
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals("Play4", maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testChangeLogo()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Optional<Platform> maybePlatform = platformDao.changeLogo(p.getId(), "http://ps4.com/logo.png");
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals("http://ps4.com/logo.png", maybePlatform.get().getLogo());
	}

	@Test
	public void	testGetAllPlatforms()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Platform ns = TestMethods.addPlatform("Nintendo Switch", "NS", "http://nintendo.com/switch.png", platformInsert);
		Platform ps4 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ns);
		myList.add(ps4);
		
		List<Platform> platformList = platformDao.getAllPlatforms();
		Assert.assertFalse(platformList.isEmpty());
		Assert.assertEquals(2, platformList.size());
		Assert.assertEquals(platformList.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList.get(1).getName(), myList.get(1).getName());
	}
	
	@Test
	public void testGetBiggestPlatforms() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, VERSION_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		Platform ns = TestMethods.addPlatform("Nintendo Switch", "NS", PLATFORM_LOGO, platformInsert);
		Platform play = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform sn = TestMethods.addPlatform("Super Nintendo", "SN", PLATFORM_LOGO, platformInsert);
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ns);
		myList.add(play);
		myList.add(sn);
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("game2", GAME_COVER, GAME_DESC, gameInsert);
		TestMethods.connectPlatform(g2, play, versionInsert);
		TestMethods.connectPlatform(g1, ns, versionInsert);
		TestMethods.connectPlatform(g2, ns, versionInsert);
		
		
		List<Platform> platformList1 = platformDao.getBiggestPlatforms(1);
		List<Platform> platformList2 = platformDao.getBiggestPlatforms(3);
		
		Assert.assertFalse(platformList1.isEmpty());
		Assert.assertFalse(platformList2.isEmpty());
		
		Assert.assertEquals(1, platformList1.size());
		Assert.assertEquals(2, platformList2.size());
		
		Assert.assertEquals(platformList1.get(0).getName(), myList.get(0).getName());
		
		Assert.assertEquals(platformList2.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList2.get(1).getName(), myList.get(1).getName());
				
	}
	
	@Test
	public void testCountPlatforms()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		TestMethods.addPlatform("Nintendo Switch", "NS", PLATFORM_LOGO, platformInsert);
		TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		int count = platformDao.countPlatforms();
		Assert.assertEquals(2, count);
		TestMethods.addPlatform("Super Nintendo", "SN", PLATFORM_LOGO, platformInsert);		count = platformDao.countPlatforms();
		Assert.assertEquals(3, count);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		count = platformDao.countPlatforms();
		Assert.assertEquals(0, count);
	}
	
	@Test
	public void testgetPlatforms() 
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Platform ns = TestMethods.addPlatform("Nintendo Switch", "NS", PLATFORM_LOGO, platformInsert);
		Platform play = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform sn = TestMethods.addPlatform("Super Nintendo", "SN", PLATFORM_LOGO, platformInsert);
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ns);
		myList.add(play);
		myList.add(sn);
		List<Platform> platformList1 = platformDao.getPlatforms(1,2);
		List<Platform> platformList2 = platformDao.getPlatforms(2,2);
		List<Platform> platformList3 = platformDao.getPlatforms(1,5);

		Assert.assertFalse(platformList1.isEmpty());
		Assert.assertFalse(platformList2.isEmpty());
		Assert.assertFalse(platformList3.isEmpty());

		Assert.assertEquals(2, platformList1.size());
		Assert.assertEquals(1, platformList2.size());
		Assert.assertEquals(3, platformList3.size());


		Assert.assertEquals(platformList1.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList1.get(1).getName(), myList.get(1).getName());
		
		Assert.assertEquals(platformList2.get(0).getName(), myList.get(2).getName());
		
		Assert.assertEquals(platformList3.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList3.get(1).getName(), myList.get(1).getName());
		Assert.assertEquals(platformList3.get(2).getName(), myList.get(2).getName());
	}
	
	@Test
	public void testGetAllPlatformsWithGames() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, VERSION_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		Platform ns = TestMethods.addPlatform("Nintendo Switch", "NS", PLATFORM_LOGO, platformInsert);
		Platform play = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ns);
		myList.add(play);

		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		
		TestMethods.connectPlatform(g1, ns, versionInsert);
		
		List<Platform> platformList = platformDao.getAllPlatformsWithGames();
		
		Assert.assertFalse(platformList.isEmpty());
		Assert.assertEquals(1, platformList.size());
		Assert.assertEquals(platformList.get(0).getName(), myList.get(0).getName());
	}
	
	@Test
	public void testGetPlatformsWithGames() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, VERSION_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		Platform ns = TestMethods.addPlatform("Nintendo Switch", "NS", PLATFORM_LOGO, platformInsert);
		Platform play = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform sn = TestMethods.addPlatform("Super Nintendo", "SN", PLATFORM_LOGO, platformInsert);
		Platform xb = TestMethods.addPlatform("XBOX 360", "X360", PLATFORM_LOGO, platformInsert);

		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ns);
		myList.add(play);
		myList.add(sn);
		myList.add(xb);

		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("game2", GAME_COVER, GAME_DESC, gameInsert);
		
		TestMethods.connectPlatform(g1, ns, versionInsert);
		TestMethods.connectPlatform(g2, play, versionInsert);
		TestMethods.connectPlatform(g1, sn, versionInsert);

		List<Platform> platformList1 = platformDao.getPlatformsWithGames(1,2);
		List<Platform> platformList2 = platformDao.getPlatformsWithGames(2,2);
		List<Platform> platformList3 = platformDao.getPlatformsWithGames(1,10);

		Assert.assertFalse(platformList1.isEmpty());
		Assert.assertFalse(platformList2.isEmpty());
		Assert.assertFalse(platformList3.isEmpty());

		Assert.assertEquals(2, platformList1.size());
		Assert.assertEquals(1, platformList2.size());
		Assert.assertEquals(3, platformList3.size());


		Assert.assertEquals(platformList1.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList1.get(1).getName(), myList.get(1).getName());
		
		Assert.assertEquals(platformList2.get(0).getName(), myList.get(2).getName());
		
		Assert.assertEquals(platformList3.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList3.get(1).getName(), myList.get(1).getName());
		Assert.assertEquals(platformList3.get(2).getName(), myList.get(2).getName());
	}
	
	@Test
	public void testCountPlatformsWithGames() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, VERSION_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		Platform ns = TestMethods.addPlatform("Nintendo Switch", "NS", PLATFORM_LOGO, platformInsert);
		Platform play = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform sn = TestMethods.addPlatform("Super Nintendo", "SN", PLATFORM_LOGO, platformInsert);
	
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ns);
		myList.add(play);
		myList.add(sn);

		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		TestMethods.connectPlatform(g1, ns, versionInsert);
		int count1 = platformDao.countPlatformsWithGames();
		TestMethods.connectPlatform(g1, play, versionInsert);
		int count2 = platformDao.countPlatformsWithGames();		
		Assert.assertEquals(1, count1);
		Assert.assertEquals(2, count2);

	}

}
