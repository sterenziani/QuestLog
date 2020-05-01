package ar.edu.itba.paw.service;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.model.User;

@RunWith (MockitoJUnitRunner.class)
public class UserServiceImplTest
{
	private static final String PASSWORD = "passwordpassword";
	private static final String USERNAME = "username";
	private static final String EMAIL = "example@yahoo.com";

	@InjectMocks
	private UserServiceImpl userService = new UserServiceImpl();
	
	@Mock
	private UserDao mockDao;
	
	@Test
	public void testCreateUser()
	{
		// 1. Setup!
		Mockito.when(mockDao.create(Mockito.eq(USERNAME), Mockito.eq(PASSWORD), Mockito.eq(EMAIL))).thenReturn(new User(1, USERNAME, PASSWORD, EMAIL));
		
		// 2. "ejercito" la class under test
		User user = userService.register(USERNAME, PASSWORD, EMAIL);
		
		// 3. Asserts!
		Assert.assertNotNull(user);
		Assert.assertEquals(USERNAME, user.getUsername());
	}
	
}