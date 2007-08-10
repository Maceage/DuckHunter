package sound;

import util.AbstractResourceCache;
import util.ThreadPool;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import util.Randomizer;

/**
 * The SoundCache class is an implementation of the ResourceCache
 * that manages the loading and storage sound files used in the game.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 * @see	AbstractResourceCache
 */
public class SoundCache extends AbstractResourceCache implements Serializable {
	
	/**
	 * Constant variable for version ID used for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Whether or not sounds are to be played
	 */
	private transient boolean playSounds;
	
	/**
	 * The ThreadPool onto which all sounds are run
	 */
	private transient ThreadPool sfxPool;
	
	/**
	 * Loads a sound from the specified resource
	 * @param	url	the URL of the sound to load
	 */
	protected Object loadResource(final URL url) {
		return Applet.newAudioClip(url);
	}
	
	/**
	 * Gets a sound from the specified resource
	 * @param	name	the location of the sound
	 * @return	the AudioClip object for the sound
	 */
	public AudioClip getAudioClip(final String name) {
		return (AudioClip)getResource(name);
	}
	
	/**
	 * Creates a new SoundThread for playing sounds. A random sound is chosen from
	 * the soundList parameter, created in a SoundThread and added to the pool. 
	 * @param	soundList	the ArrayList containing the specified sounds
	 * @param	loopSound	whether or not to loop the sound
	 */
	private void createThread(final ArrayList soundList, final boolean loopSound) {
		if((playSounds) && (soundList.size() >= 0)) {
			int randomNum = 0;
			
			// Check that the soundList passed is not equal or less than zero
			if(soundList.size() <= 0) {
				randomNum = 0;
			} else {
				// Create a random number for picking a sound in the list
				final Randomizer randomizer = new Randomizer();
				randomizer.randomNum(0, soundList.size() - 1);
				randomNum = randomizer.getCurrentNumber();
			}
			
			// Get the fileName from the soundList
			final String fileName = (String)soundList.get(randomNum);
			
			// Create the thread and run it on the pool
			final SoundThread soundThread = new SoundThread(fileName, this, loopSound);
			soundThread.setDaemon(true);
			soundThread.setPriority(Thread.MIN_PRIORITY);
			sfxPool.runTask(soundThread);
		}
	}
	
	/**
	 * Plays a random sound from the specified soundList
	 * @param	soundList	the ArrayList containing the sounds
	 */
	public void playSound(final ArrayList soundList) {
		this.createThread(soundList, false);
	}
	
	/**
	 * Loops a random sound from the specified soundList
	 * @param	soundList	the ArrayList containing the sounds
	 */
	public void loopSound(final ArrayList soundList) {
		this.createThread(soundList, true);
	}
	
	/**
	 * Stops all sounds contained in the specified soundList
	 * @param	soundList	the ArrayList containing the sounds to be stopped
	 */
	public void stopAllSounds(final ArrayList soundList) {
		// Loop through the list and stop all the sounds
		for(int i = 0; i < soundList.size(); i++) {
			this.getAudioClip((String)soundList.get(i)).stop();
		}
	}
	
	/**
	 * Stops the specified sound
	 * @param	name	the name of the sound to be stopped
	 */
	public void stopSound(final String name) {
		if(playSounds) {
			this.getAudioClip(name).stop();
		}
	}
	
	/**
	 * Load the specified sound
	 * @param name	the name of the sound to be loaded
	 */
	public void loadSound(final String name) {
		this.getAudioClip(name);
	}
	
	/**
	 * Globally enables all sound
	 * @param	play	whether or not to play all sounds
	 */
	public void enableSound(final boolean play) {
		playSounds = play;
	}
	
	/**
	 * The ThreadPool onto which sounds are run
	 * @param	soundPool	the ThreadPool to run the SoundThreads on
	 */
	public void setPool(final ThreadPool soundPool) {
		sfxPool = soundPool;
	}
	
	/**
	 * Gets the current ThreadPool assigned to this SoundCache for playing sounds
	 * @return	the ThreadPool for this SoundCache
	 */
	public ThreadPool getPool() {
		return sfxPool;
	}
}
