package ar.edu.itba.paw.persistence;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Review;
import ar.edu.itba.paw.model.User;

@Repository
public class ReviewJpaDao implements ReviewDao {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<Review> findReviewById(long review) {
		return Optional.ofNullable(em.find(Review.class, review));
	}

	@Override
	public List<Review> findGameReviews(Game game) {
		final TypedQuery<Review> query = em.createQuery("from Review as rev where rev.game.id = :gameId", Review.class);
		query.setParameter("gameId", game.getId());
		return query.getResultList();
	}
	
	@Override
	public List<Review> findGameReviews(Game game, int page, int pageSize) {
		Query nativeQuery = em.createNativeQuery("SELECT review FROM reviews WHERE enabled = true AND game = :gameId ORDER BY post_date desc");
		nativeQuery.setParameter("gameId", game.getId());
		nativeQuery.setFirstResult((page-1) * pageSize);
		nativeQuery.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Long> filteredIds = (List<Long>) nativeQuery.getResultList();
		if(filteredIds.isEmpty())
			return Collections.emptyList();
		final TypedQuery<Review> query = em.createQuery("from Review where review IN :filteredIds ORDER BY post_date desc", Review.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}
	
	@Override
	public int countReviewsForGame(Game game)
	{
		Query nativeQuery = em.createNativeQuery("SELECT COUNT(*) FROM reviews WHERE enabled = true AND game = :gameId");
		nativeQuery.setParameter("gameId", game.getId());
		return ((Number) nativeQuery.getSingleResult()).intValue();
	}

	@Override
	public List<Review> findGameAndPlatformReviews(Game game, Platform platform) {
		final TypedQuery<Review> query = em.createQuery("from Review as rev where enabled = true AND rev.game.id = :gameId and rev.platform.id = :platId", Review.class);
		query.setParameter("gameId", game.getId());
		query.setParameter("platId", platform.getId());
		return query.getResultList();
	}

	@Override
	public List<Review> findUserReviews(User user) {
		final TypedQuery<Review> query = em.createQuery("from Review as rev where enabled = true AND rev.user.id = :userId", Review.class);
		query.setParameter("userId", user.getId());
		return query.getResultList();
	}
	
	@Override
	public List<Review> findUserReviews(User user, int page, int pageSize) {
		Query nativeQuery = em.createNativeQuery("SELECT review FROM reviews WHERE enabled = true AND user_id = :userId ORDER BY post_date desc");
		nativeQuery.setParameter("userId", user.getId());
		nativeQuery.setFirstResult((page-1) * pageSize);
		nativeQuery.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Long> filteredIds = (List<Long>) nativeQuery.getResultList();
		if(filteredIds.isEmpty())
			return Collections.emptyList();
		final TypedQuery<Review> query = em.createQuery("from Review where enabled = true AND review IN :filteredIds ORDER BY post_date desc", Review.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}
	
	@Override
	public int countReviewsByUser(User user)
	{
		Query nativeQuery = em.createNativeQuery("SELECT COUNT(*) FROM reviews WHERE enabled = true AND user_id = :userId");
		nativeQuery.setParameter("userId", user.getId());
		return ((Number) nativeQuery.getSingleResult()).intValue();
	}

	@Override
	public List<Review> findUserAndGameReviews(User user, Game game) {
		final TypedQuery<Review> query = em.createQuery("from Review as rev where rev.enabled = true AND rev.user.id = :userId and rev.game.id = :gameId", Review.class);
		query.setParameter("gameId", game.getId());
		query.setParameter("userId", user.getId());
		return null;
	}
	
	@Override
	public List<Review> findUserAndGameReviews(User user, Game game, int page, int pageSize) {
		Query nativeQuery = em.createNativeQuery("SELECT review FROM reviews WHERE enabled = true AND user_id = :userId AND game = :gameId ORDER BY post_date desc");
		nativeQuery.setParameter("userId", user.getId());
		nativeQuery.setParameter("gameId", game.getId());
		nativeQuery.setFirstResult((page-1) * pageSize);
		nativeQuery.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Long> filteredIds = (List<Long>) nativeQuery.getResultList();
		if(filteredIds.isEmpty())
			return Collections.emptyList();
		final TypedQuery<Review> query = em.createQuery("from Review where enabled = true AND review IN :filteredIds ORDER BY post_date desc", Review.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}
	
	@Override
	public int countReviewsByUserAndGame(User user, Game game)
	{
		Query nativeQuery = em.createNativeQuery("SELECT COUNT(*) FROM reviews WHERE enabled = true AND user_id = :userId AND game = :gameId");
		nativeQuery.setParameter("userId", user.getId());
		nativeQuery.setParameter("gameId", game.getId());
		return ((Number) nativeQuery.getSingleResult()).intValue();
	}

	@Override
	public Optional<Review> changeReviewBody(long review, String body) {
		Review rev = em.find(Review.class, review);
		if(rev != null)
			rev.setBody(body);
		return Optional.of(rev);
	}

	@Override
	public List<Review> getAllReviews() {
		final TypedQuery<Review> query = em.createQuery("from Review", Review.class);
		return query.getResultList();
	}

	@Override
	public Review register(User user, Game game, Platform platform, int score, String body, LocalDate date) {
		final Review rev = new Review(user, game, platform, score, body, date);
		em.persist(rev);
		return rev;
	}
	
	@Override
	public void deleteReview(Review r)
	{
		r.disable();
	}
}
