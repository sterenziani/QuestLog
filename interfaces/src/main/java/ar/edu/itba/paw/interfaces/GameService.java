package ar.edu.itba.paw.interfaces;
import java.util.List;
import java.util.Optional;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Release;

public interface GameService
{
	/**
	 * Finds a game given its ID
	 * @param id The unique ID for the game.
	 * @return The matched game, or null otherwise.
	 */
	Optional<Game> findById(long id);
	
	/**
	 * Finds a game or several games with a given title
	 * @param title The title for the game.
	 * @return List of games that share that title.
	 */
	Optional<Game> findByTitle(String title);
	
	/**
	 * Change a game's title
	 * @param id		The id of the game
	 * @param new_title The new title for the game.
	 * @return The new, modified game, or null if the original game was not found.
	 */
	Optional<Game> changeTitle(long id, String new_title);
	
	/**
	 * Change a game's cover
	 * @param id		The id of the game
	 * @param new_cover The new cover for the game.
	 * @return The new, modified game, or null if the original game was not found.
	 */
	Optional<Game> changeCover(long id, String new_cover);
	
	/**
	 * Change a game's description
	 * @param id		The id of the game
	 * @param new_desc 	The new description for the game.
	 * @return The new, modified game, or null if the original game was not found.
	 */
	Optional<Game> changeDescription(long id, String new_desc);
	
	/**
	 * Create a new game.
	 * @param title The title of the game.
	 * @param cover A link to the cover of the game
	 * @param description A description of the game
	 * @return The registered game.
	 */
	Game register(String title, String cover, String description);
	
	/**
	 * Get a list of all available games.
	 * @return The list of all games.
	 */
	List<Game> getAllGames();
	
	/**
	 * Links a game to a specified platform
	 * @param g		The game
	 * @param p		The platform to link the game to
	 * @return		The updated game, now linked to the platform
	 */
	Optional<Game> addPlatform(Game g, Platform p);
	
	/**
	 * Unlinks a game to a specified platform
	 * @param g		The game
	 * @param p		The platform to unlink the game from
	 * @return		The updated game, now unlinked from the platform
	 */
	Optional<Game> removePlatform(Game g, Platform p);
	
	/**
	 * Get a list of all platforms the game is available on
	 * @param g		The game
	 * @return		The list of platforms
	 */
	List<Platform> getAllPlatforms(Game g);
	
	/**
	 * Links a game to a specified developer
	 * @param g		The game
	 * @param d		The developer to link the game to
	 * @return		The updated game, now linked to the developer
	 */
	Optional<Game> addDeveloper(Game g, Developer d);
	
	/**
	 * Unlinks a game to a specified developer
	 * @param g		The game
	 * @param d		The developer to unlink the game from
	 * @return		The updated game, now unlinked from the developer
	 */
	Optional<Game> removeDeveloper(Game g, Developer d);
	
	/**
	 * Get a list of all developers the game is available on
	 * @param g		The game
	 * @return		The list of developers
	 */
	List<Developer> getAllDevelopers(Game g);
	
	/**
	 * Links a game to a specified publisher
	 * @param g		The game
	 * @param pub	The publisher to link the game to
	 * @return		The updated game, now linked to the publisher
	 */
	Optional<Game> addPublisher(Game g, Publisher pub);
	
	/**
	 * Unlinks a game to a specified publisher
	 * @param g		The game
	 * @param pub	The publisher to unlink the game from
	 * @return		The updated game, now unlinked from the publisher
	 */
	Optional<Game> removePublisher(Game g, Publisher pub);
	
	/**
	 * Get a list of all publishers the game is available on
	 * @param g		The game
	 * @return		The list of publishers
	 */
	List<Publisher> getAllPublishers(Game g);

	/**
	 * Categorize a game as part of a certain genre
	 * @param game		The game
	 * @param genre		The genre
	 * @return			The updated game, now marked as being of that genre
	 */
	Optional<Game> addGenre(Game game, Genre genre);
	
	/**
	 * Stop categorizing a game as being part of a genre
	 * @param game		The game
	 * @param genre		The genre
	 * @return			The updated game, now marked as not being of that genre
	 */
	Optional<Game> removeGenre(Game game, Genre genre);
	
	/**
	 * Get a list of all genres the game is classified as
	 * @param g		The game
	 * @return		The list of genres
	 */
	List<Genre> getAllGenres(Game g);
	
	/**
	 * Adds a release (date and region) for the game
	 * @param game	The game
	 * @param r		The release
	 * @return		The updated game, now updated with the new release info.
	 */
	Optional<Game> addReleaseDate(Game game, Release r);
	
	/**
	 * Removes a release (date and region) for the game
	 * @param game	The game
	 * @param r		The release
	 * @return		The updated game, now without that release listed
	 */
	Optional<Game> removeReleaseDate(Game game, Release r);
	
	/**
	 * Get a map of the game's release date in each region.
	 * @param g		The game
	 * @return		A map with the regions as keys and the release date in each region as their value.
	 */
	List<Release> getAllReleaseDates(Game g);
	
	/**
	 * Get a list of all games with names that contain the searched term.
	 * @param search The search term.
	 * @return 	The list of matching games.
	 */
	List<Game> searchByTitle(String search);
	
	/**
	 * Get a list of all available games without the extra information.
	 * @return 	The list of all games.
	 */
	List<Game> getAllGamesSimplified();
	
	/**
	 * Get a list of all games with names that contain the searched term without the extra information.
	 * @param search The search term.
	 * @return 	The list of matching games.
	 */
	List<Game> searchByTitleSimplified(String search);
	
	/**
	 * Get a list of upcoming games.
	 * @return The list of upcoming games.
	 */
	List<Game> getUpcomingGames();

	/**
	 * Get a list of upcoming games without the extra information.
	 * @return The list of upcoming games.
	 */
	List<Game> getUpcomingGamesSimplified();

	
	boolean gameInBacklog(String backlog, long gameId);

	List<Game> getGamesInBacklog(String backlog);

	String addToBacklog(String backlog, long gameId);

	String removeFromBacklog(String backlog, long gameId);

	List<Game> getAllGamesSimplified(String backlog);

	List<Game> searchByTitleSimplified(String search, String backlog);

	List<Game> getUpcomingGamesSimplified(String backlog);

	Optional<Game> findById(long id, String backlog);

	String toggleBacklog(String backlog, long gameId);
}
