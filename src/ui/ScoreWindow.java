package ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.table.TableColumn;

import util.DataIO;
import core.Player;
import core.ScoreBoard;

/**
 * The ScoreWindow class displays high scores that have been set by previous players.
 * The scores are read from the file config/scores.dat which is a serialized version of
 * the ScoreBoard class. 
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 **/
public class ScoreWindow extends JDialog implements ActionListener, WindowListener
{
	/**
	 * Version ID for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * OK button component
	 */
	private JButton cmdOK;
	
	/**
	 * Table display component
	 */
	private transient JTable tblScores;
	
	/**
	 * GameOptions object containing the various settings for the game
	 */
	private transient ScoreBoard playerScores;
	
	/**
	 * Constructor for ScoreWindow component
	 */
	public ScoreWindow() {
		this(new ScoreBoard());
	}
	
	/**
	 * Constructor for ScoreWindow component with a set ScoreBoard
	 * @param	aScoreBoard	ScoreBoard to display in window
	 */
	public ScoreWindow(ScoreBoard aScoreBoard) {
		super();
		setupWindow();
		playerScores = aScoreBoard;
		populateScores();
	}
	
	/**
	 * Constructor for ScoreWindow component
	 * @param	playerToAdd	reference to Player that is to be added to table
	 **/
	public ScoreWindow(Player playerToAdd) {
		super();
		// Set up the window components, read the scores from disk,
		// add player to playerScores then populate table
		setupWindow();
		readScores();
		playerScores.addPlayer(playerToAdd);
		populateScores();
	}
	
	/**
	 * The setupWindow method creates all the components for the window
	 */
	private void setupWindow() {
		// Set up the window
		this.setTitle("High Scores");
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.addWindowListener(this);
		this.setModal(true);
		this.setAlwaysOnTop(true);
		
		// Create panels
		JPanel panTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel panScores = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		// Create the table
		tblScores = new JTable(10, 2);
		tblScores.setColumnSelectionAllowed(false);
		tblScores.setDragEnabled(false);
		tblScores.setEnabled(false);
		
		// Set up player name column
		TableColumn column = null;
		column = tblScores.getColumnModel().getColumn(0);
		column.setPreferredWidth(100);
		
		// Create components
		JLabel appTitle = new JLabel("High Scores");
		appTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
		cmdOK = new JButton("OK");
		
		// Add action listeners
		cmdOK.addActionListener(this);
		
		// Add components
		panTitle.add(appTitle);
		panScores.add(tblScores);
		buttonPanel.add(cmdOK);
		
		// Add panels to window
		this.add("North", panTitle);
		this.add("East", eastPanel);
		this.add("West", westPanel);
		this.add("South", buttonPanel);
		this.add("Center", panScores);
		
		// Set the form's size and location
		this.setPreferredSize(new Dimension(275, 275));
		this.pack();
		Dimension screen = getToolkit().getScreenSize();
    	this.setLocation((screen.getSize().width - getSize().width) / 2, 
    			(screen.getSize().height - getSize().height) / 2);
	}
	/**
	 * Method for detecting JButton presses
	 * @param	event	the component from which the event originated
	 **/
	public void actionPerformed (final ActionEvent event) {
		if(event.getSource() == cmdOK) {
			// Write the scores to the scores file
			writeScores();
			// Close the window
			this.setVisible(false);
			this.dispose();
		}
	}
	
	/**
	 * This method reads all the previous high scores from the file scores.dat
	 * located in the same place as the application
	 * @see	ScoreBoard
	 */
	private void readScores() {
		// Create a new DataIO object
		final DataIO scoreReader = new DataIO();
		final File scoresFile = new File("./config/scores.dat");
		
		// Try and read the scores
		try {
			playerScores = (ScoreBoard)scoreReader.loadFile(scoresFile);
		} catch(IOException ex) {
			System.out.println("Could not from read scores file");
			playerScores = new ScoreBoard();
		} catch(ClassNotFoundException ex) {
			System.out.println("Error reading scores file");
			playerScores = new ScoreBoard();
		}
	}
	
	/**
	 * This method populates the scores JTable displayed on the
	 * form with the current ScoreBoard object
	 */
	private void populateScores() {
		// Create temporary variables
		Player tempPlayer = null;
		String playerName = null;
		String playerScore = null;
		
		for(int i = 0; i < 10; i++) {
			// Set temporary variables
			playerName = "#" + String.valueOf(i + 1) + " <Nobody>";
			playerScore = "----------";
			
			if(playerScores != null) {
				// Get the Player's details
				tempPlayer = playerScores.getPlayer(i);
				if(tempPlayer != null) {
					playerName = "#" + String.valueOf(i + 1) + " " + tempPlayer.getPlayerName();
					playerScore = String.valueOf(tempPlayer.getScore());
				}
			}
			tblScores.setValueAt(playerName, i, 0);
			tblScores.setValueAt(playerScore, i, 1);
		}
		tblScores.doLayout();
	}
	
	/**
	 * This method writes the high scores to the file scores.dat
	 * when the window is closed
	 * @see	ScoreBoard
	 */
	private void writeScores() {
		// Create a new DataIO object to write the scoreBoard to
		final DataIO scoreReader = new DataIO();
		final File scoresFile = new File("./config/scores.dat");
		
		// Notify in console and attempt to save file
		System.out.println("Saving scores...");
		try {
			scoreReader.saveFile(scoresFile, playerScores);
		} catch(IOException ex) {
			System.out.println("Error: could not write to scores file");
		}
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
