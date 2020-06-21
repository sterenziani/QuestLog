package ar.edu.itba.paw.persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import ar.edu.itba.paw.model.entity.Genre;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class GenreJpaDaoTest
{
	private static final String GAME_TABLE = "games";
	private	static final String GENRE_TABLE = "genres";
	private	static final String GENRE_NAME = "Adventure";
	private	static final String GENRE_LOGO = "https://example/icon.jpg";
	
	@PersistenceContext
    private EntityManager em;
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private GenreJpaDao genreDao;
	
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void	setUp()
	{
		jdbcTemplate = new JdbcTemplate(ds);
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
		Genre g = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
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
		TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
		Optional<Genre> maybeGenre = genreDao.findByName(GENRE_NAME);
		Assert.assertTrue(maybeGenre.isPresent());
		Assert.assertEquals(GENRE_NAME, maybeGenre.get().getName());
		Assert.assertEquals(GENRE_LOGO, maybeGenre.get().getLogo());
	}
	
	@Test
	public void	testChangeGenreName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		Genre g = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
		Optional<Genre> maybeGenre = genreDao.changeName(g.getId(), "Noentiendo");
		Assert.assertTrue(maybeGenre.isPresent());
		Assert.assertEquals("Noentiendo", maybeGenre.get().getName());
		Assert.assertEquals(GENRE_LOGO, maybeGenre.get().getLogo());
	}
	
	@Test
	public void	testChangeLogo()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		Genre g = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
		Optional<Genre> maybeGenre = genreDao.changeLogo(g.getId(), "http://sega.com/logo.png");
		Assert.assertTrue(maybeGenre.isPresent());
		Assert.assertEquals(GENRE_NAME, maybeGenre.get().getName());
		Assert.assertEquals("http://sega.com/logo.png", maybeGenre.get().getLogo());
	}
	
	@Test
	public void	testGetAllGenres()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		Genre g1 = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
		Genre g2 = TestMethods.addGenre("Puzzle", GENRE_LOGO, em);
		List<Genre> myList = new ArrayList<Genre>();
		myList.add(g1);
		myList.add(g2);
		
		List<Genre> genreList = genreDao.getAllGenres();
		Assert.assertFalse(genreList.isEmpty());
		Assert.assertEquals(2, genreList.size());
		Assert.assertTrue(genreList.containsAll(myList));
	}
	
	@Test
	public void testGetGenres() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		Genre g1 = TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
		Genre g2 = TestMethods.addGenre("Puzzle", GENRE_LOGO, em);
		Genre g3 = TestMethods.addGenre("Action", GENRE_LOGO, em);
		
		List<Genre> myList = new ArrayList<Genre>();
		myList.add(g1);
		myList.add(g2);
		myList.add(g3);
		List<Genre> genreList1 = genreDao.getGenres(1,2);
		List<Genre> genreList2 = genreDao.getGenres(2,2);
		List<Genre> genreList3 = genreDao.getGenres(1,5);

		Assert.assertFalse(genreList1.isEmpty());
		Assert.assertFalse(genreList2.isEmpty());
		Assert.assertFalse(genreList3.isEmpty());

		Assert.assertEquals(2, genreList1.size());
		Assert.assertEquals(1, genreList2.size());
		Assert.assertEquals(3, genreList3.size());


		Assert.assertTrue(myList.containsAll(genreList1));

		Assert.assertTrue(myList.containsAll(genreList2));

		Assert.assertTrue(myList.containsAll(genreList3));
	}
	
	@Test
	public void testCountGenres() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		TestMethods.addGenre(GENRE_NAME, GENRE_LOGO, em);
		TestMethods.addGenre("Puzzle", GENRE_LOGO, em);
		
		int count = genreDao.countGenres();
		Assert.assertEquals(2, count);
		
		TestMethods.addGenre("Action", GENRE_LOGO, em);
		
		count = genreDao.countGenres();
		Assert.assertEquals(3, count);
		
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GENRE_TABLE);
		
		count = genreDao.countGenres();
		Assert.assertEquals(0, count);
	}
}