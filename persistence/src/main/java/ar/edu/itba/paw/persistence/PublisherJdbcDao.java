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
import ar.edu.itba.paw.interfaces.dao.PublisherDao;
import ar.edu.itba.paw.model.Publisher;

@Repository
public class PublisherJdbcDao implements PublisherDao {
	private	final SimpleJdbcInsert jdbcInsert;
	private JdbcTemplate jdbcTemplate;
	protected static final RowMapper<Publisher> PUBLISHER_MAPPER = new RowMapper<Publisher>()
	{
		@Override
		public Publisher mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new Publisher(rs.getInt("publisher"), rs.getString("publisher_name"), rs.getString("publisher_logo"));
		}
	};
	
	@Autowired
	public PublisherJdbcDao(final DataSource ds)
	{
	    jdbcTemplate = new JdbcTemplate(ds);
	    jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("publishers").usingGeneratedKeyColumns("publisher");
	}
	
	@Override
	public Optional<Publisher> findById(long id)
	{
		Optional<Publisher> p = jdbcTemplate.query("SELECT * FROM publishers WHERE publisher = ?", PUBLISHER_MAPPER, id).stream().findFirst();
		return p;
	}

	@Override
	public Optional<Publisher> findByName(String name)
	{
		Optional<Publisher> p = jdbcTemplate.query("SELECT * FROM publishers WHERE publisher_name LIKE ?", PUBLISHER_MAPPER, name).stream().findFirst();
		return p;
	}

	@Override
	public Optional<Publisher> changeName(long id, String new_name)
	{
		jdbcTemplate.update("UPDATE publishers SET publisher_name = ? WHERE publisher = ?", new_name, id);
		return findById(id);
	}

	@Override
	public Optional<Publisher> changeLogo(long id, String new_logo)
	{
		jdbcTemplate.update("UPDATE publishers SET publisher_logo = ? WHERE publisher = ?", new_logo, id);
		return findById(id);
	}

	@Override
	public Publisher register(String name, String logo)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("name", name); 
		args.put("logo", logo);
		final Number publisherId = jdbcInsert.executeAndReturnKey(args);
		return new Publisher(publisherId.longValue(), name, logo);
	}

	@Override
	public List<Publisher> getAllPublishers()
	{
		return jdbcTemplate.query("SELECT publisher, publisher_name, publisher_logo FROM (SELECT publisher, count(*) AS g FROM publishing GROUP BY publisher) AS a NATURAL JOIN publishers ORDER BY g DESC", PUBLISHER_MAPPER);
	}
	
	@Override
	public List<Publisher> getBiggestPublishers(int amount)
	{
		return jdbcTemplate.query("SELECT publisher, publisher_name, publisher_logo FROM (SELECT publisher, count(*) AS g FROM publishing GROUP BY publisher) AS a NATURAL JOIN publishers ORDER BY g DESC LIMIT ?", PUBLISHER_MAPPER, amount);
	}

	@Override
	public List<Publisher> getPublishers(int page, int pageSize)
	{
		return jdbcTemplate.query("SELECT * FROM publishers ORDER BY publisher_name LIMIT ? OFFSET ?", PUBLISHER_MAPPER, pageSize, (page-1)*pageSize);
	}

	@Override
	public int countPublishers()
	{
		return jdbcTemplate.queryForObject("SELECT count(*) FROM publishers", Integer.class);
	}
}