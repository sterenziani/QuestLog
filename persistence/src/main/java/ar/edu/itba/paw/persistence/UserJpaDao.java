package ar.edu.itba.paw.persistence;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.model.PasswordResetToken;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;

@Repository
public class UserJpaDao implements UserDao
{
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<User> findById(long id)
	{
		return Optional.ofNullable(em.find(User.class, id));
	}

	@Override
	public Optional<User> findByUsername(String username)
	{
		final TypedQuery<User> query = em.createQuery("from User as u where u.username = :username", User.class);
		query.setParameter("username", username);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public Optional<User> findByEmail(String email)
	{
		final TypedQuery<User> query = em.createQuery("from User as u where u.email = :email", User.class);
		query.setParameter("email", email);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public User create(String username, String password, String email, Locale locale)
	{
		final User user = new User(username, password, email, locale.toLanguageTag());
		em.persist(user);
		return user;
	}

	@Override
	public void updateLocale(User user, Locale locale)
	{
		user.setLocale(locale);
	}

	@Override
	public List<User> getAllUsers()
	{
		final TypedQuery<User> query = em.createQuery("from User", User.class);
		return query.getResultList();
	}

	@Override
	public void saveToken(PasswordResetToken token)
	{
		em.createQuery("delete PasswordResetToken as t where t.user.id = :userId").setParameter("userId", token.getUser().getId()).executeUpdate();
		em.persist(token);
	}

	@Override
	public Optional<PasswordResetToken> findTokenByToken(String token)
	{
		final TypedQuery<PasswordResetToken> query = em.createQuery("from PasswordResetToken as prt where prt.token = :token", PasswordResetToken.class);
		query.setParameter("token", token);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public Optional<User> findUserByToken(String token)
	{
		final TypedQuery<PasswordResetToken> query = em.createQuery("from PasswordResetToken as prt where prt.token = :token", PasswordResetToken.class);
		query.setParameter("token", token);
		Optional<PasswordResetToken> opt = query.getResultList().stream().findFirst();
		if(opt.isPresent())
			return Optional.ofNullable(opt.get().getUser());
		return Optional.empty();
	}

	@Override
	public void changePassword(User user, String password)
	{
		user.setPassword(password);
	}

	@Override
	public void deleteTokenForUser(User u)
	{
		em.createQuery("delete PasswordResetToken as t where t.user.id = :userId").setParameter("userId", u.getId()).executeUpdate();
	}

	@Override
	public int countUserSearchResults(String searchTerm)
	{
		Query nativeQuery = em.createNativeQuery("SELECT COUNT(user_id) FROM users WHERE LOWER(username) LIKE CONCAT('%', :searchTerm, '%')");
		nativeQuery.setParameter("searchTerm", searchTerm);
		return ((Number) nativeQuery.getSingleResult()).intValue();
	}

	@Override
	public List<User> searchByUsernamePaged(String searchTerm, int page, int pageSize)
	{
		Query nativeQuery = em.createNativeQuery("SELECT user_id FROM users WHERE LOWER(username) LIKE CONCAT('%', :searchTerm, '%') ORDER BY username asc");
		nativeQuery.setParameter("searchTerm", searchTerm);
		nativeQuery.setFirstResult((page-1) * pageSize);
		nativeQuery.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Long> filteredIds = (List<Long>) nativeQuery.getResultList();
		if(filteredIds.isEmpty())
			return Collections.emptyList();
		final TypedQuery<User> query = em.createQuery("from User where user_id IN :filteredIds ORDER BY username asc", User.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}

	@Override
	public void addAdmin(User u)
	{
		u.addRole(new Role("Admin"));
	}

	@Override
	public void removeAdmin(User u)
	{
		u.removeRole(new Role("Admin"));
	}		
}
