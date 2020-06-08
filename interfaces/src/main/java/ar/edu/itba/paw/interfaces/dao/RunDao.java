package ar.edu.itba.paw.interfaces.dao;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Playstyle;
import ar.edu.itba.paw.model.Run;
import ar.edu.itba.paw.model.User;

public interface RunDao {
	
	
/** RUN METHODS
 */

/**
 * Find run by  
 * @param run	The run  
 * @return The run
 */
Optional<Run> findRunById (long run);

/**
 * Find the runs of a specific user and game
 * @param game	The game
 * @param user	the user
 * @return	The list of runs
 */

List<Run> findGameRuns(Game game, User user);

/**
 * Find all runs by a user
 * @param user	The user
 * @return The list of runs
 */

List<Run> findAllUserRuns(User user);
List<Run> findRunsByUser(User user, int page, int pageSize);
int countRunsByUser(User user);

/**
 * Find all runs of a game
 * @param game	The game
 * @return The list of runs
 */

List<Run> findAllGameRuns(Game game);

/**
 * Find all runs of a game of a specific playstyle
 * @param game The game 
 * @param playstyle The playstyle
 * @return The list of runs
 */

List<Run> findPlaystyleAndGameRuns(Game game, Playstyle playstyle);

/**
 * Find all runs of a game of a specific playstyle and platform
 * @param game The game
 * @param playstyle The playstyle
 * @param platform The platform
 * @return The list of runs
 */

List<Run> findSpecificRuns(Game game, Playstyle playstyle, Platform platform);

/**
 * Get average playtime of a game in seconds
 * @param game The game
 * @return The average run
 */

long getAveragePlaytime(Game game);

/**
 * Get average playtime of a game in a specific platform in seconds
 * @param game  The game  
 * @param platform	The platform  
 * @return The average run
 */

long getAveragePlatformPlaytime(Game game, Platform platform);

/**
 * Get average playtime of a game played in a specific playstyle, in seconds
 * @param game  The game  
 * @param playstyle	The playstyle  
 * @return	The average run
 */

long getAveragePlaystylePlaytime(Game game, Playstyle playstyle);

/**
 * Get average playtime of a game played in a specific playstyle, in seconds
 * @param game  The game  
	 * @param platform	The platform  
 * @param playstyle	The playstyle  
 * @return	The average run
 */

long getAverageSpecificPlaytime(Game game , Playstyle playstyle, Platform platform);

/**
 * Get all runs
 * @return List if all runs
 */

List<Run> getAllRuns();

/**
 * Register a new run.
 * @param user	The user  	
 * @param game	The game  
 * @param platform	The platform  
 * @param playstyle		The playstyle  
 * @param time 	The taken to beat the game
 * @return The run.
 */

Run register(User user, Game game, Platform platform, Playstyle playstyle, long time);

/** PLAYSTYLE METHODS
 */

/**
 * Get list of all playstyles
 * @return list of playstyles
 */

List<Playstyle> getAllPlaystyles();

/**
 * Change playstyle name
 * @param new_name	New name for playstyle
 * @param playstyle	Playstyle id
 * @return New playstyle
 */

Optional<Playstyle> changePlaystyleName(String new_name, long playstyle);

/**
 * Find a playstyle by the  
 * @param playstyle The playstyle  id
 * @return The playstyle
 */

Optional<Playstyle> findPlaystyleById(long playstyle);

/**
 * Register a playstyle
 * @param name	name of playstyle
 * @return	the playstyle
 */
Playstyle register(String name);

/**
 * Get playstyle by name
 * @param name	name of playstyle
 * @return playstlye
 */

Optional<Playstyle> findPlaystyleByName(String name);

List<Run> getTopRuns(Game game, int amount);
}
