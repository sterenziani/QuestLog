package ar.edu.itba.paw.persistence;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Region;

public class TestMethods
{
	public static Game addGame(String title, String cover, String desc, SimpleJdbcInsert gameInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("title", title);
		args.put("cover", cover);
		args.put("description", desc);
		return new Game(gameInsert.executeAndReturnKey(args).longValue(), title, cover, desc);
	}
	
	public static Developer addDeveloper(String name, String logo, SimpleJdbcInsert devInsert)
	{
		final Map<String, Object> devArgs = new HashMap<>();
		devArgs.put("developer_name", name);
		devArgs.put("developer_logo", logo);
		return new Developer(devInsert.executeAndReturnKey(devArgs).longValue(), name, logo);
	}
	
	public static Publisher addPublisher(String name, String logo, SimpleJdbcInsert pubInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("publisher_name", name);
		args.put("publisher_logo", logo);
		return new Publisher(pubInsert.executeAndReturnKey(args).longValue(), name, logo);
	}
	
	public static Genre addGenre(String name, String logo, SimpleJdbcInsert genreInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("genre_name", name);
		args.put("genre_logo", logo);
		return new Genre(genreInsert.executeAndReturnKey(args).longValue(), name, logo);
	}
	
	public static Platform addPlatform(String name, String shortName, String logo, SimpleJdbcInsert platformInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("platform_name", name);
		args.put("platform_name_short", shortName);
		args.put("platform_logo", logo);
		return new Platform(platformInsert.executeAndReturnKey(args).longValue(), name, shortName, logo);
	}
	
	public static Region addRegion(String name, String shortName, SimpleJdbcInsert regionInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("region_name", name);
		args.put("region_short", shortName);
		return new Region(regionInsert.executeAndReturnKey(args).longValue(), name, shortName);
	}
	
	public static void addRelease(Game g, Region r, Date d, SimpleJdbcInsert releaseInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("game", g.getId());
		args.put("region", r.getId());
		args.put("release_date", d);
		releaseInsert.execute(args);
	}
	
	public static void connectDev(Game g, Developer d, SimpleJdbcInsert developmentInsert)
	{
		final Map<String, Object> developmentArgs = new HashMap<>();
		developmentArgs.put("game", g.getId());
		developmentArgs.put("developer", d.getId());
		developmentInsert.execute(developmentArgs);
	}
	
	public static void connectPub(Game g, Publisher p, SimpleJdbcInsert publishingInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("game", g.getId());
		args.put("publisher", p.getId());
		publishingInsert.execute(args);
	}
	
	public static void connectGenre(Game g, Genre genre, SimpleJdbcInsert classificationInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("game", g.getId());
		args.put("genre", genre.getId());
		classificationInsert.execute(args);
	}
	
	public static void connectPlatform(Game g, Platform p, SimpleJdbcInsert versionInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("game", g.getId());
		args.put("platform", p.getId());
		versionInsert.execute(args);
	}
}
