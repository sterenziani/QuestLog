package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	public Optional<User> findById(long id)
	{
		Optional<User> opt = userDao.findById(id);
		return opt;
	}
	
	@Override
	public Optional<User> findByIdWithDetails(long id)
	{
		return userDao.findByIdWithDetails(id);
	}
	
	@Override
	public Optional<User> findByUsername(String username)
	{
		Optional<User> opt =  userDao.findByUsername(username);
		return opt;
	}
	
	@Override
	public Optional<User> findByUsernameWithDetails(String username)
	{
		return userDao.findByUsernameWithDetails(username);
	}
	
	@Override
	public Optional<User> findByEmail(String email)
	{
		return userDao.findByEmail(email);
	}
	
	@Override
	public Optional<User> findByEmailWithDetails(String email)
	{
		return userDao.findByEmailWithDetails(email);
	}

	@Override
	public User register(String username, String password, String email)
	{
		if(findByUsername(username).isPresent())
			return null;
		String encodedPassword = encoder.encode(password);
		User u = userDao.create(username, encodedPassword, email);
		emailService.sendWelcomeEmail(u);
		return u;
	}
	
	@Override
	public User getLoggedUser()
	{
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null)
		{
			final Optional<User> opt = findByUsername((String) auth.getName());
			return opt.orElseGet(() -> null);
		}
		return null;
	}
	
	@Override
	public List<User> getAllUsers()
	{
		return userDao.getAllUsers();
	}
}
