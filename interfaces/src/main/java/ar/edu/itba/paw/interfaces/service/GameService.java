package ar.edu.itba.paw.interfaces.service;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Release;
import ar.edu.itba.paw.model.User;

public interface GameService
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
	 * @return List of games that share that title.
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
	 * @param title 		The title of the game.
	 * @param cover			The cover of the game.
	 * @param description	The description of the game
	 * @param platforms		The platforms the game is on
	 * @param developers	The developers of the game
	 * @param publishers    The publishers of the game
	 * @param genres		The genres the game belongs to
	 * @param releaseDates  The dates when the game releases
	 * @return 				The registered game.
	 */

	Game register(String title, String cover, String description, long[] platforms, long[] developers, long[] publishers, long[] genres, LocalDate[] releaseDates);
	
	/**
	 * Get a list of all available games.
	 * @return 	The list of all games.
	 */
	List<Game> getAllGames();
	List<Game> getAllGamesWithDetails();
	
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
	 * Get a list of all games with names that contain the searched term.
	 * @param search The search term.
	 * @param page	Page number
	 * @param pageSize	Page size
	 * @return 	The list of matching games.
	 */
	List<Game> searchByTitle(String search, int page, int pageSize);
	List<Game> searchByTitleWithDetails(String search);

	/**
	 * Get a list of upcoming games.
	 * @return The list of upcoming games.
	 */
	List<Game> getUpcomingGames();

	boolean gameInBacklog(long gameId);

	List<Game> getGamesInBacklog();
	List<Game> getGamesInBacklog(User u);

	void addToBacklog(long gameId);

	void removeFromBacklog(long gameId);

	void toggleBacklog(long gameId);

	void updateBacklogDetails(Collection<Game> list);

	List<Game> getRecommendedGames();

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

	List<Game> getPopularGames();

	List<Game> getGamesReleasingTomorrow();

	List<Game> getGamesInBacklogReleasingTomorrow(User u);

	int countSearchResults(String searchTerm);

	int countSearchResultsFiltered(String searchTerm, List<String> genres, List<String> platforms, int scoreLeft, int scoreRight, int timeLeft, int timeRight);

	List<Game> getGamesForPlatform(Platform p, int page, int pageSize);
	int countGamesForPlatform(Platform p);

	List<Game> getGamesForDeveloper(Developer d, int page, int pageSize);
	int countGamesForDeveloper(Developer d);

	List<Game> getGamesForPublisher(Publisher p, int page, int pageSize);
	int countGamesForPublisher(Publisher p);
}
