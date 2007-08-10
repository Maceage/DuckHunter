package core;

import ui.MainMenu;
import ui.SplashScreen;
import config.GameOptions;
import config.PreLoader;

/**
 * Main boot class for loading game - this class is responsible for taking any commad-line
 * parameters that have been passed, creating a new GameOptions object based on those
 * parameters and then starting an instance of the game with those options. The class
 * also utilises 
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class GameLauncher {
	private static PreLoader resLoader;
	private static SplashScreen loadingScreen;
	
	/**
	 * Prevents this class from being instantiated
	 */
	private GameLauncher() {
		// Do nothing
	}
	
	/**
	 * Starts the game using the specified command-line arguments
	 * @param args	the game options to use when running the game
	 */
	public static void main(final String args[]) {
		// Print command-line argument information
		System.out.println("Starting DuckHunter...");
		System.out.println("Parameters:");
		System.out.println("GameLauncher [-dev | -fps | -nosound | -noambience | -noshotsound | -noducksound]");
		System.out.println("   -dev 		\t to run in debug mode");
		System.out.println("   -fps 		\t to turn on the fps counter");
		System.out.println("   -nodecals 	\t to turn off decal graphics");
		System.out.println("   -nodecallimit	\t turn off decal limiter");
		System.out.println("   -nosound 		\t to turn off all sound");
		System.out.println("   -noambience 		\t to turn off ambience sound");
		System.out.println("   -noshotsound 	\t to turn off gunshot sound");
		System.out.println("   -noducksound 	\t to turn off duck sound");
		
		// Create splash screen
		System.out.println("Creating splash screen...");
		loadingScreen = new SplashScreen();
		loadingScreen.setVisible(true);
		
		// Set defaults for command-line switches
		boolean debugMode = false;
		boolean fpsDisplayOn = false;
		boolean decalsEnabled = true;
		boolean decalLimiter = true;
		boolean soundEnabled = true;
		boolean soundAmbience = true;
		boolean soundGunShot = true;
		boolean soundDuck = true;
		
		// Check all command-line switches and set variables appropriately
		if(args.length != 0) {
			for(int i = 0; i < args.length; i++) {
				if(args[i].equals("-dev")) {
					debugMode = true;
				} else if(args[i].equals("-fps")) {
					fpsDisplayOn = true;
				} else if(args[i].equals("-nodecals")) {
					decalsEnabled = false;
				} else if(args[i].equals("-nodecallimit")) {
					decalLimiter = false;
				} else if(args[i].equals("-nosound")) {
					soundEnabled = false;
				} else if(args[i].equals("-noambience")) {
					soundAmbience = false;
				} else if(args[i].equals("-noshotsound")) {
					soundGunShot = false;
				} else if(args[i].equals("-noducksound")) {
					soundDuck = false;
				}
			}
		}
		
		// Create GameOptions based on parameters passed
		debugMode = true;
		final GameOptions gameOpts = new GameOptions(debugMode, fpsDisplayOn, soundEnabled, 
				soundAmbience, soundGunShot, soundDuck, decalsEnabled, decalLimiter);
		System.out.println(gameOpts);
		
		// Precache resources
		System.out.println("Precaching resources...");
		resLoader = new PreLoader();
		resLoader.loadResourcesByFile("config/reslist.txt");
		// resLoader.loadResourcesByDirectory("./resources/");
		System.out.println("Precache complete.");

		// Start the game's main menu
		final MainMenu gameMenu = new MainMenu(resLoader.getSpriteCache(), resLoader.getSoundCache(), gameOpts);
		final Thread menuThread = new Thread(gameMenu);
		menuThread.setDaemon(false);
		menuThread.setPriority(Thread.NORM_PRIORITY);
		
		
		// Get rid of splash screen
		System.out.println("Removing splash screen...");
		loadingScreen.setVisible(false);
		loadingScreen.dispose();
		
		// Show game menu
		gameMenu.setVisible(true);
		
		System.out.println("Ready - \"Press play on tape\"");
	}
}
