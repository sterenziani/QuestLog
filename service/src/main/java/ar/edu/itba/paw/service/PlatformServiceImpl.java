package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Optional;
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
		return platformDao.changeName(id, new_name);
	}

	@Transactional
	@Override
	public Optional<Platform> changeLogo(long id, String new_logo)
	{
		return platformDao.changeLogo(id, new_logo);
	}

	@Transactional
	@Override
	public Optional<Platform> changeShortName(long id, String new_shortName)
	{
		return platformDao.changeShortName(id, new_shortName);
	}

	@Transactional
	@Override
	public Platform register(String title, String shortName, String logo)
	{
		return platformDao.register(title, shortName, logo);
	}

	@Transactional
	@Override
	public List<Platform> getAllPlatforms()
	{
		return platformDao.getAllPlatforms();
	}

	@Override
	public List<Platform> getPlatforms(int page, int pageSize)
	{
		return platformDao.getPlatforms(page, pageSize);
	}

	@Override
	public int countPlatforms()
	{
		return platformDao.countPlatforms();
	}

	@Override
	public List<Platform> getBiggestPlatforms(int amount)
	{
		return platformDao.getBiggestPlatforms(amount);
	}

	@Override
	public List<Platform> getAllPlatformsWithGames()
	{
		return platformDao.getAllPlatformsWithGames();
	}

	@Override
	public List<Platform> getPlatformsWithGames(int page, int pageSize)
	{
		return platformDao.getPlatformsWithGames(page, pageSize);
	}

	@Override
	public int countPlatformsWithGames()
	{
		return platformDao.countPlatformsWithGames();
	}
}
