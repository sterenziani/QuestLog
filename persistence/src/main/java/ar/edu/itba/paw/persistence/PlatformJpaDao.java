package ar.edu.itba.paw.persistence;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ar.edu.itba.paw.model.entity.Platform;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.dao.PlatformDao;

@Repository
public class PlatformJpaDao implements PlatformDao
{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Optional<Platform> findById(long id)
	{
		return Optional.ofNullable(em.find(Platform.class, id));
	}

	@Override
	public Optional<Platform> findByName(String name)
	{
		final TypedQuery<Platform> query = em.createQuery("from Platform as p where p.name = :name", Platform.class);
		query.setParameter("name", name);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public Optional<Platform> changeName(long id, String new_name)
	{
		Platform p = em.find(Platform.class, id);
		if(p != null)
			p.setName(new_name);
		return Optional.of(p);
	}

	@Override
	public Optional<Platform> changeLogo(long id, String new_logo)
	{
		Platform p = em.find(Platform.class, id);
		if(p != null)
			p.setLogo(new_logo);
		return Optional.of(p);
	}

	@Override
	public Optional<Platform> changeShortName(long id, String new_shortName)
	{
		Platform p = em.find(Platform.class, id);
		if(p != null)
			p.setShortName(new_shortName);
		return Optional.of(p);
	}

	@Override
	public Platform register(String name, String shortName, String logo)
	{
		final Platform p = new Platform(name, shortName, logo);
		em.persist(p);
		return p;
	}

	@Override
	public List<Platform> getAllPlatforms()
	{
		final TypedQuery<Platform> query = em.createQuery("from Platform ORDER BY platform_name asc", Platform.class);
		return query.getResultList();
	}

	@Override
	public List<Platform> getPlatforms(int page, int pageSize)
	{
		Query nativeQuery = em.createNativeQuery("SELECT platform FROM platforms ORDER BY platform_name asc");
		nativeQuery.setFirstResult((page-1) * pageSize);
		nativeQuery.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Long> filteredIds = (List<Long>) nativeQuery.getResultList();
		if(filteredIds.isEmpty())
			return Collections.emptyList();
		final TypedQuery<Platform> query = em.createQuery("from Platform where platform IN :filteredIds ORDER BY name asc", Platform.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}

	@Override
	public int countPlatforms()
	{
		final Query query = em.createQuery("SELECT count(*) FROM Platform", Long.class);
		return ((Number) query.getSingleResult()).intValue();
	}

	@Override
	public List<Platform> getBiggestPlatforms(int amount)
	{
		Query nativeQuery = em.createNativeQuery("SELECT platform FROM (SELECT platform, count(*) AS g FROM game_versions GROUP BY platform) AS a NATURAL JOIN platforms ORDER BY g DESC");
		nativeQuery.setMaxResults(amount);
		@SuppressWarnings("unchecked")
		List<Long> filteredIds = (List<Long>) nativeQuery.getResultList();
		if(filteredIds.isEmpty())
			return Collections.emptyList();
		final TypedQuery<Platform> query = em.createQuery("from Platform where platform IN :filteredIds ORDER BY name", Platform.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}

	@Override
	public List<Platform> getAllPlatformsWithGames()
	{
		Query nativeQuery = em.createNativeQuery("SELECT platform FROM (SELECT platform, count(*) AS g FROM game_versions GROUP BY platform) AS a NATURAL JOIN platforms ORDER BY platform_name ASC");
		@SuppressWarnings("unchecked")
		List<Long> filteredIds = (List<Long>) nativeQuery.getResultList();
		if(filteredIds.isEmpty())
			return Collections.emptyList();
		final TypedQuery<Platform> query = em.createQuery("from Platform where platform IN :filteredIds ORDER BY name ASC", Platform.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}

	@Override
	public List<Platform> getPlatformsWithGames(int page, int pageSize)
	{
		Query nativeQuery = em.createNativeQuery("SELECT platform FROM (SELECT platform, count(*) AS g FROM game_versions GROUP BY platform) AS a NATURAL JOIN platforms ORDER BY platform_name ASC");
		nativeQuery.setMaxResults(pageSize);
		nativeQuery.setFirstResult((page-1)*pageSize);
		@SuppressWarnings("unchecked")
		List<Long> filteredIds = (List<Long>) nativeQuery.getResultList();
		if(filteredIds.isEmpty())
			return Collections.emptyList();
		final TypedQuery<Platform> query = em.createQuery("from Platform where platform IN :filteredIds ORDER BY name ASC", Platform.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}

	@Override
	public int countPlatformsWithGames()
	{
		Query nativeQuery = em.createNativeQuery("SELECT count(*) FROM (SELECT platform, platform_name, platform_name_short, platform_logo FROM (SELECT platform, count(*) AS g FROM game_versions GROUP BY platform) AS a NATURAL JOIN platforms) AS p");
		return ((Number) nativeQuery.getSingleResult()).intValue();
	}

}