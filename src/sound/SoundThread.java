package sound;

/**
 * The SoundThread class provides a way of being able to play or loop
 * a specified sound by assigning it to a ThreadPool and then running it
 * on that ThreadPool
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class SoundThread extends Thread implements Runnable {
	/**
	 * Variable for storing the name of the sound
	 */
	private transient final String soundName;
	
	/**
	 * Pointer to the location of the SoundCache currrently in memory
	 */
	private transient final SoundCache soundCache;
	
	/**
	 * Flag to specify whether the sound is looped or not
	 */
	private transient final boolean loopSound;
	
	/**
	 * Constructor for the class SoundThread
	 * @param	sName	the String reference to the name of the file to be played
	 * @param	sCache	the SoundCache from which the sound is loaded from
	 * @param	loop	specifies whether or not the sound is looped or played once
	 */
	public SoundThread(String sName, SoundCache sCache, boolean loop) {
		super();
		
		// Set this thread's priority before it is started
		this.setPriority(Thread.MIN_PRIORITY);
		this.setDaemon(true);
		
		// Assign variables from parameters passed
		soundName = sName;
		soundCache = sCache;
		loopSound = loop;
	}
	
	/**
	 * Runs this SoundThread. If looping the sound is set to true,
	 * then the sound continues to play until the SoundThread is interrupted
	 * or killed.
	 */
	public void run() {
		// If the sound is to be looped
		if(loopSound) {
			// Then play the sound repeatedly
			soundCache.getAudioClip(soundName).loop();
		} else {
			// Otherwise, just play once
			soundCache.getAudioClip(soundName).play();
		}
	}
}
