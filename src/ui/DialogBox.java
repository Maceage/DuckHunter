package ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Used to ask the user a simple yes/no with the specified question.
 * The answer can be obtained through using the getResult method
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 **/
public class DialogBox extends Dialog implements ActionListener
{
    /**
	 * Version ID for this component used in serialization
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Confirm button for this DialogBox
	 */
	private transient final JButton confirm;
	
	/**
	 * Cancel button for this DialogBox
	 */
	private transient final JButton cancel; 
	
	/**
	 * Variable for storing the result selected in the DialogBox
	 **/
	private transient boolean result = false;

	/**
	 * Constructor for DialogBox object with parameters
	 * @param	parent	the frame to which the dialog should attach itself to
	 * @param	title	the title to display in the titlebar of the dialog box
	 * @param	message	the message to display in the body of the dialog box
	 **/
	public DialogBox (Frame parent, String title, String message) {  
		// Setup the form
		super(parent,title);
		this.setSize(200,100);
		this.setResizable(false);
		this.setLayout(new FlowLayout());
		this.setModal(true);
		confirm = new JButton ("Yes");
		cancel = new JButton ("No");
		
		setForeground(Color.red);
		JLabel messageL = new JLabel(message);
		
		// Add the confirm and cancel buttons and label to the form
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		add (messageL);
		add (confirm);
		add (cancel);
		pack();
		
		// Set the form to appear in the middle of the screen
		Dimension screen = getToolkit().getScreenSize();
    	this.setLocation((screen.getSize().width - getSize().width) / 2, (screen.getSize().height - getSize().height) / 2);
	}

	/**
	 * Method for interpreting which button has been pressed on the form
	 * @param	event	the id of the component which triggered the event
	 **/
	public void actionPerformed (final ActionEvent event) {
		if (event.getSource() == cancel) {
			result = false; 
		} else if (event.getSource() == confirm) {
			result = true;
		}
		this.dispose();	
	}
	
	/**
	 * Returns the action the user pressed for this component
	 */
	public boolean getResult() {
		return result;
	}
}
