package ar.edu.itba.paw.interfaces;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.User;

public interface PlatformService
{
	/**
	 * Finds a platform given its ID
	 * @param id The unique ID for the platform.
	 * @return The matched platform, or null otherwise.
	 */
	Optional<Platform> findById(long id);
	Optional<Platform> findById(long id, User u);
	
	/**
	 * Finds a platform or several platforms with a given name
	 * @param title The name of the platform.
	 * @return List of platforms that share that name.
	 */
	Optional<Platform> findByName(String name);
	Optional<Platform> findByName(String name, User u);
	
	/**
	 * Change a platform's name
	 * @param id		The id of the platform
	 * @param new_title The new name for the platform.
	 * @return The new, modified platform, or null if the original platform was not found.
	 */
	Optional<Platform> changeName(long id, String new_name);
	
	/**
	 * Change a platform's logo
	 * @param id		The id of the platform
	 * @param new_cover The new logo for the platform.
	 * @return The new, modified platform, or null if the original platform was not found.
	 */
	Optional<Platform> changeLogo(long id, String new_logo);
	
	/**
	 * Change a platform's short version of its name.
	 * @param id		The id of the platform
	 * @param new_desc 	The new short name for the platform.
	 * @return The new, modified platform, or null if the original platform was not found.
	 */
	Optional<Platform> changeShortName(long id, String new_shortName);
	
	/**
	 * Create a new platform.
	 * @param name The name of the platform.
	 * @param shortName The short form of the platform's name
	 * @param logo A link to the platform's logo
	 * @return The registered platform.
	 */
	Platform register(String name, String shortName, String logo);
	
	/**
	 * Get a list of all available platforms.
	 * @return The list of all platforms.
	 */
	List<Platform> getAllPlatforms();
	
	/**
	 * Get a list of all available platforms with their games filled in.
	 * @return The list of all platforms.
	 */
	List<Platform> getAllPlatformsWithGames();
}
