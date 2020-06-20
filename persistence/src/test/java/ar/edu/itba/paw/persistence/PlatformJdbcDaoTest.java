package ar.edu.itba.paw.persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Platform;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class PlatformJdbcDaoTest
{
	private	static final String GAME_TABLE = "games";
	private static final String GAME_TITLE = "Example Game";
	private static final String GAME_COVER = "http://sega.com/game.jpg";
	private static final String GAME_DESC = "Explore the world!";
	private static final String GAME_TRAILER			= "DpHDJRGuL7w";
	private	static final String PLATFORM_TABLE = "platforms";
	private	static final String PLATFORM_NAME = "PlayStation 4";
	private	static final String PLATFORM_SHORT_NAME = "PS4";
	private	static final String PLATFORM_LOGO = "https://i.ytimg.com/vi/56NxUWEFpL0/maxresdefault.jpg";
	private static final String VERSION_TABLE = "game_versions";
	
    @PersistenceContext
    private EntityManager em;
	
	@Autowired
	private PlatformJpaDao platformDao;
		
	@Before
	public void	setUp()
	{

	}

	@Test
	public void	testRegisterPlatform()
	{
		Platform p = platformDao.register("XBOX", "XBOX", PLATFORM_LOGO);
		Assert.assertNotNull(p);
        Assert.assertEquals("XBOX", p.getName());
        Assert.assertEquals("XBOX", p.getShortName());
        Assert.assertEquals(PLATFORM_LOGO, p.getLogo());
	}

	@Test
	public void	testFindPlatformByIdDoesntExist()
	{
		Optional<Platform> maybePlatform = platformDao.findById(1);
		Assert.assertFalse(maybePlatform.isPresent());
	}

	@Test
	public void	testFindPlatformByIdExists()
	{
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Optional<Platform> maybePlatform = platformDao.findById(p.getId());
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}

	@Test
	public void	testFindPlatformByNameDoesntExist()
	{
		Optional<Platform> maybePlatform = platformDao.findByName("XBOX");
		Assert.assertFalse(maybePlatform.isPresent());
	}
	
	@Test
	public void	testFindPlatformByNameExists()
	{
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Optional<Platform> maybePlatform = platformDao.findByName(PLATFORM_NAME);
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testChangePlatformName()
	{
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Optional<Platform> maybePlatform = platformDao.changeName(p.getId(), "La Plei Cuatro");
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals("La Plei Cuatro", maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testChangePlatformShortName()
	{
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Optional<Platform> maybePlatform = platformDao.changeShortName(p.getId(), "Play4");
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals("Play4", maybePlatform.get().getShortName());
		Assert.assertEquals(PLATFORM_LOGO, maybePlatform.get().getLogo());
	}
	
	@Test
	public void	testChangeLogo()
	{
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Optional<Platform> maybePlatform = platformDao.changeLogo(p.getId(), "http://ps4.com/logo.png");
		Assert.assertTrue(maybePlatform.isPresent());
		Assert.assertEquals(PLATFORM_NAME, maybePlatform.get().getName());
		Assert.assertEquals(PLATFORM_SHORT_NAME, maybePlatform.get().getShortName());
		Assert.assertEquals("http://ps4.com/logo.png", maybePlatform.get().getLogo());
	}

	@Test
	public void	testGetAllPlatforms()
	{
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Platform ns = platformDao.register("Nintendo Switch", "NS", "http://nintendo.com/switch.png");
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ns);
		myList.add(p);
		
		List<Platform> platformList = platformDao.getAllPlatforms();
		Assert.assertFalse(platformList.isEmpty());
		Assert.assertEquals(2, platformList.size());
		Assert.assertEquals(platformList.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList.get(1).getName(), myList.get(1).getName());
	}
	
	@Test
	public void testGetBiggestPlatforms()
	{
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Platform ns = platformDao.register("Nintendo Switch", "NS", "http://nintendo.com/switch.png");
		Platform snes = platformDao.register("Super Nintendo", "SNES", "http://nintendo.com/snes.png");
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ns);
		myList.add(p);
		myList.add(snes);
		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		Game g2 = TestMethods.addGame("game2", GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		TestMethods.connectPlatform(g2, p, em);
		TestMethods.connectPlatform(g1, ns, em);
		TestMethods.connectPlatform(g2, ns, em);
		
		
		List<Platform> platformList1 = platformDao.getBiggestPlatforms(1);
		List<Platform> platformList2 = platformDao.getBiggestPlatforms(3);
		
		Assert.assertFalse(platformList1.isEmpty());
		Assert.assertFalse(platformList2.isEmpty());
		
		Assert.assertEquals(1, platformList1.size());
		Assert.assertEquals(2, platformList2.size());
		
		Assert.assertEquals(platformList1.get(0).getName(), myList.get(0).getName());
		
		Assert.assertEquals(platformList2.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList2.get(1).getName(), myList.get(1).getName());
				
	}
	
	@Test
	public void testCountPlatforms()
	{
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		platformDao.register("Nintendo Switch", "NS", "http://nintendo.com/switch.png");
		platformDao.register("Super Nintendo", "SNES", "http://nintendo.com/snes.png");
		int count = platformDao.countPlatforms();
		Assert.assertEquals(3, count);
	}
	
	@Test
	public void testGetPlatforms() 
	{
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Platform ns = platformDao.register("Nintendo Switch", "NS", "http://nintendo.com/switch.png");
		Platform snes = platformDao.register("Super Nintendo", "SNES", "http://nintendo.com/snes.png");
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ns);
		myList.add(p);
		myList.add(snes);
		List<Platform> platformList1 = platformDao.getPlatforms(1,2);
		List<Platform> platformList2 = platformDao.getPlatforms(2,2);
		List<Platform> platformList3 = platformDao.getPlatforms(1,5);

		Assert.assertFalse(platformList1.isEmpty());
		Assert.assertFalse(platformList2.isEmpty());
		Assert.assertFalse(platformList3.isEmpty());

		Assert.assertEquals(2, platformList1.size());
		Assert.assertEquals(1, platformList2.size());
		Assert.assertEquals(3, platformList3.size());


		Assert.assertEquals(platformList1.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList1.get(1).getName(), myList.get(1).getName());
		
		Assert.assertEquals(platformList2.get(0).getName(), myList.get(2).getName());
		
		Assert.assertEquals(platformList3.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList3.get(1).getName(), myList.get(1).getName());
		Assert.assertEquals(platformList3.get(2).getName(), myList.get(2).getName());
	}
	
	@Test
	public void testGetAllPlatformsWithGames()
	{
		Platform ns = TestMethods.addPlatform("Nintendo Switch", "NS", "http://nintendo.com/switch.png", em);
		
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ns);

		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		
		TestMethods.connectPlatform(g1, ns, em);
		
		List<Platform> platformList = platformDao.getAllPlatformsWithGames();
		
		Assert.assertFalse(platformList.isEmpty());
		Assert.assertEquals(1, platformList.size());
		Assert.assertEquals(platformList.get(0).getName(), myList.get(0).getName());
	}
	
	@Test
	public void testGetPlatformsWithGames()
	{
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Platform ns = platformDao.register("Nintendo Switch", "NS", "http://nintendo.com/switch.png");
		Platform snes = platformDao.register("Super Nintendo", "SNES", "http://nintendo.com/snes.png");
		Platform xbox = platformDao.register("XBOX", "XBOX", "http://microsoft.com/xbox.png");

		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ns);
		myList.add(p);
		myList.add(snes);
		myList.add(xbox);

		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		Game g2 = TestMethods.addGame("game2", GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		
		TestMethods.connectPlatform(g1, ns, em);
		TestMethods.connectPlatform(g2, p, em);
		TestMethods.connectPlatform(g1, snes, em);

		List<Platform> platformList1 = platformDao.getPlatformsWithGames(1,2);
		List<Platform> platformList2 = platformDao.getPlatformsWithGames(2,2);
		List<Platform> platformList3 = platformDao.getPlatformsWithGames(1,10);

		Assert.assertFalse(platformList1.isEmpty());
		Assert.assertFalse(platformList2.isEmpty());
		Assert.assertFalse(platformList3.isEmpty());

		Assert.assertEquals(2, platformList1.size());
		Assert.assertEquals(1, platformList2.size());
		Assert.assertEquals(3, platformList3.size());


		Assert.assertEquals(platformList1.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList1.get(1).getName(), myList.get(1).getName());
		
		Assert.assertEquals(platformList2.get(0).getName(), myList.get(2).getName());
		
		Assert.assertEquals(platformList3.get(0).getName(), myList.get(0).getName());
		Assert.assertEquals(platformList3.get(1).getName(), myList.get(1).getName());
		Assert.assertEquals(platformList3.get(2).getName(), myList.get(2).getName());
	}
	
	@Test
	public void testCountPlatformsWithGames()
	{
		Platform p = TestMethods.addPlatform(PLATFORM_NAME, PLATFORM_SHORT_NAME, PLATFORM_LOGO, em);
		Platform ns = platformDao.register("Nintendo Switch", "NS", "http://nintendo.com/switch.png");
		Platform snes = platformDao.register("Super Nintendo", "SNES", "http://nintendo.com/snes.png");
	
		List<Platform> myList = new ArrayList<Platform>();
		myList.add(ns);
		myList.add(p);
		myList.add(snes);

		Game g1 = TestMethods.addGame(GAME_TITLE, GAME_COVER, GAME_DESC, GAME_TRAILER, em);
		TestMethods.connectPlatform(g1, ns, em);
		int count1 = platformDao.countPlatformsWithGames();
		TestMethods.connectPlatform(g1, p, em);
		int count2 = platformDao.countPlatformsWithGames();
		Assert.assertEquals(1, count1);
		Assert.assertEquals(2, count2);
	}

}