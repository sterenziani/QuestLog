/*package ar.edu.itba.paw.persistence;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private UserJpaDao userDao;
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
		Assert.assertFalse(maybeUser.get().getAdminStatus());

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
		Assert.assertFalse(users.get(0).getAdminStatus());
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
	
	@Test
	public void testFindUserByToken()
	{
		int role = TestMethods.addRole("Admin", roleInsert);
		User u1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, jdbcInsert);
		TestMethods.connectRoles(u1, role, assignInsert);
		User u2 = TestMethods.addUser("user2", PASSWORD, EMAIL + ".ar", LOCALE, jdbcInsert);
		TestMethods.addToken(u1, TOKEN, DATE, tokenInsert);
		TestMethods.addToken(u2, TOKEN + "2", DATE, tokenInsert);
		Optional<User> user1 = userDao.findUserByToken(TOKEN);
		Optional<User> user2 = userDao.findUserByToken(TOKEN + "2");
		Optional<User> user3 = userDao.findUserByToken(TOKEN + "3");
		
		Assert.assertTrue(user1.isPresent());	
		Assert.assertTrue(user2.isPresent());	
		Assert.assertFalse(user3.isPresent());	

		Assert.assertTrue(user1.get().getAdminStatus());	
		Assert.assertFalse(user2.get().getAdminStatus());
		
		Assert.assertEquals(user1.get().getUsername(), u1.getUsername());
		Assert.assertEquals(user2.get().getUsername(), u2.getUsername());
	}
	
	@Test
	public void	searchByUsernamePaged()
	{	
		int role = TestMethods.addRole("Admin", roleInsert);
		User u1 = TestMethods.addUser(USERNAME, PASSWORD, EMAIL, LOCALE, jdbcInsert);
		TestMethods.connectRoles(u1, role, assignInsert);
		User u2 = TestMethods.addUser("user2", PASSWORD, "user2@aol.com", LOCALE, jdbcInsert);
		User u3 = TestMethods.addUser("uSer3", PASSWORD, "user3@aol.com", LOCALE, jdbcInsert);
		User u4 = TestMethods.addUser("pepe1234", PASSWORD, "user4@aol.com", LOCALE, jdbcInsert);

		List<User> users1 = userDao.searchByUsernamePaged("", 2,2);
		List<User> users2 = userDao.searchByUsernamePaged("us", 2,2);
		List<User> users3 = userDao.searchByUsernamePaged("us", 1,4);
		List<User> users4 = userDao.searchByUsernamePaged("asdgh", 1,4);

		List<User> myList = new ArrayList<User>();
		myList.add(u1);
		myList.add(u2);
		myList.add(u3);
		myList.add(u4);
		
		Assert.assertFalse(users1.isEmpty());
		Assert.assertFalse(users2.isEmpty());
		Assert.assertFalse(users3.isEmpty());
		Assert.assertTrue(users4.isEmpty());

		Assert.assertEquals(2, users1.size());
		Assert.assertEquals(1, users2.size());
		Assert.assertEquals(3, users3.size());

		Assert.assertEquals(users1.get(0).getId(), myList.get(2).getId());
		Assert.assertEquals(users1.get(1).getId(), myList.get(3).getId());
		
		Assert.assertEquals(users2.get(0).getId(), myList.get(2).getId());
		
		Assert.assertEquals(users3.get(0).getId(), myList.get(0).getId());
		Assert.assertEquals(users3.get(1).getId(), myList.get(1).getId());
		Assert.assertEquals(users3.get(2).getId(), myList.get(2).getId());
		Assert.assertTrue(users3.get(0).getAdminStatus());
		Assert.assertFalse(users3.get(1).getAdminStatus());
		
	}
}
*/