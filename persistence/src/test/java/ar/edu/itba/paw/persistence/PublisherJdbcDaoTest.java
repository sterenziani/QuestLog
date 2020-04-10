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

import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Game;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class PublisherJdbcDaoTest
{
	private static final String GAME_TABLE = "games";
	private	static final String PUBLISHER_TABLE = "publishers";
	private	static final String PUBLISHER_NAME = "Nintendo";
	private	static final String PUBLISHER_LOGO = "https://nintendo/logo.jpg";

	@Autowired
	private DataSource ds;
	
	@Autowired
	private PublisherJdbcDao publisherDao;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	
	@Before
	public void	setUp()
	{
		publisherDao = new PublisherJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(ds).withTableName(PUBLISHER_TABLE).usingGeneratedKeyColumns("publisher");
	}
	
	@Test
	public void	testRegisterPublisher()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		final Publisher d = publisherDao.register(PUBLISHER_NAME, PUBLISHER_LOGO);
        Assert.assertNotNull(d);
        Assert.assertEquals(PUBLISHER_NAME, d.getName());
        Assert.assertEquals(PUBLISHER_LOGO, d.getLogo());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PUBLISHER_TABLE));
	}
	
	@Test
	public void	testFindPublisherByIdDoesntExist()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		Optional<Publisher> maybePublisher = publisherDao.findById(1);
		Assert.assertFalse(maybePublisher.isPresent());
	}
	
	@Test
	public void	testFindPublisherByIdExists()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("publisher_name", PUBLISHER_NAME);
		args.put("publisher_logo", PUBLISHER_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		
		Optional<Publisher> maybePublisher = publisherDao.findById(key.longValue());
		Assert.assertTrue(maybePublisher.isPresent());
		Assert.assertEquals(PUBLISHER_NAME, maybePublisher.get().getName());
		Assert.assertEquals(PUBLISHER_LOGO, maybePublisher.get().getLogo());
	}
	
	@Test
	public void	testFindPublisherByNameDoesntExist()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		Optional<Publisher> maybePublisher = publisherDao.findByName(PUBLISHER_NAME);
		Assert.assertFalse(maybePublisher.isPresent());
	}
	
	@Test
	public void	testFindPublisherByNameExists()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("publisher_name", PUBLISHER_NAME);
		args.put("publisher_logo", PUBLISHER_LOGO);
		jdbcInsert.execute(args);
		
		Optional<Publisher> maybePublisher = publisherDao.findByName(PUBLISHER_NAME);
		Assert.assertTrue(maybePublisher.isPresent());
		Assert.assertEquals(PUBLISHER_NAME, maybePublisher.get().getName());
		Assert.assertEquals(PUBLISHER_LOGO, maybePublisher.get().getLogo());
	}
	
	@Test
	public void	testChangePublisherName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("publisher_name", PUBLISHER_NAME);
		args.put("publisher_logo", PUBLISHER_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		Optional<Publisher> maybePublisher = publisherDao.changeName(key.longValue(), "Noentiendo");
		Assert.assertTrue(maybePublisher.isPresent());
		Assert.assertEquals("Noentiendo", maybePublisher.get().getName());
		Assert.assertEquals(PUBLISHER_LOGO, maybePublisher.get().getLogo());
	}
	
	@Test
	public void	testChangeLogo()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("publisher_name", PUBLISHER_NAME);
		args.put("publisher_logo", PUBLISHER_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		
		Optional<Publisher> maybePublisher = publisherDao.changeLogo(key.longValue(), "http://sega.com/logo.png");
		Assert.assertTrue(maybePublisher.isPresent());
		Assert.assertEquals(PUBLISHER_NAME, maybePublisher.get().getName());
		Assert.assertEquals("http://sega.com/logo.png", maybePublisher.get().getLogo());
	}
	
	@Test
	public void	testGetAllPublishers()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		final Map<String, Object> args1 = new HashMap<>();
		args1.put("publisher_name", PUBLISHER_NAME);
		args1.put("publisher_logo", PUBLISHER_LOGO);
		Number key1 = jdbcInsert.executeAndReturnKey(args1);
		final Map<String, Object> args2 = new HashMap<>();
		args2.put("publisher_name", "Sega");
		args2.put("publisher_logo", "http://sega.com/logo.png");
		Number key2 = jdbcInsert.executeAndReturnKey(args2);
		
		Publisher nintendo = new Publisher(key1.longValue(), PUBLISHER_NAME, PUBLISHER_LOGO);
		Publisher sega = new Publisher(key2.longValue(), "Sega", "http://sega.com/logo.png");
		List<Publisher> myList = new ArrayList<Publisher>();
		myList.add(nintendo);
		myList.add(sega);
		
		List<Publisher> publisherList = publisherDao.getAllPublishers();
		Assert.assertFalse(publisherList.isEmpty());
		Assert.assertEquals(2, publisherList.size());
		Assert.assertEquals(publisherList.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(publisherList.get(1).getName(), myList.get(1).getName());
	}
	
	@Test
	public void	testGetAllGamesByPublisher()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("publisher_name", PUBLISHER_NAME);
		args.put("publisher_logo", PUBLISHER_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		Publisher d = new Publisher(key.longValue(), PUBLISHER_NAME, PUBLISHER_LOGO);
		SimpleJdbcInsert jdbcInsertGames = new SimpleJdbcInsert(ds).withTableName(GAME_TABLE).usingGeneratedKeyColumns("game");
		SimpleJdbcInsert jdbcInsertPublishing = new SimpleJdbcInsert(ds).withTableName("publishing");
		
		// Insert Mario
		final Map<String, Object> gameArgs = new HashMap<>();
		gameArgs.put("title", "Mario");
		gameArgs.put("cover", "http://nintendo.com/mario.png");
		gameArgs.put("description", "A game with Mario");
		Number gameKey = jdbcInsertGames.executeAndReturnKey(gameArgs);
		Game g1 = new Game(gameKey.longValue(), "Mario", "http://nintendo.com/mario.png", "A game with Mario");
		// Say Mario is on the publisher
		final Map<String, Object> publishingArgs = new HashMap<>();
		publishingArgs.put("game", gameKey.longValue());
		publishingArgs.put("publisher", key.longValue());
		jdbcInsertPublishing.execute(publishingArgs);
		
		final Map<String, Object> gameArgs2 = new HashMap<>();
		gameArgs2.put("title", "Zelda");
		gameArgs2.put("cover", "http://nintendo.com/zelda.png");
		gameArgs2.put("description", "A game with Link");
		Number gameKey2 = jdbcInsertGames.executeAndReturnKey(gameArgs2);
		Game g2 = new Game(gameKey2.longValue(), "Zelda", "http://nintendo.com/zelda.png", "A game with Link");
		final Map<String, Object> publishingArgs2 = new HashMap<>();
		publishingArgs2.put("game", gameKey2.longValue());
		publishingArgs2.put("publisher", key.longValue());
		jdbcInsertPublishing.execute(publishingArgs2);
		
		final Map<String, Object> gameArgs3 = new HashMap<>();
		gameArgs3.put("title", "Sonic");
		gameArgs3.put("cover", "http://sega.com/sonic.png");
		gameArgs3.put("description", "A game with Sonic");
		jdbcInsertGames.execute(gameArgs3);
		// Let's say this one is not on this publisher
		
		List<Game> gamesList = publisherDao.getAllGames(d);
		List<Game> myList = new ArrayList<Game>();
		myList.add(g1);
		myList.add(g2);
		
		Assert.assertNotNull(gamesList);
		Assert.assertEquals(myList.size(), gamesList.size());
		Assert.assertEquals(myList.get(0).getTitle(), gamesList.get(0).getTitle());
		Assert.assertEquals(myList.get(1).getTitle(), gamesList.get(1).getTitle());
	}
}
