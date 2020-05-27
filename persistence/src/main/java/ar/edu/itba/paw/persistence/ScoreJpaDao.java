package ar.edu.itba.paw.persistence;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.dao.ScoreDao;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Score;
import ar.edu.itba.paw.model.User;

@Repository
public class ScoreJpaDao implements ScoreDao
{
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<Score> findScore(User user, Game game) 
	{
		final TypedQuery<Score> query = em.createQuery("from Score as sc where sc.user.id = :user.id and sc.game.id = :game.id", Score.class);
		query.setParameter("user", user);
		query.setParameter("game", game);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public Optional<Score> changeScore(int new_score, User user, Game game) 
	{
		final TypedQuery<Score> query = em.createQuery("from Score as sc where sc.user.id = :user.id and sc.game.id = :game.id", Score.class);
		query.setParameter("user", user);
		query.setParameter("game", game);
		Optional<Score> sc = query.getResultList().stream().findFirst();
		if(sc.isPresent())
			sc.get().setScore(new_score);
		return sc;
	}

	@Override
	public Integer findAverageScore(Game game) {
		final Query query = em.createQuery("SELECT AVG(score) FROM Score WHERE game.id = :gameId", Long.class).setParameter("gameId", game.getId());
		return ((Number) query.getSingleResult()).intValue();
	}

	@Override
	public Score register(User user, Game game, int score) 
	{
		final Score sc = new Score(user, game, score);
		em.persist(sc);
		return sc;
	}

	@Override
	public List<Score> findAllUserScores(User user) 
	{
		final TypedQuery<Score> query = em.createQuery("from Score as sc where sc.user.id = :userId", Score.class);
		query.setParameter("userId", user.getId());
		return query.getResultList();
	}

	@Override
	public List<Score> findAllUserScores(User user, int page, int pageSize) 
	{
		final TypedQuery<Score> query = em.createQuery("From Score as sc where sc.user.id = userId", Score.class).setParameter("userId", user.getId());
		query.setFirstResult((page-1) * pageSize); 
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	@Override
	public int countAllUserScores(User user) {
		final Query query = em.createQuery("SELECT count(*) FROM Score WHERE user.id = :userId", Long.class).setParameter("userId", user.getId());
		return ((Number) query.getSingleResult()).intValue();
	}

	@Override
	public List<Score> getAllScores() {
		final TypedQuery<Score> query = em.createQuery("from Score", Score.class);
		return query.getResultList();
	}		
}