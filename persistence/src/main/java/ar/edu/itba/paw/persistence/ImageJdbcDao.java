package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ImageJdbcDao implements ImageDao {

    private static final String     IMAGE_TABLE          = "images";
    private static final String     IMAGE_KEY_COLUMN     = "image";
    private static final String     IMAGE_NAME_COLUMN    = "image_name";
    private static final String     IMAGE_DATA_COLUMN    = "image_data";

    private final SimpleJdbcInsert  jdbcInsert;
    private final JdbcTemplate      jdbcTemplate;

    protected static final RowMapper<Image> IMAGE_MAPPER = (rs, rowNum) -> new Image(rs.getInt(IMAGE_KEY_COLUMN), rs.getString(IMAGE_NAME_COLUMN), rs.getBytes(IMAGE_DATA_COLUMN));

    @Autowired
    public ImageJdbcDao(final DataSource ds){
        jdbcTemplate    = new JdbcTemplate(ds);
        jdbcInsert      = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(IMAGE_TABLE)
                .usingGeneratedKeyColumns(IMAGE_KEY_COLUMN);
    }

    @Override
    public Optional<Image> findByImageName(String image_name) {
        return jdbcTemplate.query("SELECT * FROM images WHERE image_name = ?", IMAGE_MAPPER, image_name).stream().findFirst();
    }

    @Override
    public Image uploadImage(String image_name, byte[] image_data) {
        final Map<String, Object> args = new HashMap<>();
        args.put(IMAGE_NAME_COLUMN, image_name);
        args.put(IMAGE_DATA_COLUMN, image_data);
        final Number key = jdbcInsert.executeAndReturnKey(args);
        return new Image(key.longValue(), image_name, image_data);
    }

    @Override
    public void removeByName(String image_name) {
        jdbcTemplate.update("DELETE FROM images WHERE image_name = ?", image_name);
    }
}
