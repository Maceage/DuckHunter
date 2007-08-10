package config.gamemodes;

import config.AbstractGameMode;

/**
 * The 'Classic Quacks*2' GameMode. This mode adds two Ducks to the screen
 * and increments the difficulty level by one each round that is completed.
 * The player has 3 shots and 2 Ducks to shoot and 30 seconds in which to kill them.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class ClassicMode2 extends AbstractGameMode {
	/**
	 * Resets the initial variables for this GameMode
	 */
	public void resetGame() {
		this.setDifficultyLevel(1);
		this.setRoundNumber(1);
		this.setAmmoCount(3);
		this.setDuckCount(2);
		this.setLifeCount(3);
		this.setTimeLimit(30000);
		this.setShotModifier(false);
		this.setName("Classic Quacks*2");
		this.setDescription("3 shots, 2 ducks, 5 rounds per level, 3 lives & 30 second time limit");
	}
	
	/**
	 * Update's this GameMode's state - in this case only the difficulty level
	 * and round number are incremented.
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