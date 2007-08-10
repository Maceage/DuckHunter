package config;

/**
 * The GameMode class provides the default implementation for the game modes
 * used within the game. This includes information such as the current
 * level/round, the amount of shots available to the player and the
 * amount of ducks to display on the screen. 
 * @author	Graham Mace
 * @version	1.0
 */
public abstract class AbstractGameMode {
	/**
	 * The game difficulty level
	 */
	protected int difficultyLevel = 0;
	
	/**
	 * The game's current round number
	 */
	protected int roundNumber = 0;
	
	/**
	 * The amount of ammo available per round
	 */
	protected int ammoCount = 0;
	
	/**
	 * The amount of Ducks to spawn per round
	 */
	protected int duckCount = 0;
	
	/**
	 * The amount of lives available to the player per game
	 */
	protected int lifeCount = 0;
	
	/**
	 * The time limit per round for this game mode
	 */
	protected long timeLimit = 0;
	
	/**
	 * The state of the score shot modifier
	 */
	protected boolean shotModifier = false;
	
	/**
	 * The name of this game mode
	 */
	protected transient String modeName;
	
	/**
	 * The description of the game mode
	 */
	protected transient String modeDescription;
	
	/**
	 * Default constructor for all child subclasses - this simply calls
	 * the resetGame method on the class when it is created
	 */
	public AbstractGameMode() {
		resetGame();
	}
	
	/**
	 * Method that must be implemented by child classes. This method is called when the 
	 * child class is instantiated and when the player has lost all their lives and 
	 * is at the 'Game Over' screen.
	 */
	public abstract void resetGame();
	
	/**
	 * Method that must be implemented by child classes. This method is called after every
	 * round has finished - it is up to the child class as to what variables or options are 
	 * changed for the next round.
	 */
	public abstract void updateState();
	
	/**
	 * Gets the current difficulty level for this GameMode
	 * @return	the current difficulty
	 */
	public int getDifficultyLevel() { return difficultyLevel; }
	
	/**
	 * Sets the difficulty level for this GameMode
	 * @param	level	the level of difficulty to set
	 */
	public void setDifficultyLevel(final int level) { difficultyLevel = level; }
	
	/**
	 * Gets the current round number for this GameMode
	 * @return	the current round number
	 */
	public int getRoundNumber() { return roundNumber; }
	
	/**
	 * Sets the round number for this GameMode
	 * @param	round	the round number to set
	 */
	public void setRoundNumber(final int round) { roundNumber = round; } 		
	
	/**
	 * Gets the current maximum shot count for this GameMode
	 * @return	the current maximum ammo count
	 */
	public int getAmmoCount() { return ammoCount; }
	
	/**
	 * Sets the maximum shot count for this GameMode
	 * @param	sCount	the maximum shot count to set
	 */
	public void setAmmoCount(final int sCount) { ammoCount = sCount; }
	
	/**
	 * Gets the current amount of Ducks to be spawned per round for this GameMode
	 * @return	the amount of Ducks to be spawned
	 */
	public int getDuckCount() { return duckCount; }
	
	/**
	 * Sets the amount of Ducks to be spawned per round for this GameMode
	 * @param	dCount	the amount of Ducks per round to be set
	 */
	public void setDuckCount(final int dCount) { duckCount = dCount; }
	
	/**
	 * Gets the amount of lives available to the player for this GameMode
	 * @return	the amount of lives currently available
	 */
	public int getLifeCount() { return lifeCount; }
	
	/**
	 * Sets the amount of lives available to the player for this GameMode
	 * @param	lCount	the amount of lives to be set
	 */
	public void setLifeCount(final int lCount) { lifeCount = lCount; }
	
	/**
	 * Returns whether or not the shot modifier is being used by this GameMode.
	 * The shot modifier specifies whether or not the Player gets less points depending
	 * on how many shots they have currently taken
	 * @return	whether or not the shot modifier is enabled
	 */
	public boolean getShotModifier() { return shotModifier; }
	
	/**
	 * Sets whether or not the shot modifier is to be used by this GameMode.
	 * @param	sModifier	whether or not the shot modifier is used
	 */
	public void setShotModifier(final boolean sModifier) { shotModifier = sModifier; }
	
	/**
	 * Returns whether this GameMode has a time limit
	 */
	public boolean hasTimeLimit() { return (timeLimit > 0); }
	
	/**
	 * Sets the time limit for this GameMode
	 * @param	time	the amount of time in milliseconds per round
	 */
	public void setTimeLimit(long time) { timeLimit = time; }
	
	/**
	 * Returns the time limit per round for this GameMode
	 */
	public long getTimeLimit() { return timeLimit; }
	
	/**
	 * Returns the name of this GameMode
	 * @return	the String representation of this GameMode.
	 */
	public String getName() { return modeName; }
	
	/**
	 * Sets the name of this GameMode
	 * @param	name	the name to set this GameMode to
	 **/
	public void setName(final String name) { modeName = name; } 
	
	/**
	 * Returns the description for this GameMode
	 * @return	the String representation of this GameMode.
	 */
	public String getDescription() { return modeDescription; }
	
	/**
	 * Sets the description for this GameMode
	 * @param	desc	the description to add to this GameMode
	 **/
	public void setDescription(final String desc) { modeDescription = desc; } 
}
