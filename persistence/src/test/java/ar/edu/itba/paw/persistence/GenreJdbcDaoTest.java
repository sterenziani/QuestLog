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
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Game;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class GenreJdbcDaoTest
{
	private static final String GAME_TABLE = "games";
	private	static final String GENRE_TABLE = "genres";
	private	static final String GENRE_NAME = "Adventure";
	private	static final String GENRE_LOGO = "https://example/icon.jpg";
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private GenreJdbcDao genreDao;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	
	@Before
	public void	setUp()
	{
		genreDao = new GenreJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(ds).withTableName(GENRE_TABLE).usingGeneratedKeyColumns("genre");
	}
	
	@Test
	public void	testRegisterGenre()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		final Genre g = genreDao.register(GENRE_NAME, GENRE_LOGO);
        Assert.assertNotNull(g);
        Assert.assertEquals(GENRE_NAME, g.getName());
        Assert.assertEquals(GENRE_LOGO, g.getLogo());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, GENRE_TABLE));
	}
	

	@Test
	public void	testFindGenreByIdDoesntExist()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		Optional<Genre> maybeGenre = genreDao.findById(1);
		Assert.assertFalse(maybeGenre.isPresent());
	}
	
	@Test
	public void	testFindGenreByIdExists()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("genre_name", GENRE_NAME);
		args.put("genre_logo", GENRE_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		
		Optional<Genre> maybeGenre = genreDao.findById(key.longValue());
		Assert.assertTrue(maybeGenre.isPresent());
		Assert.assertEquals(GENRE_NAME, maybeGenre.get().getName());
		Assert.assertEquals(GENRE_LOGO, maybeGenre.get().getLogo());
	}
	
	@Test
	public void	testFindGenreByNameDoesntExist()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		Optional<Genre> maybeGenre = genreDao.findByName(GENRE_NAME);
		Assert.assertFalse(maybeGenre.isPresent());
	}
	
	@Test
	public void	testFindGenreByNameExists()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("genre_name", GENRE_NAME);
		args.put("genre_logo", GENRE_LOGO);
		jdbcInsert.execute(args);
		
		Optional<Genre> maybeGenre = genreDao.findByName(GENRE_NAME);
		Assert.assertTrue(maybeGenre.isPresent());
		Assert.assertEquals(GENRE_NAME, maybeGenre.get().getName());
		Assert.assertEquals(GENRE_LOGO, maybeGenre.get().getLogo());
	}
	
	@Test
	public void	testChangeGenreName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("genre_name", GENRE_NAME);
		args.put("genre_logo", GENRE_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		Optional<Genre> maybeGenre = genreDao.changeName(key.longValue(), "Noentiendo");
		Assert.assertTrue(maybeGenre.isPresent());
		Assert.assertEquals("Noentiendo", maybeGenre.get().getName());
		Assert.assertEquals(GENRE_LOGO, maybeGenre.get().getLogo());
	}
	
	@Test
	public void	testChangeLogo()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("genre_name", GENRE_NAME);
		args.put("genre_logo", GENRE_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		
		Optional<Genre> maybeGenre = genreDao.changeLogo(key.longValue(), "http://sega.com/logo.png");
		Assert.assertTrue(maybeGenre.isPresent());
		Assert.assertEquals(GENRE_NAME, maybeGenre.get().getName());
		Assert.assertEquals("http://sega.com/logo.png", maybeGenre.get().getLogo());
	}
	
	@Test
	public void	testGetAllGenres()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		final Map<String, Object> args1 = new HashMap<>();
		args1.put("genre_name", GENRE_NAME);
		args1.put("genre_logo", GENRE_LOGO);
		Number key1 = jdbcInsert.executeAndReturnKey(args1);
		final Map<String, Object> args2 = new HashMap<>();
		args2.put("genre_name", "Sega");
		args2.put("genre_logo", "http://sega.com/logo.png");
		Number key2 = jdbcInsert.executeAndReturnKey(args2);
		
		Genre nintendo = new Genre(key1.longValue(), GENRE_NAME, GENRE_LOGO);
		Genre sega = new Genre(key2.longValue(), "Sega", "http://sega.com/logo.png");
		List<Genre> myList = new ArrayList<Genre>();
		myList.add(nintendo);
		myList.add(sega);
		
		List<Genre> genreList = genreDao.getAllGenres();
		Assert.assertFalse(genreList.isEmpty());
		Assert.assertEquals(2, genreList.size());
		Assert.assertEquals(genreList.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(genreList.get(1).getName(), myList.get(1).getName());
	}
	
	@Test
	public void	testGetAllGamesByGenre()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		final Map<String, Object> args = new HashMap<>();
		args.put("genre_name", GENRE_NAME);
		args.put("genre_logo", GENRE_LOGO);
		Number key = jdbcInsert.executeAndReturnKey(args);
		Genre d = new Genre(key.longValue(), GENRE_NAME, GENRE_LOGO);
		SimpleJdbcInsert jdbcInsertGames = new SimpleJdbcInsert(ds).withTableName(GAME_TABLE).usingGeneratedKeyColumns("game");
		SimpleJdbcInsert jdbcInsertDevelopment = new SimpleJdbcInsert(ds).withTableName("classifications");
		
		// Insert Mario
		final Map<String, Object> gameArgs = new HashMap<>();
		gameArgs.put("title", "Mario");
		gameArgs.put("cover", "http://nintendo.com/mario.png");
		gameArgs.put("description", "A game with Mario");
		Number gameKey = jdbcInsertGames.executeAndReturnKey(gameArgs);
		Game g1 = new Game(gameKey.longValue(), "Mario", "http://nintendo.com/mario.png", "A game with Mario");
		// Say Mario is on the genre
		final Map<String, Object> classificationsArgs = new HashMap<>();
		classificationsArgs.put("game", gameKey.longValue());
		classificationsArgs.put("genre", key.longValue());
		jdbcInsertDevelopment.execute(classificationsArgs);
		
		final Map<String, Object> gameArgs2 = new HashMap<>();
		gameArgs2.put("title", "Zelda");
		gameArgs2.put("cover", "http://nintendo.com/zelda.png");
		gameArgs2.put("description", "A game with Link");
		Number gameKey2 = jdbcInsertGames.executeAndReturnKey(gameArgs2);
		Game g2 = new Game(gameKey2.longValue(), "Zelda", "http://nintendo.com/zelda.png", "A game with Link");
		final Map<String, Object> classificationsArgs2 = new HashMap<>();
		classificationsArgs2.put("game", gameKey2.longValue());
		classificationsArgs2.put("genre", key.longValue());
		jdbcInsertDevelopment.execute(classificationsArgs2);
		
		final Map<String, Object> gameArgs3 = new HashMap<>();
		gameArgs3.put("title", "Sonic");
		gameArgs3.put("cover", "http://sega.com/sonic.png");
		gameArgs3.put("description", "A game with Sonic");
		jdbcInsertGames.execute(gameArgs3);
		// Let's say this one is not on this genre
		
		List<Game> gamesList = genreDao.getAllGames(d);
		List<Game> myList = new ArrayList<Game>();
		myList.add(g1);
		myList.add(g2);
		
		Assert.assertNotNull(gamesList);
		Assert.assertEquals(myList.size(), gamesList.size());
		Assert.assertEquals(myList.get(0).getTitle(), gamesList.get(0).getTitle());
		Assert.assertEquals(myList.get(1).getTitle(), gamesList.get(1).getTitle());
	}
}
