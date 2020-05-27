package ar.edu.itba.paw.persistence;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.dao.RegionDao;
import ar.edu.itba.paw.model.Region;

@Repository
public class RegionJpaDao implements RegionDao
{
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Region> getAllRegions()
	{
		final TypedQuery<Region> query = em.createQuery("from Region", Region.class);
		return query.getResultList();
	}
}
