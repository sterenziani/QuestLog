package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.dao.GenreDao;
import ar.edu.itba.paw.interfaces.service.GenreService;
import ar.edu.itba.paw.model.Genre;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GenreServiceImpl implements GenreService {
	
	@Autowired
	private GenreDao genreDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GenreServiceImpl.class);

	@Transactional
	@Override
	public Optional<Genre> findById(long id)
	{
		Optional<Genre> genre = genreDao.findById(id);
		return genre;
	}

	@Transactional
	@Override
	public Optional<Genre> findByName(String name)
	{
		Optional<Genre> genre = genreDao.findByName(name);
		return genre;
	}

	@Transactional
	@Override
	public Optional<Genre> changeName(long id, String new_name)
	{
		LOGGER.debug("Changing name of Genre of ID {} to {}", id, new_name);
		return genreDao.changeName(id, new_name);
	}

	@Transactional
	@Override
	public Optional<Genre> changeLogo(long id, String new_logo)
	{
		LOGGER.debug("Changing logo of Genre of ID {} into {}", id, new_logo);
		return genreDao.changeLogo(id, new_logo);
	}

	@Transactional
	@Override
	public Genre register(String name, String logo)
	{
		LOGGER.debug("Registering Genre {} with logo {}", name, logo);
		return genreDao.register(name, logo);
	}

	@Transactional
	@Override
	public List<Genre> getAllGenres()
	{
		return genreDao.getAllGenres();
	}

	@Transactional
	@Override
	public List<Genre> getGenres(int page, int pageSize)
	{
		return genreDao.getGenres(page, pageSize);
	}

	@Transactional
	@Override
	public int countGenres()
	{
		return genreDao.countGenres();
	}
}
