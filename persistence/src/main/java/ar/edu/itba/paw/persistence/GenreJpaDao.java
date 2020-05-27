package ar.edu.itba.paw.persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.dao.GenreDao;
import ar.edu.itba.paw.model.Genre;

@Repository
public class GenreJpaDao implements GenreDao
{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Optional<Genre> findById(long id)
	{
		return Optional.ofNullable(em.find(Genre.class, id));
	}

	@Override
	public Optional<Genre> findByName(String name)
	{
		final TypedQuery<Genre> query = em.createQuery("from Genre as gen where gen.name = :name", Genre.class);
		query.setParameter("name", name);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public Optional<Genre> changeName(long id, String new_name)
	{
		Genre gen = em.find(Genre.class, id);
		if(gen != null)
			gen.setName(new_name);
		return Optional.of(gen);
	}

	@Override
	public Optional<Genre> changeLogo(long id, String new_logo)
	{
		Genre gen = em.find(Genre.class, id);
		if(gen != null)
			gen.setLogo(new_logo);
		return Optional.of(gen);
	}

	@Override
	public Genre register(String name, String logo)
	{
		final Genre gen = new Genre(name, logo);
		em.persist(gen);
		return gen;
	}

	@Override
	public List<Genre> getAllGenres()
	{
		final TypedQuery<Genre> query = em.createQuery("from Genre ORDER BY genre_name asc", Genre.class);
		return query.getResultList();
	}

	@Override
	public List<Genre> getGenres(int page, int pageSize)
	{
		Query nativeQuery = em.createNativeQuery("SELECT genre FROM genres ORDER BY genre_name asc");
		nativeQuery.setFirstResult((page-1) * pageSize);
		nativeQuery.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Integer> list = nativeQuery.getResultList();
		List<Long> filteredIds = new ArrayList<Long>();
		for(Integer i : list)
			filteredIds.add(new Long(i));
		final TypedQuery<Genre> query = em.createQuery("from Genre where genre IN :filteredIds ORDER BY name asc", Genre.class);
		query.setParameter("filteredIds", filteredIds);
		return query.getResultList();
	}

	@Override
	public int countGenres()
	{
		final Query query = em.createQuery("SELECT count(*) FROM Genre", Long.class);
		return ((Number) query.getSingleResult()).intValue();
	}
}