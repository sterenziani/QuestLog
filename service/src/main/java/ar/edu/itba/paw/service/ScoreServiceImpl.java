package ar.edu.itba.paw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.itba.paw.interfaces.dao.ScoreDao;
import ar.edu.itba.paw.interfaces.service.ScoreService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Score;
import ar.edu.itba.paw.model.User;

@Service
public class ScoreServiceImpl implements ScoreService{
	
	@Autowired
	private ScoreDao scoreDao;

	@Override
	public Optional<Score> findScore(User user, Game game) {
		return scoreDao.findScore(user, game);
	}

	@Override
	public Optional<Score> changeScore(int new_score, User user, Game game) {
		return scoreDao.changeScore(new_score, user, game);
	}

	@Override
	public Integer findAverageScore(Game game) {
		return scoreDao.findAverageScore(game);
	}

	@Override
	public List<Score> getAllScores() {
		return scoreDao.getAllScores();
	}

	@Override
	public Score register(User user, Game game, int score) {
		return scoreDao.register(user, game, score);
	}
	


}
