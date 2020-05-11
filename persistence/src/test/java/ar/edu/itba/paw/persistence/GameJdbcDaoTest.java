package ar.edu.itba.paw.persistence;
import java.sql.Date;
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
	private SimpleJdbcInsert gameInsert;
	private SimpleJdbcInsert developmentInsert;
	private SimpleJdbcInsert devInsert;
	private SimpleJdbcInsert publishingInsert;
	private SimpleJdbcInsert pubInsert;
	private SimpleJdbcInsert classificationInsert;
	private SimpleJdbcInsert genreInsert;
	private SimpleJdbcInsert platformInsert;
	private SimpleJdbcInsert versionInsert;
	private SimpleJdbcInsert regionInsert;
	private SimpleJdbcInsert releaseInsert;
	
	@Before
	public void	setUp()
	{
		gameDao = new GameJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		gameInsert = new SimpleJdbcInsert(ds).withTableName(GAME_TABLE).usingGeneratedKeyColumns("game");
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
	
	/*@Test
	public void testRegisterGame()
	{
		final Game g = gameDao.register(GAME_TITLE, GAME_COVER, GAME_DESC);
		Assert.assertNotNull(g);
		Assert.assertEquals(GAME_TITLE, g.getTitle());
		Assert.assertEquals(GAME_COVER, g.getCover());
		Assert.assertEquals(GAME_DESC, g.getDescription());
	}*/
	
	@Test
	public void	testFindGameByIdDoesntExist()
	{
		Optional<Game> maybeGame = gameDao.findById(1);
		Assert.assertFalse(maybeGame.isPresent());
	}
	
	@Test
	public void	testFindGameByIdExists()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
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
		TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Optional<Game> maybeGame = gameDao.findByTitle(GAME_TITLE);
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertEquals(GAME_TITLE, maybeGame.get().getTitle());
		Assert.assertEquals(GAME_COVER, maybeGame.get().getCover());
		Assert.assertEquals(GAME_DESC, maybeGame.get().getDescription());
	}
	
	@Test
	public void	testChangeGameTitle()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Optional<Game> maybeGame = gameDao.changeTitle(g.getId(), "Zenobureido");
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertEquals("Zenobureido", maybeGame.get().getTitle());
		Assert.assertEquals(GAME_COVER, maybeGame.get().getCover());
		Assert.assertEquals(GAME_DESC, maybeGame.get().getDescription());
	}
	
	@Test
	public void	testChangeGameCover()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Optional<Game> maybeGame = gameDao.changeCover(g.getId(), "http://fake.com/image.png");
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertEquals(GAME_TITLE, maybeGame.get().getTitle());
		Assert.assertEquals("http://fake.com/image.png", maybeGame.get().getCover());
		Assert.assertEquals(GAME_DESC, maybeGame.get().getDescription());
	}
	
	@Test
	public void	testChangeGameDesc()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Optional<Game> maybeGame = gameDao.changeDescription(g.getId(), "This is a new description!");
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertEquals(GAME_TITLE, maybeGame.get().getTitle());
		Assert.assertEquals(GAME_COVER, maybeGame.get().getCover());
		Assert.assertEquals("This is a new description!", maybeGame.get().getDescription());
	}
	
	@Test
	public void	testGetAllGames()
	{
		Game example = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game botw = TestMethods.addGame("The Legend of Zelda: Breath of the Wild", GAME_COVER, GAME_DESC, gameInsert);
		
		List<Game> gamesList = gameDao.getAllGames();
		List<Game> myList = new ArrayList<Game>();
		myList.add(example);
		myList.add(botw);
		
		Assert.assertFalse(gamesList.isEmpty());
		Assert.assertEquals(2, gamesList.size());
		Assert.assertEquals(gamesList.get(0).getTitle(), myList.get(0).getTitle());
		Assert.assertEquals(gamesList.get(1).getTitle(), myList.get(1).getTitle());
	}
	/*
	@Test
	public void testAddDeveloper()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Developer d1 = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		Developer d2 = TestMethods.addDeveloper("Nintendo", DEVELOPER_LOGO, devInsert);
		TestMethods.connectDev(g, d1, developmentInsert);
		Optional<Game> maybeGame = gameDao.addDeveloper(g, d2);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getDevelopers().contains(d1));
		Assert.assertTrue(maybeGame.get().getDevelopers().contains(d2));
	}

	@Test
	public void testAddPublisher()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Publisher p = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Optional<Game> maybeGame = gameDao.addPublisher(g, p);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getPublishers().contains(p));
	}
	
	@Test
	public void testAddGenre()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Genre g1 = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Optional<Game> maybeGame = gameDao.addGenre(g, g1);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getGenres().contains(g1));
	}
	
	@Test
	public void testAddPlatform()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Optional<Game> maybeGame = gameDao.addPlatform(g, p);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getPlatforms().contains(p));
	}

	@Test
	public void testAddRelease()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Region r = TestMethods.addRegion(REGION_NAME, REGION_SHORT, regionInsert);
		Release rel = new Release(r, new Date(2017, 03, 03));
		Optional<Game> maybeGame = gameDao.addReleaseDate(g, rel);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getReleaseDates().contains(rel));
	}

	@Test
	public void testRemoveDeveloper()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Developer d1 = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		Developer d2 = TestMethods.addDeveloper("Nintendo", DEVELOPER_LOGO, devInsert);
		TestMethods.connectDev(g, d1, developmentInsert);
		TestMethods.connectDev(g, d2, developmentInsert);
		Optional<Game> maybeGame = gameDao.removeDeveloper(g, d2);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertFalse(maybeGame.get().getDevelopers().isEmpty());
		Assert.assertTrue(maybeGame.get().getDevelopers().contains(d1));
		Assert.assertFalse(maybeGame.get().getDevelopers().contains(d2));
	}
	
	@Test
	public void testRemovePublisher()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Publisher p1 = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Publisher p2 = TestMethods.addPublisher("SquareEnix", PUBLISHER_LOGO, pubInsert);
		TestMethods.connectPub(g, p1, publishingInsert);
		TestMethods.connectPub(g, p2, publishingInsert);
		Optional<Game> maybeGame = gameDao.removePublisher(g, p1);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertFalse(maybeGame.get().getPublishers().isEmpty());
		Assert.assertFalse(maybeGame.get().getPublishers().contains(p1));
		Assert.assertTrue(maybeGame.get().getPublishers().contains(p2));
	}
	
	@Test
	public void testRemoveGenre()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Genre g1 = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		TestMethods.connectGenre(g, g1, classificationInsert);
		Optional<Game> maybeGame = gameDao.removeGenre(g, g1);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getPlatforms().isEmpty());
	}
	
	@Test
	public void testRemovePlatform()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		TestMethods.connectPlatform(g, p, versionInsert);
		Optional<Game> maybeGame = gameDao.removePlatform(g, p);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getPlatforms().isEmpty());
	}
	
	@Test
	public void testRemoveRelease()
	{
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Region r1 = TestMethods.addRegion(REGION_NAME, REGION_SHORT, regionInsert);
		Region r2 = TestMethods.addRegion("Japan", "JP", regionInsert);
		Release rel1 = new Release(r1, Date.valueOf("2017-03-03"));
		Release rel2 = new Release(r2, Date.valueOf("2017-06-20"));
		TestMethods.addRelease(g, r1, rel1.getDate(), releaseInsert);
		TestMethods.addRelease(g, r2, rel2.getDate(), releaseInsert);
		Optional<Game> maybeGame = gameDao.removeReleaseDate(g, rel1);
		
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertFalse(maybeGame.get().getReleaseDates().contains(rel1));
		Assert.assertTrue(maybeGame.get().getReleaseDates().contains(rel2));
	}

	@Test 
	public void	testSearchByTitle() 
	{ 
		Game raym = TestMethods.addGame("Rayman Legends", GAME_COVER, GAME_DESC, gameInsert); 
		Game mario = TestMethods.addGame("Super Mario 3D World", GAME_COVER, GAME_DESC, gameInsert); 
		TestMethods.addGame("Cuphead", GAME_COVER, GAME_DESC, gameInsert); 
		 
		List<Game> myList = new ArrayList<Game>(); 
		myList.add(raym); 
		myList.add(mario); 
		 
		List<Game> gamesList1 = gameDao.searchByTitle("mA"); 
		List<Game> gamesList2 = gameDao.searchByTitle("qwerty"); 
		 
		Assert.assertFalse(gamesList1.isEmpty()); 
		Assert.assertEquals(2, gamesList1.size()); 
		Assert.assertEquals(gamesList1.get(0).getTitle(), myList.get(0).getTitle()); 
		Assert.assertEquals(gamesList1.get(1).getTitle(), myList.get(1).getTitle()); 
		Assert.assertTrue(gamesList2.isEmpty()); 
	}
	
	@Test 
	public void	testSearchByTitleSimplified() 
	{ 
		Game raym = TestMethods.addGame("Rayman Legends", GAME_COVER, GAME_DESC, gameInsert); 
		Game mario = TestMethods.addGame("Super Mario 3D World", GAME_COVER, GAME_DESC, gameInsert); 
		TestMethods.addGame("Cuphead", GAME_COVER, GAME_DESC, gameInsert); 
		 
		List<Game> myList = new ArrayList<Game>(); 
		myList.add(raym); 
		myList.add(mario); 
		 
		List<Game> gamesList1 = gameDao.searchByTitle("mA"); 
		List<Game> gamesList2 = gameDao.searchByTitle("qwerty"); 
		 
		Assert.assertFalse(gamesList1.isEmpty()); 
		Assert.assertEquals(2, gamesList1.size()); 
		Assert.assertEquals(gamesList1.get(0).getTitle(), myList.get(0).getTitle()); 
		Assert.assertEquals(gamesList1.get(1).getTitle(), myList.get(1).getTitle()); 
		Assert.assertTrue(gamesList2.isEmpty()); 
	}

	*/
	
	@Test
	public void	testGetAllGamesSimplified()
	{
		Game example = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game botw = TestMethods.addGame("The Legend of Zelda: Breath of the Wild", GAME_COVER, GAME_DESC, gameInsert);
		
		List<Game> gamesList = gameDao.getAllGames();
		List<Game> myList = new ArrayList<Game>();
		myList.add(example);
		myList.add(botw);
		
		Assert.assertFalse(gamesList.isEmpty());
		Assert.assertEquals(2, gamesList.size());
		Assert.assertEquals(gamesList.get(0).getTitle(), myList.get(0).getTitle());
		Assert.assertEquals(gamesList.get(1).getTitle(), myList.get(1).getTitle());
	}
	
}