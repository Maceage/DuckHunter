package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import config.GameOptions;

/**
 * The OptionsWindow class is used for displaying various game settings that are available to 
 * the user in a standard JDialog. The window consists of JCheckBoxes for the options along
 * with two JButtons for saving and closing the window.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 **/
public class OptionsWindow extends JDialog implements ActionListener, WindowListener
{
	/**
	 * Version ID for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * OK button component
	 */
	private transient final JButton cmdOK;
	
	/**
	 * Cancel button component
	 */
	private transient final JButton cmdCancel;
	
	/**
	 * Checkbox for FPS display checkbox component
	 */
	private transient final JCheckBox chkFpsDisplay;
	
	/**
	 * Checkbox for the Sound enabled checkbox component
	 */
	private transient final JCheckBox chkSoundEnabled;
	
	/**
	 * Checkbox for the Ambience sound checkbox component
	 */
	private transient final JCheckBox chkSoundAmbience;

	/**
	 * Checkbox for the Shot sound checkbox component
	 */
	private transient final JCheckBox chkSoundShot;
	
	/**
	 * Checkbox for the Duck sound checkbox component
	 */
	private transient final JCheckBox chkSoundDuck;
	
	/**
	 * Checkbox for the Decals option checkbox component
	 */
	private transient final JCheckBox chkDecals;

	/**
	 * Checkbox for the Decal limiter option checkbox component
	 */
	private transient final JCheckBox chkDecalLimiter;
	
	/**
	 * Value of FPS display option
	 */
	private transient boolean optFpsDisplay;

	/**
	 * Value of sound enabled option
	 */
	
	private transient boolean optSoundEnabled;

	/**
	 * Value of ambience sound enabled option
	 */
	private transient boolean optSoundAmbience;

	/**
	 * Value of gunshot sound enabled option
	 */
	private transient boolean optSoundShot;

	/**
	 * Value of duck sound enabled option
	 */
	private transient boolean optSoundDuck;
	
	/**
	 * Value of decals enabled option
	 */
	private transient boolean optDecals;
	
	/**
	 * Value of decal limiter enabled option
	 */
	private transient boolean optDecalLimiter;
	
	/**
	 * GameOptions object containing the various settings for the game
	 */
	private transient final GameOptions gameOptions;
	
	/**
	 * Constructor for StartWindow object
	 * @param	gOptions	the GameOptions to be shown by the window
	 **/
	public OptionsWindow(GameOptions gOptions) {
		super();
		
		// Get options from object reference passed
		gameOptions = gOptions;
		readOptions();
		
		// Set up the window
		this.setTitle("Options");
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.addWindowListener(this);
		this.setModal(true);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// Create panels
		JPanel panTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		GridLayout panOptionsLayout = new GridLayout(7, 1);
		panOptionsLayout.setVgap(10);
		panOptionsLayout.setHgap(10);
		JPanel panOptions = new JPanel(panOptionsLayout);

		// Create components
		JLabel appTitle = new JLabel("Options");
		cmdOK = new JButton("OK");
		cmdCancel = new JButton("Cancel");
		chkFpsDisplay = new JCheckBox("FPS display", optFpsDisplay);
		chkDecals = new JCheckBox("Decals enabled", optDecals);
		chkDecalLimiter = new JCheckBox("Decal limiter", optDecalLimiter);
		chkSoundEnabled = new JCheckBox("Sound enabled", optSoundEnabled);
		chkSoundAmbience = new JCheckBox("Ambience sound enabled", optSoundAmbience);
		chkSoundShot = new JCheckBox("Shot sound enabled", optSoundShot);
		chkSoundDuck = new JCheckBox("Duck sound enabled", optSoundDuck);
		
		// Set tooltips
		cmdOK.setToolTipText("Save changes and exit");
		cmdCancel.setToolTipText("Discard changes and exit");
		chkFpsDisplay.setToolTipText("Turn FPS display on/off");
		chkDecals.setToolTipText("Turn shot/blood decals on/off");
		chkDecalLimiter.setToolTipText("Turn off decal limiter (NOT recommended!)");
		chkSoundEnabled.setToolTipText("Turn all sounds on/off");
		chkSoundAmbience.setToolTipText("Turn ambience sound on/off");
		chkSoundShot.setToolTipText("Turn gunshot sound on/off");
		chkSoundDuck.setToolTipText("Turn duck quacking sound on/off");
		
		// Add action listeners
		cmdOK.addActionListener(this);
		cmdCancel.addActionListener(this);
		chkFpsDisplay.addActionListener(this);
		chkDecals.addActionListener(this);
		chkDecalLimiter.addActionListener(this);
		chkSoundEnabled.addActionListener(this);
		chkSoundAmbience.addActionListener(this);
		chkSoundShot.addActionListener(this);
		chkSoundDuck.addActionListener(this);
		
		// Add components
		panTitle.add(appTitle);
		panOptions.add(chkFpsDisplay);
		panOptions.add(chkDecals);
		panOptions.add(chkDecalLimiter);
		panOptions.add(chkSoundEnabled);
		panOptions.add(chkSoundAmbience);
		panOptions.add(chkSoundShot);
		panOptions.add(chkSoundDuck);
		buttonPanel.add(cmdOK);
		buttonPanel.add(cmdCancel);
		
		// Enable or disable the sound checkboxes
		enableSoundOptions(optSoundEnabled);
		
		// Add panels to window
		this.add("North", panTitle);
		this.add("East", eastPanel);
		this.add("West", westPanel);
		this.add("South", buttonPanel);
		this.add("Center", panOptions);
		
		// Set the form's size and location
		this.pack();
		Dimension screen = getToolkit().getScreenSize();
    	this.setLocation((screen.getSize().width - getSize().width) / 2, (screen.getSize().height - getSize().height) / 2);
	}
	
