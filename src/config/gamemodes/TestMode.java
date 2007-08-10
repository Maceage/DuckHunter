package config.gamemodes;

import config.AbstractGameMode;

/**
 * A generic 'Test Mode' GameMode that is used only when debug mode 
 * has been activated in GameOptions. This GameMode gives the player 5
 * shots, one Duck and only 1 round per level.
 * NOTE: This GameMode should only be available when the game is run with
 * the -dev command-line switch.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class TestMode extends AbstractGameMode {
	/**
	 * Resets the initial variables for this GameMode
	 */
	public void resetGame() {
		this.setDifficultyLevel(1);
		this.setRoundNumber(1);
		this.setAmmoCount(5);
		this.setDuckCount(1);
		this.setLifeCount(1);
		this.setTimeLimit(60000);
		this.setShotModifier(false);
		this.setName("Test Mode");
		this.setDescription("Special mode for testing");
	}
	
	/**
	 * Update's this GameMode's state - in this case only the difficulty level
	 * is incremented once the specified number of rounds has been completed.
	 */
	public void updateState() {
		if(roundNumber >= 5) {
			roundNumber = 1;
			difficultyLevel++;
		} else {
			roundNumber++;
		}
	}
}