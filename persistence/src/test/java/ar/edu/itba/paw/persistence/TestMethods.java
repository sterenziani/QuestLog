package ar.edu.itba.paw.persistence;
import java.time.LocalDate;
import java.util.List;

import ar.edu.itba.paw.model.entity.*;

import javax.persistence.EntityManager;
public class TestMethods
{
	public static Game addGame(String title, String cover, String desc, String trailer, EntityManager em)
	{
		Game g = new Game(title, cover, desc, trailer);
		em.persist(g);
		em.flush();
		return g;
	}
	
	public static Developer addDeveloper(String name, String logo, EntityManager em)
	{
		Developer d = new Developer(name, logo);
		em.persist(d);
		em.flush();
		return d;
	}
	
	public static Publisher addPublisher(String name, String logo, EntityManager em)
	{
		Publisher p = new Publisher(name, logo);
		em.persist(p);
		em.flush();
		return p;
	}
	
	public static Genre addGenre(String name, String logo, EntityManager em)
	{
		Genre g = new Genre(name, logo);
		em.persist(g);
		em.flush();
		return g;
	}
	
	public static Platform addPlatform(String name, String shortName, String logo, EntityManager em)
	{
		Platform p = new Platform(name, shortName, logo);
		em.persist(p);
		em.flush();
		return p;
	}
	
	public static Region addRegion(String name, String shortName, EntityManager em)
	{
		Region r = new Region(name, shortName);
		em.persist(r);
		em.flush();
		return r;
	}
	
	public static void addRelease(Game g, Region r, LocalDate d, EntityManager em)
	{
		Release realease = new Release(g, r, d);
		g.addReleaseDate(realease);
		em.persist(realease);
		em.flush();
	}
	
	public static User addUser(String username, String password, String email, String locale, EntityManager em)
	{
		User u = new User(username, password, email, locale);
		em.persist(u);
		em.flush();
		return u;
	}
	
	public static Score addScore(User user, Game game, int score, EntityManager em) {
		Score s = new Score(user, game, score);
		em.persist(s);
		em.flush();
		return s;
	}
	
	public static Review addReview(User user, Game game, Platform platform, int score, List<String> body, LocalDate date, EntityManager em) {
		Review r = new Review(user, game, platform, score, body, date);
		em.persist(r);
		em.flush();
		return r;
	}
	
	public static Run addRun(User user, Game game, Platform platform, Playstyle playstyle, long time, EntityManager em) {
		Run r = new Run(user, game, platform, playstyle, time);
		em.persist(r);
		em.flush();
		return r;
	}
	
	public static Playstyle addPlaystyle(String name, EntityManager em)
	{
		Playstyle p = new Playstyle(name);
		em.persist(p);
		em.flush();
		return p;
	}
	
	public static Image addImage(String name, byte[] data, EntityManager em) {
		Image i = new Image(name, data);
		em.persist(i);
		em.flush();
		return i;
	}
	
	public static int addRole(String roleName, EntityManager em)
	{
		Role r = new Role(roleName);
		em.persist(r);
		em.flush();
		return r.getRole().intValue();
	}
	
	public static void connectDev(Game g, Developer d, EntityManager em)
	{
		g.addDeveloper(d);
		d.addGame(g);
		em.flush();
	}
	
	public static void connectPub(Game g, Publisher p, EntityManager em)
	{
		g.addPublisher(p);
		p.addGame(g);
		em.flush();
	}
	
	public static void connectGenre(Game g, Genre genre, EntityManager em)
	{
		g.addGenre(genre);
		genre.addGame(g);
		em.flush();
	}
	
	public static void connectPlatform(Game g, Platform p, EntityManager em)
	{
		g.addPlatform(p);
		p.addGame(g);
		em.flush();
	}

	public static void addBacklog(Game g, User u, EntityManager em)
	{
		u.addToBacklog(g);
		em.flush();
	}
	
	public static void connectRoles(User u, int roleId, EntityManager em)
	{
		u.addRole(new Role(roleId));
		em.flush();
	}
	
	public static PasswordResetToken addToken(User u, String token, LocalDate date, EntityManager em)
	{
		PasswordResetToken prt = new PasswordResetToken(token, u, date);
		em.persist(prt);
		em.flush();
		return prt;
	}
}
