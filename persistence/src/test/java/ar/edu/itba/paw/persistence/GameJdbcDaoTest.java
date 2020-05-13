package ar.edu.itba.paw.persistence;
import java.sql.Array;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import ar.edu.itba.paw.model.*;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class GameJdbcDaoTest
{
	private static final String GAME_TABLE 				= "games";
	private static final String GAME_TITLE 				= "Example Game";
	private static final String GAME_COVER 				= "http://sega.com/game.jpg";
	private static final String GAME_DESC 				= "Explore the world!";
	private	static final String DEVELOPER_TABLE 		= "developers";
	private	static final String DEVELOPER_NAME 			= "ITBA Studios";
	private	static final String DEVELOPER_LOGO 			= "https://itba.com/itba.jpg";
	private	static final String PUBLISHER_TABLE 		= "publishers";
	private	static final String PUBLISHER_NAME 			= "Nintendo";
	private	static final String PUBLISHER_LOGO 			= "https://nintendo.com/logo.jpg";
	private	static final String PLATFORM_TABLE 			= "platforms";
	private	static final String PLATFORM_NAME 			= "Wii";
	private	static final String PLATFORM_SHORT_NAME 	= "Wii";
	private	static final String PLATFORM_LOGO 			= "https://nintendo.com/wii.jpg";
	private	static final String GENRE_TABLE 			= "genres";
	private	static final String GENRE_NAME 				= "RPG";
	private	static final String GENRE_LOGO 				= "https://example.com/icon.jpg";
	private static final String DEVELOPMENT_TABLE 		= "development";
	private static final String PUBLISHING_TABLE 		= "publishing";
	private static final String CLASSIFICATION_TABLE 	= "classifications";
	private static final String VERSION_TABLE 			= "game_versions";
	private static final String REGION_TABLE 			= "regions";
	private static final String RELEASE_TABLE 			= "releases";
	private static final String REGION_NAME 			= "North America";
	private static final String REGION_SHORT 			= "NA";
	private static final Date RELEASE_DATE 				= Date.valueOf(LocalDate.now());
	private static final String USER_TABLE				= "users";
	private static final String BACKLOG_TABLE			= "backlogs";
	private static final String SCORE_TABLE				= "scores";
	
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
	private SimpleJdbcInsert userInsert;
	private SimpleJdbcInsert backlogInsert;
	private SimpleJdbcInsert scoreInsert;
	
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
		userInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE).usingGeneratedKeyColumns("user_id");
		backlogInsert = new SimpleJdbcInsert(ds).withTableName(BACKLOG_TABLE);
		scoreInsert = new SimpleJdbcInsert(ds).withTableName(SCORE_TABLE);
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
		JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, BACKLOG_TABLE);
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
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Optional<Game> maybeGame = gameDao.findById(g.getId());
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertEquals(GAME_TITLE, maybeGame.get().getTitle());
		Assert.assertEquals(GAME_COVER, maybeGame.get().getCover());
		Assert.assertEquals(GAME_DESC, maybeGame.get().getDescription());
	}

	@Test
	public void testFindGameByIdWithDetailsGetsPlatform(){
		Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Platform p 						= TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		TestMethods.connectPlatform(g, p, versionInsert);
		Optional<GameDetail> maybeGame 	= gameDao.findByIdWithDetails(g.getId());
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getPlatforms().stream().findFirst().isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybeGame.get().getPlatforms().stream().findFirst().get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybeGame.get().getPlatforms().stream().findFirst().get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybeGame.get().getPlatforms().stream().findFirst().get().getLogo());
	}

	@Test
	public void testFindGameByIdWithDetailsGetsDevelopers(){
		Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Developer d 					= TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		TestMethods.connectDev(g, d, developmentInsert);
		Optional<GameDetail> maybeGame 	= gameDao.findByIdWithDetails(g.getId());
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getDevelopers().stream().findFirst().isPresent());
		Assert.assertEquals(DEVELOPER_NAME, maybeGame.get().getDevelopers().stream().findFirst().get().getName());
		Assert.assertEquals(DEVELOPER_LOGO, maybeGame.get().getDevelopers().stream().findFirst().get().getLogo());
	}

	@Test
	public void testFindGameByIdWithDetailsGetsPublishers(){
		Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Publisher p 					= TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		TestMethods.connectPub(g, p, publishingInsert);
		Optional<GameDetail> maybeGame 	= gameDao.findByIdWithDetails(g.getId());
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getPublishers().stream().findFirst().isPresent());
		Assert.assertEquals(PUBLISHER_NAME, maybeGame.get().getPublishers().stream().findFirst().get().getName());
		Assert.assertEquals(PUBLISHER_LOGO, maybeGame.get().getPublishers().stream().findFirst().get().getLogo());
	}

	@Test
	public void testFindGameByIdWithDetailsGetsGenres(){
		Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Genre gen 						= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		TestMethods.connectGenre(g, gen, classificationInsert);
		Optional<GameDetail> maybeGame 	= gameDao.findByIdWithDetails(g.getId());
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getGenres().stream().findFirst().isPresent());
		Assert.assertEquals(GENRE_NAME, maybeGame.get().getGenres().stream().findFirst().get().getName());
		Assert.assertEquals(GENRE_LOGO, maybeGame.get().getGenres().stream().findFirst().get().getLogo());
	}

	@Test
	public void testFindGameByIdWithDetailsGetsReleaseDates(){
		Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Region region 					= TestMethods.addRegion(REGION_NAME, REGION_SHORT, regionInsert);
		TestMethods.addRelease(g, region, RELEASE_DATE, releaseInsert);
		Optional<GameDetail> maybeGame 	= gameDao.findByIdWithDetails(g.getId());
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getReleaseDates().stream().findFirst().isPresent());
		Assert.assertNotNull(maybeGame.get().getReleaseDates().stream().findFirst().get().getRegion());
		Assert.assertEquals(REGION_NAME, maybeGame.get().getReleaseDates().stream().findFirst().get().getRegion().getName());
		Assert.assertEquals(REGION_SHORT, maybeGame.get().getReleaseDates().stream().findFirst().get().getRegion().getShortName());
		Assert.assertEquals(RELEASE_DATE, maybeGame.get().getReleaseDates().stream().findFirst().get().getDate());
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
	public void testFindGameByTitleWithDetailsGetsPlatform(){
		Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Platform p 						= TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		TestMethods.connectPlatform(g, p, versionInsert);
		Optional<GameDetail> maybeGame 	= gameDao.findByTitleWithDetails(GAME_TITLE);
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getPlatforms().stream().findFirst().isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybeGame.get().getPlatforms().stream().findFirst().get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybeGame.get().getPlatforms().stream().findFirst().get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybeGame.get().getPlatforms().stream().findFirst().get().getLogo());
	}

	@Test
	public void testFindGameByTitleWithDetailsGetsDevelopers(){
		Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Developer d 					= TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		TestMethods.connectDev(g, d, developmentInsert);
		Optional<GameDetail> maybeGame 	= gameDao.findByTitleWithDetails(GAME_TITLE);
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getDevelopers().stream().findFirst().isPresent());
		Assert.assertEquals(DEVELOPER_NAME, maybeGame.get().getDevelopers().stream().findFirst().get().getName());
		Assert.assertEquals(DEVELOPER_LOGO, maybeGame.get().getDevelopers().stream().findFirst().get().getLogo());
	}

	@Test
	public void testFindGameByTitleWithDetailsGetsPublishers(){
		Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Publisher p 					= TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		TestMethods.connectPub(g, p, publishingInsert);
		Optional<GameDetail> maybeGame 	= gameDao.findByTitleWithDetails(GAME_TITLE);
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getPublishers().stream().findFirst().isPresent());
		Assert.assertEquals(PUBLISHER_NAME, maybeGame.get().getPublishers().stream().findFirst().get().getName());
		Assert.assertEquals(PUBLISHER_LOGO, maybeGame.get().getPublishers().stream().findFirst().get().getLogo());
	}

	@Test
	public void testFindGameByTitleWithDetailsGetsGenres(){
		Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Genre gen 						= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		TestMethods.connectGenre(g, gen, classificationInsert);
		Optional<GameDetail> maybeGame 	= gameDao.findByTitleWithDetails(GAME_TITLE);
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getGenres().stream().findFirst().isPresent());
		Assert.assertEquals(GENRE_NAME, maybeGame.get().getGenres().stream().findFirst().get().getName());
		Assert.assertEquals(GENRE_LOGO, maybeGame.get().getGenres().stream().findFirst().get().getLogo());
	}

	@Test
	public void testFindGameByTitleWithDetailsGetsReleaseDates(){
		Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Region region 					= TestMethods.addRegion(REGION_NAME, REGION_SHORT, regionInsert);
		TestMethods.addRelease(g, region, RELEASE_DATE, releaseInsert);
		Optional<GameDetail> maybeGame 	= gameDao.findByTitleWithDetails(GAME_TITLE);
		Assert.assertTrue(maybeGame.isPresent());
		Assert.assertTrue(maybeGame.get().getReleaseDates().stream().findFirst().isPresent());
		Assert.assertNotNull(maybeGame.get().getReleaseDates().stream().findFirst().get().getRegion());
		Assert.assertEquals(REGION_NAME, maybeGame.get().getReleaseDates().stream().findFirst().get().getRegion().getName());
		Assert.assertEquals(REGION_SHORT, maybeGame.get().getReleaseDates().stream().findFirst().get().getRegion().getShortName());
		Assert.assertEquals(RELEASE_DATE, maybeGame.get().getReleaseDates().stream().findFirst().get().getDate());
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

	public void testRegister()
	{
		Game g = gameDao.register(GAME_TITLE, GAME_COVER, GAME_DESC);
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, GAME_TABLE, "title ='" + GAME_TITLE +"' AND cover = '"
				+ GAME_COVER + "' AND description = '" + GAME_DESC + "'"));
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

	@Test
	public void testAddPlatform(){
		Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Platform p 	= TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		gameDao.addPlatform(g, p);
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, VERSION_TABLE, "game ='" + g.getId() +"' AND platform = '" + p.getId() + "'"));
	}

	final static String ALTERNATIVE_PLATFORM_NAME 		= "ps4";
	final static String ALTERNATIVE_PLATFORM_SHORT_NAME = "ps4";
	final static String ALTERNATIVE_PLATFORM_LOGO		= "ps4.jpg";

	@Test
	public void testAddPlatforms(){
		Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Platform p 	= TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform ap	= TestMethods.addPlatform(ALTERNATIVE_PLATFORM_NAME, ALTERNATIVE_PLATFORM_SHORT_NAME, ALTERNATIVE_PLATFORM_LOGO, platformInsert);

		List<Long> p_ids = Arrays.asList(p.getId(), ap.getId());

		gameDao.addPlatforms(g.getId(), p_ids);

		Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, VERSION_TABLE));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, VERSION_TABLE, "game ='" + g.getId() +"' AND platform = '" + p.getId() + "'"));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, VERSION_TABLE, "game ='" + g.getId() +"' AND platform = '" + ap.getId() + "'"));
	}

	@Test
	public void testRemoveSpecificPlatform(){
		Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Platform p 	= TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform ap	= TestMethods.addPlatform(ALTERNATIVE_PLATFORM_NAME, ALTERNATIVE_PLATFORM_SHORT_NAME, ALTERNATIVE_PLATFORM_LOGO, platformInsert);

		TestMethods.connectPlatform(g, p, versionInsert);
		TestMethods.connectPlatform(g, ap, versionInsert);

		gameDao.removePlatform(g, p);

		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, VERSION_TABLE));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, VERSION_TABLE, "game ='" + g.getId() +"' AND platform = '" + ap.getId() + "'"));
	}

	@Test
	public void testRemoveAllPlatforms(){
		Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Platform p 	= TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform ap	= TestMethods.addPlatform(ALTERNATIVE_PLATFORM_NAME, ALTERNATIVE_PLATFORM_SHORT_NAME, ALTERNATIVE_PLATFORM_LOGO, platformInsert);

		TestMethods.connectPlatform(g, p, versionInsert);
		TestMethods.connectPlatform(g, ap, versionInsert);

		gameDao.removeAllPlatforms(g);

		Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, VERSION_TABLE));
	}

	@Test
	public void testAddDeveloper(){
		Game g 			= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Developer d 	= TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		gameDao.addDeveloper(g, d);
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DEVELOPMENT_TABLE, "game ='" + g.getId() +"' AND developer = '" + d.getId() + "'"));
	}

	final static String ALTERNATIVE_DEVELOPER_NAME 		= "Monolith Soft";
	final static String ALTERNATIVE_DEVELOPER_LOGO		= "monolith.jpg";

	@Test
	public void testAddDevelopers(){
		Game g 		 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Developer d  = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		Developer ad = TestMethods.addDeveloper(ALTERNATIVE_DEVELOPER_NAME, ALTERNATIVE_DEVELOPER_LOGO, devInsert);

		List<Long> d_ids = Arrays.asList(d.getId(), ad.getId());

		gameDao.addDevelopers(g.getId(), d_ids);

		Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, DEVELOPMENT_TABLE));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DEVELOPMENT_TABLE, "game ='" + g.getId() +"' AND developer = '" + d.getId() + "'"));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DEVELOPMENT_TABLE, "game ='" + g.getId() +"' AND developer = '" + ad.getId() + "'"));
	}

	@Test
	public void testRemoveSpecificDeveloper(){
		Game g 		 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Developer d  = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		Developer ad = TestMethods.addDeveloper(ALTERNATIVE_DEVELOPER_NAME, ALTERNATIVE_DEVELOPER_LOGO, devInsert);

		TestMethods.connectDev(g, d, developmentInsert);
		TestMethods.connectDev(g, ad, developmentInsert);

		gameDao.removeDeveloper(g, d);

		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, DEVELOPMENT_TABLE));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DEVELOPMENT_TABLE, "game ='" + g.getId() +"' AND developer = '" + ad.getId() + "'"));
	}

	@Test
	public void testRemoveAllDevelopers(){
		Game g 		 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Developer d  = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		Developer ad = TestMethods.addDeveloper(ALTERNATIVE_DEVELOPER_NAME, ALTERNATIVE_DEVELOPER_LOGO, devInsert);

		TestMethods.connectDev(g, d, developmentInsert);
		TestMethods.connectDev(g, ad, developmentInsert);

		gameDao.removeAllDevelopers(g);

		Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, DEVELOPMENT_TABLE));
	}

	@Test
	public void testAddPublisher(){
		Game g 			= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Publisher p 	= TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		gameDao.addPublisher(g, p);
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, PUBLISHING_TABLE, "game ='" + g.getId() +"' AND publisher = '" + p.getId() + "'"));
	}

	final static String ALTERNATIVE_PUBLISHER_NAME 		= "SEGA";
	final static String ALTERNATIVE_PUBLISHER_LOGO		= "sega.jpg";

	@Test
	public void testAddPublishers(){
		Game g 		 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Publisher p  = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Publisher ap = TestMethods.addPublisher(ALTERNATIVE_PUBLISHER_NAME, ALTERNATIVE_PUBLISHER_LOGO, pubInsert);

		List<Long> p_ids = Arrays.asList(p.getId(), ap.getId());

		gameDao.addPublishers(g.getId(), p_ids);

		Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, PUBLISHING_TABLE));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, PUBLISHING_TABLE, "game ='" + g.getId() +"' AND publisher = '" + p.getId() + "'"));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, PUBLISHING_TABLE, "game ='" + g.getId() +"' AND publisher = '" + ap.getId() + "'"));
	}

	@Test
	public void testRemoveSpecificPublisher(){
		Game g 		 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Publisher p  = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Publisher ap = TestMethods.addPublisher(ALTERNATIVE_PUBLISHER_NAME, ALTERNATIVE_PUBLISHER_LOGO, pubInsert);

		TestMethods.connectPub(g, p, publishingInsert);
		TestMethods.connectPub(g, ap, publishingInsert);

		gameDao.removePublisher(g, p);

		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PUBLISHING_TABLE));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, PUBLISHING_TABLE, "game ='" + g.getId() +"' AND publisher = '" + ap.getId() + "'"));
	}

	@Test
	public void testRemoveAllPublishers(){
		Game g 		 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Publisher p  = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Publisher ap = TestMethods.addPublisher(ALTERNATIVE_PUBLISHER_NAME, ALTERNATIVE_PUBLISHER_LOGO, pubInsert);

		TestMethods.connectPub(g, p, publishingInsert);
		TestMethods.connectPub(g, ap, publishingInsert);

		gameDao.removeAllPublishers(g);

		Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, PUBLISHING_TABLE));
	}

	@Test
	public void testAddGenre(){
		Game g 			= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Genre gen 		= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		gameDao.addGenre(g, gen);
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, CLASSIFICATION_TABLE, "game ='" + g.getId() +"' AND genre = '" + gen.getId() + "'"));
	}

	final static String ALTERNATIVE_GENRE_NAME 	= "fps";
	final static String ALTERNATIVE_GENRE_LOGO	= "metal-gear.jpg";

	@Test
	public void testAddGenres(){
		Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Genre gen  	= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Genre agen 	= TestMethods.addGenre(ALTERNATIVE_GENRE_NAME, ALTERNATIVE_GENRE_LOGO, genreInsert);

		List<Long> gen_ids = Arrays.asList(gen.getId(), agen.getId());

		gameDao.addGenres(g.getId(), gen_ids);

		Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLASSIFICATION_TABLE));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, CLASSIFICATION_TABLE, "game ='" + g.getId() +"' AND genre = '" + gen.getId() + "'"));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, CLASSIFICATION_TABLE, "game ='" + g.getId() +"' AND genre = '" + agen.getId() + "'"));
	}

	@Test
	public void testRemoveSpecificGenre(){
		Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Genre gen  	= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Genre agen 	= TestMethods.addGenre(ALTERNATIVE_GENRE_NAME, ALTERNATIVE_GENRE_LOGO, genreInsert);

		TestMethods.connectGenre(g, gen, classificationInsert);
		TestMethods.connectGenre(g, agen, classificationInsert);

		gameDao.removeGenre(g, gen);

		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLASSIFICATION_TABLE));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, CLASSIFICATION_TABLE, "game ='" + g.getId() +"' AND genre = '" + agen.getId() + "'"));
	}

	@Test
	public void testRemoveAllGenres(){
		Game g 	 	= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Genre gen  	= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Genre agen 	= TestMethods.addGenre(ALTERNATIVE_GENRE_NAME, ALTERNATIVE_GENRE_LOGO, genreInsert);

		TestMethods.connectGenre(g, gen, classificationInsert);
		TestMethods.connectGenre(g, agen, classificationInsert);

		gameDao.removeAllGenres(g);

		Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLASSIFICATION_TABLE));
	}