	/**
	 * Method for detecting JButton presses
	 * @param	event	the component from which the event originated
	 **/
	public void actionPerformed (final ActionEvent event) {
		if(event.getSource() == cmdCancel) {
			// Close the window
			this.setVisible(false);
			this.dispose();
		} else if(event.getSource() == cmdOK) {
			// Write the options to the GameOptions
			writeOptions();
			// Close the window
			this.setVisible(false);
			this.dispose();
		} else if(event.getSource() == chkFpsDisplay) {
			optFpsDisplay ^= true; // Invert value using bitwise operator
		} else if(event.getSource() == chkSoundEnabled) {
			optSoundEnabled ^= true; // Invert value using bitwise operator
			enableSoundOptions(optSoundEnabled);
		} else if(event.getSource() == chkSoundAmbience) {
			optSoundAmbience ^= true; // Invert value using bitwise operator
		} else if(event.getSource() == chkSoundShot) {
			optSoundShot ^= true; // Invert value using bitwise operator
		} else if(event.getSource() == chkSoundDuck) {
			optSoundDuck ^= true; // Invert value using bitwise operator
		} else if(event.getSource() == chkDecals) {
			optDecals ^= optDecals; // Invert value using bitwise operator
		} else if(event.getSource() == chkDecalLimiter) {
			optDecalLimiter ^= true; // Invert value using bitwise operator
		}
	}
	
	/**
	 * Enables or disables the sound option checkboxes
	 * @param 	enableOptions	the state to set the checkboxes to
	 */
	private void enableSoundOptions(final boolean enableOptions) {
		chkSoundAmbience.setEnabled(enableOptions);
		chkSoundShot.setEnabled(enableOptions);
		chkSoundDuck.setEnabled(enableOptions);
	}
	
	/**
	 * This method reads back all the properties stored on the GameOptions object
	 * and puts them in the appropriate variables
	 * @see	GameOptions
	 */
	private void readOptions() {
		optFpsDisplay = gameOptions.getFpsDisplay();
		optDecals = gameOptions.getDecalsEnabled();
		optDecalLimiter = gameOptions.getDecalLimitEnabled();
		optSoundEnabled = gameOptions.getSoundEnabled();
		optSoundAmbience = gameOptions.getSoundAmbience();
		optSoundShot = gameOptions.getSoundShot();
		optSoundDuck = gameOptions.getSoundDuck();
	}
	
	/**
	 * This method goes through all the options selected in the window
	 * and modifies them accordingly in the GameOptions object
	 * @see	GameOptions
	 */
	private void writeOptions() {
		gameOptions.setFpsDisplay(optFpsDisplay);
		gameOptions.setDecalsEnabled(optDecals);
		gameOptions.setDecalLimitEnabled(optDecalLimiter);
		gameOptions.setSoundEnabled(optSoundEnabled);
		gameOptions.setSoundAmbience(optSoundAmbience);
		gameOptions.setSoundShot(optSoundShot);
		gameOptions.setSoundDuck(optSoundDuck);
	}
	
	/**
	 * Inherited method from implemented superclass WindowListener
	 */
	public void windowClosing(final WindowEvent event) { 
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
