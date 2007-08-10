package core.sprites;

import graphics.Animation;
import graphics.DisplayView;
import graphics.Sprite;
import graphics.AnimGenerator;
import util.Randomizer;

/**
 * The Duck class represents a Duck on the screen. It has a simple AI in that it will
 * move constantly at the velocities specified. The Duck also has a percentage chance of changing
 * direction, which is specified by the DIR_CHANGE_CHANCE constant.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class Duck extends Sprite {
	
	/**
	 * The AnimGenerator object that is responsible for loading the Sprites
	 * for this Duck.
	 */
	private transient AnimGenerator animationLoader;
	
	/**
	 * Animation associated with Duck when flying right and upwards
	 */
	private transient Animation rightUpAnimation;
	
	/**
	 * Animation associated with Duck when flying left and upwards
	 */
	private transient Animation leftUpAnimation;
	
	/**
	 * Animation associated with Duck when flying left
	 */
	private transient Animation leftAnimation; 
		
	/**
	 * Animation associated with Duck when flying right
	 */
	private transient Animation rightAnimation;
	
	/**
	 * Animation associated with Duck when dying
	 */
	private transient Animation deathAnimation;
	
	/**
	 * Animation associated with Duck when been shot
	 */
	private transient Animation shotAnimation;
	
	
	/**
	 * The Dead state ID for all Ducks
	 */
	public static final int STATE_DEAD = 0;
	
	/**
	 * The Alive state ID for all Ducks
	 */
	public static final int STATE_ALIVE = 1;
	
	/**
	 * The Dying state ID for all Ducks
	 */
	public static final int STATE_DYING = 2;
	
	/**
	 * The Shot state ID for all Ducks
	 */
	public static final int STATE_SHOT = 4;
	
	/**
	 * The Fly Away state ID for all Ducks
	 */
	public static final int STATE_FLY_AWAY = 8;
	
	/**
	 * The fall speed amount for all Ducks
	 */
	private static final int FALL_SPEED = 7;
	
	/**
	 * The fly away speed for all Ducks
	 */
	private static final int FLY_AWAY_SPEED = -10;
	
	/**
	 * The amount of time for all Ducks to 'wait' when they are shot
	 */
	private static final int SHOT_WAIT_TIME = 20;
	
	/**
	 * The bounding box size of the screen that ensures the Duck does not get
	 * 'caught' on the edge of the screen.
	 */
	private static final int BOUNDING_BOX_SIZE = 5;
	
	/**
	 * The percentage chance that all Ducks will change it direction
	 */
	private static final int DIR_CHANGE_CHANCE = 4;
	
	/**
	 * The difficulty for this Duck
	 */
	private int duckDifficulty;
	
	/**
	 * The current state of this Duck
	 */
	private transient int currentState;
	
	/**
	 * The amount of time a Duck should 'hang' in when it is shot
	 */
	private transient int currentHangTime;
	
	/**
	 * Constructor for the class Duck
	 * @param displayView	the Stage object onto which the Duck is painted
	 * @param animGenerator	the AnimGenerator object which generates the frames for the Duck
	 * @param xPos	the x-position of the Duck on-screen
	 * @param yPos	the y-position of the Duck on-screen
	 */
	public Duck(DisplayView displayView, AnimGenerator animGenerator, int xPos, int yPos, int difficulty) {
		super(displayView, xPos, yPos);
		animationLoader = animGenerator;
		setupAnimation();
		this.setAnimation(leftAnimation);
		
		// Set up velocity
		duckDifficulty = difficulty;
		this.setupVelocity();
		
		// Set default Duck values
		currentState = STATE_ALIVE;
		currentHangTime = 0;
	}
	
	/**
	 * Constructor for the class Duck
	 * @param displayView	the DisplayView onto which the Duck is painted
	 * @param animGenerator	the AnimGenerator that generates the frames for the Duck
	 */
	public Duck(DisplayView displayView, AnimGenerator animGenerator, int difficulty) {
		this(displayView, animGenerator, 0, 0, difficulty);
	}
	
	/**
	 * Constructor for the class Duck
	 * @param displayView	the DisplayView onto which the Duck is painted
	 * @param animGenerator	the AnimGenerator that generates frames for the Duck
	 */
	public Duck(DisplayView displayView, AnimGenerator animGenerator) {
		this(displayView, animGenerator, 0, 0, 0);
	}
	
	/**
	 * Sets up the velocities for the Duck. A random number is
	 * generated from the formula:
	 * upperBound  = difficulty + 10
	 * lowerBound = difficulty
	 * A random number between these this range is obtained to
	 * give this Duck a random movement speed.
	 */
	private void setupVelocity() {
		// Clear the X and Y variables
		int randomDX = 0; int randomDY = 0;
		
		// Find the upperBound
		final int upperBound = duckDifficulty + 10;
		Randomizer randomizer = new Randomizer();
		
		// Loop until we have a greater X than Y velocity to
		// 'encourage' the duck to move more horizontally than vertically
		do {
			randomDX = randomizer.randomNum(duckDifficulty, upperBound);
			randomDY = randomizer.randomNum(duckDifficulty, upperBound);
		} while (randomDX < randomDY);
		
		// Randomize left/right velocity
		randomizer = new Randomizer();
		final boolean changeDir = randomizer.getBoolean();
		if(changeDir) { randomDX = -randomDX; }
		
		// Set the Duck velocity
		xVelocity = randomDX;
		yVelocity = randomDY;
	}
	
	/**
	 * This method updates the Animation object associated with the Duck
	 * so the next frame in the sequence is displayed. Additionally, the 
	 * position of the Duck is incremented by the velocity specified to
	 * simulate movement on-screen.
	 */
	public void act() {
		super.act();
		
		switch(this.getState()) {
			case STATE_ALIVE:
				this.aliveMovement();
				break;
			case STATE_SHOT:
				this.shotMovement();
				break;
			case STATE_DYING:
				this.deathMovement();
				break;
			case STATE_FLY_AWAY:
				this.flyAwayMovement();
				break;
			default:
				// Do nothing
				break;
		}

	}
	
	/**
	 * This method gives the Duck a percentage chance to change
	 * direction, which is specified by the constant DIR_CHANGE_CHANCE.
	 * This Duck is then checked to see if it is on the edge of the screen,
	 * at which point it changes direction and updates the animation
	 * depending on the direction it is going.
	 */
	private void aliveMovement() {
		// Generate a random number between 1 and 100
		// If the number is below the direction change percentage
		// then change directions, otherwise leave the direction
		Randomizer randomizer = new Randomizer();
		randomizer.randomNum(1, 100);
		if((randomizer.getCurrentNumber() < DIR_CHANGE_CHANCE) 
		&& (xVelocity > 0 || xPosition < (DisplayView.WIDTH - this.getWidth()))) { 
			xVelocity = -xVelocity; 
		}
		
		randomizer.randomNum(1, 100);
		if((randomizer.getCurrentNumber() < DIR_CHANGE_CHANCE) 
		&& (yVelocity > 0 || yPosition < (DisplayView.HEIGHT - this.getHeight()))) { 
			yVelocity = -yVelocity; 
		}
		
		// Check whether the Duck has hit the side of the screen
		this.checkBounds();
		
		// Update the animation to reflect which direction the Duck
		// is currently moving
		if(xVelocity > 0) {
			this.setAnimation(leftAnimation);
		} else {
			this.setAnimation(rightAnimation);
		}
		
		// Increment the position by the velocity specified
		yPosition += yVelocity;
		xPosition += xVelocity;
	}
	
	/**
	 * Checks whether the Duck is near the edge of the screen. If it is,
	 * the direction is reversed by inverting the dx and dy velocities.
	 * To prevent the Duck from getting stuck at the edge of the screen
	 * the direction is inverted if the Ducks position is within the
	 * BOUNDING_BOX_SIZE constant.
	 */
	private void checkBounds() {
		int boxLeft, boxRight, boxTop, boxBottom;
		boxLeft = BOUNDING_BOX_SIZE;
		boxRight = (DisplayView.WIDTH - this.getWidth()) - BOUNDING_BOX_SIZE;
		boxTop = BOUNDING_BOX_SIZE;
		boxBottom = (DisplayView.HEIGHT - this.getHeight()) - BOUNDING_BOX_SIZE;
		
		// Invert x-direction if the Duck is on the edge of the screen
		if((xPosition < boxLeft) && (xVelocity < 0)) { 
			xVelocity = -xVelocity;
		} else if((xPosition > boxRight) && (xVelocity > 0)) {
			xVelocity = -xVelocity;
		}
		
		// Invert y-direction if the Duck is on the edge of the screen
		if((yPosition < boxTop) && (yVelocity < 0)) {
			yVelocity = -yVelocity;
		} else if((yPosition > boxBottom) && (yVelocity > 0)) {
			yVelocity = -yVelocity;
		}
	}
	
	/**
	 * This method changes the animation of this Duck when
	 * its state is changed through the setState method. The
	 * Duck continues to fall at the speed specified by the
	 * FALL_SPEED constant, until it reaches the bottom of the
	 * DisplayView. When it does, the state is updated to STATE_DEAD.
	 */
	private void deathMovement() {
		// As the Duck is dead, the 'death spiral' animation
		// is set as the current Animation
		this.setAnimation(deathAnimation);
		
		// The Duck is then moved downwards at a constant 
		// velocity till it goes past the bottom of the screen
		if(yPosition >= DisplayView.HEIGHT) {
			this.setState(STATE_DEAD);
		} else {
			yPosition += FALL_SPEED;
		}
	}
	
	/**
	 * The shotMovement method causes this Duck to change
	 * its animation if the Duck is not currently dying.
	 * If it is not dying, then increment the currentHangTime
	 * variable until it has reached the number specified
	 * by the constant SHOT_WAIT_TIME.
	 */
	private void shotMovement() {
		// If the duck is currently not dying
		if(this.getState() != Duck.STATE_DYING) {
			// Update its animation
			if(this.getAnimation() != shotAnimation) {
				this.setAnimation(shotAnimation);
			}
			
			// Increment currentHangTime until the wait time is reached
			if(currentHangTime > SHOT_WAIT_TIME) {
				this.setState(Duck.STATE_DYING);
			} else {
				currentHangTime++;
			}
		}
	}
	
	/**
	 * If the Duck has been set to fly away, then continue to
	 * make the Duck move upwards at the FLY_AWAY_SPEED specified.
	 * If the Duck has reached the top of the screen, then set its
	 * current state to STATE_DEAD.
	 */
	private void flyAwayMovement() {
		if(yPosition < (-this.getHeight())) {
			this.setState(STATE_DEAD);
		} else {
			if(xVelocity > 0) {
				if(this.getAnimation() != leftUpAnimation) {
					this.setAnimation(leftUpAnimation);
				}
			} else {
				if(this.getAnimation() != rightUpAnimation) {
					this.setAnimation(rightUpAnimation);
				}
			}
			
			yVelocity = FLY_AWAY_SPEED;
			yPosition += FLY_AWAY_SPEED;
		}
	}
	
	/**
	 * Sets up all Animations for this Duck. The Animations are
	 * loaded from the AnimGenerator, assigned to this Duck and then
	 * started.
	 */
	private void setupAnimation() {
		// Loads the resources for the on-screen representation of our duck
		String duckFolderPrefix, duckFilePrefix = null;
		
		// Load the upwards left and right animations
		duckFolderPrefix = "resources/images/duck/flyupright/";
		duckFilePrefix = "bvw3-flyupright_frame_0";
		rightUpAnimation = animationLoader.getFrames(duckFolderPrefix, duckFilePrefix, AnimGenerator.RIGHT_FACING, 23);
		leftUpAnimation = animationLoader.getFrames(duckFolderPrefix, duckFilePrefix, AnimGenerator.LEFT_FACING, 23);
		
		// Load the left and right animations
		duckFolderPrefix = "resources/images/duck/flyright/";
		duckFilePrefix = "bvw3-flyright_frame_0";
		rightAnimation = animationLoader.getFrames(duckFolderPrefix, duckFilePrefix, AnimGenerator.RIGHT_FACING, 23);
		leftAnimation = animationLoader.getFrames(duckFolderPrefix, duckFilePrefix, AnimGenerator.LEFT_FACING, 23);
		
		// Load the death animation
		duckFolderPrefix = "resources/images/duck/deathspiral/";
		duckFilePrefix = "bvw3-deathspiral_frame_0";
		deathAnimation = animationLoader.getFrames(duckFolderPrefix, duckFilePrefix, AnimGenerator.LEFT_FACING, 20);
		
		// Load the shot animation
		duckFolderPrefix = "resources/images/duck/shot/";
		duckFilePrefix = "bvw3-shot.png";
		shotAnimation = animationLoader.getFrame(duckFolderPrefix, duckFilePrefix, AnimGenerator.RIGHT_FACING);
		
		// Start all the animations
		leftUpAnimation.start();
		rightUpAnimation.start();
		leftAnimation.start();
		rightAnimation.start();
		deathAnimation.start();
		shotAnimation.start();
	}
	
	/**
	 * Sets the current state of this Duck
	 * @param	state the state to which to set the Duck to	
	 */
	public void setState(final int state) {
		currentState = state;
	}
	
	/**
	 * Returns the current state of this Duck.
	 * @return	the current state
	 */
	public int getState() {
		return currentState;
	}
	
	/**
	 * Sets this Duck's difficulty
	 * @param	difficulty	the difficulty to set the Duck to
	 */
	public void setDuckDifficulty(final int difficulty) {
		duckDifficulty = difficulty;
	}
	
	/**
	 * Returns the current difficulty of this Duck
	 * @return	the difficulty of this Duck
	 */
	public int getDuckDifficulty() {
		return duckDifficulty;
	}
}