package graphics;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.AffineTransform;

/**
 * The ImageTransform class is responsible for scaling and transforming
 * an image based on the Image and parameters passed.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class ImageTransform {
	/**
	 * The current configuration of the system's graphics device
	 */
	private final transient GraphicsConfiguration graphicsConfig;
	
	/**
	 * Constructor for the class ImageTransform
	 * @param	gfxConfig	the system's current graphics device configuration
	 */
	public ImageTransform(GraphicsConfiguration gfxConfig) {
		this.graphicsConfig = gfxConfig;
	}
	
	/**
	 * Returns an Image scaled to the specified dimensions
	 * @param	image	the Image to be scaled
	 * @param	scaleX	the X scale of the Image to be modified
	 * @param	scaleY	the Y scale of the Image to be modified
	 * @return	the scaled Image
	 */
	private BufferedImage getScaledImage(final Image image, final float scaleX, final float scaleY) {
		// Set up the transform
		final AffineTransform transform = new AffineTransform();
		transform.scale(scaleX, scaleY);
		transform.translate(
				(scaleX-1) * image.getWidth(null) / 2,
				(scaleY-1) * image.getHeight(null) / 2);
		
		// Create a transparent (not translucent) image
		final Image newImage = graphicsConfig.createCompatibleImage(
			image.getWidth(null),
			image.getHeight(null),
			Transparency.BITMASK);
		
		// Draw the image out to memory to render it
		final Graphics2D gfx = (Graphics2D)newImage.getGraphics();
		gfx.drawImage(image, transform, null);
		gfx.dispose();
		
		// Return the modified image
		return (BufferedImage)newImage;
	}
	
	/**
	 * Returns a mirror image of the Image specified
	 * @param	image	the Image to be scaled
	 * @return	the modified scaled Image
	 */
	public BufferedImage getMirrorImage(final Image image) {
		return getScaledImage(image, -1, 1);
	}
	
	/**
	 * Returns a flipped image of the Image specified
	 * @param	image	the Image to be scaled
	 * @return	the modified scaled Image
	 */
	public BufferedImage getFlippedImage(final Image image) {
		return getScaledImage(image, 1, -1);
	}
}
