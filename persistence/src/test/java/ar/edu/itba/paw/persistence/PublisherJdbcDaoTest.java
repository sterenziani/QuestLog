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
	private SimpleJdbcInsert pubInsert;
	
	@Before
	public void	setUp()
	{
		publisherDao = new PublisherJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		pubInsert = new SimpleJdbcInsert(ds).withTableName(PUBLISHER_TABLE).usingGeneratedKeyColumns("publisher");
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
	/*
	@Test
	public void	testGetAllPublishers()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PUBLISHER_TABLE);
		Publisher nintendo = TestMethods.addPublisher(PUBLISHER_NAME, PUBLISHER_LOGO, pubInsert);
		Publisher sega = TestMethods.addPublisher("Sega", "http://sega.com/logo.png", pubInsert);
		List<Publisher> myList = new ArrayList<Publisher>();
		myList.add(nintendo);
		myList.add(sega);
		
		List<Publisher> publisherList = publisherDao.getAllPublishers();
		Assert.assertFalse(publisherList.isEmpty());
		Assert.assertEquals(2, publisherList.size());
		Assert.assertEquals(publisherList.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(publisherList.get(1).getName(), myList.get(1).getName());
	}
	*/
}
