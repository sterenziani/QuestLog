package ar.edu.itba.paw.interfaces.dao;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import ar.edu.itba.paw.model.PasswordResetToken;
import ar.edu.itba.paw.model.User;

public interface UserDao
{
	/**
	 * Finds a user given its ID
	 * @param id The unique ID for the user.
	 * @return The matched user, or null otherwise.
	 */
	Optional<User> findById(long id);
	
	/**
	 * Finds a user given its usernme
	 * @param username The unique username for the user.
	 * @return The matched user, or null otherwise.
	 */
	Optional<User> findByUsername(String username);
	
	/**
	 * Finds a user given its email
	 * @param username The unique email for the user.
	 * @return The matched user, or null otherwise.
	 */
	Optional<User> findByEmail(String email);
	
	/**
	 * Create a new user.
	 * @param username	The name of the user.
	 * @param password	The password of the user.
	 * @param email		The email of the user.
	 * @param locale 	The locale the user uses
	 * @return The created user.
	 */
	User create(String username, String password, String email, Locale locale);
	
	void updateLocale(User user, Locale locale);
	
	List<User> getAllUsers();
	void saveToken(PasswordResetToken token);
	Optional<PasswordResetToken> findTokenByToken(String token);
	Optional<User> findUserByToken(String token);
	void changePassword(User user, String password);
	void deleteTokenForUser(User u);
	
}
