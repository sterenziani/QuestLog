package ar.edu.itba.paw.persistence;
import ar.edu.itba.paw.interfaces.dao.GameDao;
import ar.edu.itba.paw.model.entity.*;
import org.springframework.stereotype.Repository;
import javax.persistence.*;
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
            g.getGenres().size();
            g.getDevelopers().size();
            g.getPublishers().size();
            g.getPlatforms().size();
            g.getReleaseDates().size();
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
            g.getGenres().size();
            g.getDevelopers().size();
            g.getPublishers().size();
            g.getPlatforms().size();
            g.getReleaseDates().size();
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
    public Game register(String title, String cover, String description, String trailer) {
        Game g = new Game(title, cover, description, trailer);
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
            g.getGenres().size();
            g.getDevelopers().size();
            g.getPublishers().size();
            g.getPlatforms().size();
            g.getReleaseDates().size();
        }
        return list;
    }

    @Override
    public Optional<Game> addPlatform(Game g, Platform p) {
        if(g == null || p == null)
            return Optional.empty();
        g.addPlatform(p);
        p.addGame(g);
        
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
            if(p != null) {
                game.addPlatform(p);
                p.addGame(game);
            }
        }
        
    }

    @Override
    public Optional<Game> removePlatform(Game g, Platform p) {
        if(g == null || p == null)
            return Optional.empty();
        g.removePlatform(p);
        p.removeGame(g);
        
        return Optional.of(g);
    }

    @Override
    public void removeAllPlatforms(Game g) {
        if(g == null)
            return;
        for (Platform platform : g.getPlatforms()){
            platform.removeGame(g);
        }
        g.removePlatforms();
        
    }

    @Override
    public Optional<Game> addDeveloper(Game g, Developer d) {
        if(g == null || d == null)
            return Optional.empty();
        g.addDeveloper(d);
        d.addGame(g);
        
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
            if(d != null) {
                game.addDeveloper(d);
                d.addGame(game);
            }
        }
        
    }

    @Override
    public Optional<Game> removeDeveloper(Game g, Developer d) {
        if(g == null || d == null)
            return Optional.empty();
        g.removeDeveloper(d);
        d.removeGame(g);
        
        return Optional.of(g);
    }

    @Override
    public void removeAllDevelopers(Game g) {
        if(g == null)
            return;
        for(Developer dev : g.getDevelopers()){
            dev.removeGame(g);
        }
        g.removeDevelopers();
        
    }

    @Override
    public Optional<Game> addPublisher(Game g, Publisher pub) {
        if(g == null || pub == null)
            return Optional.empty();
        g.addPublisher(pub);
        pub.addGame(g);
        
        return Optional.of(g);
    }

    @Override
    public Optional<Game> removePublisher(Game g, Publisher pub) {
        if(g == null || pub == null)
            return Optional.empty();
        g.removePublisher(pub);
        pub.removeGame(g);
        
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
            if(p != null) {
                game.addPublisher(p);
                p.addGame(game);
            }
        }
        
    }

    @Override
    public void removeAllPublishers(Game g) {
        if(g == null)
            return;
        for(Publisher pub : g.getPublishers()){
            pub.removeGame(g);
        }
        g.removePublishers();
        
    }

    @Override
    public Optional<Game> addGenre(Game game, Genre genre) {
        if(game == null || genre == null)
            return Optional.empty();
        game.addGenre(genre);
        genre.addGame(game);
        
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
            if(gen != null) {
                game.addGenre(gen);
                gen.addGame(game);
            }
        }
        
    }

    @Override
    public Optional<Game> removeGenre(Game game, Genre genre) {
        if(game == null || genre == null)
            return Optional.empty();
        game.removeGenre(genre);
        genre.removeGame(game);
        
        return Optional.of(game);
    }

    @Override
    public void removeAllGenres(Game g) {
        if(g == null)
            return;
        for(Genre gen : g.getGenres()){
            gen.removeGame(g);
        }
        g.removeGenres();
        
    }

    @Override
    public Optional<Game> addReleaseDate(Game game, Release r) {
        if(game == null || r == null)
            return Optional.empty();
        game.addReleaseDate(r);
        
        return Optional.of(game);
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
            if(date.getValue() != null) {
                Region r = em.find(Region.class, date.getKey());
                if (r != null) {
                    Release release = new Release(game, r, date.getValue());
                    em.persist(release);
                    game.addReleaseDate(release);
                }
            }
        }
        
    }

    @Override
    public Optional<Game> removeReleaseDate(Game game, Release r) {
        if(game == null || r == null)
            return Optional.empty();
        game.removeReleaseDate(r);
        em.remove(r);
        
        return Optional.of(game);
    }

    @Override
    public void removeAllReleaseDates(Game g) {
        if(g == null)
            return;
        for(Release release : g.getReleaseDates()){
            em.remove(release);
        }
        g.removeReleaseDates();
        
    }

    @Override
    public List<Game> searchByTitle(String search, int page, int pageSize) {
    	search = search.replace("%", "\\%").replace("_", "\\_");
        final TypedQuery<Game> query = em.createQuery("select g from Game as g where lower(g.title) like concat('%', lower(:search),'%')", Game.class);
        query.setParameter("search", search);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<Game> getUpcomingGames() {
        Query nativeQuery = em.createNativeQuery("select g from (select distinct game as g, min(release_date) as d from releases r group by game having min(release_date) >= CURRENT_DATE ORDER BY d) as x");
        nativeQuery.setMaxResults(MAX_RESULT_FOR_SHOWCASE);
        @SuppressWarnings("unchecked")
        List<Long> ids = ((List<Object>) nativeQuery.getResultList()).stream().map((num) -> ((Number) num).longValue()).collect(Collectors.toList());
        if(ids.isEmpty())
        	return Collections.emptyList();
        final TypedQuery<Game> query = em.createQuery("from Game as g where g.game in :ids", Game.class);
        query.setParameter("ids", ids);
        return query.getResultList();
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
        Query nativeQuery = em.createNativeQuery("select distinct b.game from backlogs b where b.user_id = :u_id");
        nativeQuery.setParameter("u_id", u.getId());
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<Long> ids = ((List<Object>) nativeQuery.getResultList()).stream().map((num) -> ((Number) num).longValue()).collect(Collectors.toList());
        
        if(ids.isEmpty())
        	return Collections.emptyList();
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
    	Query nativeQuery = em.createNativeQuery("SELECT distinct game FROM (SELECT t2.game AS game FROM backlogs AS t1 JOIN backlogs AS t2 ON t1.user_id = t2.user_id AND t1.user_id != :u_id AND t1.game IN"
    		    + " (SELECT game FROM backlogs WHERE user_id = :u_id) AND t2.game NOT IN (SELECT game FROM backlogs WHERE user_id = :u_id) GROUP BY t2.game HAVING"
    		    + " count(*) >= :min_amount_for_overlap ORDER BY count(*) DESC) AS a NATURAL JOIN games");
    	nativeQuery.setParameter("u_id", u.getId());
    	nativeQuery.setParameter("min_amount_for_overlap", MIN_AMOUNT_FOR_OVERLAP);
    	nativeQuery.setMaxResults(MAX_RESULT_FOR_SHOWCASE);
    	    	
        @SuppressWarnings("unchecked")
        List<Long> ids = ((List<Object>) nativeQuery.getResultList()).stream().map((num) -> ((Number) num).longValue()).collect(Collectors.toList());
        if(ids.isEmpty())
        	return Collections.emptyList();
        TypedQuery<Game> query = em.createQuery("from Game g where g.game in :filteredIds", Game.class);
        query.setParameter("filteredIds", ids);
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
    	searchTerm = searchTerm.replace("%", "\\%").replace("_", "\\_");
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

        Query nativeQuery = em.createNativeQuery("SELECT game FROM (SELECT distinct game, title FROM (SELECT * FROM games WHERE LOWER(title) LIKE LOWER(CONCAT('%',:searchTerm,'%'))) as z" + genreFilter + platformFilter + scoreFilter + timeFilter
                + " ORDER BY title) AS w");
        nativeQuery.setParameter("searchTerm", searchTerm);
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<Long> ids = ((List<Object>) nativeQuery.getResultList()).stream().map((num) -> ((Number) num).longValue()).collect(Collectors.toList());
        if(ids.isEmpty())
        	return Collections.emptyList();
        TypedQuery<Game> query = em.createQuery("from Game g where g.game in :filteredIds order by title", Game.class);
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
        final TypedQuery<Release> query = em.createQuery("from Release as r where r.date = CURRENT_DATE + 1", Release.class);
        final List<Release> list = query.getResultList();
        if(list.isEmpty())
            return Collections.emptyList();
        List<Game> games = new ArrayList<>();
        for(Release r : list){
        	if(u.isInBacklog(r.getGame()))
        		games.add(r.getGame());
        }
        return games;
    }

    @Override
    public int countSearchResults(String searchTerm) {
    	searchTerm = searchTerm.replace("%", "\\%").replace("_", "\\_");
        final Query query = em.createNativeQuery("select * from games as g where lower(g.title) like '%' || lower(:searchTerm) || '%'");
        query.setParameter("searchTerm", searchTerm);
        return query.getResultList().size();
    }

    @Override
    public int countSearchResultsFiltered(String searchTerm, List<String> genres, List<String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight) {
    	searchTerm = searchTerm.replace("%", "\\%").replace("_", "\\_");
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
        Query nativeQuery = em.createNativeQuery("select foo.game from (select distinct game, title from game_versions natural join games where platform = :p_id order by title) as foo");
        nativeQuery.setParameter("p_id", p.getId());
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<Long> ids = ((List<Object>) nativeQuery.getResultList()).stream().map((num) -> ((Number) num).longValue()).collect(Collectors.toList());
        if(ids.isEmpty())
        	return Collections.emptyList();
        TypedQuery<Game> query = em.createQuery("from Game g where g.game in :filteredIds order by title", Game.class);
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
        Query nativeQuery = em.createNativeQuery("select foo.game from (select distinct game, title from classifications natural join games where genre = :gen_id order by title) as foo");
        nativeQuery.setParameter("gen_id", g.getId());
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
		List<Long> ids = ((List<Object>) nativeQuery.getResultList()).stream().map((num) -> ((Number) num).longValue()).collect(Collectors.toList());
        if(ids.isEmpty())
        	return Collections.emptyList();
        TypedQuery<Game> query = em.createQuery("from Game g where g.game in :filteredIds order by title", Game.class);
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
        Query nativeQuery = em.createNativeQuery("select foo.game from (select distinct game, title from development natural join games where developer = :dev_id order by title) as foo");
        nativeQuery.setParameter("dev_id", d.getId());
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<Long> ids = ((List<Object>) nativeQuery.getResultList()).stream().map((num) -> ((Number) num).longValue()).collect(Collectors.toList());
        if(ids.isEmpty())
        	return Collections.emptyList();
        TypedQuery<Game> query = em.createQuery("from Game g where g.game in :filteredIds order by title", Game.class);
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
        Query nativeQuery = em.createNativeQuery("select foo.game from (select distinct game, title from publishing natural join games where publisher = :pub_id order by title) as foo");
        nativeQuery.setParameter("pub_id", p.getId());
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<Long> ids = ((List<Object>) nativeQuery.getResultList()).stream().map((num) -> ((Number) num).longValue()).collect(Collectors.toList());
        if(ids.isEmpty())
        	return Collections.emptyList();
        TypedQuery<Game> query = em.createQuery("from Game g where g.game in :filteredIds order by title", Game.class);
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
        em.merge(g);
    }
}
