/**
 * The Actor class is a base class from which all the
 * graphical objects in the game extend from
 **/
package graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * The Sprite class contains a set of frames which are used in the
 * game to display on-screen graphics and animation. The position and
 * velocity of the Sprite is either set in the constructor or through the
 * respective set and get methods. 
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class Sprite {
	/**
	 * The X position of this Sprite on the Canvas
	 */
	protected int xPosition;
	
	/**
	 * The Y position of this Sprite on the Canvas
	 */
	protected int yPosition;
	
	/**
	 * The X velocity of this Sprite
	 */
	protected transient int xVelocity;
	
	/**
	 * The X velocity of this Sprite
	 */
	protected transient int yVelocity;
	
	/**
	 * The name of this Sprite
	 */
	protected String spriteName;
	
	/**
	 * This Sprite's current DisplayView Canvas area
	 */
	protected transient DisplayView displayView;
	
	/**
	 * The SpriteCache used by this Sprite to retrieve Images
	 */
	protected transient SpriteCache spriteCache;
	
	/**
	 * The Animation associated with this Sprite
	 */
	protected transient Animation spriteAnimation;
	
	/**
	 * Constructor for the Sprite object
	 * @param dView	the Stage Canvas object onto which the Sprite is painted
	 */
	public Sprite(DisplayView dView) {
		displayView = dView;
		spriteCache = displayView.getSpriteCache();
	}
	
	/**
	 * Constructor for the Sprite object
	 * @param dView	the Stage Canvas object onto which the Sprite is painted
	 * @param xPos	the x-position of the Sprite on the Canvas
	 * @param yPos	the y-position of the Sprite on the Canvas
	 */
	public Sprite(DisplayView dView, int xPos, int yPos) {
		displayView = dView;
		spriteCache = displayView.getSpriteCache();
		xPosition = xPos; yPosition = yPos;
	}
	
	/**
	 * Constructor for the Sprite object
	 * @param dView	the Stage Canvas object onto which the Sprite is painted
	 * @param anim		the Animation object containing the frames for painting
	 */
	public Sprite(DisplayView dView, Animation anim) {
		displayView = dView;
		spriteCache = displayView.getSpriteCache();
		spriteAnimation = anim;
		xPosition = 0; yPosition = 0;
	}
	
	/**
	 * Constructor for the Sprite object
	 * @param dView	the Stage Canvas object onto which the Sprite is painted
	 * @param anim	the Animation object containing the frames for painting
	 * @param xPos	the x-position of the Sprite on the Canvas
	 * @param yPos	the y-position of the Sprite on the Canvas
	 */
	public Sprite(DisplayView dView, Animation anim, int xPos, int yPos) {
		displayView = dView;
		spriteCache = displayView.getSpriteCache();
		spriteAnimation = anim;
		xPosition = xPos; yPosition = yPos;
	}
	
	/**
	 * Paints the Sprite onto the Canvas
	 * @param gfx	the Graphics2D context onto which to paint the Sprite
	 */
	public void paint(final Graphics2D gfx) {
		final BufferedImage currentImage = (BufferedImage)spriteAnimation.getImage();
		gfx.drawImage(currentImage, xPosition, yPosition, displayView);
	}
	
	/**
	 * Gets the Sprite's current x-position
	 * @return	the Sprite's x-position
	 */
	public int getXPosition() { return xPosition; }
	
	/**
	 * Sets the Sprite's current x-position
	 * @param val	the position of the Sprite
	 */
	public void setXPosition(final int val) { xPosition = val; }
	
	/**
	 * Gets the Sprite's current y-position
	 * @return the Sprite's y-position
	 */
	public int getYPosition() { return yPosition; }
	
	/**
	 * Sets the Sprite's current y-position
	 * @param val	the position of the Sprite
	 */
	public void setYPosition(final int val) { yPosition = val; }
	
	/**
	 * Gets the Sprite's current x-velocity
	 * @return	the Sprite's current x-velocity
	 */
	public int getDX() { return xVelocity; }
	
	/**
	 * Sets the Sprite's x-velocity
	 * @param val	the x-velocity to set the Sprite to
	 */
	public void setDX(final int val) { xVelocity = val; }
	
	/**
	 *  Gets the Sprite's current y-velocity
	 * @return	the Sprite's current y-velocity
	 */
	public int getDY() { return yVelocity; }
	
	/**
	 * Sets the Sprite's y-velocity
	 * @param val	the y-velocity to set the Sprite to
	 */
	public void setDY(final int val) { yVelocity = val; }
	
	/**
	 * Returns the Animation object currently associated with the Sprite
	 */
	public Animation getAnimation() { return spriteAnimation; }
	
	/**
	 * Sets the Animation object containing the frames to be associated 
	 * with the Sprite for painting
	 * @param anim
	 */
	public void setAnimation(final Animation anim) { 
		if(!anim.equals(spriteAnimation)) {
			spriteAnimation = anim; 
		}
	}

	/**
	 * Update's the Sprite's Animation object
	 * All subclasses must implement any additional code required for
	 * the Sprite's behaviour on-screen
	 */
	public void act() {
		spriteAnimation.update();
	}
	
	/**
	 * Gets the width of the Sprite's current frame
	 * @return	the width size of the Sprite's current frame
	 */
	public int getHeight() { return spriteAnimation.getImage().getHeight(null);	}
	
	/**
	 * Gets the height of the Sprite's current frame
	 * @return	the height size of the Sprite's current frame
	 */
	public int getWidth() { return spriteAnimation.getImage().getWidth(null); }
}