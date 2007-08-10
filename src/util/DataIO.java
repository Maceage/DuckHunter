package util;

import java.io.*;

/**
 * The DataIO class provides methods for reading and writing serialized
 * objects to disk. The file is read back as a standard Object - any class
 * wishing to read an Object from disk must cast it back to the Object's previous 
 * type before attempting to access or use it.
 * @author	Graham Mace
 * @version	1.0 - 03/06/2006
 **/
public class DataIO
{
	/**
	 * Saves the specified Object to a file path
	 * @param	filePath	the filepath location to save to
	 * @param	theData		the Object to save
	 */
	public void saveFile(final File filePath, final Object theData) throws IOException {
		// TODO: Fix file path when saving scores
		
		// Get the filename of the file we're saving
		final String theFileName = new String(filePath.getAbsolutePath());
		
		// Open output stream and write object to file
		final FileOutputStream output = new FileOutputStream (theFileName);
		final ObjectOutputStream objectOutput = new ObjectOutputStream(output);
		objectOutput.writeObject(theData);
		
		// Close the output stream
		objectOutput.flush();
		objectOutput.close();
		
	}
	
	/**
	 * Loads an Object from the specified file path. If the Object or file 
	 * cannot be found then an IOException is thrown
	 * @param	filePath	the filepath of the saved file
	 * @return	returns		the Object that has been loaded from the file
	 */
	public Object loadFile(final File filePath) throws IOException, ClassNotFoundException {
		// TODO: Fix file path when loading scores
		
		// Get the filename of the file we're loading
		final String theFileName = new String(filePath.getAbsolutePath());
		
		// Open an input stream and read the object from the file
		final FileInputStream inStream = new FileInputStream(theFileName);
		final ObjectInputStream objectInput = new ObjectInputStream(inStream);
		final Object theFile = objectInput.readObject();
		
		//Close the stream
		objectInput.close();
		return theFile;
	}
}