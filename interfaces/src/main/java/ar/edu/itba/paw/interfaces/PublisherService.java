package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.User;

public interface PublisherService {
	/**
	 * Finds a publisher given its ID
	 * @param id The unique ID for the publisher.
	 * @return The matched publisher, or null otherwise.
	 */
	Optional<Publisher> findById(long id);
	Optional<Publisher> findById(long id, User u);
	
	/**
	 * Finds a publisher or several publishers with a given name
	 * @param name The name of the publisher.
	 * @return List of publishers that share that name.
	 */
	Optional<Publisher> findByName(String name);
	Optional<Publisher> findByName(String name, User u);
	
	/**
	 * Change a publisher's name
	 * @param id		The id of the publisher
	 * @param new_name The new name for the publisher.
	 * @return The new, modified publisher, or null if the original publisher was not found.
	 */
	Optional<Publisher> changeName(long id, String new_name);
	
	/**
	 * Change a publisher's logo
	 * @param id		The id of the publisher
	 * @param new_cover The new logo for the publisher.
	 * @return The new, modified publisher, or null if the original publisher was not found.
	 */
	Optional<Publisher> changeLogo(long id, String new_logo);
	
	/**
	 * Create a new publisher.
	 * @param name The name of the publisher.
	 * @param logo A link to the publisher's logo
	 * @return The registered publisher.
	 */
	Publisher register(String name, String logo);
	
	/**
	 * Get a list of all available publishers.
	 * @return The list of all publishers.
	 */
	List<Publisher> getAllPublishers();
	
	/**
	 * Get a list of all available publishers with their lists of games filled in.
	 * @return The list of all publishers.
	 */
	List<Publisher> getAllPublishersWithGames();
	
	/**
	 * Get a list of all games available by the publisher
	 * @param pub	The publisher
	 * @return		The list of games
	 */
	List<Game> getAllGames(Publisher pub);
}
