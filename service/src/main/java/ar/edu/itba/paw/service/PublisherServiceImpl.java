package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.dao.PublisherDao;
import ar.edu.itba.paw.interfaces.service.PublisherService;
import ar.edu.itba.paw.model.Publisher;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublisherServiceImpl implements PublisherService
{
	@Autowired
	private PublisherDao publisherDao;

	@Transactional
	@Override
	public Optional<Publisher> findById(long id)
	{
		Optional<Publisher> p = publisherDao.findById(id);
		return p;
	}

	@Transactional
	@Override
	public Optional<Publisher> findByName(String name)
	{
		Optional<Publisher> p = publisherDao.findByName(name);
		return p;
	}

	@Transactional
	@Override
	public Optional<Publisher> changeName(long id, String new_name)
	{
		return publisherDao.changeName(id, new_name);
	}

	@Transactional
	@Override
	public Optional<Publisher> changeLogo(long id, String new_logo)
	{
		return publisherDao.changeLogo(id, new_logo);
	}

	@Transactional
	@Override
	public Publisher register(String title, String logo)
	{
		return publisherDao.register(title, logo);
	}

	@Transactional
	@Override
	public List<Publisher> getAllPublishers()
	{
		return publisherDao.getAllPublishers();
	}
}
