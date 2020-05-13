package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import org.hsqldb.types.BinaryData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(scripts = {"classpath:schema.sql"})
public class ImageJdbcDaoTest {

    private static final String IMAGE_TABLE         = "images";
    private static final String IMAGE_KEY_COLUMN    = "image";

    @Autowired
    private DataSource ds;

    @Autowired
    private ImageJdbcDao        imageJdbcDao;
    private JdbcTemplate        jdbcTemplate;
    private SimpleJdbcInsert    jdbcInsert;

    @Before
    public void setUp(){
        imageJdbcDao    = new ImageJdbcDao(ds);
        jdbcTemplate    = new JdbcTemplate(ds);
        jdbcInsert      = new SimpleJdbcInsert(ds).withTableName(IMAGE_TABLE).usingGeneratedKeyColumns(IMAGE_KEY_COLUMN);
    }

    private static final String      IMAGE_NAME      = "sample.jpg";
    private static final byte[] IMAGE_DATA      = null;

    @Test
    public void testUploadImage(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGE_TABLE);
        final Image image = imageJdbcDao.uploadImage(IMAGE_NAME, IMAGE_DATA);
        Assert.assertNotNull(image);
        Assert.assertEquals(image.getImageName(), IMAGE_NAME);
        Assert.assertEquals(image.getImageData(), IMAGE_DATA);
    }

    @Test
    public void	testFindByImageName()
    {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGE_TABLE);

        TestMethods.addImage(IMAGE_NAME, IMAGE_DATA, jdbcInsert);

        Optional<Image> maybeImage = imageJdbcDao.findByImageName(IMAGE_NAME);
        Assert.assertTrue(maybeImage.isPresent());
        Assert.assertEquals(IMAGE_NAME, maybeImage.get().getImageName());
        Assert.assertEquals(IMAGE_DATA, maybeImage.get().getImageData());

    }
    
    @Test
    public void testRemoveByName()
    {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGE_TABLE);
        TestMethods.addImage(IMAGE_NAME, IMAGE_DATA, jdbcInsert);
        Image image2 = TestMethods.addImage("pic", IMAGE_DATA, jdbcInsert);
        
        imageJdbcDao.removeByName(image2.getImageName());

        Optional<Image> maybeImage = imageJdbcDao.findByImageName(image2.getImageName());
        Assert.assertFalse(maybeImage.isPresent());
        
        imageJdbcDao.removeByName("pic2");
        Optional<Image> maybeImage2 = imageJdbcDao.findByImageName(IMAGE_NAME);
        Assert.assertTrue(maybeImage2.isPresent());
        Assert.assertEquals(IMAGE_NAME, maybeImage2.get().getImageName());
    }
}
