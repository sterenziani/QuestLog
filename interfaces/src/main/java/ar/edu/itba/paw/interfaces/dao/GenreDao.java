package ar.edu.itba.paw.interfaces.dao;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.entity.Genre;

public interface GenreDao {
	/**
	 * Finds a genre given its ID
	 * @param id The unique ID for the genre.
	 * @return The matched genre, or null otherwise.
	 */
	Optional<Genre> findById(long id);
	
	/**
	 * Finds a genre or several genres with a given name
	 * @param name The name of the genre.
	 * @return List of genres that share that name.
	 */
	Optional<Genre> findByName(String name);
	
	/**
	 * Change a genre's name
	 * @param id		The id of the genre
	 * @param new_name The new name for the genre.
	 * @return The new, modified genre, or null if the original genre was not found.
	 */
	Optional<Genre> changeName(long id, String new_name);
	
	/**
	 * Change a genre's logo
	 * @param id		The id of the genre
	 * @param new_logo The new logo for the genre.
	 * @return The new, modified genre, or null if the original genre was not found.
	 */
	Optional<Genre> changeLogo(long id, String new_logo);
	
	/**
	 * Create a new genre.
	 * @param name The name of the genre.
	 * @param logo A link to the genre's logo
	 * @return The registered genre.
	 */
	Genre register(String name,String logo);
	
	
	/**
	 * Get a list of all available genres.
	 * @return The list of all genres.
	 */
	List<Genre> getAllGenres();
	
	List<Genre> getGenres(int page, int pageSize);
	
	int countGenres();
}
