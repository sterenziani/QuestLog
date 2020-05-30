/*package ar.edu.itba.paw.persistence;
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
	private static final String GAME_TITLE = "Example Game";
	private static final String GAME_COVER = "http://sega.com/game.jpg";
	private static final String GAME_DESC = "Explore the world!";
	private static final String PUBLISHING_TABLE = "publishing";


	@Autowired
	private DataSource ds;
	
	@Autowired
	private PublisherJdbcDao publisherDao;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert pubInsert;
	private SimpleJdbcInsert gameInsert;
	private SimpleJdbcInsert publishingInsert;
	
	@Before
	public void	setUp()
	{
		publisherDao = new PublisherJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		pubInsert = new SimpleJdbcInsert(ds).withTableName(PUBLISHER_TABLE).usingGeneratedKeyColumns("publisher");
		gameInsert = new SimpleJdbcInsert(ds).withTableName(GAME_TABLE).usingGeneratedKeyColumns("game");
		publishingInsert = new SimpleJdbcInsert(ds).withTableName(PUBLISHING_TABLE);
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
		Publisher p = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Optional<Publisher> maybePublisher = publisherDao.findById(p.getId());
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
		TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Optional<Publisher> maybePublisher = publisherDao.findByName(PUBLISHER_NAME);
		Assert.assertTrue(maybePublisher.isPresent());
		Assert.assertEquals(PUBLISHER_NAME, maybePublisher.get().getName());
		Assert.assertEquals(PUBLISHER_LOGO, maybePublisher.get().getLogo());
	}
	
	@Test
	public void	testChangePublisherName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		Publisher p = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Optional<Publisher> maybePublisher = publisherDao.changeName(p.getId(), "Noentiendo");
		Assert.assertTrue(maybePublisher.isPresent());
		Assert.assertEquals("Noentiendo", maybePublisher.get().getName());
		Assert.assertEquals(PUBLISHER_LOGO, maybePublisher.get().getLogo());
	}
	
	@Test
	public void	testChangeLogo()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		Publisher p = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Optional<Publisher> maybePublisher = publisherDao.changeLogo(p.getId(), "http://sega.com/logo.png");
		Assert.assertTrue(maybePublisher.isPresent());
		Assert.assertEquals(PUBLISHER_NAME, maybePublisher.get().getName());
		Assert.assertEquals("http://sega.com/logo.png", maybePublisher.get().getLogo());
	}

	@Test
	public void	testGetAllPublishers()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHING_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		Publisher nintendo = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Publisher sega = TestMethods.addPublisher("Sega", "http://sega.com/logo.png", pubInsert);
		Publisher konami = TestMethods.addPublisher("Konami", PUBLISHER_LOGO, pubInsert);
		List<Publisher> myList = new ArrayList<Publisher>();
		myList.add(nintendo);
		myList.add(sega);
		myList.add(konami);
		
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		TestMethods.connectPub(g1, nintendo, publishingInsert);
		TestMethods.connectPub(g1, sega, publishingInsert);
		
		List<Publisher> publisherList = publisherDao.getAllPublishers();
		
		Assert.assertFalse(publisherList.isEmpty());
		Assert.assertEquals(2, publisherList.size());
		Assert.assertEquals(publisherList.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(publisherList.get(1).getName(), myList.get(1).getName());
	}
	
	@Test
	public void testGetBiggestPublishers() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHING_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, GAME_TABLE);
		
		Publisher konami = TestMethods.addPublisher("Konami", PUBLISHER_LOGO, pubInsert);
		Publisher nintendo = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Publisher sega = TestMethods.addPublisher("Sega", PUBLISHER_LOGO, pubInsert);
		List<Publisher> myList = new ArrayList<Publisher>();
		myList.add(konami);
		myList.add(nintendo);
		myList.add(sega);
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, gameInsert);
		Game g2 = TestMethods.addGame("game2", GAME_COVER, GAME_DESC, gameInsert);
		TestMethods.connectPub(g2, nintendo, publishingInsert);
		TestMethods.connectPub(g1, konami, publishingInsert);
		TestMethods.connectPub(g2, konami, publishingInsert);
		
		List<Publisher> publisherList1 = publisherDao.getBiggestPublishers(1);
		List<Publisher> publisherList2 = publisherDao.getBiggestPublishers(3);
		
		Assert.assertFalse(publisherList1.isEmpty());
		Assert.assertFalse(publisherList2.isEmpty());
		
		Assert.assertEquals(1, publisherList1.size());
		Assert.assertEquals(2, publisherList2.size());
		
		Assert.assertEquals(publisherList1.get(0).getName(), myList.get(0).getName());
		
		Assert.assertEquals(publisherList2.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(publisherList2.get(1).getName(), myList.get(1).getName());
				
	}
	
	@Test
	public void testGetPublishers() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		
		Publisher konami = TestMethods.addPublisher("Konami", PUBLISHER_LOGO, pubInsert);
		Publisher nintendo = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Publisher sega = TestMethods.addPublisher("Sega", PUBLISHER_LOGO, pubInsert);

		List<Publisher> myList = new ArrayList<Publisher>();
		myList.add(konami);
		myList.add(nintendo);
		myList.add(sega);
		
		List<Publisher> publisherList1 = publisherDao.getPublishers(1,2);
		List<Publisher> publisherList2 = publisherDao.getPublishers(2,2);
		List<Publisher> publisherList3 = publisherDao.getPublishers(1,10);

		
		Assert.assertFalse(publisherList1.isEmpty());
		Assert.assertFalse(publisherList2.isEmpty());
		Assert.assertFalse(publisherList3.isEmpty());

		
		Assert.assertEquals(2, publisherList1.size());
		Assert.assertEquals(1, publisherList2.size());
		Assert.assertEquals(3, publisherList3.size());

		
		Assert.assertEquals(publisherList1.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(publisherList1.get(1).getName(), myList.get(1).getName());

		Assert.assertEquals(publisherList2.get(0).getName(), myList.get(2).getName());

		Assert.assertEquals(publisherList3.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(publisherList3.get(1).getName(), myList.get(1).getName());
		Assert.assertEquals(publisherList3.get(2).getName(), myList.get(2).getName());
	}
	
	@Test
	public void testCountPublishers()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		TestMethods.addPublisher("Sega", PUBLISHER_LOGO, pubInsert);
		int count = publisherDao.countPublishers();
		Assert.assertEquals(2, count);
		TestMethods.addPublisher("Gamefreak", PUBLISHER_LOGO, pubInsert);
		count = publisherDao.countPublishers();
		Assert.assertEquals(3, count);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		count = publisherDao.countPublishers();
		Assert.assertEquals(0, count);
	}

}
*/