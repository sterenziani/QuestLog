package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.dao.ScoreDao;
import ar.edu.itba.paw.interfaces.service.ScoreService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Score;
import ar.edu.itba.paw.model.entity.User;

@Service
public class ScoreServiceImpl implements ScoreService{
	
	@Autowired
	private ScoreDao scoreDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScoreServiceImpl.class);

	@Transactional
	@Override
	public Optional<Score> findScore(User user, Game game) {
		return scoreDao.findScore(user, game);
	}

	@Transactional
	@Override
	public Optional<Score> changeScore(int new_score, User user, Game game) {
		return scoreDao.changeScore(new_score, user, game);
	}

	@Transactional
	@Override
	public Integer findAverageScore(Game game) {
		return scoreDao.findAverageScore(game);
	}

	@Transactional
	@Override
	public List<Score> getAllScores() {
		return scoreDao.getAllScores();
	}

	@Transactional
	@Override
	public Score register(User user, Game game, int score)
	{
		LOGGER.debug("Registering that {} rated {} with a {}", user.getUsername(), score, game.getTitle());
		return scoreDao.register(user, game, score);
	}

	@Transactional
	@Override
	public List<Score> findAllUserScores(User user)
	{
		return scoreDao.findAllUserScores(user);
	}

	@Transactional
	@Override
	public List<Score> findAllUserScores(User user, int page, int pageSize)
	{
		return scoreDao.findAllUserScores(user, page, pageSize);
	}

	@Transactional
	@Override
	public int countAllUserScores(User user)
	{
		return scoreDao.countAllUserScores(user);
	}

	@Transactional
	@Override
	public List<Score> findAllGameScores(Game game) {
		return scoreDao.findAllGameScores(game);
	}

	@Transactional
	@Override
	public List<Score> findAllGameScores(Game game, int page, int pageSize) {
		return scoreDao.findAllGameScores(game, page, pageSize);
	}

	@Transactional
	@Override
	public int countAllGameScores(Game game) {
		return scoreDao.countAllGameScores(game);
	}
}
