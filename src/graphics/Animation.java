package graphics;

import java.awt.Image;
import java.util.ArrayList;

/**
 * The Animation object stores a series of Image frames which are then
 * looped through as the object is updated.
 * @author 	Graham Mace
 * @version	1.0 - 03/06/2006
 */
public class Animation {
	/**
	 * The list of frames in the Animation object
	 */
	private transient ArrayList frames;
	
	/**
	 * The current frame index of the Animation object
	 */
	private transient int currentFrameIndex = 0;
	
	/**
	 * Constructor for the class Animation
	 */
	public Animation() {
		this(new ArrayList());
	}
	
	/**
	 * Constructor for the class Animation. Frames are added
	 * to the frames ArrayList and the Animation is started at the beginning.
	 * @param	frames	the ArrayList of frames
	 */
	public Animation(ArrayList frames) {
		// Set the frames
		this.frames = frames;
	}
	
	/**
	 * Adds a new Image frame to this Animation.
	 * @param	image	the Image to be added to the frame list
	 */
	public void addFrame(final Image image) {
		frames.add(new AnimFrame(image));
	}
	
	/**
	 * Starts this Animation playing over from the beginning
	 */
	public void start() {
		currentFrameIndex = 0;
	}
	
	/**
	 * Updates this Animation's current frame index
	 */
	public void update() 
	{
		// Increment if there are more than 1 frames
		if(frames.size() > 1) {
				currentFrameIndex++;
		}
		
		// If we've reached the end of the list, then start from
		// the beginning again
		if(currentFrameIndex >= frames.size()) {
				currentFrameIndex = 0;
		}
	}
	
	/**
	 * Gets this Animation's current Image. Returns null if Animation has no images.
	 * @return	the current Image
	 */
	public Image getImage() {
		// Check we have frames
		if(frames.size() == 0) {
			return null;
		} else {
			return getFrame(currentFrameIndex).image;
		}
	}
	
	/**
	 * Gets the Animation frame at the specified index
	 * @param	index	the index of the frame requested
	 * @return	the frame at the index requested
	 */
	private AnimFrame getFrame(final int index) {
		return (AnimFrame)frames.get(index);
	}
	
	/**
	 * The Animframe inner-class stores the specified Image  
	 * @author	Graham Mace
	 *
	 */
	private class AnimFrame {
		private transient final Image image;
		
		/**
		 * Constructor for the class AnimFrame
		 * @param	image	the Image to be set for the frame
		 */
		public AnimFrame(Image image) {
			this.image = image;
		}
	}
}