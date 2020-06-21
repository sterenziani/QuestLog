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

import ar.edu.itba.paw.model.entity.Developer;
import ar.edu.itba.paw.model.entity.Game;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DeveloperJpaDaoTest
{
	private	static final String DEVELOPER_TABLE = "developers";
	private	static final String DEVELOPER_NAME = "Nintendo";
	private	static final String DEVELOPER_LOGO = "https://nintendo/logo.jpg";
	private static final String DEVELOPMENT_TABLE = "development";
	private static final String GAME_TABLE = "games";
	private static final String GAME_TITLE = "Example Game";
	private static final String GAME_COVER = "http://sega.com/game.jpg";
	private static final String GAME_DESC = "Explore the world!";
    private static final String GAME_TRAILER			= "DpHDJRGuL7w";

	@PersistenceContext
    private EntityManager em;

	@Autowired
	private DataSource ds;
	
	@Autowired
	private DeveloperJpaDao developerDao;

	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void	setUp()
	{
		jdbcTemplate = new JdbcTemplate(ds);
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
		Developer d = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
		Optional<Developer> maybeDeveloper = developerDao.findById(d.getId());
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
		TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
		Optional<Developer> maybeDeveloper = developerDao.findByName(DEVELOPER_NAME);
		Assert.assertTrue(maybeDeveloper.isPresent());
		Assert.assertEquals(DEVELOPER_NAME, maybeDeveloper.get().getName());
		Assert.assertEquals(DEVELOPER_LOGO, maybeDeveloper.get().getLogo());
	}
	
	@Test
	public void	testChangeDeveloperName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		Developer d = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
		Optional<Developer> maybeDeveloper = developerDao.changeName(d.getId(), "Noentiendo");
		Assert.assertTrue(maybeDeveloper.isPresent());
		Assert.assertEquals("Noentiendo", maybeDeveloper.get().getName());
		Assert.assertEquals(DEVELOPER_LOGO, maybeDeveloper.get().getLogo());
	}
	
	@Test
	public void	testChangeLogo()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		Developer d = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
		Optional<Developer> maybeDeveloper = developerDao.changeLogo(d.getId(), "http://sega.com/logo.png");
		Assert.assertTrue(maybeDeveloper.isPresent());
		Assert.assertEquals(DEVELOPER_NAME, maybeDeveloper.get().getName());
		Assert.assertEquals("http://sega.com/logo.png", maybeDeveloper.get().getLogo());
	}
	
	@Test
	public void	testGetAllDevelopers()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		Developer nintendo = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
		Developer sega = TestMethods.addDeveloper("Sega", DEVELOPER_LOGO, em);
		List<Developer> myList = new ArrayList<Developer>();
		
		myList.add(sega);
		myList.add(nintendo);
		
		List<Developer> developerList = developerDao.getAllDevelopers();
		Assert.assertFalse(developerList.isEmpty());
		Assert.assertEquals(2, developerList.size());
		Assert.assertTrue(developerList.containsAll(myList));
	}
	
	@Test
	public void testCountDevelopers()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
		TestMethods.addDeveloper("Sega", DEVELOPER_LOGO, em);
		int count = developerDao.countDevelopers();
		Assert.assertEquals(2, count);
		TestMethods.addDeveloper("Gamefreak", DEVELOPER_LOGO, em);
		count = developerDao.countDevelopers();
		Assert.assertEquals(3, count);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		count = developerDao.countDevelopers();
		Assert.assertEquals(0, count);
	}
	
	@Test
	public void testgetDevelopers() 
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		Developer gamefreak = TestMethods.addDeveloper("Gamefreak", DEVELOPER_LOGO, em);
		Developer nintendo = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
		Developer sega = TestMethods.addDeveloper("Sega", DEVELOPER_LOGO, em);
		List<Developer> myList = new ArrayList<Developer>();
		myList.add(gamefreak);
		myList.add(nintendo);
		myList.add(sega);
		List<Developer> developerList1 = developerDao.getDevelopers(1,2);
		List<Developer> developerList2 = developerDao.getDevelopers(2,2);
		List<Developer> developerList3 = developerDao.getDevelopers(1,5);

		Assert.assertFalse(developerList1.isEmpty());
		Assert.assertFalse(developerList2.isEmpty());
		Assert.assertFalse(developerList3.isEmpty());

		Assert.assertEquals(2, developerList1.size());
		Assert.assertEquals(1, developerList2.size());
		Assert.assertEquals(3, developerList3.size());


		Assert.assertEquals(developerList1.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(developerList1.get(1).getName(), myList.get(1).getName());
		
		Assert.assertEquals(developerList2.get(0).getName(), myList.get(2).getName());
		
		Assert.assertEquals(developerList3.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(developerList3.get(1).getName(), myList.get(1).getName());
		Assert.assertEquals(developerList3.get(2).getName(), myList.get(2).getName());
	}
	
	@Test
	public void testGetBiggestDevelopers() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPMENT_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		Developer gamefreak = TestMethods.addDeveloper("Gamefreak", DEVELOPER_LOGO, em);
		Developer nintendo = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, em);
		Developer sega = TestMethods.addDeveloper("Sega", DEVELOPER_LOGO, em);
		List<Developer> myList = new ArrayList<Developer>();
		myList.add(gamefreak);
		myList.add(nintendo);
		myList.add(sega);
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		Game g2 = TestMethods.addGame("game2", GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		TestMethods.connectDev(g2, nintendo, em);
		TestMethods.connectDev(g1, gamefreak, em);
		TestMethods.connectDev(g2, gamefreak, em);
		
		List<Developer> developerList1 = developerDao.getBiggestDevelopers(1);
		List<Developer> developerList2 = developerDao.getBiggestDevelopers(3);
		
		Assert.assertFalse(developerList1.isEmpty());
		Assert.assertFalse(developerList2.isEmpty());
		
		Assert.assertEquals(1, developerList1.size());
		Assert.assertEquals(2, developerList2.size());
		
		Assert.assertEquals(developerList1.get(0).getName(), myList.get(0).getName());
		
		Assert.assertEquals(developerList2.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(developerList2.get(1).getName(), myList.get(1).getName());
				
	}
}