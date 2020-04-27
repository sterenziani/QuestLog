package ar.edu.itba.paw.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.RunDao;
import ar.edu.itba.paw.interfaces.RunService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Playstyle;
import ar.edu.itba.paw.model.Run;
import ar.edu.itba.paw.model.User;

@Service
public class RunServiceImpl implements RunService {
	
	@Autowired
	private RunDao runDao;

	@Override
	public Optional<Run> findRunById(long run) {
		return runDao.findRunById(run);
	}

	@Override
	public List<Run> findGameRuns(Game game, User user) {
		return runDao.findGameRuns(game, user);
	}

	@Override
	public List<Run> findAllUserRuns(User user) {
		return runDao.findAllUserRuns(user);
	}

	@Override
	public List<Run> findAllGameRuns(Game game) {
		return runDao.findAllGameRuns(game);
	}

	@Override
	public List<Run> findPlatformAndGameRuns(Game game, Platform platform) {
		return runDao.findPlatformAndGameRuns(game, platform);
	}

	@Override
	public List<Run> findPlaystyleAndGameRuns(Game game, Playstyle playstyle) {
		return runDao.findPlaystyleAndGameRuns(game, playstyle);
	}

	@Override
	public List<Run> findSpecificRuns(Game game, Playstyle playstyle, Platform platform) {
		return runDao.findSpecificRuns(game, playstyle, platform);
	}

	@Override
	public long getAveragePlaytime(Game game) {
		return runDao.getAveragePlaytime(game);
	}

	@Override
	public long getAveragePlatformPlaytime(Game game, Platform platform) {
		return runDao.getAveragePlatformPlaytime(game, platform);
	}

	@Override
	public long getAveragePlaystylePlaytime(Game game, Playstyle playstyle) {
		return runDao.getAveragePlaystylePlaytime(game, playstyle);
	}

	@Override
	public long getAverageSpecificPlaytime(Game game, Playstyle playstyle, Platform platform) {
		return runDao.getAverageSpecificPlaytime(game, playstyle, platform);
	}

	@Override
	public List<Run> getAllRuns() {
		return runDao.getAllRuns();
	}

	@Override
	public Run register(User user, Game game, Platform platform, Playstyle playstyle, long time) {
		return runDao.register(user, game, platform, playstyle, time);
	}

	@Override
	public List<Playstyle> getAllPlaystyles() {
		return runDao.getAllPlaystyles();
	}

	@Override
	public Optional<Playstyle> changePlaystyleName(String new_name, long playstyle) {
		return runDao.changePlaystyleName(new_name, playstyle);
	}

	@Override
	public Optional<Playstyle> findPlaystyleById(long playstyle) {
		return runDao.findPlaystyleById(playstyle);
	}

	@Override
	public Playstyle register(String name) {
		return runDao.register(name);
	}

	@Override
	public Optional<Playstyle> findPlaystyleByName(String name) {
		return runDao.findPlaystyleByName(name);
	}
	
	@Override
	public HashMap<Playstyle,String> getAverageAllPlayStyles(Game g){
		HashMap<Playstyle,String> map = new HashMap<Playstyle,String>();
		for(Playstyle p: runDao.getAllPlaystyles()) {
			Long l = runDao.getAveragePlaystylePlaytime(g, p);
			int hours = (int) (l/3600);
			int mins = (int) (l/60 - hours*60);
			int secs = (int) (l - hours*3600 - mins*60);
			map.put(p, hours+":"+mins+":"+secs);
		}
		return map;
	}

}
