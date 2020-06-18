package ar.edu.itba.paw.interfaces.dao;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.entity.Developer;

public interface DeveloperDao 
{
	/**
	 * Finds a developer given its ID
	 * @param id The unique ID for the developer.
	 * @return The matched developer, or null otherwise.
	 */
	Optional<Developer> findById(long id);
	
	/**
	 * Finds a developer or several developers with a given name
	 * @param name The name of the developer.
	 * @return List of developers that share that name.
	 */
	Optional<Developer> findByName(String name);
	
	/**
	 * Change a developer's name
	 * @param id		The id of the developer
	 * @param new_name The new name for the developer.
	 * @return The new, modified developer, or null if the original developer was not found.
	 */
	Optional<Developer> changeName(long id, String new_name);
	
	/**
	 * Change a developer's logo
	 * @param id		The id of the developer
	 * @param new_cover The new logo for the developer.
	 * @return The new, modified developer, or null if the original developer was not found.
	 */
	Optional<Developer> changeLogo(long id, String new_logo);
	
	/**
	 * Create a new developer.
	 * @param name The name of the developer.
	 * @param logo A link to the developer's logo
	 * @return The registered developer.
	 */
	Developer register(String name, String logo);
	
	/**
	 * Get a list of all available developers.
	 * @return The list of all developers.
	 */
	List<Developer> getAllDevelopers();
	
	List<Developer> getDevelopers(int page, int pageSize);
	
	int countDevelopers();
	
	List<Developer> getBiggestDevelopers(int amount);
}
