package graphics;

import sound.SoundCache;

import java.awt.image.ImageObserver;

/**
 * The DisplayView interface extends the ImageObserver interface to provide a 
 * set of common properties and methods for the game's display area, sound effects
 * and graphics.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 **/
public interface DisplayView extends ImageObserver {
	/**
	 * The width of the DisplayView
	 */
	int WIDTH = 1024;
	
	/**
	 * The height of the DisplayView
	 */
	int HEIGHT = 768;
	
	/**
	 * The amount of time to sleep per render pass
	 */
	int THREAD_WAIT_TIME = 17;
	
	/**
	 * Returns the current SpriteCache object
	 * @return	the current SpriteCache
	 */
	SpriteCache getSpriteCache();
	
	/**
	 * Returns the current SoundCache object 
	 * @return	the current SoundCache
	 */
	SoundCache getSoundCache();
	
}