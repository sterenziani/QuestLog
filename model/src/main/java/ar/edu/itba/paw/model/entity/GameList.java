package ar.edu.itba.paw.model.entity;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GameList implements Set<Game>
{
	String name;
	Set<Game> games;
	
	public GameList(String name)
	{
		this.name = name;
		games = new HashSet<Game>();
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String newName)
	{
		name = newName;
	}
	
	@Override
	public boolean add(Game g)
	{
		return games.add(g);
	}

	@Override
	public boolean addAll(Collection<? extends Game> c)
	{
		return games.addAll(c);
	}

	@Override
	public void clear()
	{
		games.clear();
	}

	@Override
	public boolean contains(Object o)
	{
		return games.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return games.containsAll(c);
	}

	@Override
	public boolean isEmpty()
	{
		return games.isEmpty();
	}

	@Override
	public Iterator<Game> iterator()
	{
		return games.iterator();
	}

	@Override
	public boolean remove(Object o)
	{
		return games.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return games.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return games.retainAll(c);
	}

	@Override
	public int size()
	{
		return games.size();
	}

	@Override
	public Object[] toArray()
	{
		return games.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return games.toArray(a);
	}
}
