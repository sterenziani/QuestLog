package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.model.Game;

@Service
public class GameServiceImpl implements GameService
{
	@Autowired
	private GameDao gameDao;
	
	@Override
	public Optional<Game> findById(long id)
	{
		return gameDao.findById(id);
	}

	@Override
	public List<Game> findByTitle(String title)
	{
		return gameDao.findByTitle(title);
	}

	@Override
	public Optional<Game> changeTitle(long id, String new_title)
	{
		return gameDao.changeTitle(id, new_title);
	}

	@Override
	public Optional<Game> changeCover(long id, String new_cover)
	{
		return gameDao.changeCover(id, new_cover);
	}

	@Override
	public Optional<Game> changeDescription(long id, String new_desc)
	{
		return gameDao.changeDescription(id, new_desc);
	}

	@Override
	public Game register(String title, String cover, String description)
	{
		return gameDao.register(title, cover, description);
	}

	@Override
	public List<Game> getAllGames()
	{
		return gameDao.getAllGames();
	}
}
