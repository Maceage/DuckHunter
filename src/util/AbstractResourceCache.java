package util;

import java.net.URL;
import java.util.HashMap;

/**
 * The ResourceCache class defines a skeleton class for loading
 * and storing resources used in the game. The resource can then be accessed
 * using the getResource method and specifying the name and path of the resource.
 * If a particular file cannot be found in the cache, then it is read
 * and loaded into the cache for future reference.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public abstract class AbstractResourceCache {
	/**
	 * The HashMap containing the resources for this class
	 */
	protected HashMap resources;
	
	/**
	 * Constructor for the class AbstractResourceCache
	 */
	public AbstractResourceCache() {
		// Creates a new HashMap for this class
		resources = new HashMap();
	}
	
	/**
	 * Loads the specified resource from disk/network
	 * @param	name	the file path of the resource filename to load
	 * @return	the Object representation of the resource requested
	 */
	protected Object loadResource(final String name) {
		// Attempt to load the resource by calling the current
		// class' classloader and getResource method
		System.out.println("Load: " + name);
		URL url = null;
		url = getClass().getClassLoader().getResource(name);
		return loadResource(url);
	}
	
	/**
	 * Returns the specified resource from the ResourceCache.
	 * If the resource cannot be found, then it is loaded from
	 * disk/network and put into the resource HashMap for later use.
	 * @param	name	the filepath of the resource to be loaded
	 * @return	the Object representation of the resource requested
	 */
	protected Object getResource(final String name) {
		// Attempt to get the object from the resources HashMap
		Object res = resources.get(name);
		if(res == null) {
			// If the resource cannot be found, then load it
			// and store in the resources HashMap
			res = loadResource(name);
			resources.put(name, res);
		}
		return res;
	}
	
	/**
	 * Method that should be overridden by classes that extend from the
	 * ResourceCache class
	 * @param	url	the URL from which the resource is to be loaded
	 * @return	the Object representation of the resource requested
	 */
	protected abstract Object loadResource(URL url);
}
