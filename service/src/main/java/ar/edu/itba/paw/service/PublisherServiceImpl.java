package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.dao.PublisherDao;
import ar.edu.itba.paw.interfaces.service.PublisherService;
import ar.edu.itba.paw.model.entity.Publisher;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublisherServiceImpl implements PublisherService
{
	@Autowired
	private PublisherDao publisherDao;

	private static final Logger LOGGER = LoggerFactory.getLogger(PublisherServiceImpl.class);
	
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
		LOGGER.debug("Changing name of Publisher of ID {} to {}", id, new_name);
		return publisherDao.changeName(id, new_name);
	}

	@Transactional
	@Override
	public Optional<Publisher> changeLogo(long id, String new_logo)
	{
		LOGGER.debug("Changing logo of Publisher of ID {} to {}", id, new_logo);
		return publisherDao.changeLogo(id, new_logo);
	}

	@Transactional
	@Override
	public Publisher register(String name, String logo)
	{
		LOGGER.debug("Registering publisher {} with logo {}", name, logo);
		return publisherDao.register(name, logo);
	}

	@Transactional
	@Override
	public List<Publisher> getAllPublishers()
	{
		return publisherDao.getAllPublishers();
	}

	@Transactional
	@Override
	public List<Publisher> getPublishers(int page, int pageSize)
	{
		return publisherDao.getPublishers(page, pageSize);
	}

	@Transactional
	@Override
	public List<Publisher> getBiggestPublishers(int amount)
	{
		return publisherDao.getBiggestPublishers(amount);
	}

	@Transactional
	@Override
	public int countPublishers()
	{
		return publisherDao.countPublishers();
	}
	
	@Transactional
	@Override
	public List<Publisher> searchByName(String searchTerm, int page, int pageSize)
	{
		return publisherDao.searchByName(searchTerm, page, pageSize);
	}
	
	@Override
	public int countByName(String searchTerm, int page, int pageSize)
	{
		return publisherDao.countByName(searchTerm, page, pageSize);
	}
}
