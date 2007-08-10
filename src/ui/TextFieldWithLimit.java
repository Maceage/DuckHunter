package ui;

import java.awt.event.*;
import javax.swing.*;

/**
 * The TextFieldWithLimit extends the JTextField component to provide a graphical
 * text component that only allows characters contained in the String set to the 
 * component using the setValidChars method.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class TextFieldWithLimit extends JTextField implements KeyListener 
{
	/**
	 * Version ID for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The maximum length for characters in this text field
	 */
	private transient int maxLength;
	
	/**
	 * The characters available for typing in this text field
	 */
	private transient String vChars;
	
	/**
	 * Specifies whether or not any special keys are currently pressed in this component
	 */
	private transient boolean isSpKeyPressed;	

	/**
	 * Constructor for TextFieldWithLimit object with parameters
	 * @param	initialStr	the string to which the textfield will be set when created
	 * @param	col			number of columns in textfield
	 * @param	maxLength	maximum number of characters allowed in the field
	 */
	public TextFieldWithLimit(String initialStr, int col, int maxLength) {
		super(initialStr, col);
		this.maxLength = maxLength;
		addKeyListener(this);
	}
	
	/**
	 * Constructor for TextFieldWithLimit object with parameters
	 * @param	col			number of columns in textfield
	 * @param	maxLength	maximum number of characters allowed in the field
	 */
	public TextFieldWithLimit (int col, int maxLength) {
		this("",col,maxLength);
	}

	/**
	 * Method for capturing key typed events
	 * @param	theEvent	the key event source that triggered the method
	 */
	public void keyTyped(final KeyEvent theEvent) { 
	// Get the character that has been input
		
		final char keyChar = theEvent.getKeyChar();
		
		// Get the length of the text entered
		final int textLength = getText().length();
		
		// Variable for holding whether a valid character has been pressed
		boolean validChar = false;
		
		if(isSpKeyPressed)
		{
			//If the user has fired the keyPressed with a control key, allow it through regardless. 
			return;
		}
		
		// Check the character against the list of characters in the validation string
		if (keyChar != KeyEvent.CHAR_UNDEFINED)	{
				// Loop through the numbers and characters string to see if the char pressed is
				// in that string
				for(int charPlace = 0; charPlace < vChars.length(); charPlace++)
				{
					if (vChars.charAt(charPlace) == keyChar) { validChar = true; break; }	
				}
		}
		
		// Check to see if textbox limit is not being reached
		if((textLength >= maxLength)) { validChar = false; }
		
		if (!validChar) {
			theEvent.consume();
		}
	}
	
	/**
	 * Method for capturing key pressed events
	 * @param	theEvent	the key event source that triggered the method
	 **/
	public void keyPressed(final KeyEvent theEvent)  
	{ 
		// Set private state flag if they're using control keys
		isSpKeyPressed = isSpecialKey(theEvent);
	}
	
	/**
	 * Method for capturing key released events
	 * @param	theEvent	the key event source that triggered the method
	 **/
	public void keyReleased (final KeyEvent theEvent) 
	{ 
		// They let go of the key. Doesnt matter if it was delete or not, they're not deleting any more!
		isSpKeyPressed = false;
	}

	/**
	 * Method to determine if a key event came from a 'control key'
	 * @param	theEvent	the key event source that triggered the method
	 *						Note that the keyTyped event does not appear to pass
	 *						in the keycode of special characters, rather it passes 
	 *						in some kind of empty key event.
	 **/	
	private boolean isSpecialKey(final KeyEvent theEvent)
	{
		// Get the event's keyCode
		final int keyCharInt = theEvent.getKeyCode();
		
		// Check a special key has been pressed
		return (keyCharInt == KeyEvent.VK_DELETE) || (keyCharInt == KeyEvent.VK_BACK_SPACE)
		 || (keyCharInt == KeyEvent.VK_ENTER) ||  (keyCharInt == KeyEvent.VK_TAB) 
		 || (keyCharInt == KeyEvent.VK_SHIFT) || (theEvent.isActionKey());
	}

	/**
	 * Sets the valid characters that the textbox will allow
	 * @param	chars	valid characters for the textbox
	 **/
	public void setValidChars(final String chars) { vChars = chars; }
	
	/**
	 * Gets the current valid characters that the textbox allows
	 * @return	returns valid characters for the textbox
	 **/
	public String getValidChars() { return vChars; }

}