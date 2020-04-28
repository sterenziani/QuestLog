package ar.edu.itba.paw.persistence;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.hsqldb.jdbc.JDBCDriver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.UserJdbcDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class UserJdbcDaoTest
{

	//private static final String PASSWORD = "Password";
	private	static final String USERNAME = "Username";
	private	static final String USER_TABLE = "users";

	@Autowired
	private DataSource ds;
	
	@Autowired
	private UserJdbcDao userDao;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	
	@Before
	public void	setUp()
	{
		userDao = new UserJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE).usingGeneratedKeyColumns("user_id");
	}
/*
	@Test
	public void	testCreateUser()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
		final User user = userDao.create(USERNAME);
        Assert.assertNotNull(user);
        Assert.assertEquals(USERNAME, user.getUsername());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
	}

	@Test
	public void	testFindByIdDoesntExist()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
		Optional<User> maybeUser = userDao.findById(1);
		Assert.assertFalse(maybeUser.isPresent());
	}
	*/
	@Test
	public void	testFindByIdUserExists ()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
		
		final Map<String, Object> args = new HashMap<>();
		args.put("username", USERNAME);
		Number key = jdbcInsert.executeAndReturnKey(args);
		
		Optional<User> maybeUser = userDao.findById(key.longValue());
		Assert.assertTrue(maybeUser.isPresent());
		Assert.assertEquals(USERNAME, maybeUser.get().getUsername());
	}

}