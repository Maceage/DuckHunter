package config.gamemodes;

import config.AbstractGameMode;

/**
 * The 'Classic Quacks*2' GameMode. This mode adds two Ducks to the screen
 * and increments the difficulty level by one each round that is completed.
 * The player has 3 shots and 1 Duck to shoot and 15 seconds in which to kill them.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class ClassicMode extends AbstractGameMode {
	/**
	 * Resets the initial variables for this GameMode
	 */
	public void resetGame() {
		this.setDifficultyLevel(1);
		this.setRoundNumber(1);
		this.setAmmoCount(3);
		this.setDuckCount(1);
		this.setLifeCount(3);
		this.setTimeLimit(15000);
		this.setShotModifier(false);
		this.setName("Classic Quacks");
		this.setDescription("3 shots, 1 duck, 5 rounds per level, 3 lives & 15 second time limit");
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
