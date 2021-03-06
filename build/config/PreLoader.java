package config;

import graphics.SpriteCache;
import sound.SoundCache;
import util.FileLister;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * The PreLoader class generates a SpriteCache and SoundCache either from the list of
 * resources specified in a file or the files in a directory structure on disk.
 * @author	Graham Mace
 * @version	1.0
 */
public class PreLoader {
	/**
	 * Pointer to the location of the SpriteCache generated
	 **/
	private final transient SpriteCache spriteCache;
	
	/**
	 * Pointer to the location of the SoundCache generated
	 */
	private final transient SoundCache soundCache;
	
	/**
	 * Logger for any errors
	 */
	private static final transient Logger log = Logger.getLogger(PreLoader.class.getName());
	
	/**
	 * Creates an instance of the PreLoader class for use
	 */
	public PreLoader() {
		// Create the caches
		spriteCache = new SpriteCache();
		soundCache = new SoundCache();
	}
	
	/**
	 * Loads all resources using the specified resources file. All files listed in the
	 * resources file are read line-by-line and checked. The corresponding caches are then
	 * generated and can be obtained through the getSoundCache and getSpriteCache methods.
	 * @param resFile	the file containing the list of resources to be cached
	 */
	public void loadResourcesByFile(final String resFile) {
		// Check the resources file is not empty or null
		if((resFile == null) || (resFile == "")) {
			throw new IllegalArgumentException();
		}
		
		// Create temporary variables
		final ArrayList resList = new ArrayList();
		String resourceName = null;
		
		// Attempt to load the resources
		try {
			// Get the path to the resources file
			final URL resFileURL = ClassLoader.getSystemResource(resFile);
			
			// Open the resources file buffer
			resFileURL.openStream();
			final BufferedReader fileIn = new BufferedReader(new InputStreamReader(resFileURL.openStream()));
			
			// Check the file is ready
			if(!fileIn.ready()) {
				throw new IOException();
			}
			
			// Loop through each line in file and add to resources list
			resourceName = fileIn.readLine();
			while(resourceName != null) {
				resList.add(resourceName);
				resourceName = fileIn.readLine();
			}
		    
			// Close stream
			fileIn.close();
			
			// Generate caches
			cacheResources(resList);
			
		} catch(IOException e) {
			// Print error and exit if resources could not be loaded
			log.fine("Could not cache resources - " + e);
			System.exit(1);
		}

	}
	
	/**
	 * Loads all resources from the specified directory. All files in the subdirectories
	 * of the directory specified are loaded into a list. The corresponding caches are then
	 * generated and can be obtained through the getSoundCache and getSpriteCache methods.
	 * @param	resourcePath	the directory containing the resources to be cached
	 */
	public void loadResourcesByDirectory(final String resourcePath) {
		// Check the path supplied is not empty or null
		if((resourcePath == null) || (resourcePath == "")) {
			throw new IllegalArgumentException();
		}
		
		// Create and initialize our temporary variables
		final ArrayList resList = new ArrayList();
		String resourceName = null;
		
		// Attempt to load the resources
		try {
			final URL resourceLocation = getClass().getClassLoader().getResource(resourcePath);
			final File resourceFile = new File(resourceLocation.getFile());
			
			// Get the list of files
			final List filesList = FileLister.getFileListing(resourceFile);
			
			// Loop through the list of files and add them to the resources list
			final Iterator filesIter = filesList.iterator();
		    while(filesIter.hasNext()){
		      resourceName = (String)filesIter.next();
		      resList.add(resourceName);
		    }
			
		    // Generate caches
			cacheResources(resList);
			
		} catch(IOException e) {
			// Print error if resources could not be loaded 
			log.fine("Could not cache resources - " + e);
			System.exit(1);
		}

	}
	
	/**
	 * Loads the resources into this PreLoader's cache ready for retrieval
	 * @param 	resList		the list of resources to load
	 */
	private void cacheResources(final ArrayList resList) {
		// Loop through each file, checking the extension and preloading it
		String resName = null;
		String checkString = null;
		for(int i = 0; i < resList.size(); i++) {
			resName = (String)resList.get(i);
			
			if((resName != null) && (resName.length() != 0)) {
				// Get extension of file
				checkString = resName.substring(resName.length() - 4, resName.length());
				
				// If the file is a media file, then load it into the appropriate cache
				if(".png".equals(checkString)) {
					spriteCache.getSprite(resName);
				} else if(".wav".equals(checkString)) {
					soundCache.loadSound(resName);
				}
			}
		}
	}
	
	/**
	 * Returns the SpriteCache generated by this PreLoader
	 * @return	the generated SpriteCache
	 * @see		SpriteCache
	 */
	public SpriteCache getSpriteCache() {
		return spriteCache;
	}
	
	/**
	 * Returns the SoundCache generated by this PreLoader
	 * @return	the generated SoundCache
	 * @see		SoundCache
	 */
	public SoundCache getSoundCache() {
		return soundCache;
	}
}
