package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.dao.GenreDao;
import ar.edu.itba.paw.interfaces.service.GenreService;
import ar.edu.itba.paw.model.Genre;

@Service
public class GenreServiceImpl implements GenreService {
	
	@Autowired
	private GenreDao genreDao;
	
	@Override
	public Optional<Genre> findById(long id)
	{
		Optional<Genre> genre = genreDao.findById(id);
		return genre;
	}

	@Override
	public Optional<Genre> findByName(String name)
	{
		Optional<Genre> genre = genreDao.findByName(name);
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
}
