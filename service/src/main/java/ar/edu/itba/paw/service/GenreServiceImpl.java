package ar.edu.itba.paw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.GenreDao;
import ar.edu.itba.paw.interfaces.GenreService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.User;

@Service
public class GenreServiceImpl implements GenreService {
	
	@Autowired
	private GenreDao genreDao;
	
	@Autowired
	private GameService gs;
	
	@Override
	public Optional<Genre> findById(long id)
	{
		return genreDao.findById(id);
	}
	
	@Override
	public Optional<Genre> findById(long id, User u)
	{
		Optional<Genre> genre = genreDao.findById(id);
		if(genre.isPresent())
		{
			gs.updateBacklogDetails(genre.get().getGames(), u);
		}
		return genre;
	}

	@Override
	public Optional<Genre> findByName(String name)
	{
		return genreDao.findByName(name);
	}
	
	@Override
	public Optional<Genre> findByName(String name, User u)
	{
		Optional<Genre> genre = genreDao.findByName(name);
		if(genre.isPresent())
		{
			gs.updateBacklogDetails(genre.get().getGames(), u);
		}
		return genre;
	}

	@Override
	public Optional<Genre> changeName(long id, String new_name)
	{
		return genreDao.changeName(id, new_name);
	}
	
	@Override
	public Optional<Genre> changeLogo(long id, String new_logo) {
		return genreDao.changeLogo(id, new_logo);
	}


	@Override
	public Genre register(String title, String logo)
	{
		return genreDao.register(title, logo);
	}

	@Override
	public List<Genre> getAllGenres()
	{
		return genreDao.getAllGenres();
	}
	
	@Override
	public List<Genre> getAllGenresWithGames()
	{
		return genreDao.getAllGenresWithGames();
	}

	@Override
	public List<Game> getAllGames(Genre g)
	{
		return genreDao.getAllGames(g);
	}

}
