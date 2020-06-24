package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.entity.Image;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ImageJpaDaoTest {

    private static final String IMAGE_TABLE         = "images";
    
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ImageJpaDao         imageJdbcDao;

    private static final String      IMAGE_NAME      = "sample.jpg";
    private static final byte[] IMAGE_DATA      = null;

    @Test
    public void testUploadImage(){
        TestMethods.deleteFromTable(IMAGE_TABLE, em);
        final Image image = imageJdbcDao.uploadImage(IMAGE_NAME, IMAGE_DATA);
        Assert.assertNotNull(image);
        Assert.assertEquals(image.getImageName(), IMAGE_NAME);
        Assert.assertEquals(image.getImageData(), IMAGE_DATA);
    }

    @Test
    public void	testFindByImageName()
    {
        TestMethods.deleteFromTable(IMAGE_TABLE, em);

        TestMethods.addImage(IMAGE_NAME, IMAGE_DATA, em);

        Optional<Image> maybeImage = imageJdbcDao.findByImageName(IMAGE_NAME);
        Assert.assertTrue(maybeImage.isPresent());
        Assert.assertEquals(IMAGE_NAME, maybeImage.get().getImageName());
        Assert.assertEquals(IMAGE_DATA, maybeImage.get().getImageData());

    }
    
    @Test
    public void testRemoveByName()
    {
        TestMethods.deleteFromTable(IMAGE_TABLE, em);
        TestMethods.addImage(IMAGE_NAME, IMAGE_DATA, em);
        Image image2 = TestMethods.addImage("pic", IMAGE_DATA, em);
        
        imageJdbcDao.removeByName(image2.getImageName());

        Optional<Image> maybeImage = imageJdbcDao.findByImageName(image2.getImageName());
        Assert.assertFalse(maybeImage.isPresent());
        
        imageJdbcDao.removeByName("pic2");
        Optional<Image> maybeImage2 = imageJdbcDao.findByImageName(IMAGE_NAME);
        Assert.assertTrue(maybeImage2.isPresent());
        Assert.assertEquals(IMAGE_NAME, maybeImage2.get().getImageName());
    }
}