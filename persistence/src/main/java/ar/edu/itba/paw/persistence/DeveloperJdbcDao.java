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

import ar.edu.itba.paw.interfaces.dao.DeveloperDao;
import ar.edu.itba.paw.model.Developer;

//@Repository
public class DeveloperJdbcDao implements DeveloperDao {

	private	final SimpleJdbcInsert jdbcInsert;
	private JdbcTemplate jdbcTemplate;
	protected static final RowMapper<Developer> DEVELOPER_MAPPER = new RowMapper<Developer>()
	{
		@Override
		public Developer mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new Developer(rs.getInt("developer"), rs.getString("developer_name"), rs.getString("developer_logo"));
		}
	};
	
	@Autowired
	public DeveloperJdbcDao(final DataSource ds)
	{
	    jdbcTemplate = new JdbcTemplate(ds);
	    jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("developers").usingGeneratedKeyColumns("developer");
	}
	
	@Override
	public Optional<Developer> findById(long id)
	{
		Optional<Developer> d = jdbcTemplate.query("SELECT * FROM developers WHERE developer = ?", DEVELOPER_MAPPER, id).stream().findFirst();
		return d;
	}

	@Override
	public Optional<Developer> findByName(String name)
	{
		Optional<Developer> d =  jdbcTemplate.query("SELECT * FROM developers WHERE developer_name LIKE ?", DEVELOPER_MAPPER, name).stream().findFirst();
		return d;
	}

	@Override
	public Optional<Developer> changeName(long id, String new_name)
	{
		jdbcTemplate.update("UPDATE developers SET developer_name = ? WHERE developer = ?", new_name, id);
		return findById(id);
	}

	@Override
	public Optional<Developer> changeLogo(long id, String new_logo)
	{
		jdbcTemplate.update("UPDATE developers SET developer_logo = ? WHERE developer = ?", new_logo, id);
		return findById(id);
	}

	@Override
	public Developer register(String name, String logo)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("name", name); 
		args.put("logo", logo);
		final Number developerId = jdbcInsert.executeAndReturnKey(args);
		return new Developer(developerId.longValue(), name, logo);
	}

	@Override
	public List<Developer> getAllDevelopers()
	{
		return jdbcTemplate.query("SELECT * FROM developers ORDER BY developer_name DESC", DEVELOPER_MAPPER);
	}
	
	@Override
	public List<Developer> getBiggestDevelopers(int amount)
	{
		return jdbcTemplate.query("SELECT developer, developer_name, developer_logo FROM (SELECT developer, count(*) AS g FROM development GROUP BY developer) AS a NATURAL JOIN developers ORDER BY g DESC LIMIT ?", DEVELOPER_MAPPER, amount);
	}

	@Override
	public List<Developer> getDevelopers(int page, int pageSize)
	{
		return jdbcTemplate.query("SELECT * FROM developers ORDER BY developer_name LIMIT ? OFFSET ?", DEVELOPER_MAPPER, pageSize, (page-1)*pageSize);
	}

	@Override
	public int countDevelopers()
	{
		return jdbcTemplate.queryForObject("SELECT count(*) FROM developers", Integer.class);
	}
}
