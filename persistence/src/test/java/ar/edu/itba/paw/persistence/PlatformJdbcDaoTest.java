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

import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class PlatformJdbcDaoTest
{
	private	static final String GAME_TABLE = "games";
	private	static final String PLATFORM_TABLE = "platforms";
	private	static final String PLATFORM_NAME = "PlayStation 4";
	private	static final String PLATFORM_SHORT_NAME = "PS4";
	private	static final String PLATFORM_LOGO = "https://i.ytimg.com/vi/56NxUWEFpL0/maxresdefault.jpg";

	@Autowired
	private DataSource ds;
	
	@Autowired
	private PlatformJdbcDao platformDao;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert platformInsert;
	
	@Before
	public void	setUp()
	{
		platformDao = new PlatformJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		platformInsert = new SimpleJdbcInsert(ds).withTableName(PLATFORM_TABLE).usingGeneratedKeyColumns("platform");
	}
	
	@Test
	public void	testRegisterPlatform()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		final Platform p = platformDao.register(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO);
        Assert.assertNotNull(p);
        Assert.assertEquals(PLATFORM_NAME, p.getName());
        Assert.assertEquals(PLATFORM_SHORT_NAME, p.getShortName());
        Assert.assertEquals(PLATFORM_LOGO, p.getLogo());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PLATFORM_TABLE));
	}
	
	@Test
	public void	testFindPlatformByIdDoesntExist()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Optional<Platform> maybePlatform = platformDao.findById(1);
		Assert.assertFalse(maybePlatform.isPresent());
	}
	
	@Test
	public void	testFindPlatformByIdExists()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Optional<Platform> maybePlatform = platformDao.findById(p.getId());
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testFindPlatformByNameDoesntExist()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Optional<Platform> maybePlatform = platformDao.findByName(PLATFORM_NAME);
		Assert.assertFalse(maybePlatform.isPresent());
	}
	
	@Test
	public void	testFindPlatformByNameExists()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Optional<Platform> maybePlatform = platformDao.findByName(PLATFORM_NAME);
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testChangePlatformName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Optional<Platform> maybePlatform = platformDao.changeName(p.getId(), "La Plei Cuatro");
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals("La Plei Cuatro", maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testChangePlatformShortName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Optional<Platform> maybePlatform = platformDao.changeShortName(p.getId(), "Play4");
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals("Play4", maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testChangeLogo()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Optional<Platform> maybePlatform = platformDao.changeLogo(p.getId(), "http://ps4.com/logo.png");
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals("http://ps4.com/logo.png", maybePlatform.get().getLogo());
	}
	/*
	@Test
	public void	testGetAllPlatforms()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, PLATFORM_TABLE);
		Platform ps4 = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, platformInsert);
		Platform ns = TestMethods.addPlatform("Nintendo Switch", "NS", "http://nintendo.com/switch.png", platformInsert);
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ps4);
		myList.add(ns);
		
		List<Platform> platformList = platformDao.getAllPlatforms();
		Assert.assertFalse(platformList.isEmpty());
		Assert.assertEquals(2, platformList.size());
		Assert.assertEquals(platformList.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList.get(1).getName(), myList.get(1).getName());
	}
	*/
}
