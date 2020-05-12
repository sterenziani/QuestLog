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

import ar.edu.itba.paw.interfaces.dao.RunDao;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Playstyle;
import ar.edu.itba.paw.model.Run;
import ar.edu.itba.paw.model.User;

@Repository
public class RunJdbcDao implements RunDao {

	private	final SimpleJdbcInsert jdbcRunInsert;
	private JdbcTemplate jdbcRunTemplate;
	
	
	private	final SimpleJdbcInsert jdbcPlaystyleInsert;
	private JdbcTemplate jdbcPlaystyleTemplate;
	
	@Autowired
	private UserJdbcDao userDao;
	
	@Autowired
	private GameJdbcDao gameDao;
	
	@Autowired
	private PlatformJdbcDao platformDao;

	protected static final RowMapper<Run> RUN_MAPPER = new RowMapper<Run>()
	{
		@Override
		public Run mapRow(ResultSet rs, int rowNum) throws SQLException
		{	Game game = new Game(rs.getLong("game"), null, null, null);
			User user = new User(rs.getLong("user_id"), null, null, null, "en");
			Platform platform = new Platform(rs.getLong("platform"), null, null, null);
			Playstyle playstyle = new Playstyle(rs.getLong("playstyle"), null);
			return new Run (rs.getInt("run"), user, game, platform, playstyle, rs.getInt("time"));
		}
	};
	
	
	protected static final RowMapper<Playstyle> PLAY_MAPPER = new RowMapper<Playstyle>() {
		@Override
		public Playstyle mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new Playstyle(rs.getLong("playstyle"), rs.getString("playstyle_name"));
		}
	};
	
	
	@Autowired
	public RunJdbcDao(final DataSource ds)
	{
	    jdbcRunTemplate = new JdbcTemplate(ds);
	    jdbcRunInsert = new SimpleJdbcInsert(jdbcRunTemplate).withTableName("runs").usingGeneratedKeyColumns("run");
		jdbcPlaystyleTemplate = new JdbcTemplate(ds);
	    jdbcPlaystyleInsert = new SimpleJdbcInsert(jdbcRunTemplate).withTableName("playstyles").usingGeneratedKeyColumns("playstyle");
	}
	
	@Override
	public Optional<Run> findRunById(long run) {
		Optional<Run> r = jdbcRunTemplate.query("SELECT * FROM runs WHERE run = ?", RUN_MAPPER, run).stream().findFirst();
		if(r.isPresent()) {
			Optional <Game> game = gameDao.findById(r.get().getGame().getId());
			Optional <User> user = userDao.findById(r.get().getUser().getId());
			Optional <Platform> platform = platformDao.findById(r.get().getPlatform().getId());
			Optional <Playstyle> ps = findPlaystyleById(r.get().getPlaystyle().getId());
			if(game.isPresent() && user.isPresent() && platform.isPresent() && ps.isPresent()) {
				r.get().setGame(game.get());
				r.get().setPlatform(platform.get());
				r.get().setPlaystyle(ps.get());
				r.get().setUser(user.get());
			}
		}
		return r;
	}

	@Override
	public List<Run> findGameRuns(Game game, User user) {
		List<Run> runs = jdbcRunTemplate.query("SELECT * FROM runs WHERE user_id = ? AND game = ?", RUN_MAPPER, user.getId(), game.getId());
		for(Run r : runs) {
			Optional <Platform> platform = platformDao.findById(r.getPlatform().getId());
			Optional <Playstyle> ps = findPlaystyleById(r.getPlaystyle().getId());
			if(platform.isPresent() && ps.isPresent()) {
				r.setGame(game);
				r.setPlatform(platform.get());
				r.setPlaystyle(ps.get());
				r.setUser(user);
			}
		}
		return runs;
	}

	@Override
	public List<Run> findAllUserRuns(User user) {
		List<Run> runs = jdbcRunTemplate.query("SELECT * FROM runs WHERE user_id = ?", RUN_MAPPER, user.getId());
		for(Run r : runs){
			Optional <Game> game = gameDao.findById(r.getGame().getId());
			Optional <Platform> platform = platformDao.findById(r.getPlatform().getId());
			Optional <Playstyle> ps = findPlaystyleById(r.getPlaystyle().getId());
			if(game.isPresent() && platform.isPresent() && ps.isPresent()) {
				r.setGame(game.get());
				r.setPlatform(platform.get());
				r.setPlaystyle(ps.get());
				r.setUser(user);
			}
		}
		return runs;
	}

	@Override
	public List<Run> findAllGameRuns(Game game) {
		List<Run> runs = jdbcRunTemplate.query("SELECT * FROM runs WHERE game = ?", RUN_MAPPER, game.getId());
		for(Run r : runs){
			Optional <User> user = userDao.findById(r.getUser().getId());
			Optional <Platform> platform = platformDao.findById(r.getPlatform().getId());
			Optional <Playstyle> ps = findPlaystyleById(r.getPlaystyle().getId());
			if(user.isPresent() && platform.isPresent() && ps.isPresent()) {
				r.setGame(game);
				r.setPlatform(platform.get());
				r.setPlaystyle(ps.get());
				r.setUser(user.get());
			}
		}
		return runs;
	}

	@Override
	public List<Run> getAllRuns() {
		List<Run> runs = jdbcRunTemplate.query("SELECT * FROM runs", RUN_MAPPER);
		for (Run r : runs){
			Optional <Game> game = gameDao.findById(r.getGame().getId());
			Optional <User> user = userDao.findById(r.getUser().getId());
			Optional <Platform> platform = platformDao.findById(r.getPlatform().getId());
			Optional <Playstyle> ps = findPlaystyleById(r.getPlaystyle().getId());
			if(game.isPresent() && user.isPresent() && platform.isPresent() && ps.isPresent()) {
				r.setGame(game.get());
				r.setPlatform(platform.get());
				r.setPlaystyle(ps.get());
				r.setUser(user.get());
			}
		}
		return runs;
	}
	
	@Override
	public List<Run> findPlatformAndGameRuns(Game game, Platform platform) {
		List<Run> runs = jdbcRunTemplate.query("SELECT * FROM runs WHERE platform = ? AND game = ?", RUN_MAPPER, platform.getId(), game.getId());
		for (Run r : runs){
			Optional <User> user = userDao.findById(r.getUser().getId());
			Optional <Playstyle> ps = findPlaystyleById(r.getPlaystyle().getId());
			if(user.isPresent() && ps.isPresent()) {
				r.setGame(game);
				r.setPlatform(platform);
				r.setPlaystyle(ps.get());
				r.setUser(user.get());
			}
		}
		return runs;
	}


	@Override
	public List<Run> findPlaystyleAndGameRuns(Game game, Playstyle playstyle) {
		List<Run> runs = jdbcRunTemplate.query("SELECT * FROM runs WHERE playstyle = ? AND game = ?", RUN_MAPPER, playstyle.getId(), game.getId());
		for (Run r : runs){
			Optional <User> user = userDao.findById(r.getUser().getId());
			Optional <Platform> platform = platformDao.findById(r.getPlatform().getId());
			if(user.isPresent() && platform.isPresent()) {
				r.setGame(game);
				r.setPlatform(platform.get());
				r.setPlaystyle(playstyle);
				r.setUser(user.get());
			}
		}
		return runs;
	}


	@Override
	public List<Run> findSpecificRuns(Game game, Playstyle playstyle, Platform platform) {
		List<Run> runs = jdbcRunTemplate.query("SELECT * FROM runs WHERE playstyle = ? AND game = ? AND platform = ?", RUN_MAPPER, playstyle.getId(), game.getId(), platform.getId());
		for (Run r : runs){
			Optional <User> user = userDao.findById(r.getUser().getId());
			if(user.isPresent()) {
				r.setGame(game);
				r.setPlatform(platform);
				r.setPlaystyle(playstyle);
				r.setUser(user.get());
			}
		}
		return runs;
	}


	@Override
	public long getAveragePlaytime(Game game) {
		Long average = jdbcRunTemplate.queryForObject("SELECT AVG(time) FROM runs WHERE game = ?",Long.class, game.getId());
		if(average == null) {
			return 0;
		}
		return average;
	}



	@Override
	public long getAveragePlatformPlaytime(Game game, Platform platform) {
		Long average = jdbcRunTemplate.queryForObject("SELECT AVG(time) FROM runs WHERE game = ? AND platform = ?",Long.class, game.getId(), platform.getId());
		if(average == null) {
			return 0;
		}
		return average;
	}


	@Override
	public long getAveragePlaystylePlaytime(Game game, Playstyle playstyle) {
		Long average = jdbcRunTemplate.queryForObject("SELECT AVG(time) FROM runs WHERE game = ? AND playstyle = ?",Long.class, game.getId(), playstyle.getId());
		if(average == null) {
			return 0;
		}
		return average;
	}

	@Override
	public long getAverageSpecificPlaytime(Game game, Playstyle playstyle, Platform platform) {
		Long average = jdbcRunTemplate.queryForObject("SELECT AVG(time) FROM runs WHERE game = ? AND platform = ? AND playstyle = ?",Long.class, game.getId(), platform.getId(), playstyle.getId());
		if(average == null) {
			return 0;
		}
		return average;
	}

	@Override
	public Run register(User user, Game game, Platform platform, Playstyle playstyle, long time) {
		final Map<String, Object> args = new HashMap<>();
		args.put("user_id", user.getId()); 
		args.put("game", game.getId()); 
		args.put("platform", platform.getId());
		args.put("playstyle", playstyle.getId());
		args.put("time", time);
		final Number runId = jdbcRunInsert.executeAndReturnKey(args);
		return new Run(runId.longValue(), user, game, platform, playstyle, time);
	}
	
	/**PLAYSTYLE METHODS
	 */

	@Override
	public List<Playstyle> getAllPlaystyles() {
		List<Playstyle> playstyles = jdbcPlaystyleTemplate.query("SELECT * FROM playstyles",PLAY_MAPPER);
		return playstyles;
	}


	@Override
	public Optional<Playstyle> changePlaystyleName(String new_name, long playstyle) {
		jdbcPlaystyleTemplate.update("UPDATE playstyles SET playstyle_name = ? WHERE playstyle = ?", new_name, playstyle);
		return findPlaystyleById(playstyle);
	}


	@Override
	public Optional<Playstyle> findPlaystyleById(long playstyle) {
		return jdbcPlaystyleTemplate.query("SELECT * FROM playstyles WHERE playstyle = ?", PLAY_MAPPER, playstyle).stream().findFirst();
	}
	
	@Override
	public Optional<Playstyle> findPlaystyleByName(String name) {
		return jdbcPlaystyleTemplate.query("SELECT * FROM playstyles WHERE playstyle_name = ?", PLAY_MAPPER, name).stream().findFirst();
	}
	
	@Override
	public Playstyle register(String name) {
		final Map<String, Object> args = new HashMap<>();
		args.put("playstyle_name", name); 
		return new Playstyle(jdbcPlaystyleInsert.executeAndReturnKey(args).longValue(), name);
	}

	@Override
	public List<Run> findRunsByUser(User user, int page, int pageSize)
	{
		List<Run> runs = jdbcRunTemplate.query("SELECT * FROM runs WHERE user_id = ? LIMIT ? OFFSET ?", RUN_MAPPER, user.getId(), pageSize, (page-1)*pageSize);
		for (Run r : runs)
		{
			Optional <Game> game = gameDao.findById(r.getGame().getId());
			Optional <Platform> platform = platformDao.findById(r.getPlatform().getId());
			Optional <Playstyle> ps = findPlaystyleById(r.getPlaystyle().getId());
			if(game.isPresent() && platform.isPresent() && ps.isPresent())
			{
				r.setGame(game.get());
				r.setPlatform(platform.get());
				r.setPlaystyle(ps.get());
				r.setUser(user);
			}
		}
		return runs;
	}

	@Override
	public int countRunsByUser(User user)
	{
		return jdbcRunTemplate.queryForObject("SELECT count(*) FROM runs WHERE user_id = ?", Integer.class, user.getId());
	}
}
