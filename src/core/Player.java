package core;

import java.io.Serializable;

/**
 * The Player class represents the variables associated with the user when they are playing the game.
 * This class stores options such as the user's name, score, lives and shot count.
 * @author Mace
 * @version	1.0 - 03/06/2006
 */
public class Player implements Serializable {
	/**
	 * Constant variable for serailization version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The name of the Player
	 */
	private String playerName;
	
	/**
	 * The score of the Player
	 */
	private transient int playerScore;
	
	/**
	 * The lives available to the Player
	 */
	private transient int playerLives;
	
	/**
	 * Stores the total amount of lives available to the Player
	 */
	private transient int totalLives;
	
	/**
	 * The amount of shots available to the player
	 */
	private int shotCount;
	
	/**
	 * Stores the total amount of shots available to the Player
	 */
	private transient final int totalShotCount;
	
	/**
	 * Constructor for the class Player
	 * @param	name	the name of the Player
	 * @param 	startingShots	the amount of shots the Player starts with
	 * @param 	initialLives	the amount of lives the Player starts with
	 */
	public Player(String name, int startingShots, int initialLives) {
		// Set up passed parameters and initialize variables
		playerName = name;
		playerScore = 0;
		totalShotCount = startingShots;
		shotCount = totalShotCount;
		totalLives = initialLives;
		playerLives = totalLives;
	}
	
	/**
	 * Sets this Player's name to the specified String
	 * @param	name	the name of the Player to set
	 */
	public void setPlayerName(final String name) {
		playerName = name;
	}
	
	/**
	 * Returns this Player's current name
	 * @return	this Player's name
	 */
	public String getPlayerName() {
		return playerName;
	}
	
	/**
	 * This method decrements the amount of lives the Player currently
	 * has, which is called when the Player runs out of shots or time
	 */
	public void loseLife() {
		playerLives--;
	}
	
	/**
	 * Sets the amount of lives available to this Player
	 * @param	lives	the amount of lives to set
	 */
	public void setLives(final int lives) {
		totalLives = lives;
	}
	
	/**
	 * Returns this Player's current amount of lives
	 * @return	this Player's amount of lives
	 */
	public int getLives() {
		return playerLives;
	}
	
	/**
	 * Resets the amount of lives this Player currently has.
	 * This method simply sets the current amount of lives to
	 * the total amount of lives specified when the class was instantiated.
	 */
	public void resetLives() {
		playerLives = totalLives;
	}
	
	/**
	 * Returns whether or not this Player currently has any lives left or not
	 * @return	whether this Player has lives left
	 */
	public boolean hasLives() {
		return playerLives > 0;
	}
	
	/**
	 * Sets the amount of shots available to this Player. The amount
	 * of shots available cannot exceed the amount specified when this
	 * class was instantiated.
	 * @param	count	the amount of shots to set
	 */
	public void setShotCount(final int count) {
		if(count <= totalShotCount) {
			shotCount = count;
		}
	}
	
	/**
	 * Method that is called on this Player when they hit a Duck
	 */
	public void hitDuck() {
		shotCount--;
	}
	
	/**
	 * Method that is called on this Player when they miss a Duck
	 */
	public void missedDuck() {
		shotCount--;
	}
	
	/**
	 * Resets the amount of shots this Player currently has to the
	 * total shot count specified when this class was instantiated.
	 */
	public void resetShots() {
		shotCount = totalShotCount;
	}
	
	/**
	 * Returns the current amount of shots available to this Player
	 * @return	the amount of shots available
	 */
	public int getShotCount() {
		return shotCount;
	}
	
	/**
	 * Returns the current total amount of shots available to this Player
	 * @return	the total amount of shots available
	 */
	public int getTotalShotCount() {
		return totalShotCount;
	}
	
	/**
	 * Returns this Player's current score
	 * @return	the Player's score
	 */
	public int getScore() {
		return playerScore;
	}
	
	/**
	 * Resets this Player's score to zero
	 */
	public void resetScore() {
		playerScore = 0;
	}
	
	/**
	 * Adds the specified score to this Player
	 * @param	value	the points to be added
	 */
	public void addScore(final int value) {
		playerScore += value;
	}
}
