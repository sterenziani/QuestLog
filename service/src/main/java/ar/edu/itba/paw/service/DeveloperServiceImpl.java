package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.dao.DeveloperDao;
import ar.edu.itba.paw.interfaces.service.DeveloperService;
import ar.edu.itba.paw.model.entity.Developer;

import org.springframework.transaction.annotation.Transactional;

@Service
public class DeveloperServiceImpl implements DeveloperService
{
	
	@Autowired
	private DeveloperDao developerDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeveloperServiceImpl.class);

	@Transactional
	@Override
	public Optional<Developer> findById(long id)
	{
		Optional<Developer> d = developerDao.findById(id);
		return d;
	}

	@Transactional
	@Override
	public Optional<Developer> findByName(String name)
	{
		Optional<Developer> d = developerDao.findByName(name);
		return d;
	}

	@Transactional
	@Override
	public Optional<Developer> changeName(long id, String new_name)
	{
		LOGGER.debug("Changing name of Developer of ID {} to {}", id, new_name);
		return developerDao.changeName(id, new_name);
	}

	@Transactional
	@Override
	public Optional<Developer> changeLogo(long id, String new_logo)
	{
		LOGGER.debug("Changing logo of Developer of ID {} into {}", id, new_logo);
		return developerDao.changeLogo(id, new_logo);
	}

	@Transactional
	@Override
	public Developer register(String name, String logo)
	{
		LOGGER.debug("Registering Developer {} with logo {}", name, logo);
		return developerDao.register(name, logo);
	}

	@Transactional
	@Override
	public List<Developer> getAllDevelopers()
	{
		return developerDao.getAllDevelopers();
	}

	@Transactional
	@Override
	public List<Developer> getDevelopers(int page, int pageSize)
	{
		return developerDao.getDevelopers(page, pageSize);
	}

	@Transactional
	@Override
	public int countDevelopers()
	{
		return developerDao.countDevelopers();
	}

	@Transactional
	@Override
	public List<Developer> getBiggestDevelopers(int amount)
	{
		return developerDao.getBiggestDevelopers(amount);
	}
}