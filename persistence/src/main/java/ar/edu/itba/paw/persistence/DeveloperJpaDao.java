package ar.edu.itba.paw.persistence;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.dao.DeveloperDao;
import ar.edu.itba.paw.model.Developer;

@Repository
public class DeveloperJpaDao implements DeveloperDao
{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Optional<Developer> findById(long id)
	{
		return Optional.ofNullable(em.find(Developer.class, id));
	}

	@Override
	public Optional<Developer> findByName(String name)
	{
		final TypedQuery<Developer> query = em.createQuery("from Developer as dev where dev.name = :name", Developer.class);
		query.setParameter("name", name);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public Optional<Developer> changeName(long id, String new_name)
	{
		Developer dev = em.find(Developer.class, id);
		if(dev != null)
			dev.setName(new_name);
		return Optional.of(dev);
	}

	@Override
	public Optional<Developer> changeLogo(long id, String new_logo)
	{
		Developer dev = em.find(Developer.class, id);
		if(dev != null)
			dev.setLogo(new_logo);
		return Optional.of(dev);
	}

	@Override
	public Developer register(String name, String logo)
	{
		final Developer dev = new Developer(name, logo);
		em.persist(dev);
		return dev;
	}

	@Override
	public List<Developer> getAllDevelopers()
	{
		final TypedQuery<Developer> query = em.createQuery("from Developer ORDER BY developer_name asc", Developer.class);
		return query.getResultList();
	}

	@Override
	public List<Developer> getDevelopers(int page, int pageSize)
	{
		Query nativeQuery = em.createNativeQuery("SELECT developer FROM developers ORDER BY developer_name asc");
		nativeQuery.setFirstResult((page-1) * pageSize);
		nativeQuery.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Long> filteredIds = (List<Long>) nativeQuery.getResultList();
		if(filteredIds.isEmpty())
			return Collections.emptyList();
		final TypedQuery<Developer> query = em.createQuery("from Developer where developer IN :filteredIds ORDER BY name asc", Developer.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}

	@Override
	public int countDevelopers()
	{
		final Query query = em.createQuery("SELECT count(*) FROM Developer", Long.class);
		return ((Number) query.getSingleResult()).intValue();
	}

	@Override
	public List<Developer> getBiggestDevelopers(int amount)
	{
		Query nativeQuery = em.createNativeQuery("SELECT developer FROM (SELECT developer, count(*) AS g FROM development GROUP BY developer) AS a NATURAL JOIN developers ORDER BY g DESC");
		nativeQuery.setMaxResults(amount);
		@SuppressWarnings("unchecked")
		List<Long> filteredIds = (List<Long>) nativeQuery.getResultList();
		if(filteredIds.isEmpty())
			return Collections.emptyList();
		final TypedQuery<Developer> query = em.createQuery("from Developer where developer IN :filteredIds ORDER BY name", Developer.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}
}