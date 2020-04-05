package ar.edu.itba.paw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.GenreDao;
import ar.edu.itba.paw.interfaces.GenreService;
import ar.edu.itba.paw.model.Genre;

@Service
public class GenreServiceImpl implements GenreService {
	
	@Autowired
	private GenreDao genreDao;
	
	@Override
	public Optional<Genre> findById(long id)
	{
		return genreDao.findById(id);
	}

	@Override
	public Optional<Genre> findByName(String name)
	{
		return genreDao.findByName(name);
	}

	@Override
	public Optional<Genre> changeName(long id, String new_name)
	{
		return genreDao.changeName(id, new_name);
	}

	@Override
	public Genre register(String title)
	{
		return genreDao.register(title);
	}

	@Override
	public List<Genre> getAllGenres()
	{
		return genreDao.getAllGenres();
	}

}
