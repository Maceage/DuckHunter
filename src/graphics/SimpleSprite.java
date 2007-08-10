package graphics;

import java.awt.Graphics2D;
import java.awt.image.*;

/**
 * The SimpleSprite class extends a Sprite class and allows movement
 * of a single Image around on the screen. The logic of this Sprite is a simple
 * movement by the velocities specified until it reaches the edge, at which
 * point it inverts direction.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class SimpleSprite extends Sprite {
	protected transient BufferedImage simpleImage;
	
	/**
	 * Constructor for the class FlyAwaySprite
	 * @param displayView			the DisplayView object onto which the Duck is painted
	 * @param animGenerator			the Animation object which is associated with the Duck
	 * @param xPos			the x-position of the Duck on-screen
	 * @param yPos			the y-position of the Duck on-screen
	 * @param imagePath		path to the location of the image file
	 */
	public SimpleSprite(DisplayView displayView, AnimGenerator animGenerator, int xPos, int yPos, String imagePath) {
		super(displayView, xPos, yPos);
		simpleImage = animGenerator.getSpriteCache().getSprite(imagePath);
	}
	
	/**
	 * Constructor for the class FlyAwaySprite
	 * @param displayView			the DisplayView object onto which the Duck is painted
	 * @param animGenerator			the AnimGenerator from which the sprites are taken
	 * @param imagePath		path to the location of the image file
	 */
	public SimpleSprite(DisplayView displayView, AnimGenerator animGenerator, String imagePath) {
		this(displayView, animGenerator, 0, 0, imagePath);
	}
	
	/**
	 * Paints this SimpleSprite to the specified Graphics context
	 * @param	gfx	the Graphics context onto which to paint
	 */
	public void paint(final Graphics2D gfx) {
		final BufferedImage currentImage = (BufferedImage)simpleImage;
		gfx.drawImage(currentImage, xPosition, yPosition, displayView);
	}
	
	/**
	 * Gets the width of the Sprite's current frame
	 * @return	the width size of the Sprite's current frame
	 */
	public int getHeight() { return simpleImage.getHeight(null);	}
	
	/**
	 * Gets the height of the Sprite's current frame
	 * @return	the height size of the Sprite's current frame
	 */
	public int getWidth() { return simpleImage.getWidth(null); }
	
	/**
	 * Causes this Sprite to move at the velocity specified until it reaches the edge of the DisplayView.
	 * The velocity of this Sprite is then inverted so that it goes in the other direction.
	 */
	public void act() {
		if((xPosition >= (DisplayView.WIDTH - this.getWidth()) || (xPosition < 0))) { xVelocity = -xVelocity; }
		if((yPosition >= (DisplayView.HEIGHT - this.getHeight()) || (yPosition < 0))) { yVelocity = -yVelocity; }
		
		xPosition += xVelocity; yPosition += yVelocity;
	}
}
