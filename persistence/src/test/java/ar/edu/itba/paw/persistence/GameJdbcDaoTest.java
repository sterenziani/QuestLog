package ar.edu.itba.paw.persistence;
import java.util.ArrayList;
import java.sql.Date;
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
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Region;
import ar.edu.itba.paw.model.Release;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class GameJdbcDaoTest
{
	private static final String GAME_TABLE = "games";
	private static final String GAME_TITLE = "Example Game";
	private static final String GAME_COVER = "http://sega.com/game.jpg";
	private static final String GAME_DESC = "Explore the world!";
	private	static final String DEVELOPER_TABLE = "developers";
	private	static final String DEVELOPER_NAME = "ITBA Studios";
	private	static final String DEVELOPER_LOGO = "https://itba.com/itba.jpg";
	private	static final String PUBLISHER_TABLE = "publishers";
	private	static final String PUBLISHER_NAME = "Nintendo";
	private	static final String PUBLISHER_LOGO = "https://nintendo.com/logo.jpg";
	private	static final String PLATFORM_TABLE = "platforms";
	private	static final String PLATFORM_NAME = "Wii";
	private	static final String PLATFORM_SHORT_NAME = "Wii";
	private	static final String PLATFORM_LOGO = "https://nintendo.com/wii.jpg";
	private	static final String GENRE_TABLE = "genres";
	private	static final String GENRE_NAME = "RPG";
	private	static final String GENRE_LOGO = "https://example.com/icon.jpg";
	private static final String DEVELOPMENT_TABLE = "development";
	private static final String PUBLISHING_TABLE = "publishing";
	private static final String CLASSIFICATION_TABLE = "classifications";
	private static final String VERSION_TABLE = "game_versions";
	private static final String REGION_TABLE = "regions";
	private static final String RELEASE_TABLE = "releases";
	private static final String REGION_NAME = "North America";
	private static final String REGION_SHORT = "NA";
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private GameJdbcDao gameDao;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	SimpleJdbcInsert developmentInsert;
	SimpleJdbcInsert devInsert;
	SimpleJdbcInsert publishingInsert;
	SimpleJdbcInsert pubInsert;
	SimpleJdbcInsert classificationInsert;
	SimpleJdbcInsert genreInsert;
	SimpleJdbcInsert platformInsert;
	SimpleJdbcInsert versionInsert;
	SimpleJdbcInsert regionInsert;
	SimpleJdbcInsert releaseInsert;
	
	
	private Game addGame(String title, String cover, String desc)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("title", title);
		args.put("cover", cover);
		args.put("description", desc);
		return new Game(jdbcInsert.executeAndReturnKey(args).longValue(), title, cover, desc);
	}
	
	private Developer addDeveloper(String name, String logo)
	{
		final Map<String, Object> devArgs = new HashMap<>();
		devArgs.put("developer_name", name);
		devArgs.put("developer_logo", logo);
		return new Developer(devInsert.executeAndReturnKey(devArgs).longValue(), name, logo);
	}
	
	private Publisher addPublisher(String name, String logo)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("publisher_name", name);
		args.put("publisher_logo", logo);
		return new Publisher(pubInsert.executeAndReturnKey(args).longValue(), name, logo);
	}
	
	private Genre addGenre(String name, String logo)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("genre_name", name);
		args.put("genre_logo", logo);
		return new Genre(genreInsert.executeAndReturnKey(args).longValue(), name, logo);
	}
	
	private Platform addPlatform(String name, String shortName, String logo)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("platform_name", name);
		args.put("platform_name_short", shortName);
		args.put("platform_logo", logo);
		return new Platform(platformInsert.executeAndReturnKey(args).longValue(), name, shortName, logo);
	}
	
	private Region addRegion(String name, String shortName)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("region_name", name);
		args.put("region_short", shortName);
		return new Region(regionInsert.executeAndReturnKey(args).longValue(), name, shortName);
	}
	
	private void addRelease(Game g, Region r, Date d)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("game", g.getId());
		args.put("region", r.getId());
		args.put("release_date", d);
		releaseInsert.execute(args);
	}
	
	private void connectDev(Game g, Developer d)
	{
		final Map<String, Object> developmentArgs = new HashMap<>();
		developmentArgs.put("game", g.getId());
		developmentArgs.put("developer", d.getId());
		developmentInsert.execute(developmentArgs);
	}
	
	private void connectPub(Game g, Publisher p)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("game", g.getId());
		args.put("publisher", p.getId());
		publishingInsert.execute(args);
	}
	
	private void connectGenre(Game g, Genre genre)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("game", g.getId());
		args.put("genre", genre.getId());
		classificationInsert.execute(args);
	}
	
	private void connectPlatform(Game g, Platform p)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("game", g.getId());
		args.put("platform", p.getId());
		versionInsert.execute(args);
	}
	
	@Before
	public void	setUp()
	{
		gameDao = new GameJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(ds).withTableName(GAME_TABLE).usingGeneratedKeyColumns("game");
		developmentInsert = new SimpleJdbcInsert(ds).withTableName(DEVELOPMENT_TABLE);
		devInsert = new SimpleJdbcInsert(ds).withTableName(DEVELOPER_TABLE).usingGeneratedKeyColumns("developer");
		pubInsert = new SimpleJdbcInsert(ds).withTableName(PUBLISHER_TABLE).usingGeneratedKeyColumns("publisher");
		publishingInsert = new SimpleJdbcInsert(ds).withTableName(PUBLISHING_TABLE);
		classificationInsert = new SimpleJdbcInsert(ds).withTableName(CLASSIFICATION_TABLE);
		genreInsert  = new SimpleJdbcInsert(ds).withTableName(GENRE_TABLE).usingGeneratedKeyColumns("genre");
		platformInsert = new SimpleJdbcInsert(ds).withTableName(PLATFORM_TABLE).usingGeneratedKeyColumns("platform");
		versionInsert = new SimpleJdbcInsert(ds).withTableName(VERSION_TABLE);
		regionInsert = new SimpleJdbcInsert(ds).withTableName(REGION_TABLE).usingGeneratedKeyColumns("region");
		releaseInsert = new SimpleJdbcInsert(ds).withTableName(RELEASE_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHING_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPMENT_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, CLASSIFICATION_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, VERSION_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, REGION_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, RELEASE_TABLE);
	}
	
	@Test
	public void testRegisterGame()
	{
		final Game g = gameDao.register(GAME_TITLE, GAME_COVER, GAME_DESC);
		Assert.assertNotNull(g);
		Assert.assertEquals(GAME_TITLE, g.getTitle());
		Assert.assertEquals(GAME_COVER, g.getCover());
		Assert.assertEquals(GAME_DESC, g.getDescription());
	}
	
	@Test
	public void	testFindGameByIdDoesntExist()
	{
		Optional<Game> maybeGame = gameDao.findById(1);
		Assert.assertFalse(maybeGame.isPresent());
	}
	
	@Test
	public void	testFindGameByIdExists()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Optional<Game> maybeGame = gameDao.findById(g.getId());
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertEquals(GAME_TITLE, maybeGame.get().getTitle());
		Assert.assertEquals(GAME_COVER, maybeGame.get().getCover());
		Assert.assertEquals(GAME_DESC, maybeGame.get().getDescription());
	}
	
	@Test
	public void	testFindGameByTitleDoesntExist()
	{
		Optional<Game> maybeGame = gameDao.findByTitle(GAME_TITLE);
		Assert.assertFalse(maybeGame.isPresent());
	}
	
	@Test
	public void	testFindGameByTitleExists()
	{
		addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Optional<Game> maybeGame = gameDao.findByTitle(GAME_TITLE);
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertEquals(GAME_TITLE, maybeGame.get().getTitle());
		Assert.assertEquals(GAME_COVER, maybeGame.get().getCover());
		Assert.assertEquals(GAME_DESC, maybeGame.get().getDescription());
	}
	
	@Test
	public void	testChangeGameTitle()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Optional<Game> maybeGame = gameDao.changeTitle(g.getId(), "Zenobureido");
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertEquals("Zenobureido", maybeGame.get().getTitle());
		Assert.assertEquals(GAME_COVER, maybeGame.get().getCover());
		Assert.assertEquals(GAME_DESC, maybeGame.get().getDescription());
	}
	
	@Test
	public void	testChangeGameCover()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Optional<Game> maybeGame = gameDao.changeCover(g.getId(), "http://fake.com/image.png");
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertEquals(GAME_TITLE, maybeGame.get().getTitle());
		Assert.assertEquals("http://fake.com/image.png", maybeGame.get().getCover());
		Assert.assertEquals(GAME_DESC, maybeGame.get().getDescription());
	}
	
	@Test
	public void	testChangeGameDesc()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Optional<Game> maybeGame = gameDao.changeDescription(g.getId(), "This is a new description!");
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertEquals(GAME_TITLE, maybeGame.get().getTitle());
		Assert.assertEquals(GAME_COVER, maybeGame.get().getCover());
		Assert.assertEquals("This is a new description!", maybeGame.get().getDescription());
	}
	
	@Test
	public void	testGetAllGames()
	{
		Game xeno = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Game botw = addGame("The Legend of Zelda: Breath of the Wild", GAME_COVER, GAME_DESC);
		
		List<Game> gamesList = gameDao.getAllGames();
		List<Game> myList = new ArrayList<Game>();
		myList.add(xeno);
		myList.add(botw);
		
		Assert.assertFalse(gamesList.isEmpty());
		Assert.assertEquals(2, gamesList.size());
		Assert.assertEquals(gamesList.get(0).getTitle(), myList.get(0).getTitle());
		Assert.assertEquals(gamesList.get(1).getTitle(), myList.get(1).getTitle());
	}
	
	@Test
	public void	testGetGameDevs()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Developer d1 = addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO);
		Developer d2 = addDeveloper("Nintendo", DEVELOPER_LOGO);
		connectDev(g, d1);
		connectDev(g, d2);
		
		List<Developer> devsList = gameDao.getAllDevelopers(g);
		List<Developer> myList = new ArrayList<Developer>();
		myList.add(d1);
		myList.add(d2);
		
		Assert.assertFalse(devsList.isEmpty());
		Assert.assertEquals(myList.size(), devsList.size());
		Assert.assertEquals(myList.get(0).getName(), devsList.get(0).getName());
		Assert.assertEquals(myList.get(1).getName(), devsList.get(1).getName());
	}
	
	@Test
	public void	testGetGamePublishers()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Publisher p1 = addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO);
		Publisher p2 = addPublisher("SquareEnix", PUBLISHER_LOGO);
		connectPub(g, p1);
		connectPub(g, p2);
		
		List<Publisher> pubsList = gameDao.getAllPublishers(g);
		List<Publisher> myList = new ArrayList<Publisher>();
		myList.add(p1);
		myList.add(p2);
		
		Assert.assertFalse(pubsList.isEmpty());
		Assert.assertEquals(myList.size(), pubsList.size());
		Assert.assertEquals(myList.get(0).getName(), pubsList.get(0).getName());
		Assert.assertEquals(myList.get(1).getName(), pubsList.get(1).getName());
	}
	
	@Test
	public void	testGetGameGenres()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Genre g1 = addGenre(GENRE_NAME, GENRE_LOGO);
		Genre g2 = addGenre("Adventure", GENRE_LOGO);
		connectGenre(g, g1);
		connectGenre(g, g2);
		
		List<Genre> genreList = gameDao.getAllGenres(g);
		List<Genre> myList = new ArrayList<Genre>();
		myList.add(g1);
		myList.add(g2);
		
		Assert.assertFalse(genreList.isEmpty());
		Assert.assertEquals(myList.size(), genreList.size());
		Assert.assertEquals(myList.get(0).getName(), genreList.get(0).getName());
		Assert.assertEquals(myList.get(1).getName(), genreList.get(1).getName());
	}
	
	@Test
	public void	testGetGamePlatforms()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Platform p1 = addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO);
		Platform p2 = addPlatform("PlayStation 4", "PS4", PLATFORM_LOGO);
		connectPlatform(g, p1);
		connectPlatform(g, p2);
		
		List<Platform> platformsList = gameDao.getAllPlatforms(g);
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(p1);
		myList.add(p2);
		
		Assert.assertFalse(platformsList.isEmpty());
		Assert.assertEquals(myList.size(), platformsList.size());
		Assert.assertEquals(myList.get(0).getName(), platformsList.get(0).getName());
		Assert.assertEquals(myList.get(1).getName(), platformsList.get(1).getName());
	}
	
	@Test
	public void testAddDeveloper()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Developer d1 = addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO);
		Developer d2 = addDeveloper("Nintendo", DEVELOPER_LOGO);
		connectDev(g, d1);
		Optional<Game> maybeGame = gameDao.addDeveloper(g, d2);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getDevelopers().contains(d1));
		Assert.assertTrue(maybeGame.get().getDevelopers().contains(d2));
	}
	
	@Test
	public void testAddPublisher()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Publisher p = addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO);
		Optional<Game> maybeGame = gameDao.addPublisher(g, p);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getPublishers().contains(p));
	}
	
	@Test
	public void testAddGenre()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Genre g1 = addGenre(GENRE_NAME, GENRE_LOGO);
		Optional<Game> maybeGame = gameDao.addGenre(g, g1);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getGenres().contains(g1));
	}
	
	@Test
	public void testAddPlatform()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Platform p = addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO);
		Optional<Game> maybeGame = gameDao.addPlatform(g, p);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getPlatforms().contains(p));
	}

	@Test
	public void testAddRelease()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Region r = addRegion(REGION_NAME, REGION_SHORT);
		Release rel = new Release(r, new Date(2017, 03, 03));
		Optional<Game> maybeGame = gameDao.addReleaseDate(g, rel);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getReleaseDates().contains(rel));
	}

	@Test
	public void testRemoveDeveloper()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Developer d1 = addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO);
		Developer d2 = addDeveloper("Nintendo", DEVELOPER_LOGO);
		connectDev(g, d1);
		connectDev(g, d2);
		Optional<Game> maybeGame = gameDao.removeDeveloper(g, d2);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertFalse(maybeGame.get().getDevelopers().isEmpty());
		Assert.assertTrue(maybeGame.get().getDevelopers().contains(d1));
		Assert.assertFalse(maybeGame.get().getDevelopers().contains(d2));
	}
	
	@Test
	public void testRemovePublisher()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Publisher p1 = addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO);
		Publisher p2 = addPublisher("SquareEnix", PUBLISHER_LOGO);
		connectPub(g, p1);
		connectPub(g, p2);
		Optional<Game> maybeGame = gameDao.removePublisher(g, p1);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertFalse(maybeGame.get().getPublishers().isEmpty());
		Assert.assertFalse(maybeGame.get().getPublishers().contains(p1));
		Assert.assertTrue(maybeGame.get().getPublishers().contains(p2));
	}
	
	@Test
	public void testRemoveGenre()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Genre g1 = addGenre(GENRE_NAME, GENRE_LOGO);
		connectGenre(g, g1);
		Optional<Game> maybeGame = gameDao.removeGenre(g, g1);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getPlatforms().isEmpty());
	}
	
	@Test
	public void testRemovePlatform()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Platform p = addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO);
		connectPlatform(g, p);
		Optional<Game> maybeGame = gameDao.removePlatform(g, p);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getPlatforms().isEmpty());
	}
	
	@Test
	public void testRemoveRelease()
	{
		Game g = addGame(GAME_TITLE, GAME_COVER, GAME_DESC);
		Region r1 = addRegion(REGION_NAME, REGION_SHORT);
		Region r2 = addRegion("Japan", "JP");
		Release rel1 = new Release(r1, Date.valueOf("2017-03-03"));
		Release rel2 = new Release(r2, Date.valueOf("2017-06-20"));
		addRelease(g, r1, rel1.getDate());
		addRelease(g, r2, rel2.getDate());
		Optional<Game> maybeGame = gameDao.removeReleaseDate(g, rel1);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertFalse(maybeGame.get().getReleaseDates().contains(rel1));
		Assert.assertTrue(maybeGame.get().getReleaseDates().contains(rel2));
	}
}