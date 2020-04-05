package ar.edu.itba.paw.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.PlatformDao;
import ar.edu.itba.paw.interfaces.PlatformService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;

@Service
public class PlatformServiceImpl implements PlatformService
{
	@Autowired
	private PlatformDao platformDao;
	
	@Override
	public Optional<Platform> findById(long id)
	{
		return platformDao.findById(id);
	}

	@Override
	public Optional<Platform> findByName(String name)
	{
		return platformDao.findByName(name);
	}

	@Override
	public Optional<Platform> changeName(long id, String new_name)
	{
		return platformDao.changeName(id, new_name);
	}

	@Override
	public Optional<Platform> changeLogo(long id, String new_logo)
	{
		return platformDao.changeLogo(id, new_logo);
	}

	@Override
	public Optional<Platform> changeShortName(long id, String new_shortName)
	{
		return platformDao.changeShortName(id, new_shortName);
	}

	@Override
	public Platform register(String title, String shortName, String logo)
	{
		return platformDao.register(title, shortName, logo);
	}

	@Override
	public List<Platform> getAllPlatforms()
	{
		return platformDao.getAllPlatforms();
	}

	@Override
	public List<Game> getAllGames(Platform p)
	{
		return platformDao.getAllGames(p);
	}
}
