package ar.edu.itba.paw.persistence;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import ar.edu.itba.paw.model.PasswordResetToken;
import ar.edu.itba.paw.model.Run;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.UserJdbcDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class UserJdbcDaoTest
{
	private	static final String USERNAME = "Username";
	private	static final String PASSWORD = "password";
	private	static final String EMAIL = "email@example.com";
	private static final String LOCALE = "en";
	private static final Locale LOCALE_LOC = Locale.FRANCE;
	private static final String ROLES_TABLE = "roles";
	private	static final String USER_TABLE = "users";
	private	static final String ASSIGN_TABLE = "role_assignments";
	private	static final String TOKEN_TABLE = "tokens";
	private	static final String TOKEN = "token";
	private	static final Date DATE = new Date(2323223232L);
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private UserJdbcDao userDao;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	private SimpleJdbcInsert roleInsert;
	private SimpleJdbcInsert assignInsert;
	private SimpleJdbcInsert tokenInsert;

	
	@Before
	public void	setUp()
	{
		userDao = new UserJdbcDao(ds);
		jdbcTemplate = new JdbcTemplate(ds);
		
		jdbcInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE).usingGeneratedKeyColumns("user_id");
		JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

		
		roleInsert = new SimpleJdbcInsert(ds).withTableName(ROLES_TABLE).usingGeneratedKeyColumns("role");
		JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLES_TABLE);
		
		assignInsert = new SimpleJdbcInsert(ds).withTableName(ASSIGN_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, ASSIGN_TABLE);
		
		tokenInsert = new SimpleJdbcInsert(ds).withTableName(TOKEN_TABLE);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, TOKEN_TABLE);		
		
	}

	@Test
	public void	testCreateUser()
	{
		JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
		final User user = userDao.create(USERNAME,PASSWORD,EMAIL,LOCALE_LOC);
        Assert.assertNotNull(user);
        Assert.assertEquals(USERNAME, user.getUsername());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
	}

	@Test
	public void	testFindByIdDoesntExist()
	{
		Optional<User> maybeUser = userDao.findById(1);
		Assert.assertFalse(maybeUser.isPresent());
	}

	@Test
	public void	testFindByIdUserExists ()
	{
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, jdbcInsert);
		
		Optional<User> maybeUser = userDao.findById(u.getId());
		Assert.assertTrue(maybeUser.isPresent());
		Assert.assertEquals(USERNAME, maybeUser.get().getUsername());
	}
	
	@Test
	public void	testFindByUsernameDoesntExist()
	{
		Optional<User> maybeUser = userDao.findByUsername(USERNAME);
		Assert.assertFalse(maybeUser.isPresent());
	}

	@Test
	public void	testFindByUsernameExists ()
	{
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, jdbcInsert);
		
		Optional<User> maybeUser = userDao.findByUsername(u.getUsername());
		Assert.assertTrue(maybeUser.isPresent());
		Assert.assertEquals(USERNAME, maybeUser.get().getUsername());
	}
	
	@Test
	public void	testFindByEmailDoesntExist()
	{
		Optional<User> maybeUser = userDao.findByEmail(EMAIL);
		Assert.assertFalse(maybeUser.isPresent());
	}

	@Test
	public void	testFindByEmailExists ()
	{
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, jdbcInsert);
		
		Optional<User> maybeUser = userDao.findByEmail(u.getEmail());
		Assert.assertTrue(maybeUser.isPresent());
		Assert.assertEquals(USERNAME, maybeUser.get().getUsername());
	}
	
	@Test
	public void	testGetAllUsers ()
	{
		User u1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, jdbcInsert);
		User u2 = TestMethods.addUser("user2", PASSWORD, "user2@aol.com", LOCALE, jdbcInsert);

		List<User> users = userDao.getAllUsers();
		List<User> myList = new ArrayList<User>();
		myList.add(u1);
		myList.add(u2);
		
		Assert.assertFalse(users.isEmpty());
		Assert.assertEquals(2, users.size());

		Assert.assertEquals(users.get(0).getId(), myList.get(0).getId());
		Assert.assertEquals(users.get(1).getId(), myList.get(1).getId());	
	}
	
	@Test
	public void testChangePassword()
	{
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, jdbcInsert);
		userDao.changePassword(u, "NewPass");
		Optional<User> user = userDao.findById(u.getId());
		
		Assert.assertEquals("NewPass", user.get().getPassword());

	}
	
	@Test
	public void testCountUserSearchResults()
	{
		TestMethods.addUser("UsER1", PASSWORD, EMAIL, LOCALE, jdbcInsert);
		TestMethods.addUser("PoouS", PASSWORD, EMAIL + "ar", LOCALE, jdbcInsert);
		TestMethods.addUser("hola", PASSWORD, EMAIL + "uk", LOCALE, jdbcInsert);
		
		int count1 = userDao.countUserSearchResults("us");
		int count2 = userDao.countUserSearchResults("");
		int count3 = userDao.countUserSearchResults("asdgh");

		Assert.assertEquals(count1, 2);
		Assert.assertEquals(count2, 3);
		Assert.assertEquals(count3, 0);

	}
	
	@Test
	public void testFindTokenByToken()
	{
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, jdbcInsert);
		TestMethods.addToken(u, TOKEN, DATE, tokenInsert);
		Optional<PasswordResetToken> tok = userDao.findTokenByToken(TOKEN);
		
		Assert.assertTrue(tok.isPresent());
		Assert.assertEquals(tok.get().getUser().getUsername(), USERNAME);
	}
	
	@Test
	public void testDeleteTokenForUser() {
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, jdbcInsert);
		TestMethods.addToken(u, TOKEN, DATE, tokenInsert);
		Optional<PasswordResetToken> tok = userDao.findTokenByToken(TOKEN);
		
		Assert.assertTrue(tok.isPresent());
		Assert.assertEquals(tok.get().getUser().getUsername(), USERNAME);
		
		userDao.deleteTokenForUser(u);
		
		tok = userDao.findTokenByToken(TOKEN);
		Assert.assertFalse(tok.isPresent());
		
	}
	
	@Test
	public void testUpdateLocale() {
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, jdbcInsert);
		userDao.updateLocale(u, LOCALE_LOC);
		Optional<User> user = userDao.findById(u.getId());
		Assert.assertEquals(user.get().getLocale(), LOCALE_LOC);
	}
	

	@Test
	public void testAddAdmin()
	{
		int role = TestMethods.addRole("Admin", roleInsert);
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, jdbcInsert);
		userDao.addAdmin(u);
		Optional<User> user = userDao.findById(u.getId());
		Assert.assertTrue(user.get().getAdminStatus());	
	}
	
	@Test
	public void testDeleteAdmin()
	{
		int role = TestMethods.addRole("Admin", roleInsert);
		User u = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, jdbcInsert);
		TestMethods.connectRoles(u, role, assignInsert);

		
		userDao.removeAdmin(u);
		Optional<User> user = userDao.findById(u.getId());
		Assert.assertFalse(user.get().getAdminStatus());	
		
	}
}
