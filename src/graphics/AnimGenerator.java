package graphics;

import java.awt.image.BufferedImage;
import java.awt.GraphicsConfiguration;

/**
 * The AnimGenerator class returns an Animation object comprising of a set
 * number of image files that are in the form of: {folder}/{fileprefix}{number}.png
 * @author Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class AnimGenerator {
	
	/**
	 * The SpriteCache where the Images can be found
	 */
	private transient final SpriteCache spriteCache;
	
	/**
	 * The ImageTransform object that returns a scaled version of the Image
	 */
	private transient final ImageTransform imageTransformer;
	
	/**
	 * Class property used for easy referencing of default transform type
	 */
	public static final int DEFAULT = 0;
	
	/**
	 * Class property used for easy referencing of left-facing transform type
	 */
	public static final int LEFT_FACING = 0;
	
	/**
	 * Class property used for easy referencing of right-facing transform type
	 */
	public static final int RIGHT_FACING = 1;
	
	/**
	 * Constructor for the class AnimGenerator
	 * @param	theCache	the SpriteCache containing the Images for the Animation
	 * @param	gfxConfig	the system's current graphics device configuration
	 */
	public AnimGenerator(SpriteCache theCache, GraphicsConfiguration gfxConfig) {
		spriteCache = theCache;
		imageTransformer = new ImageTransform(gfxConfig);
	}
	
	/**
	 * The getFrames method returns an Animation object containing the frames in
	 * the SpriteCache with the name properties specified.
	 * @param	folderPrefix	the prefix of the folder containing the Images
	 * @param	filePrefix	the prefix of the Image files
	 * @param	faceDirection	the facing direction of the Image
	 * @param	frameCount	the amount of Image files for the frames
	 * @return	the Animation containing the frames specified
	 */
	public Animation getFrames(final String folderPrefix, final String filePrefix, 
			final int faceDirection, final int frameCount) {
		
		// Declare variables for storing Animation and images location
		String imageLocation = null;
		final Animation anim = new Animation();
		
		// Loop through and add all the Images to the Animation
		for(int i = 0; i <= frameCount; i++) {
			if(i <= 9) {
				imageLocation = folderPrefix + filePrefix + "0" + i + ".png";
			} else {
				imageLocation = folderPrefix + filePrefix + i + ".png";
			}
			
			BufferedImage currentImage = spriteCache.getSprite(imageLocation);
			
			// Flip the image if we want a left-facing animation
			if(faceDirection == RIGHT_FACING) {
				currentImage = imageTransformer.getMirrorImage(currentImage);
			}
			
			// Add the Image to the Animation
			anim.addFrame(currentImage);
		}

		return anim;
	}
	
	/**
	 * Retrieves and places single frame into the Animation object
	 * @return	the Animation object containing the frames
	 */
	public Animation getFrame(final String folderName, final String fileName, final int faceDirection) {
		String imageLocation = null;
		// Declare variable for constructing the file and folder containing the Images and
		// the Animation to get the Image from
		imageLocation = folderName + fileName;
		final Animation anim = new Animation();
		
		// Get the Image from the SpriteCache
		BufferedImage currentImage = spriteCache.getSprite(imageLocation);
		
		// Flip the image if we want a left-facing animation
		if(faceDirection == RIGHT_FACING) {
			currentImage = imageTransformer.getMirrorImage(currentImage);
		}
		
		// Add the frame to the Animation and return
		anim.addFrame(currentImage);
		return anim;
	}
	
	
	/**
	 * Returns the current SpriteCache used by this AnimGenerator
	 * @return	the SpriteCache being used
	 */
	public SpriteCache getSpriteCache() {
		return spriteCache;
	}
}
