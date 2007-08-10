package config.gamemodes;

import config.AbstractGameMode;

/**
 * The 'Aduckalypse Now' GameMode. This mode adds lots of Ducks to the screen
 * and increments the difficulty level by one after every 5 rounds. 
 * The Player has 20 shots and 15 ducks to shoot and there is no time limit.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class Aduckalypse extends AbstractGameMode {
	/**
	 * Resets the initial variables for this GameMode
	 */
	public void resetGame() {
		this.setDifficultyLevel(1);
		this.setRoundNumber(1);
		this.setAmmoCount(20);
		this.setDuckCount(15);
		this.setLifeCount(3);
		this.setTimeLimit(0);
		this.setShotModifier(false);
		this.setName("Aduckalypse Now");
		this.setDescription("20 shots, 15 ducks, 5 rounds per level, 3 lives & no time limit");
	}
	
	/**
	 * Update's this GameMode's state - in this case only the difficulty level
	 * and round number are incremented.
	 */
	public void updateState() {
		// Set the time limit based on the current round number
		if(roundNumber >= 5) {
			difficultyLevel++;
			roundNumber = 1;
		} else {
			roundNumber++;
		}
	}
}