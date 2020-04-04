package ar.edu.itba.paw.persistence;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ar.edu.itba.paw.interfaces.PlatformDao;
import ar.edu.itba.paw.model.Platform;

@Repository
public class PlatformJdbcDao implements PlatformDao
{
	private	final SimpleJdbcInsert jdbcInsert;
	private JdbcTemplate jdbcTemplate;
	protected static final RowMapper<Platform> PLATFORM_MAPPER = new RowMapper<Platform>()
	{
		@Override
		public Platform mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new Platform(rs.getInt("platform"), rs.getString("platform_name"), rs.getString("platform_name_short"), rs.getString("platform_logo"));
		}
	};
	
	@Autowired
	public PlatformJdbcDao(final DataSource ds)
	{
	    jdbcTemplate = new JdbcTemplate(ds);
	    jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("platforms").usingGeneratedKeyColumns("platform");
	}
	
	@Override
	public Optional<Platform> findById(long id)
	{
		return jdbcTemplate.query("SELECT * FROM platforms WHERE platform = ?", PLATFORM_MAPPER, id).stream().findFirst();
	}

	@Override
	public Optional<Platform> findByName(String name)
	{
		return jdbcTemplate.query("SELECT * FROM platforms WHERE platform_name LIKE ?", PLATFORM_MAPPER, name).stream().findFirst();
	}

	@Override
	public Optional<Platform> changeName(long id, String new_name)
	{
		return jdbcTemplate.query("UPDATE TABLE platforms SET platform_name LIKE ? WHERE platform = ?", PLATFORM_MAPPER, id, new_name).stream().findFirst();
	}

	@Override
	public Optional<Platform> changeLogo(long id, String new_logo)
	{
		return jdbcTemplate.query("UPDATE TABLE platforms SET platform_logo LIKE ? WHERE platform = ?", PLATFORM_MAPPER, id, new_logo).stream().findFirst();
	}

	@Override
	public Optional<Platform> changeShortName(long id, String new_shortName)
	{
		return jdbcTemplate.query("UPDATE TABLE platforms SET platform_name_short LIKE ? WHERE platform = ?", PLATFORM_MAPPER, id, new_shortName).stream().findFirst();
	}

	@Override
	public Platform register(String name, String shortName, String logo)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("name", name); 
		args.put("shortName", shortName);
		args.put("logo", logo);
		final Number platformId = jdbcInsert.executeAndReturnKey(args);
		return new Platform(platformId.longValue(), name, shortName, logo);
	}

	@Override
	public List<Platform> getAllPlatforms()
	{
		return jdbcTemplate.query("SELECT * FROM platforms", PLATFORM_MAPPER);
	}
	
}
