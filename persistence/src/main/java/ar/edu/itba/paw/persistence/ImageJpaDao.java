package ar.edu.itba.paw.persistence;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.model.entity.Image;

@Repository
public class ImageJpaDao implements ImageDao
{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Optional<Image> findByImageName(String image_name)
	{
		final TypedQuery<Image> query = em.createQuery("from Image as i where i.imageName = :imageName", Image.class);
		query.setParameter("imageName", image_name);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public Image uploadImage(String image_name, byte[] image_data)
	{
		final Image image = new Image(image_name, image_data);
		em.persist(image);
		return image;
	}

	@Override
	public void removeByName(String image_name)
	{
		em.createQuery("delete Image as i where i.imageName = :imageName").setParameter("imageName", image_name).executeUpdate();
	}

}
