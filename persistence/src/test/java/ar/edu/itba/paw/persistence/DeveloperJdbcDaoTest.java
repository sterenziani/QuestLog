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
import ar.edu.itba.paw.model.Developer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class DeveloperJdbcDaoTest
{
	private static final String GAME_TABLE = "games";
	private	static final String DEVELOPER_TABLE = "developers";
	private	static final String DEVELOPER_NAME = "Nintendo";
	private	static final String DEVELOPER_LOGO = "https://nintendo/logo.jpg";

	@Autowired
	private DataSource ds;
	
	@Autowired
	private DeveloperJdbcDao developerDao;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	
	@Before
	public void	setUp()
	{
		developerDao = new DeveloperJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(ds).withTableName(DEVELOPER_TABLE).usingGeneratedKeyColumns("developer");
	}
	
	@Test
	public void	testRegisterDeveloper()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		final Developer d = developerDao.register(DEVELOPER_NAME, DEVELOPER_LOGO);
        Assert.assertNotNull(d);
        Assert.assertEquals(DEVELOPER_NAME, d.getName());
        Assert.assertEquals(DEVELOPER_LOGO, d.getLogo());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, DEVELOPER_TABLE));
	}
	
	@Test
	public void	testFindDeveloperByIdDoesntExist()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		Optional<Developer> maybeDeveloper = developerDao.findById(1);
		Assert.assertFalse(maybeDeveloper.isPresent());
	}
	
	@Test
	public void	testFindDeveloperByIdExists()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("developer_name", DEVELOPER_NAME);
		args.put("developer_logo", DEVELOPER_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		
		Optional<Developer> maybeDeveloper = developerDao.findById(key.longValue());
		Assert.assertTrue(maybeDeveloper.isPresent());
		Assert.assertEquals(DEVELOPER_NAME, maybeDeveloper.get().getName());
		Assert.assertEquals(DEVELOPER_LOGO, maybeDeveloper.get().getLogo());
	}
	
	@Test
	public void	testFindDeveloperByNameDoesntExist()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		Optional<Developer> maybeDeveloper = developerDao.findByName(DEVELOPER_NAME);
		Assert.assertFalse(maybeDeveloper.isPresent());
	}
	
	@Test
	public void	testFindDeveloperByNameExists()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("developer_name", DEVELOPER_NAME);
		args.put("developer_logo", DEVELOPER_LOGO);
		jdbcInsert.execute(args);
		
		Optional<Developer> maybeDeveloper = developerDao.findByName(DEVELOPER_NAME);
		Assert.assertTrue(maybeDeveloper.isPresent());
		Assert.assertEquals(DEVELOPER_NAME, maybeDeveloper.get().getName());
		Assert.assertEquals(DEVELOPER_LOGO, maybeDeveloper.get().getLogo());
	}
	
	@Test
	public void	testChangeDeveloperName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("developer_name", DEVELOPER_NAME);
		args.put("developer_logo", DEVELOPER_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		Optional<Developer> maybeDeveloper = developerDao.changeName(key.longValue(), "Noentiendo");
		Assert.assertTrue(maybeDeveloper.isPresent());
		Assert.assertEquals("Noentiendo", maybeDeveloper.get().getName());
		Assert.assertEquals(DEVELOPER_LOGO, maybeDeveloper.get().getLogo());
	}
	
	@Test
	public void	testChangeLogo()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("developer_name", DEVELOPER_NAME);
		args.put("developer_logo", DEVELOPER_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		
		Optional<Developer> maybeDeveloper = developerDao.changeLogo(key.longValue(), "http://sega.com/logo.png");
		Assert.assertTrue(maybeDeveloper.isPresent());
		Assert.assertEquals(DEVELOPER_NAME, maybeDeveloper.get().getName());
		Assert.assertEquals("http://sega.com/logo.png", maybeDeveloper.get().getLogo());
	}
	
	@Test
	public void	testGetAllDevelopers()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		final Map<String, Object> args1 = new HashMap<>();
		args1.put("developer_name", DEVELOPER_NAME);
		args1.put("developer_logo", DEVELOPER_LOGO);
		Number key1 = jdbcInsert.executeAndReturnKey(args1);
		final Map<String, Object> args2 = new HashMap<>();
		args2.put("developer_name", "Sega");
		args2.put("developer_logo", "http://sega.com/logo.png");
		Number key2 = jdbcInsert.executeAndReturnKey(args2);
		
		Developer nintendo = new Developer(key1.longValue(), DEVELOPER_NAME, DEVELOPER_LOGO);
		Developer sega = new Developer(key2.longValue(), "Sega", "http://sega.com/logo.png");
		List<Developer> myList = new ArrayList<Developer>();
		myList.add(nintendo);
		myList.add(sega);
		
		List<Developer> developerList = developerDao.getAllDevelopers();
		Assert.assertFalse(developerList.isEmpty());
		Assert.assertEquals(2, developerList.size());
		Assert.assertEquals(developerList.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(developerList.get(1).getName(), myList.get(1).getName());
	}
	
	@Test
	public void	testGetAllGames()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("developer_name", DEVELOPER_NAME);
		args.put("developer_logo", DEVELOPER_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		Developer d = new Developer(key.longValue(), DEVELOPER_NAME, DEVELOPER_LOGO);
		SimpleJdbcInsert jdbcInsertGames = new SimpleJdbcInsert(ds).withTableName(GAME_TABLE).usingGeneratedKeyColumns("game");
		SimpleJdbcInsert jdbcInsertDevelopment = new SimpleJdbcInsert(ds).withTableName("development");
		
		// Insert Mario
		final Map<String, Object> gameArgs = new HashMap<>();
		gameArgs.put("title", "Mario");
		gameArgs.put("cover", "http://nintendo.com/mario.png");
		gameArgs.put("description", "A game with Mario");
		Number gameKey = jdbcInsertGames.executeAndReturnKey(gameArgs);
		Game g1 = new Game(gameKey.longValue(), "Mario", "http://nintendo.com/mario.png", "A game with Mario");
		// Say Mario is on the developer
		final Map<String, Object> developmentArgs = new HashMap<>();
		developmentArgs.put("game", gameKey.longValue());
		developmentArgs.put("developer", key.longValue());
		jdbcInsertDevelopment.execute(developmentArgs);
		
		final Map<String, Object> gameArgs2 = new HashMap<>();
		gameArgs2.put("title", "Zelda");
		gameArgs2.put("cover", "http://nintendo.com/zelda.png");
		gameArgs2.put("description", "A game with Link");
		Number gameKey2 = jdbcInsertGames.executeAndReturnKey(gameArgs2);
		Game g2 = new Game(gameKey2.longValue(), "Zelda", "http://nintendo.com/zelda.png", "A game with Link");
		final Map<String, Object> developmentArgs2 = new HashMap<>();
		developmentArgs2.put("game", gameKey2.longValue());
		developmentArgs2.put("developer", key.longValue());
		jdbcInsertDevelopment.execute(developmentArgs2);
		
		final Map<String, Object> gameArgs3 = new HashMap<>();
		gameArgs3.put("title", "Sonic");
		gameArgs3.put("cover", "http://sega.com/sonic.png");
		gameArgs3.put("description", "A game with Sonic");
		jdbcInsertGames.execute(gameArgs3);
		// Let's say this one is not on this developer
		
		List<Game> gamesList = developerDao.getAllGames(d);
		List<Game> myList = new ArrayList<Game>();
		myList.add(g1);
		myList.add(g2);
		
		Assert.assertNotNull(gamesList);
		Assert.assertEquals(myList.size(), gamesList.size());
		Assert.assertEquals(myList.get(0).getTitle(), gamesList.get(0).getTitle());
		Assert.assertEquals(myList.get(1).getTitle(), gamesList.get(1).getTitle());
	}
}
