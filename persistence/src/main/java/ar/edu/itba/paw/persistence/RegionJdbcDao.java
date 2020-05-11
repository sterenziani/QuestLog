package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.RegionDao;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RegionJdbcDao implements RegionDao {
    final private JdbcTemplate                  jdbcRegionTemplate;
    final private SimpleJdbcInsert              jdbcRegionInsert;
    protected static final RowMapper<Region>    REGION_MAPPER = new RowMapper<Region>()
    {
        @Override
        public Region mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            return new Region(rs.getInt("region"), rs.getString("region_name"), rs.getString("region_short"));
        }
    };

    @Autowired
    public RegionJdbcDao(final DataSource ds)
    {
        jdbcRegionTemplate  = new JdbcTemplate(ds);
        jdbcRegionInsert    = new SimpleJdbcInsert(jdbcRegionTemplate).withTableName("regions").usingGeneratedKeyColumns("region");
    }

    @Override
    public List<Region> getAllRegions() {
        return jdbcRegionTemplate.query("SELECT * FROM regions", REGION_MAPPER);
    }
}
