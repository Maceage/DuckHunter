package core;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.geom.Point2D;

import config.AbstractGameMode;
import config.GameOptions;
import graphics.*;
import core.sprites.*;
import sound.*;
import util.*;
import ui.*;

/**
 * The GameCore class is responsible for creating and setting up the main
 * window, then running the main game loop. This class extends the Canvas
 * object for painting and implements the Stage, KeyListener and MouseListener
 * interfaces for setting the window size, receiving user keyboard input and
 * detecting user mouse input respectively. The class constantly paints and updates
 * all the objects and sprites in the class until the window is closed and the 
 * thread is exited.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class GameCore extends Canvas implements DisplayView, KeyListener, MouseListener, 
	WindowListener, WindowFocusListener, Runnable {
	
	/**
	 * Constant variable for the game's name
	 */
	public static final String GAME_NAME = "Duck Hunter";
		
	/**
	 * Version ID used for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constant variable for current game version
	 */
	private static final float GAME_VERSION = 1.0f;
	
	/**
	 * Constant specifying the maximum number of sound threads to use
	 */
	private static final int SOUND_COUNT = 5;
	
	/**
	 * Constant specifying the maximum amount of decals on the scren
	 */
	private static final int DECAL_MAX_COUNT = 2;
	
	/**
	 * Constant specifying minimum amount of clouds to generate
	 */
	private static final int CLOUD_MIN_COUNT = 5;
	
	/**
	 * Constant specifying maximum amount of clouds to generate
	 */
	private static final int CLOUD_MAX_COUNT = 7;
	
	/**
	 * Constant specifying X-border of UI display
	 */
	private static final int SPRITE_UI_XPOS = 35;
	
	/**
	 * Constant specifying Y-border of UI display
	 */
	private static final int SPRITE_UI_YPOS = 55;
	
	/**
	 * String literal for "Tahoma" system font
	 */
	private static final String FONT_TAHOMA = "Tahoma";
	
	/**
	 * String literal for "Arial" system font
	 */
	private static final String FONT_ARIAL = "Arial";
	
	// Game options object
	private GameOptions gameOptions;
	
	// ArrayLists for duck, decal and cloud sprites
	private transient ArrayList duckList, decalList, cloudList;
	
	// ArrayLists for sound effects
	private transient ArrayList soundsGunHit, soundsGunMiss, soundsGunNoAmmo, soundsGunReload, 
		soundsDuckAlive, soundsDuckDead, soundsAmbience;
	
	// Graphics display BufferStrategy object
	private transient final BufferStrategy strategy;
	
	// SpriteCache for this game
	private transient final SpriteCache spriteCache;

	// SoundCache for this game
	private transient final SoundCache soundCache;
	
	// Animation generator for this game
	private transient final AnimGenerator animGenerator;
	
	// Main window for this game
	private transient final JFrame mainWindow;
	
	// The GameMode the game is currently running in
	private transient final AbstractGameMode gameMode;
	
	// Point2D objects for pointer clicked location, debug information and
	// painting duck value on-screen
	private transient Point2D pointerLoc, pointerCache, scoreLocation;
	
	// Player object
	private transient Player thePlayer;
	
	// Background Sprite images
	private transient Background backgroundSprite, backgroundFlyAway;
	
	// UI Sprites
	private transient SimpleSprite flyAwaySprite, pausedSprite, gameOverSprite, roundEndSprite,
	roundFailSprite, loadingSprite, ammoSprite, duckAliveSprite, duckDeadSprite;
	
	// Game state variables
	private transient boolean gamePaused, roundCompleted, flyAway;
	
	// Variables for point value of a Duck when shot and current number of Ducks shot
	private transient int duckValue, numDucksShot;
	
	// Variables for the time the game started and the amount of time used
	private transient long timeStarted, usedTime;
	
	// Flag for memory limiter when spawning Ducks
	private transient boolean maxDucksReached = false;
	
	// Create String for timer display
	private transient String timeString;
	
	/******* CONSTRUCTOR *******/
	
	/**
	 * Creates an instance of this class which creates and sets
	 * up a JFrame with a canvas to display graphics and component listeners to  
	 * receive user input.
	 * @param 	gMode	the mode in which to run the game
	 * @param	gOptions	the options to use when running the game
	 * @param	spCache		pointer to the SpriteCache to use
	 * @param	sCache		pointer to the SoundCache to use
	 */
	public GameCore(AbstractGameMode gMode, GameOptions gOptions, SpriteCache spCache, 
			SoundCache sCache, Player aPlayer) {
		// Call superclass constructor
		super();
		
		// Set parameters passed
		gameMode = gMode;
		gameOptions = gOptions;
		spriteCache = spCache;
		soundCache = sCache;
		thePlayer = aPlayer;
		
		// Disable or enable sound based on game options passed
		soundCache.enableSound(gameOptions.getSoundEnabled());
		
		// Create and set-up our main window
		String windowTitle = new String(GAME_NAME + " v" + GAME_VERSION + " - " + gMode.getName());
		if(gameOptions.getDebugMode()) { windowTitle = windowTitle + " (DEBUG)"; }
		mainWindow = new JFrame(windowTitle);
		mainWindow.setBounds(0, 0, DisplayView.WIDTH + 10, DisplayView.HEIGHT + 10);
		mainWindow.setVisible(true);
		mainWindow.setResizable(false);
		mainWindow.createBufferStrategy(2);
		mainWindow.setIgnoreRepaint(true);
		mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// Position the frame in the middle of the screen
		Dimension screen = getToolkit().getScreenSize();
    	mainWindow.setLocation(
    		(screen.getSize().width - DisplayView.WIDTH - 10) / 2, 
    		(screen.getSize().height - DisplayView.HEIGHT - 10) / 2);
		
		// Get the main window panel and set it up
		JPanel mainPanel = (JPanel)mainWindow.getContentPane();
		this.setBounds(0, 0, DisplayView.WIDTH, DisplayView.HEIGHT);
		mainPanel.setPreferredSize(new Dimension(DisplayView.WIDTH, DisplayView.HEIGHT));
		mainPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		mainPanel.setLayout(null);
		mainPanel.add(this);
		
		// Add window listeners
		mainWindow.addWindowListener(this);
		mainWindow.addWindowFocusListener(this);
		
		this.createBufferStrategy(2);
		strategy = getBufferStrategy();
		this.requestFocus();
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		animGenerator = new AnimGenerator(this.spriteCache, this.getGraphicsConfiguration());
	}
	
	/******* GAME LOOP *******/
	
	/**
	 * The main game loop - this first initializes the objects and then
	 * continues to loop as long as the window is visible. During this time,
	 * the world objects are updated and then painted to the screen before the
	 * loop starts over again. If the window loses focus for any reason, then
	 * the garbage collector is run in the background to clean up any unused objects.
	 **/
	public void run() {
		// Initialize all objects
		System.out.println("Init game...");
		initWorld();
		System.out.println("Starting game...");
		
		usedTime = 1000;
		while(this.isVisible()) {
			// Note when we started the update and paint operations
			final long startTime = System.currentTimeMillis();
			
			// Update the world
			updateWorld();
			
			// Draw the screen
			renderWorld();
			paint(getGraphics());
			
			// Get time used updating world and drawing graphics
			usedTime = System.currentTimeMillis() - startTime;
			
			// Sleep the thread
			do {
				Thread.yield();
			} while (System.currentTimeMillis() - startTime < THREAD_WAIT_TIME);
		}
		
		// Pause the game if the Canvas is not currently visible
		if(!this.isVisible()) {
			System.out.println("Pausing game");
			setPaused(true);
			
			// Garbage collection
			System.runFinalization();
			System.gc();
		}
	}
	
	/******* INITIALIZATION *******/
	
	/**
	 * Initializes the world by creating and loading all
	 * objects used in the game
	 **/
	private void initWorld() {
		// Report to console and display loading screen
		System.out.println("...init world");
		paintLoading();
		
		// Reset our current Duck's value to zero
		duckValue = 0;
		
		// Store time game started
		timeStarted = System.currentTimeMillis();
		
		// Initialize sound
		soundCache.setPool(new ThreadPool(SOUND_COUNT));
		System.out.println("...init sound");
		initSound();
		
		// Initialize sprites
		System.out.println("...init sprites");
		initSprites();
		
		// Create Ducks
		duckList = new ArrayList();
		resetRound();
	}
	
	/**
	 * Initializes and positions all Sprites used in the game. The initial positions of the
	 * Sprites are within the parameters specified by the WIDTH and HEIGHT
	 * properties of the DisplayView interface. The UI message Sprites are set to be positioned
	 * in the middle of the screen and the clouds are positioned within the upper portion of
	 * the display area. All other Sprites are located around the edge of the display area
	 * and the background image is stretched across the whole viewable area.
	 */
	private void initSprites() {
		// Create Randomizer for variable randomization
		decalList = new ArrayList();
		cloudList = new ArrayList();
		int xPos = 0, yPos = 0;
	
		// Create Sprite for 'Fly Away!' message in the middle of the screen
		flyAwaySprite = new SimpleSprite(this, animGenerator, "resources/images/interface/text/flyaway.png");
		xPos = ((DisplayView.WIDTH - flyAwaySprite.getWidth()) / 2);
		yPos = ((DisplayView.HEIGHT - flyAwaySprite.getHeight()) / 2);
		flyAwaySprite.setXPosition(xPos); flyAwaySprite.setYPosition(yPos);
		
		// Create Sprite for 'Paused' message in the middle of the screen
		pausedSprite = new SimpleSprite(this, animGenerator, "resources/images/interface/text/paused.png");
		xPos = ((DisplayView.WIDTH - pausedSprite.getWidth()) / 2);
		yPos = ((DisplayView.HEIGHT - pausedSprite.getHeight()) / 2);
		pausedSprite.setXPosition(xPos); pausedSprite.setYPosition(yPos);
		
		// Create Sprite for 'Round Complete' message in the middle of the screen
		roundEndSprite = new SimpleSprite(this, animGenerator, "resources/images/interface/text/roundcomplete.png");
		xPos = ((DisplayView.WIDTH - roundEndSprite.getWidth()) / 2);
		yPos = ((DisplayView.HEIGHT - roundEndSprite.getHeight()) / 2);
		roundEndSprite.setXPosition(xPos); roundEndSprite.setYPosition(yPos);
		
		// Create Sprite for 'Round Fail' message in the middle of the screen
		roundFailSprite = new SimpleSprite(this, animGenerator, "resources/images/interface/text/roundfailed.png");
		xPos = ((DisplayView.WIDTH - roundFailSprite.getWidth()) / 2);
		yPos = ((DisplayView.HEIGHT - roundFailSprite.getHeight()) / 2);
		roundFailSprite.setXPosition(xPos); roundFailSprite.setYPosition(yPos);
		
		// Create Sprite for 'Game Over' message in the bottom middle of the screen
		gameOverSprite = new SimpleSprite(this, animGenerator, "resources/images/interface/text/gameover.png");
		xPos = ((DisplayView.WIDTH - gameOverSprite.getWidth()) / 2);
		final int yIncrement = ((DisplayView.HEIGHT - gameOverSprite.getHeight()) / 2);
		yPos = yIncrement + (yIncrement / 2);
		gameOverSprite.setXPosition(xPos); gameOverSprite.setYPosition(yPos);
		
		// Create a random number of SimpleSprites for Clouds in background
		final Randomizer randomizer = new Randomizer();
		final int cloudCount = randomizer.randomNum(CLOUD_MIN_COUNT, CLOUD_MAX_COUNT);
		SimpleSprite cloudSprite = null;
		for(int i = 0; i < cloudCount; i++) {
			// Create a new cloud sprite and add it to the list
			cloudSprite = new SimpleSprite(this, animGenerator, "resources/images/background/cloud.png");
			xPos = randomizer.randomNum(10, DisplayView.WIDTH - cloudSprite.getWidth());
			yPos = randomizer.randomNum(25, DisplayView.HEIGHT / 4);
			cloudSprite.setXPosition(xPos); cloudSprite.setYPosition(yPos);
			// Randomize the x velocity and direction of the cloud
			int cloudDX = randomizer.randomNum(1, 3);
			if(randomizer.getBoolean()) {
				cloudDX = -cloudDX;
			}
			cloudSprite.setDX(cloudDX);
			// Add the sprite to the list
			cloudList.add(cloudSprite);
		}
		
		// Create Sprite for indicating how much ammo the player has left and set position in lower-left
		ammoSprite = new SimpleSprite(this, animGenerator, "resources/images/interface/icons/bullet.png");
		xPos = SPRITE_UI_XPOS; 
		yPos = ((DisplayView.HEIGHT - ammoSprite.getHeight()) - 40);
		ammoSprite.setXPosition(xPos); ammoSprite.setYPosition(yPos);
		
		// Create Sprite for indicating how many Ducks are shot and how many are left and set position in lower-right
		duckAliveSprite = new SimpleSprite(this, animGenerator, "resources/images/interface/icons/duck_alive.png");
		xPos = SPRITE_UI_XPOS;
		yPos = SPRITE_UI_YPOS;
		duckAliveSprite.setXPosition(xPos); duckAliveSprite.setYPosition(yPos);
		
		// Create Sprite for indicating how many Ducks are shot and how many are left and set position in lower-right
		duckDeadSprite = new SimpleSprite(this, animGenerator, "resources/images/interface/icons/duck_dead.png");
		xPos = SPRITE_UI_XPOS;
		yPos = SPRITE_UI_YPOS;
		duckDeadSprite.setXPosition(xPos); duckDeadSprite.setYPosition(yPos);
		
		// Create Sprite for background image
		backgroundSprite = new Background(this, animGenerator, 0, 0, 
				"resources/images/background/background.png");
		backgroundFlyAway = new Background(this, animGenerator, 0, 0, 
				"resources/images/background/background_negative.png");
	}
	
	/**
	 * Initializes all sounds used in the game. The sounds used are added to their
	 * relative ArrayLists which are then used later by the SoundCache to locate and play
	 * specific sounds. If ambience sounds are enabled, then the appropriate sounds are started.
	 */
	private void initSound() {
		// TODO: Use XML to load the resources from their relative folders and into
		// the proper arrays.
		// TODO: Or use a recursive directory scan to load resources from a folder, allowing
		// the player to drop sounds into the folder and have them automatically play
		soundsGunHit = new ArrayList();
		soundsGunMiss = new ArrayList();
		soundsGunNoAmmo = new ArrayList();
		soundsGunReload = new ArrayList();
		soundsDuckAlive = new ArrayList();
		soundsDuckDead = new ArrayList();
		soundsAmbience = new ArrayList();
		
		// Crete Sound file arrays
		soundsGunHit.add("resources/sounds/gun/gun_shotgun1.wav");
		soundsGunHit.add("resources/sounds/gun/gun_shotgun2.wav");
		soundsGunMiss.add("resources/sounds/gun/ricochet.wav");
		soundsGunReload.add("resources/sounds/gun/gun_shoot_metal2.wav");
		soundsGunNoAmmo.add("resources/sounds/gun/mp44_clipout.wav");
		soundsDuckAlive.add("resources/sounds/duck/alive/1duck.wav");
		soundsDuckAlive.add("resources/sounds/duck/alive/duck-quack1.wav");
		soundsDuckDead.add("resources/sounds/duck/dead/body_medium_impact_soft1.wav");
		soundsDuckDead.add("resources/sounds/duck/dead/body_medium_impact_soft2.wav");
		soundsDuckDead.add("resources/sounds/duck/dead/body_medium_impact_soft3.wav");
		soundsDuckDead.add("resources/sounds/duck/dead/body_medium_impact_soft4.wav");
		soundsDuckDead.add("resources/sounds/duck/dead/body_medium_impact_soft5.wav");
		soundsDuckDead.add("resources/sounds/duck/dead/body_medium_impact_soft6.wav");
		soundsDuckDead.add("resources/sounds/duck/dead/body_medium_impact_soft7.wav");
		soundsAmbience.add("resources/sounds/ambience/ambient_sound.wav");

		if(gameOptions.getSoundAmbience()) {
			// Start the ambient sounds
			soundCache.loopSound(soundsAmbience);
		}
		
		if(gameOptions.getSoundShot()) {
			// Play reload sound
			soundCache.playSound(soundsGunReload);
		}
	}
	
	/******* GAME LOGIC *******/
	
	/**
	 * Updates the world based on the player's actions, such as pausing the game or
	 * clicking the screen. This method checks whether ducks have been hit or not
	 * and plays the appropriate sound in the SoundCache. The locations of all the Sprites
	 * are then updated by calling updateSprites()
	 **/
	private void updateWorld() {
		// If the game is not paused...
		if(!gamePaused) {
			// Check there are still Ducks left
			if(duckList.isEmpty()) {
				// If the player still has lives left...
				if(thePlayer.hasLives()) {
					// ...then reset round
					this.resetRound();
				} else {
					// Otherwise, game must be over so pause game then add 
					// player to high scores table and display
					ScoreWindow highScores = new ScoreWindow(thePlayer);
					this.setPaused(true);
					highScores.setVisible(true);
					// Create a new Player based on the current Player
					thePlayer = new Player(thePlayer.getPlayerName(), 
							gameMode.getAmmoCount(), gameMode.getLifeCount());
					// Reset round when window closed and unpause game
					gameMode.resetGame();
					this.resetRound();
					this.setPaused(false);
				}
			} else {
				// Check if the player has clicked on the Canvas
				if(pointerLoc != null) {
					// ... then check for where the player clicked
					duckValue = 0;
					if((!gamePaused) || (!roundCompleted) || (!flyAway)) {
						duckValue = checkForHit();
					}
					
					if(duckValue > 0) {
						// Add a blood decal
						addDecal(true);
						numDucksShot++;
						// Increment and paint score
						thePlayer.hitDuck();
						thePlayer.addScore(duckValue);
						// Set scoreLocation to where the Duck was hit
						scoreLocation = pointerLoc;
						// Stop all Duck sounds
						soundCache.stopAllSounds(soundsDuckAlive);
						if(gameOptions.getSoundShot()) {
							soundCache.playSound(soundsGunHit);
						}
					} else {
						// Check if the player has shots
						if(thePlayer.getShotCount() <= 0) {
							// Player does not have ammo, so play gun click sound
							if(gameOptions.getSoundShot()) {
								soundCache.playSound(soundsGunNoAmmo);
							}
						} else {
							// Otherwise, the player missed the duck
							thePlayer.missedDuck();
							if(gameOptions.getSoundShot()) {
								soundCache.playSound(soundsGunMiss);
							}
							// Add a decal
							addDecal(false);
						}
					}
				}
				
				// Update all the Sprites on-screen
				updateSprites();
			}
		} else {
			// If our game has a time limit...
			if(gameMode.hasTimeLimit()) {
				// ...then set the time started to the current time minus the time left
				final long timeLeft = timeStarted + gameMode.getTimeLimit();
				timeStarted = System.currentTimeMillis() - timeLeft;
			}
		}
		
		// Clear the mouse pointer location
		pointerLoc = null;
	}
	
	/**
	 * Updates all Sprite objects in the game, including ducks, clouds and the UI.
	 * This involves updating the clouds, then checking whether the player has taken their last
	 * shot followed by updating all the ducks. 
	 */
	private void updateSprites() {
		// Create a temporary Duck
		Duck aDuck = null;
		
		// Update the Cloud Sprites
		SimpleSprite cloudSprite = null;
		for(int i = 0; i < cloudList.size(); i++) {
			cloudSprite = (SimpleSprite)cloudList.get(i);
			cloudSprite.act();
		}
		
		// If the player has taken their last 'shot' or the time limit has run out
		// then set all Ducks that are 'alive' to the 'fly away' state and set flag
		int timeLeft = (int)((timeStarted + gameMode.getTimeLimit()) - System.currentTimeMillis());
		if((thePlayer.getShotCount() <= 0) || (timeLeft < 0)) {
			if((!roundCompleted) && (!flyAway) && (gameMode.hasTimeLimit())) {
				for(int i = 0; i < duckList.size(); i++) {
					aDuck = (Duck)duckList.get(i);
					if(aDuck.getState() == Duck.STATE_ALIVE) {
						aDuck.setState(Duck.STATE_FLY_AWAY);
						thePlayer.loseLife();
						flyAway = true;
					}
				}
			}
		}
	
		// Update all the Ducks
		for(int i = 0; i < duckList.size(); i++) {
			aDuck = (Duck)duckList.get(i);
			// If the Duck is dead, then play sound (if enabled) and remove it from the list
			if(aDuck.getState() == Duck.STATE_DEAD) {
				if(gameOptions.getSoundDuck()) {
					soundCache.playSound(soundsDuckDead);
				}
				duckList.remove(i);
			} else {
				// Otherwise update
				aDuck.act();
			}
		}
		
		// If there are Ducks left, then set roundCompleted will be set to false
		roundCompleted = true;
		for(int i = 0; i < duckList.size(); i++) {
			aDuck = (Duck)duckList.get(i);
			if(aDuck.getState() == Duck.STATE_ALIVE) { 
				roundCompleted = false;
				break;
			}
		}
	}
	
	/**
	 * Resets the round by updating player status, resetting the list
	 * of ducks and decals, resetting player shots, then spawning a new set of
	 * ducks by calling spawnDucks
	 */
	private void resetRound() {
		// Restart the round
		System.out.println("Restarting round...");
		
		// Get the time the round started
		timeStarted = System.currentTimeMillis();

		// Go to next level if the Player successfully completed the round
		if(roundCompleted) {
			gameMode.updateState();
		}
		
		// Clear all 
		decalList.removeAll(decalList);
		duckList.removeAll(duckList);
		thePlayer.resetShots();
		duckValue = 0;
		numDucksShot = 0;
		flyAway = false;
		roundCompleted = false;
		
		// Play sound effects
		if(gameOptions.getSoundShot()) {
			soundCache.playSound(soundsGunReload);
		}
		if(gameOptions.getSoundDuck()) {
			soundCache.loopSound(soundsDuckAlive);
		}
		
		// Spawn the number of ducks specified
		spawnDucks(gameMode.getDuckCount());
	}
	
	/**
	 * Adds a new decal at the player's current pointer location for future rendering. If the
	 * decalLimit is enabled and has been reached, then the first item in the decalList is
	 * removed to maintain an acceptable frame rate.
	 * @param	bulletHole	specifies whether the player hit or missed the duck
	 */
	private void addDecal(final boolean bulletHole) {
		if(gameOptions.getDecalsEnabled()) {
			// Get the decal's path 
			String decalPath = null;
			if(bulletHole) {
				final Randomizer randomDecal = new Randomizer();
				randomDecal.randomNum(0, 2);
				decalPath = "resources/images/decals/blood" + randomDecal.getCurrentNumber() + ".png";
			} else {
				final Randomizer randomDecal = new Randomizer();
				randomDecal.randomNum(0, 2);
				decalPath = "resources/images/decals/hole" + randomDecal.getCurrentNumber() + ".png";
			}
			// Create a new decal and add it to the decalList
			SimpleSprite newDecal = new SimpleSprite(this, animGenerator, decalPath);
			final int decalX = (int)(pointerLoc.getX() - (newDecal.getWidth() / 2));
			final int decalY = (int)(pointerLoc.getY() - (newDecal.getHeight() / 2));
			newDecal.setXPosition(decalX); newDecal.setYPosition(decalY);
			decalList.add(newDecal);
			
			// If the decal limiter is enabled and we're over the decal limit
			if((gameOptions.getDecalLimitEnabled()) && (decalList.size() > DECAL_MAX_COUNT)) {
				do {
					// ...remove the first element
					decalList.remove(0);
				} while (decalList.size() > DECAL_MAX_COUNT);
			}
		}
	}
	
	/**
	 * Checks whether any of the Ducks on screen have been 'shot' by the user by checking whether the 
	 * pointerLoc co-ordinates intersect any area of the frame currently being displayed. 
	 * This method will return a positive integer value if a Duck has been hit and -1 if a Duck has 
	 * not been hit.
	 * @return	an integer value of the score for the Duck that has been 'hit' by the user
	 */
	private int checkForHit() {
		// Check a Duck has been 'hit' by the player by checking whether the pointerLoc co-ordinates
		// intersect any area of the frame currently being displayed. If the player hit the Duck, then
		// a score value for the Duck is returned, otherwise -1 is returned.
		int boundX, boundY, duckPoints = 0;
		Duck aDuck = null;
		duckPoints = -1;
		
		// Loop through our list of Ducks
		for(int i = 0; i < duckList.size(); i++) {
			aDuck = (Duck)duckList.get(i);
			
			// Find bounds of our Duck's current Sprite
			boundX = aDuck.getXPosition() + aDuck.getWidth();
			boundY = aDuck.getYPosition() + aDuck.getHeight();
			
			// Check that the users pointer location intersects the box, that the player
			// currently has shots and the Duck is currently alive
			if(((pointerLoc.getX() > aDuck.getXPosition()) && (pointerLoc.getX() < boundX)) &&
			((pointerLoc.getY() > aDuck.getYPosition()) && (pointerLoc.getY() < boundY)) &&
			(aDuck.getState() == Duck.STATE_ALIVE) && (thePlayer.getShotCount() > 0)) {
				// Change the Duck's state to shot
				aDuck.setState(Duck.STATE_SHOT);
				// Generate a point value for the Duck based on its velocities
				int duckPointsX = aDuck.getDX(); int duckPointsY = aDuck.getDY();
				
				// Invert any of the velocities to positive if negative values
				if(duckPointsX < 0) { duckPointsX = -duckPointsX; }
				if(duckPointsY < 0) { duckPointsY = -duckPointsY; }
				
				// Generate a score for the Duck based on its velocities multiplied by
				// the Duck's difficulty divided by how many shots the Player has left,
				// multiplied by 10 to make a 'sensible' score :o)
				duckPoints = (duckPointsX + duckPointsY) * aDuck.getDuckDifficulty();
				
				// If the current game mode uses a shot modifier, then take that into account
				if(gameMode.getShotModifier()) {
					final int shotModifier = gameMode.getAmmoCount() - thePlayer.getShotCount();
					if (shotModifier != 0) {
						duckPoints = duckPoints / shotModifier;
					}
				}
				
				// Multiply to give a 'big' number
				duckPoints = duckPoints * 10;
				
				// Print to the debug window which Duck has been hit and its point value
				if(gameOptions.getDebugMode()) {
					System.out.println("Duck " + i + " hit (" + Integer.toString(duckPoints) + " points)");
				}
				
				// Exit the loop
				break;
			}
		}
		return duckPoints;
	}
	
	/**
	 * Respawns a limited number of Ducks and adds them  to the DuckList. 
	 * The number of Ducks spawned is specified  by the parameter numDucks.
	 * NOTE: The number of Ducks that can be spawned is dependent on the amount
	 * of memory available to the JVM. Therefore, a global flag is set to true if 
	 * the OutOfMemoryError exception is caught.
	 * @param numDucks	the number of Ducks to spawn and add to the screen
	 */
	private void spawnDucks(final int numDucks) {
		// Create a temporary Duck
		Duck aDuck = null;
		// Print to console
		System.out.println("Spawning " + numDucks + " ducks... (" + (duckList.size()) + " current)");
		try {
			// If the maximum number of Ducks has not been reached, then add another to the ArrayList
			if(maxDucksReached) {
				// Otherwise, just display error
				System.out.println("Maximum amount of Ducks reached - please increase JVM heapsize");
			} else {
				// Get the game's current difficulty level
				final int currentLevel = gameMode.getDifficultyLevel();
				
				// Create the specified number of Ducks and add them to the list
				for(int i = 0; i < numDucks; i++) {
					// Create our duck
					aDuck = new Duck(this, animGenerator, currentLevel);
					// Place it in the bottom-middle of the Canvas
					final int xPos = (this.getWidth() - aDuck.getWidth()) / 2;
					final int yPos = (this.getHeight() - aDuck.getHeight()); 
					aDuck.setXPosition(xPos);	aDuck.setYPosition(yPos);
					// And add it to the list of ducks
					duckList.add(aDuck);
				}
			}
		} catch(OutOfMemoryError oom) {
			// Display error and set flag if maximum memory reached
			System.out.println("Cannot create any more Ducks: " + oom.toString());
			maxDucksReached = true;
		}
	}
	
	/******* PAINT METHODS *******/
	
	/**
	 * Paints the complete world to the current Graphics context by calling
	 * all the paint methods available.
	 **/
	private void renderWorld() {
		// Get the current Graphics context
		final Graphics2D gfx = (Graphics2D)strategy.getDrawGraphics();
		
		// Paint all the Sprites to the context
		paintSprites(gfx);	
		paintGameStatus(gfx);
		paintDebug(gfx);
		paintToolbars(gfx);
		paintUI(gfx);
		
		// Attempt to display the graphics buffer
		try {
			strategy.show();
		} catch(NullPointerException ex) {
			// Do nothing - should only catch when closing thread
		}
	}
	
	/**
	 * Paints the Loading Screen. This method does not require a graphics context
	 * because the method is called outside of the main game loop and acquires the
	 * current graphics context itself.
	 */
	private void paintLoading() {
		// Get the current graphics drawing area
		final Graphics2D gfx = (Graphics2D)strategy.getDrawGraphics();
		
		// Draw blue box covering entire screen area
		gfx.setColor(Color.WHITE);
		gfx.fillRect(0, 0, DisplayView.WIDTH, DisplayView.HEIGHT);
		
		// Draw the "Loading..." text in the bottom, left of the screen
		// and add a 'shadow' effect to the text
		gfx.setColor(Color.GRAY);
		gfx.setFont(new Font(FONT_TAHOMA, Font.BOLD + Font.ITALIC, 125));
		gfx.drawString("Loading...", 5, DisplayView.HEIGHT - 105);
		gfx.setColor(Color.BLACK);
		gfx.setFont(new Font(FONT_TAHOMA, Font.BOLD + Font.ITALIC, 125));
		gfx.drawString("Loading...", 10, DisplayView.HEIGHT - 110);
		
		// Create Sprite for shotgun on loading screen
		loadingSprite = new SimpleSprite(this, animGenerator, "resources/images/interface/loading/shotgun.png");
		final int xPos = ((DisplayView.WIDTH - loadingSprite.getWidth()) / 2);
		final int yPos = ((DisplayView.HEIGHT - loadingSprite.getHeight()) / 2);
		loadingSprite.setXPosition(xPos); loadingSprite.setYPosition(yPos);
		loadingSprite.paint(gfx);
		
		// Paint the toolbars at the top and bottom
		paintToolbars(gfx);
		
		// Show the buffer
		strategy.show();
	}
	
	/**
	 * Paints all Sprites to screen, including Ducks, Decals and Clouds
	 * @param	gfx	the Graphics context onto which to paint
	 */
	private void paintSprites(final Graphics2D gfx) {
		// If the player lost the round, then swap the background image and paint to the screen
		if(flyAway) {
			backgroundFlyAway.paint(gfx);
		} else {
			backgroundSprite.paint(gfx);
		}
		
		if(gameOptions.getDecalsEnabled()) {
			// Paint all the Decals on the Canvas
			SimpleSprite aDecal = null;
			for(int i = 0; i < decalList.size(); i++) {
				aDecal = (SimpleSprite)decalList.get(i);
				aDecal.paint(gfx);
			}
		}
		
		// If the game is paused, don't paint any Ducks to prevent the player from cheating ;o)
		if(!gamePaused) {
			// Paint all the Ducks on the Canvas
			Duck aDuck = null;
			gfx.setColor(Color.BLACK);
			for(int i = 0; i < duckList.size(); i++) {
				aDuck = (Duck)duckList.get(i);
				aDuck.paint(gfx);
				// Add a number to the Duck's sprite if we're running in debug mode
				if(gameOptions.getDebugMode()) {
					gfx.drawString(Integer.toString(i), aDuck.getXPosition(), aDuck.getYPosition() + (aDuck.getHeight() / 2));
				}
			}
		}
		
		// Paint all the Clouds on the Canvas
		SimpleSprite cloudSprite = null;
		for(int i = 0; i < cloudList.size(); i++) {
			cloudSprite = (SimpleSprite)cloudList.get(i);
			cloudSprite.paint(gfx);
		}
	}
	
	/**
	 * Paints any status messages to the screen, including the score of the Duck
	 * just shot by the player.
	 * @param 	gfx	the Graphics context onto which to paint
	 */
	private void paintGameStatus(final Graphics2D gfx) {
		// Create a temporary Duck
		Duck aDuck = null;
		
		// Set the Graphics context properties
		gfx.setColor(Color.RED);
		gfx.setFont(new Font(FONT_ARIAL, Font.BOLD, 30));
		
		// Loop through all the Ducks
		for(int i = 0; i < duckList.size(); i++) {
			aDuck = (Duck)duckList.get(i);
			// Check the state of the Duck to see if the score for the Duck needs displaying,
			// only if the scoreLocation isn't null and the Duck's value is greater than zero
			if(((aDuck.getState() == Duck.STATE_SHOT) || (aDuck.getState() == Duck.STATE_DYING)) 
			&& (scoreLocation != null) && (duckValue > 0)) {
				// Write the score to the screen
				gfx.drawString(Integer.toString(duckValue), (int)scoreLocation.getX(), (int)scoreLocation.getY() - 75);
			}
		}
		
		
		// If the game is paused, then show pausedSprite
		if(gamePaused) {
			pausedSprite.paint(gfx);
		} else {
			// Display 'fly away' Sprite if all Ducks flew away
			if(flyAway) {
				flyAwaySprite.paint(gfx);
			} else if(roundCompleted) {
				// Display 'round end' Sprite if player run out of shots
				// and there are Ducks left
				roundEndSprite.paint(gfx);
			}
		}
	}
	
	/**
	 * Paints the UI to the screen. This includes elements such as the FPS counter,
	 * the Player's current status, score, level, round number, ammunition count and lives.
	 * @param	gfx	the Graphics context onto which to draw the Sprites
	 */
	private void paintUI(final Graphics2D gfx) {
		// Add game title
		gfx.setColor(Color.BLUE);
		gfx.setFont(new Font(FONT_TAHOMA, Font.BOLD, 35));
		gfx.drawString("Duck Hunter", SPRITE_UI_XPOS - 10, 35);
		
		// Draw FPS counter
		gfx.setColor(Color.BLUE);
		gfx.setFont(new Font(FONT_TAHOMA, Font.BOLD + Font.ITALIC, 15));
		if(gameOptions.getFpsDisplay()) {
			if(usedTime > 0) {
				gfx.drawString(String.valueOf(1000 / usedTime) + " fps", DisplayView.WIDTH - 70, DisplayView.HEIGHT - 45);
			} else {
				gfx.drawString("--- fps", 50, 40);
			}
		}
		
		//	Paint the Player's current score, lives, level and round number in the top-right
		gfx.setColor(Color.BLACK);
		gfx.fillRect(DisplayView.WIDTH - 147 - SPRITE_UI_XPOS, 10, 158, 107);
		gfx.setColor(Color.WHITE);
		gfx.fillRect(DisplayView.WIDTH - 145 - SPRITE_UI_XPOS, 11, 155, 105);
		gfx.setColor(Color.BLACK);
		gfx.setFont(new Font(FONT_TAHOMA, Font.BOLD, 18));
		gfx.drawString(thePlayer.getPlayerName(), DisplayView.WIDTH - 135 - SPRITE_UI_XPOS, 30);
		gfx.drawString("Lives: " + Integer.toString(thePlayer.getLives()), DisplayView.WIDTH - 135 - SPRITE_UI_XPOS, 50);
		gfx.drawString("Level: " + Integer.toString(gameMode.getDifficultyLevel()) 
				+ "-" + Integer.toString(gameMode.getRoundNumber()), DisplayView.WIDTH - 135 - SPRITE_UI_XPOS, 70);
		gfx.setColor(Color.RED);
		gfx.drawString(Integer.toString(thePlayer.getScore()), DisplayView.WIDTH - 135 - SPRITE_UI_XPOS, 90);
		gfx.setColor(Color.LIGHT_GRAY);
		
		// Draw round time left if the round has not been completed and ducks aren't flying away
		if((!roundCompleted) && (!flyAway)) {
			// Check our GameMode has a time limit - display time left if so
			if(gameMode.hasTimeLimit()) {
				long timeLeft = ((timeStarted + gameMode.getTimeLimit()) - System.currentTimeMillis());
				timeString = millisecondsToString(timeLeft);
			} else {
				timeString = new String("--:--:--");
			}
		}
		gfx.drawString(timeString, DisplayView.WIDTH - 135 - SPRITE_UI_XPOS, 110);
		
		// Get X and Y position of the box surrounding the sprites
		int boxX = ammoSprite.getXPosition() - 10;
		int boxY = ammoSprite.getYPosition() - 10;
		int boxWidth = ((gameMode.getAmmoCount() * ammoSprite.getWidth()) + 20);
		int boxHeight = (ammoSprite.getHeight() + 20);
		
		// Create the surrounding box
		gfx.setColor(Color.BLACK);
		gfx.fillRect(boxX - 1, boxY - 1, boxWidth + 2, boxHeight + 2);
		gfx.setColor(Color.WHITE);
		gfx.fillRect(boxX, boxY, boxWidth, boxHeight);
		
		// Draw the ammoSprite in the box
		int imageXOffset = ammoSprite.getXPosition();
		for(int i = 1; i <= thePlayer.getShotCount(); i++) {
			ammoSprite.setXPosition(imageXOffset);
			ammoSprite.paint(gfx);
			// g.drawImage(currentImage, imageXOffset, this.getY(), displayView);
			imageXOffset += ammoSprite.getWidth();
		}
		
		// Reset ammoSprite X position for next render
		ammoSprite.setXPosition(SPRITE_UI_XPOS);
		
		// Get the X and Y position of the box surrounding the sprites
		boxX = (duckAliveSprite.getXPosition() - 10);
		boxY = duckAliveSprite.getYPosition() - 10;
		boxWidth = (gameMode.getDuckCount() * duckAliveSprite.getWidth()) + 20;
		boxHeight = (duckAliveSprite.getHeight()) + 20;
		
		// Create the surrounding box
		gfx.setColor(Color.BLACK);
		gfx.fillRect(boxX - 1, boxY - 1, boxWidth + 2, boxHeight + 2);
		gfx.setColor(Color.WHITE);
		gfx.fillRect(boxX, boxY, boxWidth, boxHeight);
		
		// Draw the duckIcons in the box
		imageXOffset = duckAliveSprite.getXPosition();
		for(int i = 0; i < gameMode.getDuckCount(); i++) {
			duckAliveSprite.setXPosition(imageXOffset);
			duckDeadSprite.setXPosition(imageXOffset);
			if(i < numDucksShot) {
				duckDeadSprite.paint(gfx);
			} else {
				duckAliveSprite.paint(gfx);
			}
			imageXOffset += duckAliveSprite.getWidth();
		}
		
		// Reset duckIcons X position for next render
		duckAliveSprite.setXPosition(SPRITE_UI_XPOS);
		duckDeadSprite.setXPosition(SPRITE_UI_XPOS);
	}
	
	/**
	 * Paints the upper and lower toolbars at 10% of the screen height.
	 * @param gfx	The Graphics context onto which to paint the toolbars
	 */
	private void paintToolbars(final Graphics2D gfx) {
		// Draw the upper 'toolbar' interface at 10% of the screen height
		gfx.setColor(Color.LIGHT_GRAY);
		gfx.fillRect(0, 0, DisplayView.WIDTH, (int)(0.1 * DisplayView.HEIGHT));
		gfx.setColor(Color.BLACK);
		gfx.drawLine(0, (int)(0.1 * DisplayView.HEIGHT), DisplayView.WIDTH, (int)(0.1 * DisplayView.HEIGHT));
		
		// Draw the lower 'toolbar' interface at 10% of the screen height
		gfx.setColor(Color.LIGHT_GRAY);
		gfx.fillRect(0, (int)(0.9 * DisplayView.HEIGHT), DisplayView.WIDTH, DisplayView.HEIGHT);
		gfx.setColor(Color.BLACK);
		gfx.drawLine(0, (int)(0.9 * DisplayView.HEIGHT), DisplayView.WIDTH, (int)(0.9 * this.getHeight()));
	}
	
	/**
	 * Paints debug info onto the screen. Debug info includes the location
	 * of the last point clicked, as well as the horizontal and vertical
	 * speeds of each duck in the DuckList.
	 * @param gfx	The Graphics context onto which to paint
	 */
	private void paintDebug(final Graphics2D gfx) {
		// Write debug information to screen if flag set
		if(gameOptions.getDebugMode()) {
			// Draw Duck debug info
			int yDrawPos = 110;
			Duck aDuck = null;
			
			// Draw the location of where the pointer was last clicked
			gfx.setColor(Color.BLACK);
			gfx.setFont(new Font(FONT_ARIAL, Font.PLAIN, 12));
			gfx.drawString("Pointer clicked at: " + pointerCache, 10, yDrawPos);
			yDrawPos += 15;
			gfx.drawString("Number of Ducks: " + Integer.toString(duckList.size()), 10, yDrawPos);
			
			// Loop through all Ducks in the list and print their current location, velocity and status
			String duckDetails = null;
			for(int i = 0; i < duckList.size(); i++) {
				yDrawPos += 15;
				aDuck = (Duck)duckList.get(i);
				duckDetails = "#" + i + ": ";
				duckDetails = duckDetails + "dx: " + aDuck.getDX() + ", dy: " + aDuck.getDY() + ",";
				duckDetails = duckDetails + " x: " + aDuck.getXPosition() + ", y: " + aDuck.getYPosition();
				duckDetails = duckDetails + " status: " + aDuck.getState();
				gfx.drawString(duckDetails, 10, yDrawPos);
			}
		}
	}

	/**
	 * Converts the specified number of milliseconds to a String in the form hh:mm:ss:ms
	 * @param	time	the amount of time in milliseconds to convert
	 * @return	a String representation of the time in hh:mm:ss:ms
	 */
	private String millisecondsToString(long time) {
		long hours = 0;
		long minutes = 0;
		long seconds = 0;
		long milliseconds = 0;
		long timeLeft = ((timeStarted + gameMode.getTimeLimit()) - System.currentTimeMillis());
		hours = timeLeft / 3600000;
		timeLeft = timeLeft - (hours * 3600);
		minutes = timeLeft / 60000;
		timeLeft = timeLeft - (minutes * 60000);
		seconds = timeLeft / 1000;
		milliseconds = timeLeft - (seconds * 1000);
		return (new String(minutes + ":" + seconds + ":" + milliseconds));
	}
	
	/******* DEBUG METHODS *******/
	
	/**
	 * Removes the specified number of Ducks from the DuckList
	 * NOTE: This method is only available when running the game in debug mode
	 * @param numDucks	the number of Ducks to be removed
	 */
	private void removeDucks(final int numDucks) {
		// Check the Duck list is not currently empty
		int numDucksToRemove = numDucks;
		if(duckList.size() <= 0) {
			// Print message if empty
			System.out.println("All ducks have been removed!");
		} else {
			// Remove the last Duck that was added to the list and report to console
			int lastDuck = 0;
			System.out.println("Removing " + numDucksToRemove 
				+ " ducks... (" + (duckList.size() + 1) + " total)");
			do {
				lastDuck = duckList.size();
				duckList.remove(lastDuck - 1);
				numDucksToRemove--;
			} while ((numDucks > 0) && (duckList.size() > 0));
		}
	}
	
	/**
	 * Modifies the velocity of all the Ducks currently in the DuckList
	 * NOTE: This method is only available when running the game in debug mode
	 * and is called by the keyPressed method.
	 * @param increment	whether or not to invert the direction of the Ducks
	 * @param vertical	specifies the vertical velocity be modified, otherwise horizontal velocity is modified
	 */
	private void modifyVelocity(final boolean increment, final boolean vertical) {
		int currentVal = 0;
		Duck aDuck = null;
		
		// Loop through the Ducks in the list
		for(int i = 0; i < duckList.size(); i++) {
			// Get a Duck from the ArrayList
			aDuck = (Duck)duckList.get(i);
			
			// If the vertical velocity is to be modified, then get the current velocity value.
			// Otherwise, get the horizontal velocity value.
			if(vertical) {
				currentVal = aDuck.getDY();
			} else {
				currentVal = aDuck.getDX();
			}
			
			// If the velocity is to be incremented then do so, otherwise decrement it.
			// Additionally, if the velocity is currently negative, then  swap the 
			// increment or decrement around to prevent the Sprite from going off the screen.
			if(increment) {
				if(currentVal < 0) { currentVal--; } else { currentVal++; }
			} else {
				if(currentVal < 0) { currentVal++; } else { currentVal--; }
			}
			
			// If the vertical velocity is to be modified, then set the velocity value
			if(vertical) {
				aDuck.setDY(currentVal);
			} else {
				aDuck.setDX(currentVal);
			}
		}
	}
	
	/******* ACCESSORS *******/
	
	/**
	 * Returns the SpriteCache that the game core is currently using
	 **/
	public SpriteCache getSpriteCache() {
		return spriteCache;
	}
	
	/**
	 * Returns the SoundCache that the game is currently using
	 */
	public SoundCache getSoundCache() {
		return soundCache;
	}
	
	/**
	 * Sets the GameOptions for this instance of the game
	 * @param gOptions	options to be used
	 */
	public void setGameOptions(final GameOptions gOptions) { 
		gameOptions = gOptions; 
	}
	
	/**
	 * Gets the game's current GameOptions
	 * @return	the game's current options
	 */
	public GameOptions getGameOptions() { 
		return gameOptions; 
	}
	
	/******* INPUT *******/
	
	/**
	 * The keyPressed method receives input from the user and modifies the game
	 * variables accordingly. What the user can do depends on whether debug mode is
	 * currently activated or not. If debug mode is disabled, all the user can do
	 * is pause the game, display FPS and exit the game.
	 */
	public void keyPressed(final KeyEvent event) {
		// Parse the keys if we're in debug mode
		if(gameOptions.getDebugMode()) {
			int currentAmmo = 0;
			
			// Modify velocities if any of the cursor keys are pressed.
			// If either Enter or Backspace are pressed then spawn or remove
			// a Duck. If the numpad '+' and '-' keys are pressed, then add
			// or remove ammo to the user respectively.
			switch(event.getKeyCode()) {
				case KeyEvent.VK_UP:
					// Increment Duck vertical velocities
					modifyVelocity(true, true);
					break;
				case KeyEvent.VK_DOWN:
					// Decrement Duck vertical velocities
					modifyVelocity(false, true);
					break;
				case KeyEvent.VK_RIGHT:
					// Increment Duck horizontal velocities
					modifyVelocity(true, false);
					break;
				case KeyEvent.VK_LEFT:
					// Decrement Duck horizontal velocities
					modifyVelocity(false, false);
					break;
				case KeyEvent.VK_ENTER:
					// Add another duck
					spawnDucks(1);
					break;
				case KeyEvent.VK_BACK_SPACE:
					// Remove a duck
					removeDucks(1);
					break;
				case KeyEvent.VK_ADD:
					// Add ammo
					currentAmmo = thePlayer.getShotCount();
					currentAmmo++;
					thePlayer.setShotCount(currentAmmo);
					soundCache.playSound(soundsGunReload);
					break;
				case KeyEvent.VK_SUBTRACT:
					// Remove ammo
					currentAmmo = thePlayer.getShotCount();
					currentAmmo--;
					thePlayer.setShotCount(currentAmmo);
					break;
				default:
					// Do nothing
			}
		}
		
		// If we're not currently in debug mode, then just parse as normal
		switch(event.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				// Pause/unpause the game
				this.setPaused(!gamePaused);
				break;
			case KeyEvent.VK_ESCAPE:
				// Show the options window
				showOptions();
				break;
			case KeyEvent.VK_O:
				// Show options window
				showOptions();
				break;
			case KeyEvent.VK_F:
				// Display FPS on screen
				gameOptions.setFpsDisplay(!gameOptions.getFpsDisplay());
				break;
			default:
				// Do nothing
		}
	}
	
	/**
	 * Sets whether the game is currently paused or not. If it is paused then the
	 * update() method is not called, but the renderWorld method is still called
	 * @param	pauseGame	the paused state to set the game to
	 */
	private void setPaused(final boolean pauseGame) {
		// If the game is not currently paused and the player has lost
		// then don't let the player pause the game until all the Duck's
		// have fallen off the screen.
		if(gamePaused != pauseGame) {
			gamePaused = pauseGame;
		}
	}

	/**
	 * The mousePressed method interprets user input onto the Window.
	 * If the game is currently over, then another click of the mouse restarts the game.
	 * Otherwise, the location of where the mouse was clicked is stored for the next
	 * game update, or if we're currently in debug mode and the right-mouse is clicked,
	 * the user's shots are reset.
	 */
	public void mousePressed(final MouseEvent event) {
		// Reload the gun if we're allowed to
		if(event.getButton() == MouseEvent.BUTTON3) {
			if(gameOptions.getDebugMode()) {
				soundCache.playSound(soundsGunReload);
				thePlayer.resetShots();
			}
		} else {
			// Get the location of where the mouse was clicked on the Canvas
			pointerLoc = event.getPoint();
			pointerCache = event.getPoint();
		}
	}
	
	/**
	 * Pauses the game and then creates and displays the options window. 
	 * If any sound effects are enabled, then the ambient sound is started. 
	 * Otherwise, if all or some sound effects have been stopped, then this 
	 * method makes sure they have stopped playing. Additionally, all decals
	 * are removed from the screen if decals have been turned off.
	 */
	private void showOptions() {
		// Show the options window and pause the game
		this.setPaused(true);
		final OptionsWindow optionsWindow = new OptionsWindow(gameOptions);
		optionsWindow.setVisible(true);
		
		// Update the soundCache with the current sound status
		soundCache.enableSound(gameOptions.getSoundEnabled());
		
		// If the ambient sound has been disabled or sound has been disabled altogether
		// then stop all currently playing ambience sounds
		if((!gameOptions.getSoundAmbience()) || (!gameOptions.getSoundEnabled())) {
			soundCache.stopAllSounds(soundsAmbience);
		} else if(gameOptions.getSoundAmbience()) {
			// Otherwise, make sure ambient sounds are currently playing
			soundCache.loopSound(soundsAmbience);
		}
		
		// Stop any other sounds that are currently playing if specified
		if((!gameOptions.getSoundDuck()) || (!gameOptions.getSoundEnabled())) {
			soundCache.stopAllSounds(soundsDuckDead);
			soundCache.stopAllSounds(soundsDuckAlive);
		}

		if((!gameOptions.getSoundShot()) || (!gameOptions.getSoundEnabled())) {
			soundCache.stopAllSounds(soundsGunHit);
			soundCache.stopAllSounds(soundsGunMiss);
			soundCache.stopAllSounds(soundsGunNoAmmo);
			soundCache.stopAllSounds(soundsGunReload);
		}
		
		// Remove all decals from cache if decals are turned off
		if(!gameOptions.getDecalsEnabled()) {
			decalList.removeAll(decalList);
		}
		
		// Unpause the game
		this.setPaused(false);
	}
	
	/**
	 * Closes down the game. This method displays a simple DialogBox that prompts the
	 * user as to whether they want to exit the game or not. If true, then the score board
	 * is written to a file, all the sound threads are stopped, the main thread is stopped
	 * and the main menu is displayed.
	 */
	private void closeApp() {
		final DialogBox quitConfirm = new DialogBox(mainWindow, "Quit?", "Exit to main menu?");
		quitConfirm.setAlwaysOnTop(true);
		quitConfirm.setVisible(true);
		
		if(quitConfirm.getResult()) {
			// Notify user
			System.out.println("Closing DuckHunter...");
			
			// Stop sound effects
			System.out.println("Stopping sound threads...");
			soundCache.stopAllSounds(soundsAmbience);
			soundCache.stopAllSounds(soundsDuckAlive);
			soundCache.getPool().close();
			
			// Get rid of main window
			mainWindow.dispose();
			Thread.currentThread().interrupt();
			
			try {
				Thread.currentThread().join();
			} catch(InterruptedException ex) {
				// Should never happen
			}
			
			// Create new main menu and show
			final MainMenu mainMenu = new MainMenu(this.getSpriteCache(),
					this.getSoundCache(), this.getGameOptions());
			mainMenu.setVisible(true);
			final Thread menuThread = new Thread(mainMenu);
			menuThread.setDaemon(false);
			menuThread.setPriority(Thread.NORM_PRIORITY);
			menuThread.start();

		} else {
			quitConfirm.dispose();
			this.setPaused(false);
		}
	}
	
	/**
	 * Inherited method from superclass - calls closeApp when the window is closing
	 */
	public void windowClosing(final WindowEvent event) {
		// Call closeApp method
		closeApp(); 
	}
	
	/**
	 * Inherited method from superclass - pauses the game when window focus is lost
	 */
	public void windowLostFocus(final WindowEvent event) {
		// Pause game
		this.setPaused(true); 
	}
	
	/**
	 * Inherited method from superclass
	 * @param	gfx	the Graphics context onto which to paint
	 */
	public void paint(final Graphics gfx) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from superclass
	 * @param	gfx	the Graphics context onto which to paint
	 */
	public void update(final Graphics gfx) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass KeyListener
	 */
	public void keyTyped(final KeyEvent event) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass KeyListener
	 */
	public void keyReleased(final KeyEvent event) { 
		// Do nothing
	}
	
	/**
	 *Inherited method from implemented superclass MouseListener
	 */
	public void mouseClicked(final MouseEvent event) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass MouseListener
	 */
	public void mouseReleased(final MouseEvent event) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass MouseListener
	 */
	public void mouseEntered(final MouseEvent event) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass MouseListener
	 */
	public void mouseExited(final MouseEvent event) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass WindowListener
	 */
	public void windowOpened(final WindowEvent event) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass WindowListener
	 */
	public void windowClosed(final WindowEvent event) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass WindowListener
	 */
	public void windowIconified(final WindowEvent event) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass WindowListener
	 */
	public void windowDeiconified(final WindowEvent event) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass WindowListener
	 */
	public void windowActivated(final WindowEvent event) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass WindowListener
	 */
	public void windowDeactivated(final WindowEvent event) { 
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass WindowListener
	 */
	public void windowGainedFocus(final WindowEvent event) { 
		// Do nothing
	}
}