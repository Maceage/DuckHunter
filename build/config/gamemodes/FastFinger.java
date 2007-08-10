package config.gamemodes;

import config.AbstractGameMode;

/**
 * The 'Fast Draw McDuck' GameMode. This mode adds lots of Ducks to the screen
 * and increments the difficulty level by one each round that is completed. 
 * Same as the 'Aduckalypse Now' GameMode except the shot modifier is used to 
 * decrease the amount of points awarded per Duck. The player has 30 shots and
 * 15 Ducks to shoot - each round the time is decremented by 15 seconds up to a
 * maximum of 5 rounds, after which the difficulty is incremented.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class FastFinger extends AbstractGameMode {
	/**
	 * Resets the initial variables for this GameMode
	 */
	public void resetGame() {
		this.setDifficultyLevel(1);
		this.setRoundNumber(1);
		this.setAmmoCount(30);
		this.setDuckCount(15);
		this.setLifeCount(5);
		this.setTimeLimit(90000);
		this.setShotModifier(true);
		this.setName("Fast Draw McDuck");
		this.setDescription("30 shots, 15 ducks, 5 rounds per level, 3 lives & " +
				"1 minute time limit minus 15 seconds each round");
	}
	
	/**
	 * Update's this GameMode's state - in this case only the difficulty level
	 * and round number are incremented.
	 */
	public void updateState() {
		// Set the time limit based on the current round number
		roundNumber++;
		switch(roundNumber) {
			case 1:
				// Set time limit to 90 seconds
				this.setTimeLimit(90000);
				break;
			case 2:
				// Set time limit to 75 seconds
				this.setTimeLimit(75000);
				break;
			case 3:
				// Set time limit to 60 seconds
				this.setTimeLimit(60000);
				break;
			case 4:
				// Set time limit to 45 seconds
				this.setTimeLimit(45000);
				break;
			case 5:
				// Set time limit to 30 seconds
				this.setTimeLimit(30000);
				break;
			default:
				// Reset round number and increment difficulty
				this.setTimeLimit(90000);
				roundNumber = 1;
				difficultyLevel++;
				break;
		}
	}
}