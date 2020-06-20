package ar.edu.itba.paw.interfaces.dao;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ar.edu.itba.paw.model.entity.Developer;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Genre;
import ar.edu.itba.paw.model.entity.Platform;
import ar.edu.itba.paw.model.entity.Publisher;
import ar.edu.itba.paw.model.entity.Release;
import ar.edu.itba.paw.model.entity.User;

public interface GameDao
{
	/**
	 * Finds a game given its ID
	 * @param id 	The unique ID for the game.
	 * @return 		The matched game, or null otherwise.
	 */
	Optional<Game> findById(long id);
	
	/**
	 * Finds a game and its details (platforms, devs, genres, etc...) given its ID
	 * @param id 	The unique ID for the game.
	 * @return 		The matched game, or null otherwise.
	 */
	Optional<Game> findByIdWithDetails(long id);
	
	/**
	 * Finds a game or several games with a given title
	 * @param title The title for the game.
	 * @return 		The game with that title.
	 */
	Optional<Game> findByTitle(String title);
	
	/**
	 * Finds a game and its details (platforms, devs, genres, etc...) with a given title
	 * @param title The title for the game.
	 * @return 		The game with that title.
	 */
	Optional<Game> findByTitleWithDetails(String title);
	
	/**
	 * Change a game's title
	 * @param id		The id of the game
	 * @param new_title The new title for the game.
	 * @return 			The new, modified game, or null if the original game was not found.
	 */
	Optional<Game> changeTitle(long id, String new_title);
	
	/**
	 * Change a game's cover
	 * @param id		The id of the game
	 * @param new_cover The new cover for the game.
	 * @return 			The new, modified game, or null if the original game was not found.
	 */
	Optional<Game> changeCover(long id, String new_cover);
	
	/**
	 * Change a game's description
	 * @param id		The id of the game
	 * @param new_desc 	The new description for the game.
	 * @return 			The new, modified game, or null if the original game was not found.
	 */
	Optional<Game> changeDescription(long id, String new_desc);
	
	/**
	 * Create a new game.
	 * @param title 		The title of the game.
	 * @param cover			The cover of the game.
	 * @param description	The description of the game
	 * @return 				The registered game.
	 */

	Game register(String title, String cover, String description, String trailer);
	
	/**
	 * Get a list of all available games.
	 * @return 	The list of all games.
	 */
	List<Game> getAllGames();
	
	/**
	 * Get a list of all available games with the additional details of each game.
	 * @return 	The list of all games.
	 */
	List<Game> getAllGamesWithDetails();
	
	/**
	 * Links a game to a specified platform
	 * @param g		The game
	 * @param p		The platform to link the game to
	 * @return		The updated game, now linked to the platform
	 */
	Optional<Game> addPlatform(Game g, Platform p);

	/**
	 * Links a game to a specified platforms
	 * @param g					The game
	 * @param platforms_ids		The platforms to link the game to
	 * @return					The updated game, now linked to the platforms
	 */

	void addPlatforms(long g, List<Long> platforms_ids);
	
	/**
	 * Unlinks a game to a specified platform
	 * @param g		The game
	 * @param p		The platform to unlink the game from
	 * @return		The updated game, now unlinked from the platform
	 */
	Optional<Game> removePlatform(Game g, Platform p);

	/**
	 * Unlinks a game to all its platforms
	 * @param g		The game
	 */
	void removeAllPlatforms(Game g);

	/**
	 * Links a game to a specified developer
	 * @param g		The game
	 * @param d		The developer to link the game to
	 * @return		The updated game, now linked to the developer
	 */
	Optional<Game> addDeveloper(Game g, Developer d);

	/**
	 * Links a game to specified developers
	 * @param g				The game
	 * @param devs_ids		The developers to link the game to
	 * @return				The updated game, now linked to the developers
	 */
	void addDevelopers(long g, List<Long> devs_ids);
	
	/**
	 * Unlinks a game to a specified developer
	 * @param g		The game
	 * @param d		The developer to unlink the game from
	 * @return		The updated game, now unlinked from the developer
	 */
	Optional<Game> removeDeveloper(Game g, Developer d);

	/**
	 * Unlinks a game to all its developers
	 * @param g		The game
	 */
	void removeAllDevelopers(Game g);
	
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
	 * Links a game to specified publishers
	 * @param g				The game
	 * @param publisher_ids	The publishers to unlink the game from
	 */
	void addPublishers(long g, List<Long> publisher_ids);

	/**
	 * Unlinks a game from all its publishers
	 * @param g		The game
	 */
	void removeAllPublishers(Game g);

