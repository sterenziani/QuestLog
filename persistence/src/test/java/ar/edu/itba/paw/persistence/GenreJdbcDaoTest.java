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

import ar.edu.itba.paw.model.Genre;

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
	private SimpleJdbcInsert genreInsert;
	
	@Before
	public void	setUp()
	{
		genreDao = new GenreJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		genreInsert = new SimpleJdbcInsert(ds).withTableName(GENRE_TABLE).usingGeneratedKeyColumns("genre");
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
		Genre g = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Optional<Genre> maybeGenre = genreDao.findById(g.getId());
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
		TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Optional<Genre> maybeGenre = genreDao.findByName(GENRE_NAME);
		Assert.assertTrue(maybeGenre.isPresent());
		Assert.assertEquals(GENRE_NAME, maybeGenre.get().getName());
		Assert.assertEquals(GENRE_LOGO, maybeGenre.get().getLogo());
	}
	
	@Test
	public void	testChangeGenreName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		Genre g = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Optional<Genre> maybeGenre = genreDao.changeName(g.getId(), "Noentiendo");
		Assert.assertTrue(maybeGenre.isPresent());
		Assert.assertEquals("Noentiendo", maybeGenre.get().getName());
		Assert.assertEquals(GENRE_LOGO, maybeGenre.get().getLogo());
	}
	
	@Test
	public void	testChangeLogo()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		Genre g = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Optional<Genre> maybeGenre = genreDao.changeLogo(g.getId(), "http://sega.com/logo.png");
		Assert.assertTrue(maybeGenre.isPresent());
		Assert.assertEquals(GENRE_NAME, maybeGenre.get().getName());
		Assert.assertEquals("http://sega.com/logo.png", maybeGenre.get().getLogo());
	}
	
	@Test
	public void	testGetAllGenres()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		Genre g1 = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, genreInsert);
		Genre g2 = TestMethods.addGenre("Puzzle", GENRE_LOGO, genreInsert);
		List<Genre> myList = new ArrayList<Genre>();
		myList.add(g1);
		myList.add(g2);
		
		List<Genre> genreList = genreDao.getAllGenres();
		Assert.assertFalse(genreList.isEmpty());
		Assert.assertEquals(2, genreList.size());
		Assert.assertEquals(genreList.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(genreList.get(1).getName(), myList.get(1).getName());
	}
}
