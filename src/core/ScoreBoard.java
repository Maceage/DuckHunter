package core;

import java.io.*;
import java.util.ArrayList;

/**
 * The ScoreBoard class is responsible for maintaining the in-game player
 * high scores. When the player finishes a game, their score is checked
 * against the current list of high scores and added to the list if their
 * score is higher than a previous one.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class ScoreBoard implements Serializable {
	/**
	 * Constant variable for serailization version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Declare ArrayList for storing player scores 
	 */
	private transient final ArrayList playerScores;
	
	/**
	 * Constructor for the class ScoreBoard
	 */
	public ScoreBoard() {
		// Create new ArrayList
		playerScores = new ArrayList();
	}
	
	/**
	 * Adds the specified Player to this scoreboard. If their score is higher
	 * than any of the player's currently in the list then they are inserted
	 * above that player.
	 * @param	playerToAdd	the Player to add to the board
	 */
	public void addPlayer(final Player playerToAdd) {
		Player tempPlayer = null;
		// Check a null variable has not been passed
		if(playerToAdd != null) {
			// If there are already players with scores...
			if((playerToAdd.getScore() > 0) && (playerScores.size() != 0)) {
				System.out.println("Adding player " + playerToAdd.getPlayerName() + " to score table");
				// Find the player with the score higher than the player to be added
				for(int i = 0; i < playerScores.size(); i++) {
					tempPlayer = this.getPlayer(i);;
					if(playerToAdd.getScore() >= tempPlayer.getScore()) {
						playerScores.add(i, playerToAdd);
						break;
					}
				}
			} else {
				// Otherwise, just add the player to the list
				playerScores.add(playerToAdd);
			}
			
			// Make sure the list isn't longer than 10 players
			while(playerScores.size() > 10) {
				playerScores.remove(playerScores.size() - 1);
			}
		}
	}
	
	/**
	 * Returns the Player at the specified index
	 */
	public Player getPlayer(int index) {
		if((playerScores.size() > 0) && (index < playerScores.size())) {
			return (Player)playerScores.get(index);
		} else {
			return null;
		}
	}
}