	/**
	 * Categorize a game as part of a certain genre
	 * @param game		The game
	 * @param genre		The genre
	 * @return			The updated game, now marked as being of that genre
	 */
	Optional<Game> addGenre(Game game, Genre genre);

	/**
	 * Categorize a game as part of a certain genre
	 * @param g					The game
	 * @param genres_ids		The genre
	 * @return					The updated game, now marked as being of that genre
	 */
	void addGenres(long g, List<Long> genres_ids);
	
	/**
	 * Stop categorizing a game as being part of a genre
	 * @param game		The game
	 * @param genre		The genre
	 * @return			The updated game, now marked as not being of that genre
	 */
	Optional<Game> removeGenre(Game game, Genre genre);

	/**
	 * Stop categorizing a game
	 * @param g		The game
	 */
	void removeAllGenres(Game g);
	
	/**
	 * Adds a release (date and region) for the game
	 * @param game	The game
	 * @param r		The release
	 * @return		The updated game, now updated with the new release info.
	 */
	Optional<Game> addReleaseDate(Game game, Release r);

	/**
	 * Adds releases (date and region) for the game
	 * @param g					The game
	 * @param releaseDates		The releases
	 * @return					The updated game, now updated with the new release info.
	 */
	void addReleaseDates(long g, Map<Long, LocalDate> releaseDates);
	
	/**
	 * Removes a release (date and region) for the game
	 * @param game	The game
	 * @param r		The release
	 * @return		The updated game, now without that release listed
	 */
	Optional<Game> removeReleaseDate(Game game, Release r);

	/**
	 * Removes all releases (date and region) for the game
	 * @param g		The game
	 */
	void removeAllReleaseDates(Game g);
	
	/**
	 * Get a list of all games with names that contain the searched term.
	 * @param search The search term.
	 * @param page	Page number
	 * @param pageSize	Page size
	 * @return 	The list of matching games.
	 */
	List<Game> searchByTitle(String search, int page, int pageSize);
	
	/**
	 * Get a list of upcoming games.
	 * @return The list of upcoming games.
	 */
	List<Game> getUpcomingGames();
	
	/**
	 * Check if the game with the provided id is in the user's backlog.
	 * @param gameId	The id of the game
	 * @param u			The user
	 * @return			True if the game is in their backlog, false if not.
	 */
	boolean isInBacklog(long gameId, User u);
	
	/**
	 * Add a game to a registered user's backlog
	 * @param u	The user adding the game to their backlog
	 * @param gameId The game
	 */
	void addToBacklog(long gameId, User u);
	
	/**
	 * Remove a game from a user's backlog
	 * @param gameId	The id of the game
	 * @param u			The user
	 */
	void removeFromBacklog(long gameId, User u);
	
	/**
	 * Get a list of all games saved in a user's backlog
	 * @param u	The user
	 * @return	The list of games
	 */
	List<Game> getGamesInBacklog(User u);
	List<Game> getGamesInBacklog(User u, int page, int pageSize);
	int countGamesInBacklog(User u);
	
	List<Game> getSimilarToBacklog(User u);

	List<Game> getMostBacklogged();

	
	/**
	 * Find games through a more detailed search
	 * @param searchTerm	The term being searched
	 * @param genres	Genres to filter by
	 * @param platforms	Platforms to filter by
	 * @param scoreLeft	Minimum average score
	 * @param scoreRight	Maximum average score
	 * @param timeLeft	Minimum average gameplay in Main Game playstyle
	 * @param timeRight	Maximum average gameplay in Main Game playstyle
	 * @return	The games that pass said filters
	 */
	
	List<Game> getFilteredGames(String searchTerm, List<String> genres, List<String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight, int page, int pageSize);

	List<Game> getGamesReleasingTomorrow();

	List<Game> getGamesInBacklogReleasingTomorrow(User u);
	
	int countSearchResults(String searchTerm);
	
	int countSearchResultsFiltered(String searchTerm, List<String> genres, List<String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight);

	List<Game> getGamesForPlatform(Platform p, int page, int pageSize);
	
	int countGamesForPlatform(Platform p);
	
	List<Game> getGamesForGenre(Genre g, int page, int pageSize);
	
	int countGamesForGenre(Genre g);

	List<Game> getGamesForDeveloper(Developer d, int page, int pageSize);
	int countGamesForDeveloper(Developer d);

	List<Game> getGamesForPublisher(Publisher p, int page, int pageSize);
	int countGamesForPublisher(Publisher p);
	
	void remove(Game g);

	void update(Game g);

	void updateWithoutCover(Game g);
}
