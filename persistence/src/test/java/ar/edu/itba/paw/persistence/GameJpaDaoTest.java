package ar.edu.itba.paw.persistence;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import ar.edu.itba.paw.model.entity.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class GameJpaDaoTest
{
    private static final String GAME_TABLE 				= "games";
    private static final String GAME_TITLE 				= "Example Game";
    private static final String GAME_COVER 				= "http://sega.com/game.jpg";
    private static final String GAME_DESC 				= "Explore the world!";
    private static final String GAME_TRAILER			= "DpHDJRGuL7w";
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
    private static final LocalDate RELEASE_DATE 		= LocalDate.now();
    private static final String USER_TABLE				= "users";
    private static final String BACKLOG_TABLE			= "backlogs";
    private static final String SCORE_TABLE				= "scores";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GameJpaDao gameDao;

    
    @After
    public void cleanTables(){
        TestMethods.deleteFromTable(SCORE_TABLE, em);
        TestMethods.deleteFromTable(PUBLISHING_TABLE, em);
        TestMethods.deleteFromTable(CLASSIFICATION_TABLE, em);
        TestMethods.deleteFromTable(VERSION_TABLE, em);
        TestMethods.deleteFromTable(RELEASE_TABLE, em);
        TestMethods.deleteFromTable(DEVELOPMENT_TABLE, em);
        TestMethods.deleteFromTable(BACKLOG_TABLE, em);
        TestMethods.deleteFromTable(GENRE_TABLE, em);
        TestMethods.deleteFromTable(DEVELOPER_TABLE, em);
        TestMethods.deleteFromTable(PUBLISHER_TABLE, em);
        TestMethods.deleteFromTable(PLATFORM_TABLE, em);
        TestMethods.deleteFromTable(REGION_TABLE, em);
        TestMethods.deleteFromTable(USER_TABLE, em);
        TestMethods.deleteFromTable(GAME_TABLE, em);
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
        Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Optional<Game> maybeGame = gameDao.findById(g.getId());
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertEquals(GAME_TITLE, maybeGame.get().getTitle());
        Assert.assertEquals(GAME_COVER, maybeGame.get().getCover());
        Assert.assertEquals(GAME_DESC, maybeGame.get().getDescription());
    }

    @Test
    public void testFindGameByIdWithDetailsGetsPlatform(){
        Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Platform p 						= TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
        TestMethods.connectPlatform(g, p, em);
        Optional<Game> maybeGame 	= gameDao.findByIdWithDetails(g.getId());
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertTrue(maybeGame.get().getPlatforms().stream().findFirst().isPresent());
        Assert.assertEquals(PLATFORM_NAME, maybeGame.get().getPlatforms().stream().findFirst().get().getName());
        Assert.assertEquals(PLATFORM_SHORT_NAME, maybeGame.get().getPlatforms().stream().findFirst().get().getShortName());
        Assert.assertEquals(PLATFORM_LOGO, maybeGame.get().getPlatforms().stream().findFirst().get().getLogo());
    }

    @Test
    public void testFindGameByIdWithDetailsGetsDevelopers(){
        Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Developer d 					= TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
        TestMethods.connectDev(g, d, em);
        Optional<Game> maybeGame 	= gameDao.findByIdWithDetails(g.getId());
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertTrue(maybeGame.get().getDevelopers().stream().findFirst().isPresent());
        Assert.assertEquals(DEVELOPER_NAME, maybeGame.get().getDevelopers().stream().findFirst().get().getName());
        Assert.assertEquals(DEVELOPER_LOGO, maybeGame.get().getDevelopers().stream().findFirst().get().getLogo());
    }

    @Test
    public void testFindGameByIdWithDetailsGetsPublishers(){
        Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Publisher p 					= TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, em);
        TestMethods.connectPub(g, p, em);
        Optional<Game> maybeGame 	= gameDao.findByIdWithDetails(g.getId());
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertTrue(maybeGame.get().getPublishers().stream().findFirst().isPresent());
        Assert.assertEquals(PUBLISHER_NAME, maybeGame.get().getPublishers().stream().findFirst().get().getName());
        Assert.assertEquals(PUBLISHER_LOGO, maybeGame.get().getPublishers().stream().findFirst().get().getLogo());
    }

    @Test
    public void testFindGameByIdWithDetailsGetsGenres(){
        Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Genre gen 						= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
        TestMethods.connectGenre(g, gen, em);
        Optional<Game> maybeGame 	= gameDao.findByIdWithDetails(g.getId());
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertTrue(maybeGame.get().getGenres().stream().findFirst().isPresent());
        Assert.assertEquals(GENRE_NAME, maybeGame.get().getGenres().stream().findFirst().get().getName());
        Assert.assertEquals(GENRE_LOGO, maybeGame.get().getGenres().stream().findFirst().get().getLogo());
    }

    @Test
    public void testFindGameByIdWithDetailsGetsReleaseDates(){
        Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Region region 					= TestMethods.addRegion(REGION_NAME, REGION_SHORT, em);
        TestMethods.addRelease(g, region, RELEASE_DATE, em);
        System.out.println(g.getId());
        Optional<Game> maybeGame 	= gameDao.findByIdWithDetails(g.getId());
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
        TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Optional<Game> maybeGame = gameDao.findByTitle(GAME_TITLE);
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertEquals(GAME_TITLE, maybeGame.get().getTitle());
        Assert.assertEquals(GAME_COVER, maybeGame.get().getCover());
        Assert.assertEquals(GAME_DESC, maybeGame.get().getDescription());
    }

    @Test
    public void testFindGameByTitleWithDetailsGetsPlatform(){
        Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Platform p 						= TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
        TestMethods.connectPlatform(g, p, em);
        Optional<Game> maybeGame 	= gameDao.findByTitleWithDetails(GAME_TITLE);
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertTrue(maybeGame.get().getPlatforms().stream().findFirst().isPresent());
        Assert.assertEquals(PLATFORM_NAME, maybeGame.get().getPlatforms().stream().findFirst().get().getName());
        Assert.assertEquals(PLATFORM_SHORT_NAME, maybeGame.get().getPlatforms().stream().findFirst().get().getShortName());
        Assert.assertEquals(PLATFORM_LOGO, maybeGame.get().getPlatforms().stream().findFirst().get().getLogo());
    }

    @Test
    public void testFindGameByTitleWithDetailsGetsDevelopers(){
        Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Developer d 					= TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
        TestMethods.connectDev(g, d, em);
        Optional<Game> maybeGame 	= gameDao.findByTitleWithDetails(GAME_TITLE);
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertTrue(maybeGame.get().getDevelopers().stream().findFirst().isPresent());
        Assert.assertEquals(DEVELOPER_NAME, maybeGame.get().getDevelopers().stream().findFirst().get().getName());
        Assert.assertEquals(DEVELOPER_LOGO, maybeGame.get().getDevelopers().stream().findFirst().get().getLogo());
    }

    @Test
    public void testFindGameByTitleWithDetailsGetsPublishers(){
        Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Publisher p 					= TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, em);
        TestMethods.connectPub(g, p, em);
        Optional<Game> maybeGame 	= gameDao.findByTitleWithDetails(GAME_TITLE);
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertTrue(maybeGame.get().getPublishers().stream().findFirst().isPresent());
        Assert.assertEquals(PUBLISHER_NAME, maybeGame.get().getPublishers().stream().findFirst().get().getName());
        Assert.assertEquals(PUBLISHER_LOGO, maybeGame.get().getPublishers().stream().findFirst().get().getLogo());
    }

    @Test
    public void testFindGameByTitleWithDetailsGetsGenres(){
        Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Genre gen 						= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
        TestMethods.connectGenre(g, gen, em);
        Optional<Game> maybeGame 	= gameDao.findByTitleWithDetails(GAME_TITLE);
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertTrue(maybeGame.get().getGenres().stream().findFirst().isPresent());
        Assert.assertEquals(GENRE_NAME, maybeGame.get().getGenres().stream().findFirst().get().getName());
        Assert.assertEquals(GENRE_LOGO, maybeGame.get().getGenres().stream().findFirst().get().getLogo());
    }

    @Test
    public void testFindGameByTitleWithDetailsGetsReleaseDates(){
        Game g 							= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Region region 					= TestMethods.addRegion(REGION_NAME, REGION_SHORT, em);
        TestMethods.addRelease(g, region, RELEASE_DATE, em);
        Optional<Game> maybeGame 	= gameDao.findByTitleWithDetails(GAME_TITLE);
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
        Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Optional<Game> maybeGame = gameDao.changeTitle(g.getId(), "Zenobureido");
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertEquals("Zenobureido", maybeGame.get().getTitle());
        Assert.assertEquals(GAME_COVER, maybeGame.get().getCover());
        Assert.assertEquals(GAME_DESC, maybeGame.get().getDescription());
    }

    @Test
    public void	testChangeGameCover()
    {
        Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Optional<Game> maybeGame = gameDao.changeCover(g.getId(), "http://fake.com/image.png");
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertEquals(GAME_TITLE, maybeGame.get().getTitle());
        Assert.assertEquals("http://fake.com/image.png", maybeGame.get().getCover());
        Assert.assertEquals(GAME_DESC, maybeGame.get().getDescription());
    }

    @Test
    public void	testChangeGameDesc()
    {
        Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Optional<Game> maybeGame = gameDao.changeDescription(g.getId(), "This is a new description!");
        Assert.assertTrue(maybeGame.isPresent());
        Assert.assertEquals(GAME_TITLE, maybeGame.get().getTitle());
        Assert.assertEquals(GAME_COVER, maybeGame.get().getCover());
        Assert.assertEquals("This is a new description!", maybeGame.get().getDescription());
    }

    public void testRegister()
    {
        Game g = gameDao.register(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER);
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(GAME_TABLE,"title ='" + GAME_TITLE +"' AND cover = '"
                + GAME_COVER + "' AND description = '" + GAME_DESC + "'" + " AND trailer = '" + GAME_TRAILER + "'", em));
    }

    @Test
    public void	testGetAllGames()
    {
        Game example = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game botw = TestMethods.addGame("The Legend of Zelda: Breath of the Wild", GAME_COVER, GAME_DESC, GAME_TRAILER, em);

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
        Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Platform p 	= TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
        gameDao.addPlatform(g, p);
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(VERSION_TABLE, "game ='" + g.getId() +"' AND platform = '" + p.getId() + "'", em));
    }

    final static String ALTERNATIVE_PLATFORM_NAME 		= "ps4";
    final static String ALTERNATIVE_PLATFORM_SHORT_NAME = "ps4";
    final static String ALTERNATIVE_PLATFORM_LOGO		= "ps4.jpg";

    @Test
    public void testAddPlatforms(){
        Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Platform p 	= TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
        Platform ap	= TestMethods.addPlatform(ALTERNATIVE_PLATFORM_NAME, ALTERNATIVE_PLATFORM_SHORT_NAME, ALTERNATIVE_PLATFORM_LOGO, em);

        List<Long> p_ids = Arrays.asList(p.getId(), ap.getId());

        gameDao.addPlatforms(g.getId(), p_ids);

        Assert.assertEquals(2, TestMethods.countRowsInTable(VERSION_TABLE, em));
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(VERSION_TABLE, "game ='" + g.getId() +"' AND platform = '" + p.getId() + "'", em));
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(VERSION_TABLE, "game ='" + g.getId() +"' AND platform = '" + ap.getId() + "'", em));
    }

    @Test
    public void testRemoveSpecificPlatform(){
        Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Platform p 	= TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
        Platform ap	= TestMethods.addPlatform(ALTERNATIVE_PLATFORM_NAME, ALTERNATIVE_PLATFORM_SHORT_NAME, ALTERNATIVE_PLATFORM_LOGO, em);

        TestMethods.connectPlatform(g, p, em);
        TestMethods.connectPlatform(g, ap, em);

        gameDao.removePlatform(g, p);

        Assert.assertEquals(1, TestMethods.countRowsInTable(VERSION_TABLE, em));
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(VERSION_TABLE, "game ='" + g.getId() +"' AND platform = '" + ap.getId() + "'", em));
    }

    @Test
    public void testRemoveAllPlatforms(){
        Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Platform p 	= TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
        Platform ap	= TestMethods.addPlatform(ALTERNATIVE_PLATFORM_NAME, ALTERNATIVE_PLATFORM_SHORT_NAME, ALTERNATIVE_PLATFORM_LOGO, em);

        TestMethods.connectPlatform(g, p, em);
        TestMethods.connectPlatform(g, ap, em);

        gameDao.removeAllPlatforms(g);

        Assert.assertEquals(0, TestMethods.countRowsInTable(VERSION_TABLE, em));
    }

    @Test
    public void testAddDeveloper(){
        Game g 			= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Developer d 	= TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
        gameDao.addDeveloper(g, d);

        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(DEVELOPMENT_TABLE, "game ='" + g.getId() +"' AND developer = '" + d.getId() + "'", em));
    }

    final static String ALTERNATIVE_DEVELOPER_NAME 		= "Monolith Soft";
    final static String ALTERNATIVE_DEVELOPER_LOGO		= "monolith.jpg";

    @Test
    public void testAddDevelopers(){
        Game g 		 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Developer d  = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
        Developer ad = TestMethods.addDeveloper(ALTERNATIVE_DEVELOPER_NAME, ALTERNATIVE_DEVELOPER_LOGO, em);

        List<Long> d_ids = Arrays.asList(d.getId(), ad.getId());

        gameDao.addDevelopers(g.getId(), d_ids);

        Assert.assertEquals(2, TestMethods.countRowsInTable(DEVELOPMENT_TABLE, em));
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(DEVELOPMENT_TABLE, "game ='" + g.getId() +"' AND developer = '" + d.getId() + "'", em));
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(DEVELOPMENT_TABLE, "game ='" + g.getId() +"' AND developer = '" + ad.getId() + "'", em));
    }

    @Test
    public void testRemoveSpecificDeveloper(){
        Game g 		 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Developer d  = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
        Developer ad = TestMethods.addDeveloper(ALTERNATIVE_DEVELOPER_NAME, ALTERNATIVE_DEVELOPER_LOGO, em);

        TestMethods.connectDev(g, d, em);
        TestMethods.connectDev(g, ad, em);

        gameDao.removeDeveloper(g, d);

        Assert.assertEquals(1, TestMethods.countRowsInTable(DEVELOPMENT_TABLE, em));
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(DEVELOPMENT_TABLE, "game ='" + g.getId() +"' AND developer = '" + ad.getId() + "'", em));
    }

    @Test
    public void testRemoveAllDevelopers(){
        Game g 		 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Developer d  = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
        Developer ad = TestMethods.addDeveloper(ALTERNATIVE_DEVELOPER_NAME, ALTERNATIVE_DEVELOPER_LOGO, em);

        TestMethods.connectDev(g, d, em);
        TestMethods.connectDev(g, ad, em);

        gameDao.removeAllDevelopers(g);

        Assert.assertEquals(0, TestMethods.countRowsInTable(DEVELOPMENT_TABLE, em));
    }

    @Test
    public void testAddPublisher(){
        Game g 			= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Publisher p 	= TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, em);
        gameDao.addPublisher(g, p);
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(PUBLISHING_TABLE, "game ='" + g.getId() +"' AND publisher = '" + p.getId() + "'", em));
    }

    final static String ALTERNATIVE_PUBLISHER_NAME 		= "SEGA";
    final static String ALTERNATIVE_PUBLISHER_LOGO		= "sega.jpg";

    @Test
    public void testAddPublishers(){
        Game g 		 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Publisher p  = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, em);
        Publisher ap = TestMethods.addPublisher(ALTERNATIVE_PUBLISHER_NAME, ALTERNATIVE_PUBLISHER_LOGO, em);

        List<Long> p_ids = Arrays.asList(p.getId(), ap.getId());

        gameDao.addPublishers(g.getId(), p_ids);

        Assert.assertEquals(2, TestMethods.countRowsInTable(PUBLISHING_TABLE, em));

        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(PUBLISHING_TABLE, "game ='" + g.getId() +"' AND publisher = '" + p.getId() + "'", em));
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(PUBLISHING_TABLE, "game ='" + g.getId() +"' AND publisher = '" + ap.getId() + "'", em));
    }

    @Test
    public void testRemoveSpecificPublisher(){
        Game g 		 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Publisher p  = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, em);
        Publisher ap = TestMethods.addPublisher(ALTERNATIVE_PUBLISHER_NAME, ALTERNATIVE_PUBLISHER_LOGO, em);

        TestMethods.connectPub(g, p, em);
        TestMethods.connectPub(g, ap, em);

        gameDao.removePublisher(g, p);
        
        Assert.assertEquals(1, TestMethods.countRowsInTable(PUBLISHING_TABLE, em));
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(PUBLISHING_TABLE, "game ='" + g.getId() +"' AND publisher = '" + ap.getId() + "'", em));
    }

    @Test
    public void testRemoveAllPublishers(){
        Game g 		 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Publisher p  = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, em);
        Publisher ap = TestMethods.addPublisher(ALTERNATIVE_PUBLISHER_NAME, ALTERNATIVE_PUBLISHER_LOGO, em);

        TestMethods.connectPub(g, p, em);
        TestMethods.connectPub(g, ap, em);

        gameDao.removeAllPublishers(g);
        
        Assert.assertEquals(0, TestMethods.countRowsInTable(PUBLISHING_TABLE, em));
    }

    @Test
    public void testAddGenre(){
        Game g 			= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Genre gen 		= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
        gameDao.addGenre(g, gen);
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(CLASSIFICATION_TABLE, "game ='" + g.getId() +"' AND genre = '" + gen.getId() + "'", em));
    }

    final static String ALTERNATIVE_GENRE_NAME 	= "fps";
    final static String ALTERNATIVE_GENRE_LOGO	= "metal-gear.jpg";

    @Test
    public void testAddGenres(){
        Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Genre gen  	= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
        Genre agen 	= TestMethods.addGenre(ALTERNATIVE_GENRE_NAME, ALTERNATIVE_GENRE_LOGO, em);

        List<Long> gen_ids = Arrays.asList(gen.getId(), agen.getId());

        gameDao.addGenres(g.getId(), gen_ids);

        Assert.assertEquals(2, TestMethods.countRowsInTable(CLASSIFICATION_TABLE, em));
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(CLASSIFICATION_TABLE, "game ='" + g.getId() +"' AND genre = '" + gen.getId() + "'", em));
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(CLASSIFICATION_TABLE, "game ='" + g.getId() +"' AND genre = '" + agen.getId() + "'", em));
    }

    @Test
    public void testRemoveSpecificGenre(){
        Game g 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Genre gen  	= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
        Genre agen 	= TestMethods.addGenre(ALTERNATIVE_GENRE_NAME, ALTERNATIVE_GENRE_LOGO, em);

        TestMethods.connectGenre(g, gen, em);
        TestMethods.connectGenre(g, agen, em);

        gameDao.removeGenre(g, gen);

        Assert.assertEquals(1, TestMethods.countRowsInTable(CLASSIFICATION_TABLE, em));
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(CLASSIFICATION_TABLE, "game ='" + g.getId() +"' AND genre = '" + agen.getId() + "'", em));
    }

    @Test
    public void testRemoveAllGenres(){
        Game g 	 	= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Genre gen  	= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
        Genre agen 	= TestMethods.addGenre(ALTERNATIVE_GENRE_NAME, ALTERNATIVE_GENRE_LOGO, em);

        TestMethods.connectGenre(g, gen, em);
        TestMethods.connectGenre(g, agen, em);

        gameDao.removeAllGenres(g);
        Assert.assertEquals(0, TestMethods.countRowsInTable(CLASSIFICATION_TABLE, em));
    }

    static final String NON_MATCHING_GAME_TITLE = "Hello";
    static final String MATCHING_GAME_TITLE		= "Hola";

    static final String SEARCH_TERM				= "ola";

    @Test
    public void testSearchByTitle(){
        TestMethods.addGame(NON_MATCHING_GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        TestMethods.addGame(MATCHING_GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
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
        Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        User u = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, em);
        TestMethods.addBacklog(g, u, em);
        Assert.assertTrue(gameDao.isInBacklog(g.getId(), u));
    }

    @Test
    public void testIsInBacklogWhenItIsNot(){
        Game g = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        User u = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, em);
        Assert.assertFalse(gameDao.isInBacklog(g.getId(), u));
    }

    private static final String ALTERNATIVE_GAME_TITLE 	= "Death Stranding";
    private static final String ALTERNATIVE_GAME_COVER 	= "death-stranding.jpg";
    private static final String ALTERNATIVE_GAME_DESC 	= "Venture into a post-apocalyptic world in orden to find connections, saving yourself and the world.";

    @Test
    public void removeFromBacklog(){
        Game g 	= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        User u 	= TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, em);
        TestMethods.addBacklog(g, u, em);
        TestMethods.addBacklog(ag, u, em);
        gameDao.removeFromBacklog(g.getId(), u);
        Assert.assertEquals(1, TestMethods.countRowsInTable(BACKLOG_TABLE, em));

        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(BACKLOG_TABLE, "game ='" + ag.getId()
                + "' AND user_id = '" + u.getId() + "'", em));
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
        Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        User u 	 = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, em);
        User au  = TestMethods.addUser(ALTERNATIVE_USER_NAME, ALTERNATIVE_USER_PASSWORD, ALTERNATIVE_USER_EMAIL, ALTERNATIVE_USER_LOCALE, em);

        TestMethods.addBacklog(g, u, em);
        TestMethods.addBacklog(ag, u, em);
        TestMethods.addBacklog(ag, au, em);
        TestMethods.addBacklog(aag, au, em);

        List<Game> inBacklog = gameDao.getGamesInBacklog(u);

        Assert.assertEquals(2, inBacklog.size());
        Iterator<Game> it = inBacklog.iterator();
        Game next = it.next();
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(BACKLOG_TABLE, "game ='" + next.getId()
                + "' AND user_id = '" + u.getId() + "'", em));
        next = it.next();
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(BACKLOG_TABLE, "game ='" + next.getId()
                + "' AND user_id = '" + u.getId() + "'", em));
    }

    @Test
    public void testGetGamesInBacklogPaginated(){
        Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        User u 	 = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, em);
        User au  = TestMethods.addUser(ALTERNATIVE_USER_NAME, ALTERNATIVE_USER_PASSWORD, ALTERNATIVE_USER_EMAIL, ALTERNATIVE_USER_LOCALE, em);

        TestMethods.addBacklog(g, u, em);
        TestMethods.addBacklog(ag, u, em);
        TestMethods.addBacklog(ag, au, em);
        TestMethods.addBacklog(aag, au, em);

        List<Game> inBacklog = gameDao.getGamesInBacklog(u, 1, 1);

        Assert.assertEquals(1, inBacklog.size());
        Iterator<Game> it = inBacklog.iterator();
        Game next = it.next();
        TestMethods.countRowsInTableWhere(BACKLOG_TABLE, "game ='" + next.getId()
                + "' AND user_id = '" + u.getId() + "'", em);
    }

    @Test
    public void testCountGamesInBacklog(){
        Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        User u 	 = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, em);
        User au  = TestMethods.addUser(ALTERNATIVE_USER_NAME, ALTERNATIVE_USER_PASSWORD, ALTERNATIVE_USER_EMAIL, ALTERNATIVE_USER_LOCALE, em);

        TestMethods.addBacklog(g, u, em);
        TestMethods.addBacklog(ag, u, em);
        TestMethods.addBacklog(ag, au, em);
        TestMethods.addBacklog(aag, au, em);

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
        Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        User u 	 = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, em);
        User au  = TestMethods.addUser(ALTERNATIVE_USER_NAME, ALTERNATIVE_USER_PASSWORD, ALTERNATIVE_USER_EMAIL, ALTERNATIVE_USER_LOCALE, em);
        User aau  = TestMethods.addUser(ANOTHER_ALTERNATIVE_USER_NAME, ANOTHER_ALTERNATIVE_USER_PASSWORD, ANOTHER_ALTERNATIVE_USER_EMAIL, ANOTHER_ALTERNATIVE_USER_LOCALE, em);
        User nu  = TestMethods.addUser(NINTENDO_USER, ANOTHER_ALTERNATIVE_USER_PASSWORD, NINTENDO_USER_EMAIL, ANOTHER_ALTERNATIVE_USER_LOCALE, em);
        User pu  = TestMethods.addUser(PLAYSTATION_USER, ANOTHER_ALTERNATIVE_USER_PASSWORD, PLAYSTATION_USER_EMAIL, ANOTHER_ALTERNATIVE_USER_LOCALE, em);

        TestMethods.addBacklog(g, u, em);
        TestMethods.addBacklog(g, au, em);
        TestMethods.addBacklog(g, aau, em);
        TestMethods.addBacklog(g, nu, em);
        TestMethods.addBacklog(ag, u, em);
        TestMethods.addBacklog(ag, au, em);
        TestMethods.addBacklog(ag, aau, em);
        TestMethods.addBacklog(ag, nu, em);
        TestMethods.addBacklog(ag, pu, em);

        List<Game> similar = gameDao.getSimilarToBacklog(pu);

        Assert.assertEquals(1, similar.size());
    }

    @Test
    public void testGetMostBacklogged(){
        Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        User u 	 = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, em);
        User au  = TestMethods.addUser(ALTERNATIVE_USER_NAME, ALTERNATIVE_USER_PASSWORD, ALTERNATIVE_USER_EMAIL, ALTERNATIVE_USER_LOCALE, em);
        User aau  = TestMethods.addUser(ANOTHER_ALTERNATIVE_USER_NAME, ANOTHER_ALTERNATIVE_USER_PASSWORD, ANOTHER_ALTERNATIVE_USER_EMAIL, ANOTHER_ALTERNATIVE_USER_LOCALE, em);
        User nu  = TestMethods.addUser(NINTENDO_USER, ANOTHER_ALTERNATIVE_USER_PASSWORD, NINTENDO_USER_EMAIL, ANOTHER_ALTERNATIVE_USER_LOCALE, em);

        TestMethods.addBacklog(g, u, em);
        TestMethods.addBacklog(g, au, em);
        TestMethods.addBacklog(g, aau, em);
        TestMethods.addBacklog(g, nu, em);

        List<Game> popular = gameDao.getMostBacklogged();

        Assert.assertEquals(1, popular.size());
    }

    @Test
    public void testGetFiltered(){
        Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game sw  = TestMethods.addGame("Star Wars Republic", ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        User u 	 = TestMethods.addUser(USER_NAME, USER_PASSWORD, USER_EMAIL, USER_LOCALE, em);
        Genre gen  = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
        Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
        TestMethods.connectGenre(g, gen, em);
        TestMethods.connectGenre(ag, gen, em);
        TestMethods.connectGenre(aag, gen, em);
        TestMethods.connectGenre(sw, gen, em);
        TestMethods.connectPlatform(g, p, em);
        TestMethods.connectPlatform(ag, p, em);
        TestMethods.connectPlatform(aag, p, em);
        TestMethods.connectPlatform(sw, p, em);
        TestMethods.addScore(u, g, 7, em);
        TestMethods.addScore(u, ag, 5, em);
        TestMethods.addScore(u, aag, 6, em);
        TestMethods.addScore(u, sw, 9, em);
        List<Game> games = gameDao.getFilteredGames("", Collections.singletonList(gen.getId() + ""),Collections.singletonList(p.getId() + ""), 6, 8, 0, 9999*3600+59*60+59, 1, 4 );
        Assert.assertEquals(2, games.size());
    }

    @Test
    public void testCountSearchResults(){
        TestMethods.addGame(NON_MATCHING_GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        TestMethods.addGame(MATCHING_GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        int matches 			= gameDao.countSearchResults(SEARCH_TERM);
        Assert.assertEquals(1, matches);
    }

    @Test
    public void testGetGamesForPlatformAllGamesSinglePage(){
        Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
        Platform ap = TestMethods.addPlatform(ALTERNATIVE_PLATFORM_NAME, ALTERNATIVE_PLATFORM_SHORT_NAME, ALTERNATIVE_PLATFORM_LOGO, em);

        TestMethods.connectPlatform(g, p, em);
        TestMethods.connectPlatform(aag, p, em);
        TestMethods.connectPlatform(aag, ap, em);
        TestMethods.connectPlatform(ag, ap, em);

        List<Game> games = gameDao.getGamesForPlatform(p, 1, 2);

        Assert.assertNotNull(games);
        Assert.assertEquals(2, games.size());
        Assert.assertTrue(games.contains(g));
        Assert.assertTrue(games.contains(aag));
    }

    @Test
    public void testGetGamesForPlatformAllGamesSingleTwoPages(){
        Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
        Platform ap = TestMethods.addPlatform(ALTERNATIVE_PLATFORM_NAME, ALTERNATIVE_PLATFORM_SHORT_NAME, ALTERNATIVE_PLATFORM_LOGO, em);

        TestMethods.connectPlatform(g, p, em);
        TestMethods.connectPlatform(aag, p, em);
        TestMethods.connectPlatform(aag, ap, em);
        TestMethods.connectPlatform(ag, ap, em);

        List<Game> games2 = gameDao.getGamesForPlatform(p,2,1);

        Assert.assertNotNull(games2);
        Assert.assertEquals(1, games2.size());
    }


    @Test
    public void testCountGamesForPlatform(){
        Game g 	 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag = TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
        Platform ap = TestMethods.addPlatform(ALTERNATIVE_PLATFORM_NAME, ALTERNATIVE_PLATFORM_SHORT_NAME, ALTERNATIVE_PLATFORM_LOGO, em);

        TestMethods.connectPlatform(g, p, em);
        TestMethods.connectPlatform(aag, p, em);
        TestMethods.connectPlatform(aag, ap, em);
        TestMethods.connectPlatform(ag, ap, em);

        int amount = gameDao.countGamesForPlatform(p);

        Assert.assertEquals(2, amount);
    }


    @Test
    public void testGetGamesForGenreAllGamesSinglePage(){
        Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Genre gen 		= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
        Genre agen		= TestMethods.addGenre(ALTERNATIVE_GENRE_NAME, ALTERNATIVE_GENRE_LOGO, em);

        TestMethods.connectGenre(g, gen, em);
        TestMethods.connectGenre(aag, gen, em);
        TestMethods.connectGenre(aag, agen, em);
        TestMethods.connectGenre(ag, agen, em);

        List<Game> games = gameDao.getGamesForGenre(gen, 1, 2);

        Assert.assertNotNull(games);
        Assert.assertEquals(2, games.size());
        Assert.assertTrue(games.contains(g));
        Assert.assertTrue(games.contains(aag));
    }

    @Test
    public void testGetGamesForGenreAllGamesSingleTwoPages(){
        Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Genre gen 		= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
        Genre agen		= TestMethods.addGenre(ALTERNATIVE_GENRE_NAME, ALTERNATIVE_GENRE_LOGO, em);

        TestMethods.connectGenre(g, gen, em);
        TestMethods.connectGenre(aag, gen, em);
        TestMethods.connectGenre(aag, agen, em);
        TestMethods.connectGenre(ag, agen, em);

        List<Game> games2 = gameDao.getGamesForGenre(gen,2,1);

        Assert.assertNotNull(games2);
        Assert.assertEquals(1, games2.size());
    }

    @Test
    public void testCountGetGamesForGenre(){
        Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Genre gen 		= TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
        Genre agen		= TestMethods.addGenre(ALTERNATIVE_GENRE_NAME, ALTERNATIVE_GENRE_LOGO, em);

        TestMethods.connectGenre(g, gen, em);
        TestMethods.connectGenre(aag, gen, em);
        TestMethods.connectGenre(aag, agen, em);
        TestMethods.connectGenre(ag, agen, em);

        int amount = gameDao.countGamesForGenre(gen);

        Assert.assertEquals(2, amount);
    }

    @Test
    public void testGetGamesForDeveloperAllGamesSinglePage(){
        Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Developer d 	= TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
        Developer ad	= TestMethods.addDeveloper(ALTERNATIVE_DEVELOPER_NAME, ALTERNATIVE_DEVELOPER_LOGO, em);

        TestMethods.connectDev(g, d, em);
        TestMethods.connectDev(aag, d, em);
        TestMethods.connectDev(aag, ad, em);
        TestMethods.connectDev(ag, ad, em);

        List<Game> games = gameDao.getGamesForDeveloper(d, 1, 2);

        Assert.assertNotNull(games);
        Assert.assertEquals(2, games.size());
        Assert.assertTrue(games.contains(g));
        Assert.assertTrue(games.contains(aag));
    }

    @Test
    public void testGetGamesForDeveloperAllGamesSingleTwoPages(){
        Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Developer d 	= TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
        Developer ad	= TestMethods.addDeveloper(ALTERNATIVE_DEVELOPER_NAME, ALTERNATIVE_DEVELOPER_LOGO, em);

        TestMethods.connectDev(g, d, em);
        TestMethods.connectDev(aag, d, em);
        TestMethods.connectDev(aag, ad, em);
        TestMethods.connectDev(ag, ad, em);

        List<Game> games2 = gameDao.getGamesForDeveloper(d, 2,1);

        Assert.assertNotNull(games2);
        Assert.assertEquals(1, games2.size());
    }

    @Test
    public void testCountGetGamesForDeveloper(){
        Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Developer d 	= TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
        Developer ad	= TestMethods.addDeveloper(ALTERNATIVE_DEVELOPER_NAME, ALTERNATIVE_DEVELOPER_LOGO, em);

        TestMethods.connectDev(g, d, em);
        TestMethods.connectDev(aag, d, em);
        TestMethods.connectDev(aag, ad, em);
        TestMethods.connectDev(ag, ad, em);

        int amount = gameDao.countGamesForDeveloper(d);

        Assert.assertEquals(2, amount);
    }

    @Test
    public void testGetGamesForPublisherAllGamesSinglePage(){
        Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Publisher p 	= TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, em);
        Publisher ap	= TestMethods.addPublisher(ALTERNATIVE_PUBLISHER_NAME, ALTERNATIVE_PUBLISHER_LOGO, em);

        TestMethods.connectPub(g, p, em);
        TestMethods.connectPub(aag, p, em);
        TestMethods.connectPub(aag, ap, em);
        TestMethods.connectPub(ag, ap, em);

        List<Game> games = gameDao.getGamesForPublisher(p, 1, 2);

        Assert.assertNotNull(games);
        Assert.assertEquals(2, games.size());
        Assert.assertTrue(games.contains(g));
        Assert.assertTrue(games.contains(aag));
    }

    @Test
    public void testGetGamesForPublisherAllGamesSingleTwoPages(){
        Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Publisher p 	= TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, em);
        Publisher ap	= TestMethods.addPublisher(ALTERNATIVE_PUBLISHER_NAME, ALTERNATIVE_PUBLISHER_LOGO, em);

        TestMethods.connectPub(g, p, em);
        TestMethods.connectPub(aag, p, em);
        TestMethods.connectPub(aag, ap, em);
        TestMethods.connectPub(ag, ap, em);

        List<Game> games2 = gameDao.getGamesForPublisher(p, 2,1);

        Assert.assertNotNull(games2);
        Assert.assertEquals(1, games2.size());
    }

    @Test
    public void testCountGetGamesForPublisher(){
        Game g 	 		= TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag  		= TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Game aag 		= TestMethods.addGame(ANOTHER_ALTERNATIVE_GAME_TITLE, ANOTHER_ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        Publisher p 	= TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, em);
        Publisher ap	= TestMethods.addPublisher(ALTERNATIVE_PUBLISHER_NAME, ALTERNATIVE_PUBLISHER_LOGO, em);

        TestMethods.connectPub(g, p, em);
        TestMethods.connectPub(aag, p, em);
        TestMethods.connectPub(aag, ap, em);
        TestMethods.connectPub(ag, ap, em);

        int amount = gameDao.countGamesForPublisher(p);

        Assert.assertEquals(2, amount);
    }

    @Test
    public void testRemove(){
        Game g  = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game ag = TestMethods.addGame(ALTERNATIVE_GAME_TITLE, ALTERNATIVE_GAME_COVER, ALTERNATIVE_GAME_DESC, GAME_TRAILER, em);
        gameDao.remove(g);
        Assert.assertEquals(1, TestMethods.countRowsInTable(GAME_TABLE, em));
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(GAME_TABLE, "game = '" + ag.getId() + "'", em));
    }

    @Test
    public void testUpdate(){
        Game g  = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        g.setTitle(ALTERNATIVE_GAME_TITLE);
        g.setCover(ALTERNATIVE_GAME_COVER);
        g.setDescription(ALTERNATIVE_GAME_DESC);
        gameDao.update(g);
        Assert.assertEquals(1, TestMethods.countRowsInTableWhere(GAME_TABLE, "game = '" + g.getId()
                + "' AND title = '" + ALTERNATIVE_GAME_TITLE + "' AND description = '" + ALTERNATIVE_GAME_DESC
                + "' AND cover = '" + ALTERNATIVE_GAME_COVER + "'", em));
    }

    @Test
    public void	testGetAllGamesSimplified()
    {
        Game example = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
        Game botw = TestMethods.addGame("The Legend of Zelda: Breath of the Wild", GAME_COVER, GAME_DESC, GAME_TRAILER, em);

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