package ar.edu.itba.paw.persistence;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.paw.model.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
<<<<<<< HEAD
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.PasswordResetToken;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Playstyle;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Region;
import ar.edu.itba.paw.model.Review;
import ar.edu.itba.paw.model.Run;
import ar.edu.itba.paw.model.Score;
import ar.edu.itba.paw.model.User;
=======
>>>>>>> 733c9de97f6da66b5794e35dda06813df4fe82f8

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
	
	public static Release addRelease(Game g, Region r, Date d, SimpleJdbcInsert releaseInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("game", g.getId());
		args.put("region", r.getId());
		args.put("release_date", d);
		releaseInsert.execute(args);
		return new Release(r, d);
	}

	public static Release makeRelease(Game g, Region r, Date d)
	{
		return new Release(r, d);
	}
	
	public static User addUser(String username, String password, String email, String locale, SimpleJdbcInsert userInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("username", username);
		args.put("password", password);
		args.put("email", email);
		args.put("locale", locale);

		return new User(userInsert.executeAndReturnKey(args).longValue(), username, password, email, locale);
	}
	
	public static Score addScore(User user, Game game, int score, SimpleJdbcInsert scoreInsert) {
		final Map<String, Object> args = new HashMap<>();
		args.put("user_id", user.getId());
		args.put("game", game.getId());
		args.put("score", score);
		scoreInsert.execute(args);
		return new Score(user, game, score);
	}
	
	public static Review addReview(User user, Game game, Platform platform, int score, String body, Date date, SimpleJdbcInsert reviewInsert) {
		final Map<String, Object> args = new HashMap<>();
		args.put("user_id", user.getId());
		args.put("game", game.getId());
		args.put("platform", platform.getId());
		args.put("score", score);
		args.put("body", body);
		args.put("post_date", date);
		return new Review(reviewInsert.executeAndReturnKey(args).longValue(), user, game, platform, score, body, date);	
	}
	
	public static Run addRun(User user, Game game, Platform platform, Playstyle playstyle, long time, SimpleJdbcInsert runInsert) {
		final Map<String, Object> args = new HashMap<>();
		args.put("user_id", user.getId());
		args.put("game", game.getId());
		args.put("platform", platform.getId());
		args.put("playstyle", playstyle.getId());
		args.put("time", time);
		return new Run(runInsert.executeAndReturnKey(args).longValue(), user, game, platform, playstyle, time);	
	}
	
	public static Playstyle addPlaystyle(String name, SimpleJdbcInsert playstyleInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("playstyle_name", name);
		return new Playstyle (playstyleInsert.executeAndReturnKey(args).longValue(), name);
	}
	
	public static Image addImage(String name, byte[] data, SimpleJdbcInsert imageInsert) {
        final Map<String, Object> args = new HashMap<>();
        args.put("image_name", name);
        args.put("image_data", data);
        return new Image(imageInsert.executeAndReturnKey(args).longValue(), name, data);
		
	}
	
	public static int addRole(String roleName, SimpleJdbcInsert roleInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("role_name", roleName);
		return roleInsert.executeAndReturnKey(args).intValue();
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

	public static void connectRoles(User u, int roleId, SimpleJdbcInsert versionInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("user_id", u.getId());
		args.put("role", roleId);
		versionInsert.execute(args);
	}
	
	public static PasswordResetToken addToken(User u, String token, Date date, SimpleJdbcInsert versionInsert)
	{
		final Map<String, Object> args = new HashMap<>();
		args.put("user_id", u.getId());
		args.put("token", token);
		args.put("expiration", date);
		versionInsert.execute(args);
		return new PasswordResetToken(token,u,date);
	}

}
