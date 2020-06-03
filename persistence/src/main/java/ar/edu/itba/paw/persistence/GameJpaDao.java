package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.GameDao;
import ar.edu.itba.paw.model.*;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.swing.text.html.Option;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GameJpaDao implements GameDao {

    private static final Long MIN_AMOUNT_FOR_OVERLAP     = 3L;
    private static final Long MIN_AMOUNT_FOR_POPULAR     = 3L;
    private static final int  MAX_RESULT_FOR_SHOWCASE    = 10;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Game> findById(long id) {
        return Optional.ofNullable(em.find(Game.class, id));
    }

    @Override
    public Optional<Game> findByIdWithDetails(long id) {
        Optional<Game> maybeGame = Optional.ofNullable(em.find(Game.class, id));
        if (maybeGame.isPresent()){
            Game g = maybeGame.get();
            g.getGenres();
            g.getDevelopers();
            g.getPublishers();
            g.getPlatforms();
            g.getReleaseDates();
        }
        return maybeGame;
    }

    @Override
    public Optional<Game> findByTitle(String title) {
        final TypedQuery<Game> query = em.createQuery("from Game as g where g.title = :title", Game.class);
        query.setParameter("title", title);
        final List<Game> list = query.getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    @Override
    public Optional<Game> findByTitleWithDetails(String title) {
        final TypedQuery<Game> query = em.createQuery("from Game as g where g.title = :title", Game.class);
        query.setParameter("title", title);
        final List<Game> list = query.getResultList();
        if(list.isEmpty())
            return Optional.empty();
        Game g = list.get(0);
        if(g != null) {
            g.getGenres();
            g.getDevelopers();
            g.getPublishers();
            g.getPlatforms();
            g.getReleaseDates();
        }
        return Optional.ofNullable(g);
    }

    @Override
    public Optional<Game> changeTitle(long id, String new_title) {
        Game g = em.find(Game.class, id);
        if(g == null)
            return Optional.empty();
        g.setTitle(new_title);
        return Optional.of(g);
    }

    @Override
    public Optional<Game> changeCover(long id, String new_cover) {
        Game g = em.find(Game.class, id);
        if(g == null)
            return Optional.empty();
        g.setCover(new_cover);
        return Optional.of(g);
    }

    @Override
    public Optional<Game> changeDescription(long id, String new_desc) {
        Game g = em.find(Game.class, id);
        if(g == null)
            return Optional.empty();
        g.setDescription(new_desc);
        return Optional.of(g);
    }

    @Override
    public Game register(String title, String cover, String description) {
        Game g = new Game(title, cover, description);
        em.persist(g);
        return g;
    }

    @Override
    public List<Game> getAllGames() {
        final TypedQuery<Game> query = em.createQuery("from Game", Game.class);
        return query.getResultList();
    }

    @Override
    public List<Game> getAllGamesWithDetails() {
        final TypedQuery<Game> query = em.createQuery("from Game", Game.class);
        final List<Game> list = query.getResultList();
        for(Game g : list){
            g.getGenres();
            g.getDevelopers();
            g.getPublishers();
            g.getPlatforms();
            g.getReleaseDates();
        }
        return list;
    }

    @Override
    public Optional<Game> addPlatform(Game g, Platform p) {
        if(g == null || p == null)
            return Optional.empty();
        g.addPlatform(p);
        return Optional.of(g);
    }

    @Override
    public void addPlatforms(long g, List<Long> platforms_ids) {
        if(platforms_ids == null)
            return;
        Optional<Game> maybeGame = findById(g);
        if(!maybeGame.isPresent())
            return;
        Game game = maybeGame.get();
        for(Long p_id : platforms_ids){
            Platform p = em.find(Platform.class, p_id);
            if(p != null)
                game.addPlatform(p);
        }
    }

    @Override
    public Optional<Game> removePlatform(Game g, Platform p) {
        if(g == null || p == null)
            return Optional.empty();
        g.removePlatform(p);
        return Optional.of(g);
    }

    @Override
    public void removeAllPlatforms(Game g) {
        if(g == null)
            return;
        g.removePlatforms();
    }

    @Override
    public Optional<Game> addDeveloper(Game g, Developer d) {
        if(g == null || d == null)
            return Optional.empty();
        g.addDeveloper(d);
        return Optional.of(g);
    }

    @Override
    public void addDevelopers(long g, List<Long> devs_ids) {
        if(devs_ids == null)
            return;
        Optional<Game> maybeGame = findById(g);
        if(!maybeGame.isPresent())
            return;
        Game game = maybeGame.get();
        for(Long d_id : devs_ids){
            Developer d = em.find(Developer.class, d_id);
            if(d != null)
                game.addDeveloper(d);
        }
    }

    @Override
    public Optional<Game> removeDeveloper(Game g, Developer d) {
        if(g == null || d == null)
            return Optional.empty();
        g.addDeveloper(d);
        return Optional.of(g);
    }

    @Override
    public void removeAllDevelopers(Game g) {
        if(g == null)
            return;
        g.removeDevelopers();
    }

    @Override
    public Optional<Game> addPublisher(Game g, Publisher pub) {
        if(g == null || pub == null)
            return Optional.empty();
        g.addPublisher(pub);
        return Optional.of(g);
    }

    @Override
    public Optional<Game> removePublisher(Game g, Publisher pub) {
        if(g == null || pub == null)
            return Optional.empty();
        g.removePublisher(pub);
        return Optional.of(g);
    }

    @Override
    public void addPublishers(long g, List<Long> publisher_ids) {
        if(publisher_ids == null)
            return;
        Optional<Game> maybeGame = findById(g);
        if(!maybeGame.isPresent())
            return;
        Game game = maybeGame.get();
        for(Long p_id : publisher_ids){
            Publisher p = em.find(Publisher.class, p_id);
            if(p != null)
                game.addPublisher(p);
        }
    }

    @Override
    public void removeAllPublishers(Game g) {
        if(g == null)
            return;
        g.removePublishers();
    }

    @Override
    public Optional<Game> addGenre(Game game, Genre genre) {
        if(game == null || genre == null)
            return Optional.empty();
        game.addGenre(genre);
        return Optional.of(game);
    }

    @Override
    public void addGenres(long g, List<Long> genres_ids) {
        if(genres_ids == null)
            return;
        Optional<Game> maybeGame = findById(g);
        if(!maybeGame.isPresent())
            return;
        Game game = maybeGame.get();
        for(Long gen_id : genres_ids){
            Genre gen = em.find(Genre.class, gen_id);
            if(gen != null)
                game.addGenre(gen);
        }
    }

    @Override
    public Optional<Game> removeGenre(Game game, Genre genre) {
        if(game == null || genre == null)
            return Optional.empty();
        game.addGenre(genre);
        return Optional.of(game);
    }

    @Override
    public void removeAllGenres(Game g) {
        if(g == null)
            return;
        g.removeGenres();
    }

    @Override
    public Optional<Game> addReleaseDate(Game game, Release r) {
        return Optional.empty();
    }

    @Override
    public void addReleaseDates(long g, Map<Long, LocalDate> releaseDates) {
        if(releaseDates == null)
            return;
        Optional<Game> maybeGame = findById(g);
        if(!maybeGame.isPresent())
            return;
        Game game = maybeGame.get();
        for(Map.Entry<Long, LocalDate> date : releaseDates.entrySet()){
            Region r = em.find(Region.class, date.getKey());
            if(r != null)
                game.addReleaseDate(new Release(game, r, Date.valueOf(date.getValue())));
        }
    }

    @Override
    public Optional<Game> removeReleaseDate(Game game, Release r) {
        if(game == null || r == null)
            return Optional.empty();
        game.addReleaseDate(r);
        return Optional.of(game);
    }

    @Override
    public void removeAllReleaseDates(Game g) {
        if(g == null)
            return;
        g.removeReleaseDates();
    }

    @Override
    public List<Game> searchByTitle(String search, int page, int pageSize) {
        return null;
    }

    @Override
    public List<Game> getUpcomingGames() {
        final TypedQuery<Release> query = em.createQuery("from Release as r where r.date >= CURRENT_DATE", Release.class);
        final List<Release> list = query.getResultList();
        if(list.isEmpty())
            return Collections.emptyList();
        List<Game> games = new ArrayList<>();
        for(Release r : list){
            games.add(r.getGame());
        }
        return games;
    }

    @Override
    public boolean isInBacklog(long gameId, User u) {
        if(u == null)
            return false;
        Game g = em.find(Game.class, gameId);
        if(g == null)
            return false;
        return u.isInBacklog(g);
    }

    @Override
    public void addToBacklog(long gameId, User u) {
        if(u == null)
            return;
        Game g = em.find(Game.class, gameId);
        if(g == null)
            return;
        u.addToBacklog(g);
    }

    @Override
    public void removeFromBacklog(long gameId, User u) {
        if(u == null)
            return;
        Game g = em.find(Game.class, gameId);
        if(g == null)
            return;
        u.removeFromBacklog(g);
    }

    @Override
    public List<Game> getGamesInBacklog(User u) {
        if(u == null)
            return Collections.emptyList();
        return new ArrayList<>(u.getBacklog());
    }

    @Override
    public List<Game> getGamesInBacklog(User u, int page, int pageSize) {
        if(u == null || pageSize == 0 || page == 0) {
            return Collections.emptyList();
        }
        Query nativeQuery = em.createNativeQuery("select distinct cast(b.game as text) from backlogs b where b.user_id = :u_id");
        nativeQuery.setParameter("u_id", u.getId());
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> ids = (List<Long>) nativeQuery.getResultList().stream().map((id) -> Long.parseLong(id.toString())).collect(Collectors.toList());

        TypedQuery<Game> query = em.createQuery("from Game g where g.game in :filteredIds", Game.class);
        query.setParameter("filteredIds", ids);
        return query.getResultList();
    }

    @Override
    public int countGamesInBacklog(User u) {
        if(u == null)
            return 0;
        return u.getBacklog().size();
    }

    @Override
    public List<Game> getSimilarToBacklog(User u) {
        TypedQuery<Game> query = em.createQuery("select g from User u join u.backlog g group by g " +
                "having count(u) >= :min_amount_for_popular order by count(u)", Game.class);
        query.setParameter("min_amount_for_popular", MIN_AMOUNT_FOR_POPULAR);
        query.setMaxResults(MAX_RESULT_FOR_SHOWCASE);
        return query.getResultList();
    }

    @Override
    public List<Game> getMostBacklogged() {
        TypedQuery<Game> query = em.createQuery("select g from User u join u.backlog g group by g " +
                "having count(u) >= :min_amount_for_popular order by count(u)", Game.class);
        query.setParameter("min_amount_for_popular", MIN_AMOUNT_FOR_POPULAR);
        query.setMaxResults(MAX_RESULT_FOR_SHOWCASE);
        return query.getResultList();
    }

    @Override
    public List<Game> getFilteredGames(String searchTerm, List<String> genres, List<String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight, int page, int pageSize) {
        String genreFilter = "";
        if(genres.size()>0)
            genreFilter =  " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT * FROM genres WHERE genre IN (" + String.join(", ", genres) + ")) AS gnrs NATURAL JOIN classifications) AS a";

        String platformFilter = "";
        if(platforms.size()>0)
            platformFilter = " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT * FROM platforms WHERE platform IN (" + String.join(", ", platforms) + ")) AS plats NATURAL JOIN game_versions) AS b";

        String scoreFilter = "";
        if(scoreLeft != 0 || scoreRight != 100)
            scoreFilter = " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT game FROM scores GROUP BY game HAVING AVG(score) >= " + scoreLeft + " AND AVG(score) <= " + scoreRight + ") AS sc) AS c";
        String timeFilter = "";
        if(timeLeft != 0 || timeRight != 35999999)
            timeFilter = " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT game FROM runs WHERE playstyle = 1 GROUP BY game HAVING AVG(time) >= " + timeLeft + " AND AVG(time) <= " + timeRight + ") AS a) AS d";

        Query nativeQuery = em.createNativeQuery("SELECT distinct cast(z.game as text) FROM (SELECT * FROM games WHERE LOWER(title) LIKE LOWER(CONCAT('%',:searchTerm,'%'))) as z" + genreFilter + platformFilter + scoreFilter + timeFilter
                + " ORDER BY game");
        nativeQuery.setParameter("searchTerm", searchTerm);
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> ids = (List<Long>) nativeQuery.getResultList().stream().map((id) -> Long.parseLong(id.toString())).collect(Collectors.toList());

        TypedQuery<Game> query = em.createQuery("from Game g where g.game in :filteredIds", Game.class);
        query.setParameter("filteredIds", ids);
        return query.getResultList();
    }

    @Override
    public List<Game> getGamesReleasingTomorrow() {
        final TypedQuery<Release> query = em.createQuery("from Release as r where r.date = CURRENT_DATE + 1", Release.class);
        final List<Release> list = query.getResultList();
        if(list.isEmpty())
            return Collections.emptyList();
        List<Game> games = new ArrayList<>();
        for(Release r : list){
            games.add(r.getGame());
        }
        return games;
    }

    @Override
    public List<Game> getGamesInBacklogReleasingTomorrow(User u) {
        final TypedQuery<Release> query = em.createQuery("select u.backlog from User as u", Release.class);
        final List<Release> list = query.getResultList();
        if(list.isEmpty())
            return Collections.emptyList();
        List<Game> games = new ArrayList<>();
        for(Release r : list){
            games.add(r.getGame());
        }
        return games;
    }

    @Override
    public int countSearchResults(String searchTerm) {
        final TypedQuery<Game> query = em.createQuery("from Game as g where title ilike concat('%',:searchTerm, '%')", Game.class);
        query.setParameter("searchTerm", searchTerm);
        return query.getResultList().size();
    }

    @Override
    public int countSearchResultsFiltered(String searchTerm, List<String> genres, List<String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight) {
        String genreFilter = "";
        if(genres.size()>0)
            genreFilter =  " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT * FROM genres WHERE genre IN (" + String.join(", ", genres) + ")) AS gnrs NATURAL JOIN classifications) AS a";

        String platformFilter = "";
        if(platforms.size()>0)
            platformFilter = " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT * FROM platforms WHERE platform IN (" + String.join(", ", platforms) + ")) AS plats NATURAL JOIN game_versions) AS b";

        String scoreFilter = "";
        if(scoreLeft != 0 || scoreRight != 100)
            scoreFilter = " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT game FROM scores GROUP BY game HAVING AVG(score) >= " + scoreLeft + " AND AVG(score) <= " + scoreRight + ") AS sc) AS c";
        String timeFilter = "";
        if(timeLeft != 0 || timeRight != 35999999)
            timeFilter = " NATURAL JOIN (SELECT DISTINCT game FROM (SELECT game FROM runs WHERE playstyle = 1 GROUP BY game HAVING AVG(time) >= " + timeLeft + " AND AVG(time) <= " + timeRight + ") AS a) AS d";

        Query nativeQuery = em.createNativeQuery("SELECT distinct * FROM (SELECT * FROM games WHERE LOWER(title) LIKE LOWER(CONCAT('%',:searchTerm,'%'))) as z" + genreFilter + platformFilter + scoreFilter + timeFilter
                + " ORDER BY game");
        nativeQuery.setParameter("searchTerm", searchTerm);
        return nativeQuery.getResultList().size();
    }

    @Override
    public List<Game> getGamesForPlatform(Platform p, int page, int pageSize) {
        Query nativeQuery = em.createNativeQuery("select distinct gv.game from game_versions gv where gv.platform = :p_id");
        nativeQuery.setParameter("p_id", p.getId());
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> ids = ((List<Long>) nativeQuery.getResultList().stream().map(obj -> ((BigInteger) obj).longValue()).collect(Collectors.toList()));

        TypedQuery<Game> query = em.createQuery("from Game g where g.game in :filteredIds", Game.class);
        query.setParameter("filteredIds", ids);
        return query.getResultList();
    }

    @Override
    public int countGamesForPlatform(Platform p) {
        if(p == null)
            return 0;
        return p.getGames().size();
    }

    @Override
    public List<Game> getGamesForGenre(Genre g, int page, int pageSize) {
        Query nativeQuery = em.createNativeQuery("select distinct c.game from classifications c where c.genre = :gen_id");
        nativeQuery.setParameter("gen_id", g.getId());
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> ids = ((List<Long>) nativeQuery.getResultList().stream().map(obj -> ((BigInteger) obj).longValue()).collect(Collectors.toList()));

        TypedQuery<Game> query = em.createQuery("from Game g where g.game in :filteredIds", Game.class);
        query.setParameter("filteredIds", ids);
        return query.getResultList();
    }

    @Override
    public int countGamesForGenre(Genre g) {
        if(g == null)
            return 0;
        return g.getGames().size();
    }

    @Override
    public List<Game> getGamesForDeveloper(Developer d, int page, int pageSize) {
        Query nativeQuery = em.createNativeQuery("select distinct d.game from development d where d.developer = :dev_id");
        nativeQuery.setParameter("dev_id", d.getId());
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> ids = ((List<Long>) nativeQuery.getResultList().stream().map(obj -> ((BigInteger) obj).longValue()).collect(Collectors.toList()));

        TypedQuery<Game> query = em.createQuery("from Game g where g.game in :filteredIds", Game.class);
        query.setParameter("filteredIds", ids);
        return query.getResultList();
    }

    @Override
    public int countGamesForDeveloper(Developer d) {
        if(d == null)
            return 0;
        return d.getGames().size();
    }

    @Override
    public List<Game> getGamesForPublisher(Publisher p, int page, int pageSize) {
        Query nativeQuery = em.createNativeQuery("select distinct p.game from publishing p where p.publisher = :pub_id");
        nativeQuery.setParameter("pub_id", p.getId());
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> ids = ((List<Long>) nativeQuery.getResultList().stream().map(obj -> ((BigInteger) obj).longValue()).collect(Collectors.toList()));

        TypedQuery<Game> query = em.createQuery("from Game g where g.game in :filteredIds", Game.class);
        query.setParameter("filteredIds", ids);
        return query.getResultList();
    }

    @Override
    public int countGamesForPublisher(Publisher p) {
        if(p == null)
            return 0;
        return p.getGames().size();
    }

    @Override
    public void remove(Game g) {
        em.remove(g);
    }

    @Override
    public void update(Game g) {
        em.flush();
    }

    @Override
    public void updateWithoutCover(Game g) {
        em.flush();
    }
}