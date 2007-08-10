package config;

import java.io.Serializable;

/**
 * The GameOptions class stores all the Game's various options such as whether debug
 * mode, the FPS display, sound effects or decal limiter are to be used by the game 
 * @author	Graham Mace
 * @version	1.0
 */
public class GameOptions implements Serializable {
	// Version ID for when class is serialized
	private static final long serialVersionUID = 1L;
	
	/**
	 * The state of debug mode
	 */
	private boolean debugMode;
	
	/**
	 * The state of the FPS display
	 */
	private boolean fpsDisplay;
	
	/**
	 * The state of decals
	 */
	private boolean decalsEnabled;
	
	/**
	 * The state of the decal limiter
	 */
	private boolean decalLimitEnabled;
	
	/**
	 * The state of sound effects
	 */
	private boolean soundEnabled;
	
	/**
	 * The state of ambient sound effects
	 */
	private boolean soundAmbience;
	
	/**
	 * The state of gunshot sound effects
	 */
	private boolean soundShot;
	
	/**
	 * The state of duck sound effects
	 */
	private boolean soundDuck;
	
	/**
	 * Creates a new instance of the GameOptions class with the specified option parameters.
	 * @param	dMode	whether or not debug mode is active
	 * @param 	fDisplay	whether or not FPS is displayed
	 * @param 	sEnabled	whether or not sound is enabled
	 * @param 	sAmbience	whether or not ambience sound is enabled
	 * @param 	sShot	whether or not gunshot sound is enabled
	 * @param 	sDuck	whether or not the duck sound is enabled
	 * @param 	dEnabled	whether or not decals are enabled
	 * @param 	dLimit	whether or not the decal limiter is enabled
	 */
	public GameOptions(boolean dMode, boolean fDisplay, boolean sEnabled, boolean sAmbience, 
			boolean sShot, boolean sDuck, boolean dEnabled, boolean dLimit) {
		// Set debug variables
		debugMode = dMode;
		fpsDisplay = fDisplay;
		decalsEnabled = dEnabled;
		decalLimitEnabled = dLimit;
		
		// Sound sound variables
		soundEnabled = sEnabled;
		soundAmbience = sAmbience;
		soundShot = sShot;
		soundDuck = sDuck;
	}
	
	/**
	 * Returns whether debug mode is enabled
	 * @return	current debug mode state
	 */
	public boolean getDebugMode() { return debugMode; }
	
	/**
	 * Sets whether debug mode is enabled
	 * @param	dMode	the debug mode state to set
	 */
	public void setDebugMode(final boolean dMode) { debugMode = dMode; }
	
	/**
	 * Returns whether FPS display is enabled
	 * @return	current FPS display state
	 */
	public boolean getFpsDisplay() { return fpsDisplay; }
	
	/**
	 * Sets whether the FPS display is enabled
	 * @param	fDisplay	the FPS display state to set
	 */
	public void setFpsDisplay(final boolean fDisplay) { fpsDisplay = fDisplay; }
	
	/**
	 * Returns whether the sound effects are enabled
	 * @return	current sound effects state
	 */
	public boolean getSoundEnabled() { return soundEnabled; }
	
	/**
	 * Sets whether the sound effects are enabled or not
	 * @param	sEnabled	the sound effects state to set
	 */
	public void setSoundEnabled(final boolean sEnabled) { soundEnabled = sEnabled; }
	
	/**
	 * Returns whether or not ambient sound effects are enabled
	 * @return	current ambient sound effects state
	 */
	public boolean getSoundAmbience() { return soundAmbience; }
	
	/**
	 * Sets whether or not the ambient sound effects are enabled
	 * @param	sAmbience	the ambient sound effects state to set
	 */
	public void setSoundAmbience(final boolean sAmbience) { soundAmbience = sAmbience; }
	
	/**
	 * Returns whether or not the gunshot sound effects are enabled
	 * @return	current gunshot sound effects state
	 */
	public boolean getSoundShot() { return soundShot; }
	
	/**
	 * Sets whether or not the gunshot sound effects are enabled
	 * @param	sShot	the gunshot sound effects state to set
	 */
	public void setSoundShot(final boolean sShot) { soundShot = sShot; }
	
	/**
	 * Returns whether or not the duck sound effects are enabled
	 * @return	current duck sound effects state
	 */
	public boolean getSoundDuck() { return soundDuck; }
	
	/**
	 * Sets whether or not the duck sound effects are enabled
	 * @param	sDuck	the duck sound effects state to set
	 */
	public void setSoundDuck(final boolean sDuck) { soundDuck = sDuck; }
	
	/**
	 * Returns whether graphic decals such as blood & bullet holes in-game are enabled
	 * @return	current graphic decals state
	 */
	public boolean getDecalsEnabled() { return decalsEnabled; }
	
	/**
	 * Sets whether graphic decals such as blood & bullet holes in-game are enabled
	 * @param	dEnabled	the graphic decals state to set	
	 */
	public void setDecalsEnabled(final boolean dEnabled) { decalsEnabled = dEnabled; }
	
	/**
	 * Returns whether the decal limiter in-game is enabled
	 * @return	current decal limiter state
	 */
	public boolean getDecalLimitEnabled() { return decalLimitEnabled; }
	
	/**
	 * Sets whether the decal limiter in-game is enabled
	 * @param	dLimit	the decal limiter state to set
	 */
	public void setDecalLimitEnabled(final boolean dLimit) { decalLimitEnabled = dLimit; }
	
	/**
	 * Returns a String value based on the boolean passed. Simply converts a boolean
	 * variable to a "Yes" or "No" answer.
	 * @param	bool	the boolean value to check
	 * @return	Either "Yes" or "No", depending on the boolean value
	 */
	private String booleanToString(final boolean bool) {
		String returnString = null;
		if(bool) { returnString = "Yes"; } else { returnString = "No"; }
		return returnString;
	}
	
	/**
	 * Returns a String representation of this GameOptions object.
	 */
	public String toString() {
		return ("Debug: " + booleanToString(debugMode)
				+ "\nFPS: " + booleanToString(fpsDisplay)
				+ "\nDecals: " + booleanToString(decalsEnabled)
				+ "\nDecal Limiter: " + booleanToString(decalLimitEnabled)
				+ "\nSound: " + booleanToString(soundEnabled)
				+ "\nSound-Ambience: " + booleanToString(soundAmbience)
				+ "\nSound-Shotgun: " + booleanToString(soundShot)
				+ "\nSound-Duck: " + booleanToString(soundDuck));
	}
}
