package ar.edu.itba.paw.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private EmailService emailService;
	
	//@Autowired
	//private PasswordEncoder encoder;
	
	@Override
	public Optional<User> findById(long id)
	{
		return userDao.findById(id);
	}
	
	@Override
	public Optional<User> findByUsername(String username)
	{
		return userDao.findByUsername(username);
	}
	
	@Override
	public Optional<User> findByEmail(String email)
	{
		return userDao.findByEmail(email);
	}

	@Override
	public User register(String username, String password, String email)
	{
		if(findByUsername(username).isPresent())
			return null;
		//String encodedPassword = encoder.encode(password)
		User u = userDao.create(username, password, email);
		emailService.sendWelcomeEmail(u);
		return u;
	}
}
