package graphics;

import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

import util.AbstractResourceCache;

/**
 * The SpriteCache class is an implementation of the ResourceCache
 * that manages the loading and storage of Image files used in the game.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 * @see	AbstractResourceCache
 */
public class SpriteCache extends AbstractResourceCache {
	/**
	 * Loads the specified resource into the cache.
	 * @param	url	the URL of the resource to be loaded
	 */
	protected Object loadResource(final URL url) {
		// Create our generic object
		Object obj = null;
		
		// Attempt to load the file from disk/network
		try {
			obj = ImageIO.read(url);
		} catch(Exception e) {
			System.out.println("Cannot read file " + url);
			System.out.println("Error at: " + e.getClass().getName() + " " + e.getMessage());
			System.exit(0);
		}
		
		// Return the object
		return obj;
	}
	
	/**
	 * Returns the specified Image from the filename given.
	 * 
	 * @param	name	the path containing the Image file to be loaded
	 * @return	the image file requested
	 */
	public BufferedImage getSprite(final String name) {
		return (BufferedImage)getResource(name);
	}
}