/*
	@Test
	public void testAddReleaseDate(){
		Game g 			= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Region region 	= TestMethods.addRegion(REGION_NAME, REGION_SHORT, regionInsert);
		Release release = TestMethods.makeRelease(g, region, RELEASE_DATE);
		gameDao.addReleaseDate(g, release);
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, RELEASE_TABLE, "game ='" + g.getId()
				+ "' AND region = '" + release.getRegion().getId()
				+ "' AND release_date = '" + RELEASE_DATE.toLocalDate() + "'"));
	}

	final static String ALTERNATIVE_REGION_NAME 	= "Japan";
	final static String ALTERNATIVE_REGION_SHORT	= "JP";
	final static Date   ALTERNATIVE_RELEASE_DATE	= Date.valueOf(LocalDate.now().minusDays(2));
	*/
/*
	@Test
	public void testAddReleaseDates(){
		Game g 			 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Region region 	 = TestMethods.addRegion(REGION_NAME, REGION_SHORT, regionInsert);
		Release release  = TestMethods.makeRelease(g, region, RELEASE_DATE);
		Region aregion 	 = TestMethods.addRegion(ALTERNATIVE_REGION_NAME, ALTERNATIVE_REGION_SHORT, regionInsert);
		Release arelease = TestMethods.makeRelease(g, aregion, ALTERNATIVE_RELEASE_DATE);

		Map<Long, LocalDate> map = Stream.of(release, arelease).collect(Collectors.toMap(r -> r.getRegion().getId(), r -> {
			LocalDate         date          = r.getDate().toLocalDate();
			DateTimeFormatter formatter     = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String            plainTextDate = date.format(formatter);
			return LocalDate.parse(plainTextDate, formatter);
		}));

		gameDao.addReleaseDates(g.getId(), map);

		Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, RELEASE_TABLE));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, RELEASE_TABLE, "game ='" + g.getId()
				+"' AND region = '" + region.getId() + "' AND release_date = '" + RELEASE_DATE.toLocalDate() + "'"));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, RELEASE_TABLE, "game ='" + g.getId()
				+"' AND region = '" + aregion.getId() + "' AND release_date = '" + ALTERNATIVE_RELEASE_DATE.toLocalDate() + "'"));
	}
*//*
	@Test
	public void testRemoveSpecificReleaseDate(){
		Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Region region 	 = TestMethods.addRegion(REGION_NAME, REGION_SHORT, regionInsert);
		Region aregion 	 = TestMethods.addRegion(ALTERNATIVE_REGION_NAME, ALTERNATIVE_REGION_SHORT, regionInsert);

		Release release  = TestMethods.addRelease(g, region, RELEASE_DATE, releaseInsert);
		TestMethods.addRelease(g, aregion, ALTERNATIVE_RELEASE_DATE, releaseInsert);

		gameDao.removeReleaseDate(g, release);

		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, RELEASE_TABLE));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, RELEASE_TABLE, "game ='" + g.getId()
				+ "' AND region = '" + aregion.getId() + "' AND release_date = '" + ALTERNATIVE_RELEASE_DATE.toLocalDate() + "'"));
	}

	@Test
	public void testRemoveAllReleaseDates(){
		Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Region region 	 = TestMethods.addRegion(REGION_NAME, REGION_SHORT, regionInsert);
		Region aregion 	 = TestMethods.addRegion(ALTERNATIVE_REGION_NAME, ALTERNATIVE_REGION_SHORT, regionInsert);

		TestMethods.addRelease(g, region, RELEASE_DATE, releaseInsert);
		TestMethods.addRelease(g, aregion, ALTERNATIVE_RELEASE_DATE, releaseInsert);

		gameDao.removeAllReleaseDates(g);

		Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, RELEASE_TABLE));
	}
	*/

	static final String NON_MATCHING_GAME_TITLE = "Hello";
	static final String MATCHING_GAME_TITLE		= "Hola";

	static final String SEARCH_TERM				= "ola";

	@Test
	public void testSearchByTitle(){
		TestMethods.addGame(NON_MATCHING_GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		TestMethods.addGame(MATCHING_GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		List<Game> matches 			= gameDao.searchByTitle(SEARCH_TERM, 1, 2);
		Assert.assertEquals(1, matches.size());
		Optional<Game> maybeMatch 	= matches.stream().findFirst();
		Assert.assertTrue(maybeMatch.isPresent());
		Game match					= maybeMatch.get();
		Assert.assertEquals(MATCHING_GAME_TITLE, match.getTitle());
	}

	static final Date UPCOMING_GAME 		= Date.valueOf(LocalDate.now().plusDays(5));
	static final Date RELEASED_TODAY_GAME 	= Date.valueOf(LocalDate.now());
	static final Date RELEASED_GAME 		= Date.valueOf(LocalDate.now().minusDays(2));

	static final String USER_NAME 		= "root";
	static final String USER_PASSWORD 	= "root";
	static final String USER_EMAIL		= "root@questlog.com";
	static final String USER_LOCALE		= "en";

	@Test
	public void testIsInBacklogWhenItIs(){
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		User u = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, userInsert);
		TestMethods.addBacklog(g, u, backlogInsert);
		Assert.assertTrue(gameDao.isInBacklog(g.getId(), u));
	}

	@Test
	public void testIsInBacklogWhenItIsNot(){
		Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		User u = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, userInsert);
		Assert.assertFalse(gameDao.isInBacklog(g.getId(), u));
	}

	private static final String ALTERNATIVE_GAME_TITLE 	= "Death Stranding";
	private static final String ALTERNATIVE_GAME_COVER 	= "death-stranding.jpg";
	private static final String ALTERNATIVE_GAME_DESC 	= "Venture into a post-apocalyptic world in orden to find connections, saving yourself and the world.";

	@Test
	public void removeFromBacklog(){
		Game g 	= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		User u 	= TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, userInsert);
		TestMethods.addBacklog(g, u, backlogInsert);
		TestMethods.addBacklog(ag, u, backlogInsert);
		gameDao.removeFromBacklog(g.getId(), u);
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, BACKLOG_TABLE));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, BACKLOG_TABLE, "game ='" + ag.getId()
				+ "' AND user_id = '" + u.getId() + "'"));
	}

	private static final String ALTERNATIVE_USER_NAME 		= "admin";
	private static final String ALTERNATIVE_USER_PASSWORD 	= "admin";
	private static final String ALTERNATIVE_USER_EMAIL		= "admin@questlog.com";
	private static final String ALTERNATIVE_USER_LOCALE		= "es";

	private static final String ANOTHER_ALTERNATIVE_GAME_TITLE = "Pokemon Sword";
	private static final String ANOTHER_ALTERNATIVE_GAME_COVER = "pkmn-sword.jpg";
	private static final String ANOTHER_ALTERNATIVE_GAME_DESC  = "Throw yourself into the Galar Region! A region where Pokemon Battles take place in a huge stadium and Pokemon become as huge.";

	@Test
	public void testGetGamesInBacklog(){
		Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		User u 	 = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, userInsert);
		User au  = TestMethods.addUser(ALTERNATIVE_USER_NAME, ALTERNATIVE_USER_PASSWORD, ALTERNATIVE_USER_EMAIL, ALTERNATIVE_USER_LOCALE, userInsert);

		TestMethods.addBacklog(g, u, backlogInsert);
		TestMethods.addBacklog(ag, u, backlogInsert);
		TestMethods.addBacklog(ag, au, backlogInsert);
		TestMethods.addBacklog(aag, au, backlogInsert);

		List<Game> inBacklog = gameDao.getGamesInBacklog(u);

		Assert.assertEquals(2, inBacklog.size());
		Iterator<Game> it = inBacklog.iterator();
		Game next = it.next();
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, BACKLOG_TABLE, "game ='" + next.getId()
				+ "' AND user_id = '" + u.getId() + "'"));
		next = it.next();
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, BACKLOG_TABLE, "game ='" + next.getId()
				+ "' AND user_id = '" + u.getId() + "'"));
	}

	@Test
	public void testGetGamesInBacklogPaginated(){
		Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		User u 	 = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, userInsert);
		User au  = TestMethods.addUser(ALTERNATIVE_USER_NAME, ALTERNATIVE_USER_PASSWORD, ALTERNATIVE_USER_EMAIL, ALTERNATIVE_USER_LOCALE, userInsert);

		TestMethods.addBacklog(g, u, backlogInsert);
		TestMethods.addBacklog(ag, u, backlogInsert);
		TestMethods.addBacklog(ag, au, backlogInsert);
		TestMethods.addBacklog(aag, au, backlogInsert);

		List<Game> inBacklog = gameDao.getGamesInBacklog(u, 1, 1);

		Assert.assertEquals(1, inBacklog.size());
		Iterator<Game> it = inBacklog.iterator();
		Game next = it.next();
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, BACKLOG_TABLE, "game ='" + next.getId()
				+ "' AND user_id = '" + u.getId() + "'"));
	}

	@Test
	public void testCountGamesInBacklog(){
		Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		User u 	 = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, userInsert);
		User au  = TestMethods.addUser(ALTERNATIVE_USER_NAME, ALTERNATIVE_USER_PASSWORD, ALTERNATIVE_USER_EMAIL, ALTERNATIVE_USER_LOCALE, userInsert);

		TestMethods.addBacklog(g, u, backlogInsert);
		TestMethods.addBacklog(ag, u, backlogInsert);
		TestMethods.addBacklog(ag, au, backlogInsert);
		TestMethods.addBacklog(aag, au, backlogInsert);

		int amout = gameDao.countGamesInBacklog(u);

		Assert.assertEquals(2, amout);
	}

	private static final String ANOTHER_ALTERNATIVE_USER_NAME 		= "agua2";
	private static final String ANOTHER_ALTERNATIVE_USER_PASSWORD 	= "ague2";
	private static final String ANOTHER_ALTERNATIVE_USER_EMAIL		= "agua@questlog.com";
	private static final String ANOTHER_ALTERNATIVE_USER_LOCALE		= "es";

	private static final String NINTENDO_USER			= "nintendo";
	private static final String NINTENDO_USER_EMAIL		= "nintendo@questlog.com";

	private static final String PLAYSTATION_USER		= "playstation";
	private static final String PLAYSTATION_USER_EMAIL	= "playstation@questlog.com";

	@Test
	public void testGetSimilarToBacklog(){
		Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game sw  = TestMethods.addGame("Star Wars Republic", ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		User u 	 = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, userInsert);
		User au  = TestMethods.addUser(ALTERNATIVE_USER_NAME, ALTERNATIVE_USER_PASSWORD, ALTERNATIVE_USER_EMAIL, ALTERNATIVE_USER_LOCALE, userInsert);
		User aau  = TestMethods.addUser(ANOTHER_ALTERNATIVE_USER_NAME, ANOTHER_ALTERNATIVE_USER_PASSWORD, ANOTHER_ALTERNATIVE_USER_EMAIL, ANOTHER_ALTERNATIVE_USER_LOCALE, userInsert);
		User nu  = TestMethods.addUser(NINTENDO_USER, ANOTHER_ALTERNATIVE_USER_PASSWORD, NINTENDO_USER_EMAIL, ANOTHER_ALTERNATIVE_USER_LOCALE, userInsert);
		User pu  = TestMethods.addUser(PLAYSTATION_USER, ANOTHER_ALTERNATIVE_USER_PASSWORD, PLAYSTATION_USER_EMAIL, ANOTHER_ALTERNATIVE_USER_LOCALE, userInsert);

		TestMethods.addBacklog(g, u, backlogInsert);
		TestMethods.addBacklog(g, au, backlogInsert);
		TestMethods.addBacklog(g, aau, backlogInsert);
		TestMethods.addBacklog(g, nu, backlogInsert);
		TestMethods.addBacklog(ag, u, backlogInsert);
		TestMethods.addBacklog(ag, au, backlogInsert);
		TestMethods.addBacklog(ag, aau, backlogInsert);
		TestMethods.addBacklog(ag, nu, backlogInsert);
		TestMethods.addBacklog(ag, pu, backlogInsert);

		List<Game> similar = gameDao.getSimilarToBacklog(u);

		Assert.assertEquals(1, similar.size());
	}

	@Test
	public void testGetMostBacklogged(){
		Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		User u 	 = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, userInsert);
		User au  = TestMethods.addUser(ALTERNATIVE_USER_NAME, ALTERNATIVE_USER_PASSWORD, ALTERNATIVE_USER_EMAIL, ALTERNATIVE_USER_LOCALE, userInsert);
		User aau  = TestMethods.addUser(ANOTHER_ALTERNATIVE_USER_NAME, ANOTHER_ALTERNATIVE_USER_PASSWORD, ANOTHER_ALTERNATIVE_USER_EMAIL, ANOTHER_ALTERNATIVE_USER_LOCALE, userInsert);
		User nu  = TestMethods.addUser(NINTENDO_USER, ANOTHER_ALTERNATIVE_USER_PASSWORD, NINTENDO_USER_EMAIL, ANOTHER_ALTERNATIVE_USER_LOCALE, userInsert);

		TestMethods.addBacklog(g, u, backlogInsert);
		TestMethods.addBacklog(g, au, backlogInsert);
		TestMethods.addBacklog(g, aau, backlogInsert);
		TestMethods.addBacklog(g, nu, backlogInsert);

		List<Game> popular = gameDao.getMostBacklogged();

		Assert.assertEquals(1, popular.size());
	}

	@Test
	public void testGetFiltered(){
		Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game sw  = TestMethods.addGame("Star Wars Republic", ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		User u 	 = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, userInsert);
		Genre gen  = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		TestMethods.connectGenre(g, gen, classificationInsert);
		TestMethods.connectGenre(ag, gen, classificationInsert);
		TestMethods.connectGenre(aag, gen, classificationInsert);
		TestMethods.connectGenre(sw, gen, classificationInsert);
		TestMethods.connectPlatform(g, p, versionInsert);
		TestMethods.connectPlatform(ag, p, versionInsert);
		TestMethods.connectPlatform(aag, p, versionInsert);
		TestMethods.connectPlatform(sw, p, versionInsert);
		TestMethods.addScore(u, g, 7, scoreInsert);
		TestMethods.addScore(u, ag, 5, scoreInsert);
		TestMethods.addScore(u, aag, 6, scoreInsert);
		TestMethods.addScore(u, sw, 9, scoreInsert);
		List<Game> games = gameDao.getFilteredGames("", Collections.singletonList(gen.getId() + ""),Collections.singletonList(p.getId() + ""), 5, 8, 0, 0, 1, 4 );
		Assert.assertEquals(2, games.size());
	}

	@Test
	public void testCountSearchResults(){
		TestMethods.addGame(NON_MATCHING_GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		TestMethods.addGame(MATCHING_GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		int matches 			= gameDao.countSearchResults(SEARCH_TERM);
		Assert.assertEquals(1, matches);
	}

	@Test
	public void testGetGamesForPlatformAllGamesSinglePage(){
		Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform ap = TestMethods.addPlatform(ALTERNATIVE_PLATFORM_NAME, ALTERNATIVE_PLATFORM_SHORT_NAME, ALTERNATIVE_PLATFORM_LOGO, platformInsert);

		TestMethods.connectPlatform(g, p, versionInsert);
		TestMethods.connectPlatform(aag, p, versionInsert);
		TestMethods.connectPlatform(aag, ap, versionInsert);
		TestMethods.connectPlatform(ag, ap, versionInsert);

		List<Game> games = gameDao.getGamesForPlatform(p, 1, 2);

		Assert.assertNotNull(games);
		Assert.assertEquals(2, games.size());
		Assert.assertTrue(games.contains(g));
		Assert.assertTrue(games.contains(aag));
	}

	@Test
	public void testGetGamesForPlatformAllGamesSingleTwoPages(){
		Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform ap = TestMethods.addPlatform(ALTERNATIVE_PLATFORM_NAME, ALTERNATIVE_PLATFORM_SHORT_NAME, ALTERNATIVE_PLATFORM_LOGO, platformInsert);

		TestMethods.connectPlatform(g, p, versionInsert);
		TestMethods.connectPlatform(aag, p, versionInsert);
		TestMethods.connectPlatform(aag, ap, versionInsert);
		TestMethods.connectPlatform(ag, ap, versionInsert);

		List<Game> games2 = gameDao.getGamesForPlatform(p,2,1);

		Assert.assertNotNull(games2);
		Assert.assertEquals(1, games2.size());
	}


	@Test
	public void testCountGamesForPlatform(){
		Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform ap = TestMethods.addPlatform(ALTERNATIVE_PLATFORM_NAME, ALTERNATIVE_PLATFORM_SHORT_NAME, ALTERNATIVE_PLATFORM_LOGO, platformInsert);

		TestMethods.connectPlatform(g, p, versionInsert);
		TestMethods.connectPlatform(aag, p, versionInsert);
		TestMethods.connectPlatform(aag, ap, versionInsert);
		TestMethods.connectPlatform(ag, ap, versionInsert);

		int amount = gameDao.countGamesForPlatform(p);

		Assert.assertEquals(2, amount);
	}


	@Test
	public void testGetGamesForGenreAllGamesSinglePage(){
		Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Genre gen 		= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Genre agen		= TestMethods.addGenre(ALTERNATIVE_GENRE_NAME, ALTERNATIVE_GENRE_LOGO, genreInsert);

		TestMethods.connectGenre(g, gen, classificationInsert);
		TestMethods.connectGenre(aag, gen, classificationInsert);
		TestMethods.connectGenre(aag, agen, classificationInsert);
		TestMethods.connectGenre(ag, agen, classificationInsert);

		List<Game> games = gameDao.getGamesForGenre(gen, 1, 2);

		Assert.assertNotNull(games);
		Assert.assertEquals(2, games.size());
		Assert.assertTrue(games.contains(g));
		Assert.assertTrue(games.contains(aag));
	}

	@Test
	public void testGetGamesForGenreAllGamesSingleTwoPages(){
		Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Genre gen 		= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Genre agen		= TestMethods.addGenre(ALTERNATIVE_GENRE_NAME, ALTERNATIVE_GENRE_LOGO, genreInsert);

		TestMethods.connectGenre(g, gen, classificationInsert);
		TestMethods.connectGenre(aag, gen, classificationInsert);
		TestMethods.connectGenre(aag, agen, classificationInsert);
		TestMethods.connectGenre(ag, agen, classificationInsert);

		List<Game> games2 = gameDao.getGamesForGenre(gen,2,1);

		Assert.assertNotNull(games2);
		Assert.assertEquals(1, games2.size());
	}

	@Test
	public void testCountGetGamesForGenre(){
		Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Genre gen 		= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Genre agen		= TestMethods.addGenre(ALTERNATIVE_GENRE_NAME, ALTERNATIVE_GENRE_LOGO, genreInsert);

		TestMethods.connectGenre(g, gen, classificationInsert);
		TestMethods.connectGenre(aag, gen, classificationInsert);
		TestMethods.connectGenre(aag, agen, classificationInsert);
		TestMethods.connectGenre(ag, agen, classificationInsert);

		int amount = gameDao.countGamesForGenre(gen);

		Assert.assertEquals(2, amount);
	}

	@Test
	public void testGetGamesForDeveloperAllGamesSinglePage(){
		Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Developer d 	= TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		Developer ad	= TestMethods.addDeveloper(ALTERNATIVE_DEVELOPER_NAME, ALTERNATIVE_DEVELOPER_LOGO, devInsert);

		TestMethods.connectDev(g, d, developmentInsert);
		TestMethods.connectDev(aag, d, developmentInsert);
		TestMethods.connectDev(aag, ad, developmentInsert);
		TestMethods.connectDev(ag, ad, developmentInsert);

		List<Game> games = gameDao.getGamesForDeveloper(d, 1, 2);

		Assert.assertNotNull(games);
		Assert.assertEquals(2, games.size());
		Assert.assertTrue(games.contains(g));
		Assert.assertTrue(games.contains(aag));
	}

	@Test
	public void testGetGamesForDeveloperAllGamesSingleTwoPages(){
		Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Developer d 	= TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		Developer ad	= TestMethods.addDeveloper(ALTERNATIVE_DEVELOPER_NAME, ALTERNATIVE_DEVELOPER_LOGO, devInsert);

		TestMethods.connectDev(g, d, developmentInsert);
		TestMethods.connectDev(aag, d, developmentInsert);
		TestMethods.connectDev(aag, ad, developmentInsert);
		TestMethods.connectDev(ag, ad, developmentInsert);

		List<Game> games2 = gameDao.getGamesForDeveloper(d, 2,1);

		Assert.assertNotNull(games2);
		Assert.assertEquals(1, games2.size());
	}

	@Test
	public void testCountGetGamesForDeveloper(){
		Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Developer d 	= TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		Developer ad	= TestMethods.addDeveloper(ALTERNATIVE_DEVELOPER_NAME, ALTERNATIVE_DEVELOPER_LOGO, devInsert);

		TestMethods.connectDev(g, d, developmentInsert);
		TestMethods.connectDev(aag, d, developmentInsert);
		TestMethods.connectDev(aag, ad, developmentInsert);
		TestMethods.connectDev(ag, ad, developmentInsert);

		int amount = gameDao.countGamesForDeveloper(d);

		Assert.assertEquals(2, amount);
	}

	@Test
	public void testGetGamesForPublisherAllGamesSinglePage(){
		Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Publisher p 	= TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Publisher ap	= TestMethods.addPublisher(ALTERNATIVE_PUBLISHER_NAME, ALTERNATIVE_PUBLISHER_LOGO, pubInsert);

		TestMethods.connectPub(g, p, publishingInsert);
		TestMethods.connectPub(aag, p, publishingInsert);
		TestMethods.connectPub(aag, ap, publishingInsert);
		TestMethods.connectPub(ag, ap, publishingInsert);

		List<Game> games = gameDao.getGamesForPublisher(p, 1, 2);

		Assert.assertNotNull(games);
		Assert.assertEquals(2, games.size());
		Assert.assertTrue(games.contains(g));
		Assert.assertTrue(games.contains(aag));
	}

	@Test
	public void testGetGamesForPublisherAllGamesSingleTwoPages(){
		Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Publisher p 	= TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Publisher ap	= TestMethods.addPublisher(ALTERNATIVE_PUBLISHER_NAME, ALTERNATIVE_PUBLISHER_LOGO, pubInsert);

		TestMethods.connectPub(g, p, publishingInsert);
		TestMethods.connectPub(aag, p, publishingInsert);
		TestMethods.connectPub(aag, ap, publishingInsert);
		TestMethods.connectPub(ag, ap, publishingInsert);

		List<Game> games2 = gameDao.getGamesForPublisher(p, 2,1);

		Assert.assertNotNull(games2);
		Assert.assertEquals(1, games2.size());
	}

	@Test
	public void testCountGetGamesForPublisher(){
		Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		Publisher p 	= TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Publisher ap	= TestMethods.addPublisher(ALTERNATIVE_PUBLISHER_NAME, ALTERNATIVE_PUBLISHER_LOGO, pubInsert);

		TestMethods.connectPub(g, p, publishingInsert);
		TestMethods.connectPub(aag, p, publishingInsert);
		TestMethods.connectPub(aag, ap, publishingInsert);
		TestMethods.connectPub(ag, ap, publishingInsert);

		int amount = gameDao.countGamesForPublisher(p);

		Assert.assertEquals(2, amount);
	}

	@Test
	public void testRemove(){
		Game g  = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game ag = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, gameInsert);
		gameDao.remove(g);
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, GAME_TABLE));
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, GAME_TABLE, "game = '" + ag.getId() + "'"));
	}

	@Test
	public void testUpdate(){
		Game g  = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		g.setTitle(ALTERNATIVE_GAME_TITLE);
		g.setCover(ALTERNATIVE_GAME_COVER);
		g.setDescription(ALTERNATIVE_GAME_DESC);
		gameDao.update(g);
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, GAME_TABLE, "game = '" + g.getId()
				+ "' AND title = '" + ALTERNATIVE_GAME_TITLE + "' AND description = '" + ALTERNATIVE_GAME_DESC
				+ "' AND cover = '" + ALTERNATIVE_GAME_COVER + "'"));
	}

	@Test
	public void testUpdateWithoutCover(){
		Game g  = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		g.setTitle(ALTERNATIVE_GAME_TITLE);
		g.setCover(ALTERNATIVE_GAME_COVER);
		g.setDescription(ALTERNATIVE_GAME_DESC);
		gameDao.updateWithoutCover(g);
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, GAME_TABLE, "game = '" + g.getId()
				+ "' AND title = '" + ALTERNATIVE_GAME_TITLE + "' AND description = '" + ALTERNATIVE_GAME_DESC
				+ "' AND cover = '" + GAME_COVER + "'"));
	}

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