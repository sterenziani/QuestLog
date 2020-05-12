package ar.edu.itba.paw.service;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.PasswordResetToken;
import ar.edu.itba.paw.model.User;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private GameService gs;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PasswordEncoder encoder;

	@Transactional
	@Override
	public Optional<User> findById(long id)
	{
		Optional<User> opt = userDao.findById(id);
		return opt;
	}

	@Transactional
	@Override
	public Optional<User> findByIdWithDetails(long id)
	{
		Optional<User> opt = userDao.findByIdWithDetails(id);
		if(opt.isPresent())
			gs.updateBacklogDetails(opt.get().getBacklog());
		return opt;
	}

	@Transactional
	@Override
	public Optional<User> findByUsername(String username)
	{
		Optional<User> opt =  userDao.findByUsername(username);
		return opt;
	}

	@Transactional
	@Override
	public Optional<User> findByUsernameWithDetails(String username)
	{
		return userDao.findByUsernameWithDetails(username);
	}

	@Transactional
	@Override
	public Optional<User> findByEmail(String email)
	{
		return userDao.findByEmail(email);
	}

	@Transactional
	@Override
	public Optional<User> findByEmailWithDetails(String email)
	{
		return userDao.findByEmailWithDetails(email);
	}

	@Transactional
	@Override
	public User register(String username, String password, String email, Locale locale)
	{
		if(findByUsername(username).isPresent())
			return null;
		String encodedPassword = encoder.encode(password);
		User u = userDao.create(username, encodedPassword, email, locale);
		emailService.sendWelcomeEmail(u);
		return u;
	}

	@Transactional
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

	@Transactional
	@Override
	public List<User> getAllUsers()
	{
		return userDao.getAllUsers();
	}

	@Transactional
	@Override
	public void createPasswordResetTokenForUser(User user, String token)
	{
	    PasswordResetToken myToken = new PasswordResetToken(token, user);
	    userDao.saveToken(myToken);
	    emailService.sendAccountRecoveryEmail(user, token);
	}

	@Transactional
	@Override
	public String validatePasswordResetToken(String token)
	{
	    final Optional<PasswordResetToken> opt = userDao.findTokenByToken(token);
	    if(opt.isPresent())
	    {
	    	PasswordResetToken passToken = opt.get();
	    	if(isTokenExpired(passToken))
	    		return "expired";
	    	return null;
	    }
	    return "invalidToken";
	}

	@Transactional
	@Override
	public Optional<User> getUserByPasswordResetToken(String token)
	{
		return userDao.findUserByToken(token);
	}
	 
	private boolean isTokenExpired(PasswordResetToken passToken)
	{
	    final Calendar cal = Calendar.getInstance();
	    return passToken.getExpiryDate().before(cal.getTime());
	}

	@Transactional
	@Override
	public void changeUserPassword(User user, String password)
	{
		String encodedPassword = encoder.encode(password);
		userDao.changePassword(user, encodedPassword);
		userDao.deleteTokenForUser(user);
	}

	@Transactional
	@Override
	public void updateLocale(User user, Locale locale)
	{
		userDao.updateLocale(user, locale);
	}

	@Override
	public int countUserSearchResults(String searchTerm) {
		return userDao.countUserSearchResults(searchTerm);
	}

	@Override
	public List<User> searchByUsernamePaged(String searchTerm, int page, int pageSize) {
		return userDao.searchByUsernamePaged(searchTerm, page, pageSize);
	}
	
	@Override
	public void changeAdminStatus(User u) {
		if(u.getAdminStatus()) {
			u.setAdminStatus(false);
			userDao.removeAdmin(u);	
		}
		else {
			u.setAdminStatus(true);
			userDao.addAdmin(u);
		}
	}
}
