package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Game;

public interface GameDao
{
	/**
	 * Finds a game given its ID
	 * @param id 	The unique ID for the game.
	 * @return 		The matched game, or null otherwise.
	 */
	Optional<Game> findById(long id);
	
	/**
	 * Finds a game or several games with a given title
	 * @param title The title for the game.
	 * @return 		List of games that share that title.
	 */
	List<Game> findByTitle(String title);
	
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
	 * @param title 	The title of the game.
	 * @return 			The registered game.
	 */
	Game register(String title, String cover, String description);
	
	/**
	 * Get a list of all available games.
	 * @return 	The list of all games.
	 */
	List<Game> getAllGames();
}
