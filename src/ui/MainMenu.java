package ui;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import core.*;
import config.AbstractGameMode;
import config.GameOptions;
import config.gamemodes.*;
import sound.SoundCache;
import graphics.SpriteCache;

/**
 * The MainMenu class is a simple window containing several buttons laid from top-to-bottom
 * listing the various game modes currently available for play. When a game mode is selected,
 * the main game thread is spawned and the window is closed.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class MainMenu extends JFrame implements ActionListener, WindowListener, Runnable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Exit button component
	 */
	private transient final JButton cmdExit;
	
	/**
	 * Options button component
	 */
	private transient final JButton cmdOptions;
	
	/**
	 * GameOptions object used by the GameCore
	 */
	private transient final GameOptions gameOptions;
	
	/**
	 * The SpriteCache to be used by the GameCore
	 */
	private transient final SpriteCache spriteCache;
	
	/**
	 * The SoundCache to be used by the GameCore
	 */
	private transient final SoundCache soundCache;
	
	/**
	 * All the available GameModes for this game
	 */
	private transient final ArrayList gameModes;
	
	/**
	 * Buttons for all the available GameModes
	 */
	private transient final ArrayList gameButtons;
	
	/**
	 * Constructor for the MainMenu object
	 * @param	spCache	the SpriteCache used by the GameCore for loading Sprites
	 * @param 	sCache	the SoundCache used by the GameCore for playing sounds
	 * @param 	gOptions	the GameOptions used by the GameCore for game logic
	 */
	public MainMenu(SpriteCache spCache, SoundCache sCache, GameOptions gOptions) {
		super();
		// Set pointers to caches
		gameOptions = gOptions;
		spriteCache = spCache;
		soundCache = sCache;
		
		// TODO: Change to use XML file instead to read game modes
		// Add game modes
		AbstractGameMode tempMode = null;
		gameModes = new ArrayList();
		tempMode = new ClassicMode();
		gameModes.add(tempMode);
		tempMode = new ClassicMode2();
		gameModes.add(tempMode);
		tempMode = new Aduckalypse();
		gameModes.add(tempMode);
		tempMode = new FastFinger();
		gameModes.add(tempMode);
		if(gameOptions.getDebugMode()) {
			tempMode = new TestMode();
			gameModes.add(tempMode);
		}
		
		// Set up the window
		this.setTitle(GameCore.GAME_NAME);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// Add top panel to frame
		JPanel panTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel lblGameTitle = new JLabel(GameCore.GAME_NAME);
		lblGameTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
		panTitle.add("Center", lblGameTitle);
		this.add("North", panTitle);
		
		// Add menu panel to frame
		GridLayout panMenuLayout = new GridLayout(gameModes.size() + 2 ,1);
		panMenuLayout.setVgap(10);
		panMenuLayout.setHgap(10);
		
		// Add buttons to panel
		JPanel panMenu = new JPanel(panMenuLayout);
		gameButtons = new ArrayList();
		JButton tempButton = null;
		
		if(gameModes.size() != 0) {
			for(int i = 0; i < gameModes.size(); i++) {
				tempMode = (AbstractGameMode)gameModes.get(i);
				tempButton = new JButton(tempMode.getName());
				tempButton.addActionListener(this);
				tempButton.setToolTipText(tempMode.getDescription());
				gameButtons.add(tempButton);
				panMenu.add(tempButton);
			}
		}
		
		// Add spacer panels
		JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		// Add options and exit button
		cmdOptions = new JButton("Options");
		cmdOptions.setToolTipText("Set game options");
		cmdOptions.addActionListener(this);
		panMenu.add(cmdOptions);
		cmdExit = new JButton("Exit");
		cmdExit.setToolTipText("Exit the game");
		cmdExit.addActionListener(this);
		panMenu.add(cmdExit);
		
		// Add panels to window
		this.add("East", eastPanel);
		this.add("West", westPanel);
		this.add("South", southPanel);
		this.add("Center", panMenu);
		
		// Set the form's size and location
		this.pack();
		Dimension screen = getToolkit().getScreenSize();
    	this.setLocation((screen.getSize().width - getSize().width) / 2, (screen.getSize().height - getSize().height) / 2);
	}

	/**
	 * Starts the main game thread with in the specified game mode
	 * @param 	gMode	the GameMode with which to run the game thread
	 * @see		AbstractGameMode
	 */
	private void startGame(final AbstractGameMode gMode) {
		// TODO: Player enters name at start of game
		System.out.println("Spawning game thread...");
		this.dispose();
		final Player aPlayer = new Player("Mace", gMode.getAmmoCount(), gMode.getLifeCount());
		System.out.println("Starting game mode: " + gMode.getName());
		final GameCore dhGame = new GameCore(gMode, gameOptions, spriteCache, 
				soundCache, aPlayer);
		final Thread mainThread = new Thread(dhGame);
		mainThread.setDaemon(false);
		// t.setPriority(Thread.MAX_PRIORITY);
		mainThread.setPriority(Thread.NORM_PRIORITY);
		mainThread.start();
	}
	
	/**
	 * Closes down the game. Displays a confirmation DialogBox to the user and
	 * closes the game if the user selected yes. Otherwise the DialogBox is just hidden
	 **/
	private void closeApp() {
		// Ask user for confirmation
		final DialogBox confirmExit = new DialogBox(this, "Quit?", "Duck off?");
		confirmExit.setAlwaysOnTop(true);
		confirmExit.setVisible(true);
		
		// If yes, then go ahead and exit the program
		if (confirmExit.getResult()) {
			System.out.println("Closing DuckHunter...");
			// Print status and exit
			System.out.println("Exited");
			System.exit(0);
		}
	}
	
	/**
	 * Method for detecting JButton presses on components
	 * @param	event	the component from which the event originated
	 **/
	public void actionPerformed (final ActionEvent event) {
		// Create temporary objects
		JButton tempButton = null;
		AbstractGameMode tempMode = null;
		
		// Loop through all buttons and check if button was pressed
		for(int i = 0; i < gameButtons.size(); i++) {
			tempButton = (JButton)gameButtons.get(i);
			if(event.getSource() == tempButton) {
				// If one was pressed, then launch the corresponding game mode
				tempMode = (AbstractGameMode)gameModes.get(i);
				startGame(tempMode);
			} else if(event.getSource() == cmdExit) {
				// If the exit button was pressed, then exit app
				closeApp();
				break;
			} else if(event.getSource() == cmdOptions) {
				// If options button was pressed, then show options window
				event.setSource(null);
				showOptions();
			}
		}
	}
	
	/**
	 * Shows the game options window on top of this window
	 */
	private void showOptions() {
		// Create our window and set its visibility
		final OptionsWindow optionsWindow = new OptionsWindow(gameOptions);
		optionsWindow.setVisible(true);
	}
	
	/**
	 * Allows this class to be run on a thread
	 */
	public void run() {
		// Do nothing
	}
	
	/**
	 * Inherited method from superclass - simply calls the closeApp method
	 */
	public void windowClosing(final WindowEvent event) { closeApp(); }
	
	/**
	 * Inherited method from implemented superclass WindowListener
	 */
	public void windowClosed(final WindowEvent event) {
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
	public void windowDeactivated(final WindowEvent event) {
		// Do nothing
	}
	
	/**
	 * Inherited method from implemented superclass WindowListener
	 */
	public void windowActivated(final WindowEvent event) {
		// Do nothing
	}
}
