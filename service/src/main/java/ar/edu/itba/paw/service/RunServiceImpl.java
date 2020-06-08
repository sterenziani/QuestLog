package ar.edu.itba.paw.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.dao.RunDao;
import ar.edu.itba.paw.interfaces.service.RunService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Playstyle;
import ar.edu.itba.paw.model.Run;
import ar.edu.itba.paw.model.User;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RunServiceImpl implements RunService {
	
	@Autowired
	private RunDao runDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RunServiceImpl.class);

	@Transactional
	@Override
	public Optional<Run> findRunById(long run) {
		return runDao.findRunById(run);
	}

	@Transactional
	@Override
	public List<Run> findGameRuns(Game game, User user) {
		return runDao.findGameRuns(game, user);
	}

	@Transactional
	@Override
	public List<Run> findAllUserRuns(User user) {
		return runDao.findAllUserRuns(user);
	}

	@Transactional
	@Override
	public List<Run> findAllGameRuns(Game game) {
		return runDao.findAllGameRuns(game);
	}

	@Transactional
	@Override
	public List<Run> findPlaystyleAndGameRuns(Game game, Playstyle playstyle) {
		return runDao.findPlaystyleAndGameRuns(game, playstyle);
	}

	@Transactional
	@Override
	public List<Run> findSpecificRuns(Game game, Playstyle playstyle, Platform platform) {
		return runDao.findSpecificRuns(game, playstyle, platform);
	}

	@Transactional
	@Override
	public long getAveragePlaytime(Game game) {
		return runDao.getAveragePlaytime(game);
	}

	@Transactional
	@Override
	public long getAveragePlatformPlaytime(Game game, Platform platform) {
		return runDao.getAveragePlatformPlaytime(game, platform);
	}

	@Transactional
	@Override
	public long getAveragePlaystylePlaytime(Game game, Playstyle playstyle) {
		return runDao.getAveragePlaystylePlaytime(game, playstyle);
	}

	@Transactional
	@Override
	public long getAverageSpecificPlaytime(Game game, Playstyle playstyle, Platform platform) {
		return runDao.getAverageSpecificPlaytime(game, playstyle, platform);
	}

	@Transactional
	@Override
	public List<Run> getAllRuns() {
		return runDao.getAllRuns();
	}

	@Transactional
	@Override
	public Run register(User user, Game game, Platform platform, Playstyle playstyle, long time)
	{
		LOGGER.debug("Registering run of {} by {} for {} in style {}", user.getUsername(), game.getTitle(), platform.getShortName(), playstyle.getName());
		return runDao.register(user, game, platform, playstyle, time);
	}

	@Transactional
	@Override
	public List<Playstyle> getAllPlaystyles() {
		return runDao.getAllPlaystyles();
	}

	@Transactional
	@Override
	public Optional<Playstyle> changePlaystyleName(String new_name, long playstyle)
	{
		LOGGER.debug("Changing name of playstyle of ID {} into {}", playstyle, new_name);
		return runDao.changePlaystyleName(new_name, playstyle);
	}

	@Transactional
	@Override
	public Optional<Playstyle> findPlaystyleById(long playstyle) {
		return runDao.findPlaystyleById(playstyle);
	}

	@Transactional
	@Override
	public Playstyle register(String name)
	{
		LOGGER.debug("Registering playstyle {}", name);
		return runDao.register(name);
	}

	@Transactional
	@Override
	public Optional<Playstyle> findPlaystyleByName(String name) {
		return runDao.findPlaystyleByName(name);
	}

	@Transactional
	@Override
	public HashMap<Playstyle,String> getAverageAllPlayStyles(Game g){
		HashMap<Playstyle,String> map = new HashMap<Playstyle,String>();
		for(Playstyle p: runDao.getAllPlaystyles()) {
			Long l = runDao.getAveragePlaystylePlaytime(g, p);
			int hours = (int) (l/3600);
			int mins = (int) (l/60 - hours*60);
			int secs = (int) (l - hours*3600 - mins*60);
			String timestamp = Integer.toString(hours) + " : ";
			if(mins < 10)
				timestamp += "0";
			timestamp += Integer.toString(mins) + " : ";
			if(secs < 10)
				timestamp += "0";
			timestamp += Integer.toString(secs);
			map.put(p, timestamp);
		}
		return map;
	}

	@Override
	public List<Run> findRunsByUser(User user, int page, int pageSize)
	{
		return runDao.findRunsByUser(user, page, pageSize);
	}

	@Override
	public int countRunsByUser(User user)
	{
		return runDao.countRunsByUser(user);
	}
	
	@Override
	public List<Run> getTopRuns(Game game, int amount)
	{
		return runDao.getTopRuns(game, amount);
	}
}
