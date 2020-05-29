package ar.edu.itba.paw.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.dao.RunDao;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Playstyle;
import ar.edu.itba.paw.model.Run;
import ar.edu.itba.paw.model.User;

@Repository
public class RunJpaDao implements RunDao
{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Optional<Run> findRunById(long id)
	{
		return Optional.ofNullable(em.find(Run.class, id));
	}
	
	
	@Override
	public List<Run> findGameRuns(Game game, User user)
	{
		final TypedQuery<Run> query = em.createQuery("from Run as rn where rn.game.id = :gameId and rn.user.id = :userId", Run.class);
		query.setParameter("gameId", game.getId());
		query.setParameter("userId", user.getId());
		return query.getResultList();
	}

	@Override
	public List<Run> findAllUserRuns(User user)
	{
		final TypedQuery<Run> query = em.createQuery("from Run as rn where rn.user.id = :userId", Run.class);
		query.setParameter("userId", user.getId());
		return query.getResultList();
	}
	
	@Override
	public List<Run> findRunsByUser(User user, int page, int pageSize)
	{
		Query nativeQuery = em.createNativeQuery("SELECT run FROM runs WHERE user_id = :userId ORDER BY run asc");
		nativeQuery.setParameter("userId", user.getId());
		nativeQuery.setFirstResult((page-1) * pageSize);
		nativeQuery.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Integer> list = nativeQuery.getResultList();
		if(list.isEmpty())
			return Collections.emptyList();
		List<Long> filteredIds = new ArrayList<Long>();
		for(Integer i : list)
			filteredIds.add(new Long(i));
		final TypedQuery<Run> query = em.createQuery("from Run where id IN :filteredIds ORDER BY id asc", Run.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}

	@Override
	public int countRunsByUser(User user)
	{
		Query nativeQuery = em.createNativeQuery("SELECT COUNT(*) FROM runs WHERE user_id = :userId");
		nativeQuery.setParameter("userId", user.getId());
		return ((Number) nativeQuery.getSingleResult()).intValue();
	}


	@Override
	public List<Run> findAllGameRuns(Game game)
	{
		final TypedQuery<Run> query = em.createQuery("from Run as rn where rn.game.id = :gameId", Run.class);
		query.setParameter("gameId", game.getId());
		return query.getResultList();
	}

	@Override
	public List<Run> findPlaystyleAndGameRuns(Game game, Playstyle playstyle)
	{
		final TypedQuery<Run> query = em.createQuery("from Run as rn where rn.game.id = :gameId and rn.playstyle.id = :playId", Run.class);
		query.setParameter("gameId", game.getId());
		query.setParameter("playId", playstyle.getId());
		return query.getResultList();
	}
	
	@Override
	public List<Run> findSpecificRuns(Game game, Playstyle playstyle, Platform platform)
	{
		final TypedQuery<Run> query = em.createQuery("from Run as rn where rn.game.id = :gameId and rn.playstyle.id = :playId and rn.platform.id = :platId", Run.class);
		query.setParameter("gameId", game.getId());
		query.setParameter("playId", playstyle.getId());
		query.setParameter("platId", platform.getId());
		return query.getResultList();
	}

	@Override
	public long getAveragePlaytime(Game game)
	{
		final Query query = em.createNativeQuery("SELECT COALESCE(AVG(time),0) FROM runs WHERE game = :gameId").setParameter("gameId", game.getId());
		return((Number) query.getSingleResult()).longValue();
	}


	@Override
	public long getAveragePlatformPlaytime(Game game, Platform platform)
	{
		final Query query = em.createNativeQuery("SELECT COALESCE(AVG(time),0) FROM runs WHERE game = :gameId and platform = :platId");
		query.setParameter("gameId", game.getId());
		query.setParameter("platId", platform.getId());
		return((Number) query.getSingleResult()).longValue();
	}

	@Override
	public long getAveragePlaystylePlaytime(Game game, Playstyle playstyle)
	{
		final Query query = em.createNativeQuery("SELECT COALESCE(AVG(time),0) FROM runs WHERE game = :gameId and playstyle = :playId");
		query.setParameter("gameId", game.getId());
		query.setParameter("playId", playstyle.getId());
		return((Number) query.getSingleResult()).longValue();
	}

	@Override
	public long getAverageSpecificPlaytime(Game game , Playstyle playstyle, Platform platform)
	{
		final Query query = em.createNativeQuery("SELECT COALESCE(AVG(time),0) FROM runs WHERE game = :gameId and platform = :platId and playstyle = :playId");
		query.setParameter("gameId", game.getId());
		query.setParameter("platId", platform.getId());
		query.setParameter("playId", playstyle.getId());
		return((Number) query.getSingleResult()).longValue();
	}

	@Override
	public List<Run> getAllRuns(){
		final TypedQuery<Run> query = em.createQuery("from Runs", Run.class);
		return query.getResultList();
	}

	@Override
	public Run register(User user, Game game, Platform platform, Playstyle playstyle, long time) 
	{
		final Run run = new Run(user, game, platform, playstyle, time);
		em.persist(run);
		return run;
	}


	@Override
	public List<Playstyle> getAllPlaystyles()
	{
		final TypedQuery<Playstyle> query = em.createQuery("from Playstyle", Playstyle.class);
		return query.getResultList();
	}

	@Override
	public Optional<Playstyle> changePlaystyleName(String new_name, long playstyle)
	{
		Playstyle play = em.find(Playstyle.class, playstyle);
		if(play != null)
			play.setName(new_name);
		return Optional.of(play);
	}

	@Override
	public Optional<Playstyle> findPlaystyleById(long playstyle)
	{
		return Optional.ofNullable(em.find(Playstyle.class, playstyle));
	}

	@Override
	public Playstyle register(String name)
	{
		final Playstyle play = new Playstyle(name);
		em.persist(play);
		return play;
	}
	
	@Override
	public Optional<Playstyle> findPlaystyleByName(String name)
	{
		final TypedQuery<Playstyle> query = em.createQuery("from Playstyle as play where play.name = :name", Playstyle.class);
		query.setParameter("name", name);
		return query.getResultList().stream().findFirst();
	}

}