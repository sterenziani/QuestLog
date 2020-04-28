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

import ar.edu.itba.paw.model.Developer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class DeveloperJdbcDaoTest
{
	private	static final String DEVELOPER_TABLE = "developers";
	private	static final String DEVELOPER_NAME = "Nintendo";
	private	static final String DEVELOPER_LOGO = "https://nintendo/logo.jpg";

	@Autowired
	private DataSource ds;
	
	@Autowired
	private DeveloperJdbcDao developerDao;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert devInsert;
	
	@Before
	public void	setUp()
	{
		developerDao = new DeveloperJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		devInsert = new SimpleJdbcInsert(ds).withTableName(DEVELOPER_TABLE).usingGeneratedKeyColumns("developer");
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
		Developer d = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
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
		TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		Optional<Developer> maybeDeveloper = developerDao.findByName(DEVELOPER_NAME);
		Assert.assertTrue(maybeDeveloper.isPresent());
		Assert.assertEquals(DEVELOPER_NAME, maybeDeveloper.get().getName());
		Assert.assertEquals(DEVELOPER_LOGO, maybeDeveloper.get().getLogo());
	}
	
	@Test
	public void	testChangeDeveloperName()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		Developer d = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		Optional<Developer> maybeDeveloper = developerDao.changeName(d.getId(), "Noentiendo");
		Assert.assertTrue(maybeDeveloper.isPresent());
		Assert.assertEquals("Noentiendo", maybeDeveloper.get().getName());
		Assert.assertEquals(DEVELOPER_LOGO, maybeDeveloper.get().getLogo());
	}
	
	@Test
	public void	testChangeLogo()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		Developer d = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		Optional<Developer> maybeDeveloper = developerDao.changeLogo(d.getId(), "http://sega.com/logo.png");
		Assert.assertTrue(maybeDeveloper.isPresent());
		Assert.assertEquals(DEVELOPER_NAME, maybeDeveloper.get().getName());
		Assert.assertEquals("http://sega.com/logo.png", maybeDeveloper.get().getLogo());
	}
	/*
	@Test
	public void	testGetAllDevelopers()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, DEVELOPER_TABLE);
		Developer nintendo = TestMethods.addDeveloper(DEVELOPER_NAME, DEVELOPER_LOGO, devInsert);
		Developer sega = TestMethods.addDeveloper("Sega", DEVELOPER_LOGO, devInsert);
		List<Developer> myList = new ArrayList<Developer>();
		myList.add(nintendo);
		myList.add(sega);
		
		List<Developer> developerList = developerDao.getAllDevelopers();
		Assert.assertFalse(developerList.isEmpty());
		Assert.assertEquals(2, developerList.size());
		Assert.assertEquals(developerList.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(developerList.get(1).getName(), myList.get(1).getName());
	}
	*/
}
