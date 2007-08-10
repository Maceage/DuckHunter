package util;

import java.util.*;
import java.io.*;

/**
* @author javapractices.com
* @author Alex Wong
*/
public class FileLister {
  /**
  * Recursively walk a directory tree and return a List of all
  * Files found; the List is sorted using File.compareTo.
  *
  * @param aStartingDir is a valid directory, which can be read.
  */
  public static List getFileListing(final File aStartingDir) throws FileNotFoundException{
    validateDirectory(aStartingDir);
    List result = new ArrayList();

    final File[] filesAndDirs = aStartingDir.listFiles();
    final List filesDirs = Arrays.asList(filesAndDirs);
    final Iterator filesIter = filesDirs.iterator();
    File file = null;
    while ( filesIter.hasNext() ) {
      file = (File)filesIter.next();
      result.add(file); //always add, even if directory
      if (!file.isFile()) {
        //must be a directory
        //recursive call!
        final List deeperList = getFileListing(file);
        result.addAll(deeperList);
      }

    }
    Collections.sort(result);
    return result;
  }

  /**
  * Directory is valid if it exists, does not represent a file, and can be read.
  */
  private static void validateDirectory (final File aDirectory) throws FileNotFoundException {
    if (aDirectory == null) {
      throw new IllegalArgumentException("Directory should not be null.");
    }
    if (!aDirectory.exists()) {
      throw new FileNotFoundException("Directory does not exist: " + aDirectory);
    }
    if (!aDirectory.isDirectory()) {
      throw new IllegalArgumentException("Is not a directory: " + aDirectory);
    }
    if (!aDirectory.canRead()) {
      throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
    }
  }
} 