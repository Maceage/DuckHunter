package core.sprites;

import graphics.DisplayView;
import graphics.Sprite;
import graphics.AnimGenerator;

import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.Image;

/**
 * The Background class is an extension of the Sprite class for storing
 * and displaying the background image used in the game. 
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class Background extends Sprite {
	/**
	 * Variable for storing the Image used by this class
	 */
	private transient Image backgroundImage;
	
	/**
	 * Constructor for the class Background
	 * @param displayView	the DisplayView object onto which the Background is painted
	 * @param animGenerator	the AnimGenerator from which the image is taken
	 * @param xPos	the x-position of the Duck on-screen
	 * @param yPos	the y-position of the Duck on-screen
	 */
	public Background(DisplayView displayView, AnimGenerator animGenerator, 
			int xPos, int yPos, String imageName) {
		super(displayView, xPos, yPos);
		backgroundImage = animGenerator.getSpriteCache().getSprite(imageName);
		backgroundImage = backgroundImage.getScaledInstance(
				DisplayView.WIDTH, DisplayView.HEIGHT, BufferedImage.SCALE_DEFAULT);
	}
	
	/**
	 * Constructor for the class Background
	 * @param displayView	the DisplayView object onto which the Background is painted
	 * @param animGenerator	the AnimGenerator from which the image is taken
	 */
	public Background(DisplayView displayView, AnimGenerator animGenerator, String imageName) {
		this(displayView, animGenerator, 0, 0, imageName);
	}
	
	/**
	 * Draws the current image to the specified Graphics context
	 * @param	gfx	the Graphics context onto which to paint
	 */
	public void paint(final Graphics2D gfx) {
		gfx.drawImage(backgroundImage, getXPosition(), getYPosition(), displayView);
	}
	
	/**
	 * Gets the width of the Background's current image
	 * @return	the width size of the Background's current image
	 */
	public int getHeight() { return backgroundImage.getHeight(null);	}
	
	/**
	 * Gets the height of the Background's current image
	 * @return	the height size of the Background's current image
	 */
	public int getWidth() { return backgroundImage.getWidth(null); }
}
