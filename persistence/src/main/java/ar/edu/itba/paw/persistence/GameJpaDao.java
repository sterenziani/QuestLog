package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.GameDao;
import ar.edu.itba.paw.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GameJpaDao implements GameDao {

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
            g.getPlatforms();
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
        if(g != null)
            g.getPlatforms();
        return Optional.ofNullable(g);
    }

    @Override
    public Optional<Game> changeTitle(long id, String new_title) {
        Game g = em.find(Game.class, id);
        if(g == null)
            return Optional.empty();
        g.setTitle(new_title);
        em.persist(g);
        return Optional.of(g);
    }

    @Override
    public Optional<Game> changeCover(long id, String new_cover) {
        Game g = em.find(Game.class, id);
        if(g == null)
            return Optional.empty();
        g.setCover(new_cover);
        em.persist(g);
        return Optional.of(g);
    }

    @Override
    public Optional<Game> changeDescription(long id, String new_desc) {
        Game g = em.find(Game.class, id);
        if(g == null)
            return Optional.empty();
        g.setDescription(new_desc);
        em.persist(g);
        return Optional.of(g);
    }

    @Override
    public Game register(String title, String cover, String description) {
        return null;
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
            g.getPlatforms();
        }
        return list;
    }

    @Override
    public Optional<Game> addPlatform(Game g, Platform p) {
        return Optional.empty();
    }

    @Override
    public void addPlatforms(long g, List<Long> platforms_ids) {

    }

    @Override
    public Optional<Game> removePlatform(Game g, Platform p) {
        return Optional.empty();
    }

    @Override
    public void removeAllPlatforms(Game g) {

    }

    @Override
    public Optional<Game> addDeveloper(Game g, Developer d) {
        return Optional.empty();
    }

    @Override
    public void addDevelopers(long g, List<Long> devs_ids) {

    }

    @Override
    public Optional<Game> removeDeveloper(Game g, Developer d) {
        return Optional.empty();
    }

    @Override
    public void removeAllDevelopers(Game g) {

    }

    @Override
    public Optional<Game> addPublisher(Game g, Publisher pub) {
        return Optional.empty();
    }

    @Override
    public Optional<Game> removePublisher(Game g, Publisher pub) {
        return Optional.empty();
    }

    @Override
    public void addPublishers(long g, List<Long> publisher_ids) {

    }

    @Override
    public void removeAllPublishers(Game g) {

    }

    @Override
    public Optional<Game> addGenre(Game game, Genre genre) {
        return Optional.empty();
    }

    @Override
    public void addGenres(long g, List<Long> genres_ids) {

    }

    @Override
    public Optional<Game> removeGenre(Game game, Genre genre) {
        return Optional.empty();
    }

    @Override
    public void removeAllGenres(Game g) {

    }

    @Override
    public Optional<Game> addReleaseDate(Game game, Release r) {
        return Optional.empty();
    }

    @Override
    public void addReleaseDates(long g, Map<Long, LocalDate> releaseDates) {

    }

    @Override
    public Optional<Game> removeReleaseDate(Game game, Release r) {
        return Optional.empty();
    }

    @Override
    public void removeAllReleaseDates(Game g) {

    }

    @Override
    public List<Game> searchByTitle(String search, int page, int pageSize) {
        return null;
    }

    @Override
    public List<Game> getUpcomingGames() {
        return null;
    }

    @Override
    public boolean isInBacklog(long gameId, User u) {
        return false;
    }

    @Override
    public void addToBacklog(long gameId, User u) {

    }

    @Override
    public void removeFromBacklog(long gameId, User u) {

    }

    @Override
    public List<Game> getGamesInBacklog(User u) {
        return null;
    }

    @Override
    public List<Game> getGamesInBacklog(User u, int page, int pageSize) {
        return null;
    }

    @Override
    public int countGamesInBacklog(User u) {
        return 0;
    }

    @Override
    public List<Game> getSimilarToBacklog(User u) {
        return null;
    }

    @Override
    public List<Game> getMostBacklogged() {
        return null;
    }

    @Override
    public List<Game> getFilteredGames(String searchTerm, List<String> genres, List<String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight, int page, int pageSize) {
        return null;
    }

    @Override
    public List<Game> getGamesReleasingTomorrow() {
        return null;
    }

    @Override
    public List<Game> getGamesInBacklogReleasingTomorrow(User u) {
        return null;
    }

    @Override
    public int countSearchResults(String searchTerm) {
        return 0;
    }

    @Override
    public int countSearchResultsFiltered(String searchTerm, List<String> genres, List<String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight) {
        return 0;
    }

    @Override
    public List<Game> getGamesForPlatform(Platform p, int page, int pageSize) {
        return null;
    }

    @Override
    public int countGamesForPlatform(Platform p) {
        return 0;
    }

    @Override
    public List<Game> getGamesForGenre(Genre g, int page, int pageSize) {
        return null;
    }

    @Override
    public int countGamesForGenre(Genre g) {
        return 0;
    }

    @Override
    public List<Game> getGamesForDeveloper(Developer d, int page, int pageSize) {
        return null;
    }

    @Override
    public int countGamesForDeveloper(Developer d) {
        return 0;
    }

    @Override
    public List<Game> getGamesForPublisher(Publisher p, int page, int pageSize) {
        return null;
    }

    @Override
    public int countGamesForPublisher(Publisher p) {
        return 0;
    }

    @Override
    public void remove(Game g) {

    }

    @Override
    public void update(Game g) {

    }

    @Override
    public void updateWithoutCover(Game g) {

    }
}
