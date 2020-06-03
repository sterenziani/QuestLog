package ar.edu.itba.paw.persistence;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.dao.PublisherDao;
import ar.edu.itba.paw.model.Publisher;

@Repository
public class PublisherJpaDao implements PublisherDao
{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Optional<Publisher> findById(long id)
	{
		return Optional.ofNullable(em.find(Publisher.class, id));
	}

	@Override
	public Optional<Publisher> findByName(String name)
	{
		final TypedQuery<Publisher> query = em.createQuery("from Publisher as pub where pub.name = :name", Publisher.class);
		query.setParameter("name", name);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public Optional<Publisher> changeName(long id, String new_name)
	{
		Publisher pub = em.find(Publisher.class, id);
		if(pub != null)
			pub.setName(new_name);
		return Optional.of(pub);
	}

	@Override
	public Optional<Publisher> changeLogo(long id, String new_logo)
	{
		Publisher pub = em.find(Publisher.class, id);
		if(pub != null)
			pub.setLogo(new_logo);
		return Optional.of(pub);
	}

	@Override
	public Publisher register(String name, String logo)
	{
		final Publisher pub = new Publisher(name, logo);
		em.persist(pub);
		return pub;
	}

	@Override
	public List<Publisher> getAllPublishers()
	{
		final TypedQuery<Publisher> query = em.createQuery("from Publisher ORDER BY publisher_name asc", Publisher.class);
		return query.getResultList();
	}

	@Override
	public List<Publisher> getPublishers(int page, int pageSize)
	{
		Query nativeQuery = em.createNativeQuery("SELECT publisher FROM publishers ORDER BY publisher_name asc");
		nativeQuery.setFirstResult((page-1) * pageSize);
		nativeQuery.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Long> filteredIds = (List<Long>) nativeQuery.getResultList();
		if(filteredIds.isEmpty())
			return Collections.emptyList();
		final TypedQuery<Publisher> query = em.createQuery("from Publisher where publisher IN :filteredIds ORDER BY name asc", Publisher.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}

	@Override
	public int countPublishers()
	{
		final Query query = em.createQuery("SELECT count(*) FROM Publisher", Long.class);
		return ((Number) query.getSingleResult()).intValue();
	}

	@Override
	public List<Publisher> getBiggestPublishers(int amount)
	{
		Query nativeQuery = em.createNativeQuery("SELECT publisher FROM (SELECT publisher, count(*) AS g FROM publishing GROUP BY publisher) AS a NATURAL JOIN publishers ORDER BY g DESC");
		nativeQuery.setMaxResults(amount);
		@SuppressWarnings("unchecked")
		List<Long> filteredIds = (List<Long>) nativeQuery.getResultList();
		if(filteredIds.isEmpty())
			return Collections.emptyList();
		final TypedQuery<Publisher> query = em.createQuery("from Publisher where publisher IN :filteredIds ORDER BY name", Publisher.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}
}