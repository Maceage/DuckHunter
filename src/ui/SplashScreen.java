package ui;

import java.awt.*;
import javax.swing.*;
import core.GameCore;

/**
 * The SplashScreen class is an extension of a JFrame with the text
 * of the game and 'Loading...' displayed. The class is intentionally
 * small in order to be shown when during pre-loading of resources.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 **/
public class SplashScreen extends JFrame
{
	/**
	 * Version ID for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for the class SplashScreen
	 **/
	public SplashScreen() {
		super();
		// Set up the window
		this.setTitle("Loading...");
		this.setResizable(false);
		this.setUndecorated(true);
		this.setLayout(new BorderLayout());
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		// Create title panel
		JLabel gameTitle = new JLabel(GameCore.GAME_NAME);
		gameTitle.setFont(new Font("Tahoma", Font.BOLD, 30));
		gameTitle.setBackground(Color.DARK_GRAY);
		
		// Create loading panel
		JLabel gameLoading = new JLabel("Loading...");
		gameLoading.setFont(new Font("Tahoma", Font.BOLD + Font.ITALIC, 20));
		gameLoading.setBackground(Color.DARK_GRAY);
		
		// Add everything to frame
		this.add("North", gameTitle);		
		this.add("East", gameLoading);
		
		// Set the form's size and location
		this.setSize(200, 75);
		Dimension screen = getToolkit().getScreenSize();
    	this.setLocation((screen.getSize().width - getSize().width) / 2, (screen.getSize().height - getSize().height) / 2);
	}
}
