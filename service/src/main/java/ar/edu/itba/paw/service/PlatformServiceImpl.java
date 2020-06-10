package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.dao.PlatformDao;
import ar.edu.itba.paw.interfaces.service.PlatformService;
import ar.edu.itba.paw.model.Platform;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlatformServiceImpl implements PlatformService
{
	@Autowired
	private PlatformDao platformDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlatformServiceImpl.class);

	@Transactional
	@Override
	public Optional<Platform> findById(long id)
	{
		Optional<Platform> p = platformDao.findById(id);
		return p;
	}

	@Transactional
	@Override
	public Optional<Platform> findByName(String name)
	{
		Optional<Platform> p = platformDao.findByName(name);
		return p;
	}

	@Transactional
	@Override
	public Optional<Platform> changeName(long id, String new_name)
	{
		LOGGER.debug("Changing name of Platform of ID {} to {}", id, new_name);
		return platformDao.changeName(id, new_name);
	}

	@Transactional
	@Override
	public Optional<Platform> changeLogo(long id, String new_logo)
	{
		LOGGER.debug("Changing logo of Platform of ID {} to {}", id, new_logo);
		return platformDao.changeLogo(id, new_logo);
	}

	@Transactional
	@Override
	public Optional<Platform> changeShortName(long id, String new_shortName)
	{
		LOGGER.debug("Changing short name of Platform of ID {} to {}", id, new_shortName);
		return platformDao.changeShortName(id, new_shortName);
	}

	@Transactional
	@Override
	public Platform register(String title, String shortName, String logo)
	{
		LOGGER.debug("Registering platform {} ({}) with logo {}", title, shortName, logo);
		return platformDao.register(title, shortName, logo);
	}

	@Transactional
	@Override
	public List<Platform> getAllPlatforms()
	{
		return platformDao.getAllPlatforms();
	}

	@Transactional
	@Override
	public List<Platform> getPlatforms(int page, int pageSize)
	{
		return platformDao.getPlatforms(page, pageSize);
	}

	@Transactional
	@Override
	public int countPlatforms()
	{
		return platformDao.countPlatforms();
	}

	@Transactional
	@Override
	public List<Platform> getBiggestPlatforms(int amount)
	{
		return platformDao.getBiggestPlatforms(amount);
	}

	@Transactional
	@Override
	public List<Platform> getAllPlatformsWithGames()
	{
		return platformDao.getAllPlatformsWithGames();
	}

	@Transactional
	@Override
	public List<Platform> getPlatformsWithGames(int page, int pageSize)
	{
		return platformDao.getPlatformsWithGames(page, pageSize);
	}

	@Transactional
	@Override
	public int countPlatformsWithGames()
	{
		return platformDao.countPlatformsWithGames();
	}
}
