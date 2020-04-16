package ar.edu.itba.paw.service;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.User;

@RunWith (MockitoJUnitRunner.class)
public class UserServiceImplTest
{
	//private static final String PASSWORD = "passwordpassword";
	private static final String USERNAME = "username";

	@InjectMocks
	private UserServiceImpl userService = new UserServiceImpl();
	
	@Mock
	private UserDao mockDao;
	/*
	@Test
	public void testCreateUser()
	{
		// 1. Setup!
		Mockito.when(mockDao.create(Mockito.eq(USERNAME))).thenReturn(new User(1, USERNAME));
		
		// 2. "ejercito" la class under test
		User user = userService.register(USERNAME);
		
		// 3. Asserts!
		Assert.assertNotNull(user);
		Assert.assertEquals(USERNAME, user.getUsername());
	}
	*/
}