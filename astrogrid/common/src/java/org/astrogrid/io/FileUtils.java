/*
 * $Id: FileUtils.java,v 1.2 2008/09/17 08:16:06 pah Exp $
 * 
 * Created on 17 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.io;

import java.io.File;
import java.io.IOException;

/**
 * Some useful file manipulation utilities.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 17 Apr 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class FileUtils {

    private FileUtils(){
	
    }
    
    /**
     * Creates a temporary directory.
     * Java has an API to create a temporary file, with safeguards against
     * duplication, but doesn't have an API for a temporary directory.
     * This method uses the temporary file (name ending in .tmp) as a lock
     * and creates a directory based on that name. The simpler technique
     * of creating the file, deleting it from the file system and replacing
     * with a directory of the same name doesn't work reliably; it defeats
     * the locking in the JRE.
     * @author Guy Rixon
     * @return The directory.
     * @throw IOException If the lock file cannot be created (should never happen). 
     */
    public static File makeTemporaryDirectory() throws IOException {
      File lockFile = File.createTempFile("AGTMPDIR", ".tmp");
      lockFile.deleteOnExit(); //IMPL Javadoc says that this should not be used for file locking but the @link{FileLock} mechanism used instead - ugdate when time
      String fileName = lockFile.getAbsolutePath();
      String directoryName 
          = fileName.substring(0, fileName.lastIndexOf(".tmp")-1).toString();
      File directory = new File(directoryName);
      directory.mkdir();
      directory.deleteOnExit();
      return directory;
    }
    
    /**
     * Delete a directory even if it still contains other files. This is a utility method to augment the behaviour of the standard {@link File#delete()} method which will not delete the directory if files still exist.
     * @param dir the directory to be deleted.
     * @return
     */
    public static boolean forceDeleteDirectory(File dir) {
	boolean result = false;
	
	if (dir != null & dir.isDirectory()) {
	    File[] files = dir.listFiles();
	    for (int i = 0; i < files.length; i++) {
		File file = files[i];
		if (file.isDirectory()) {
		    result = forceDeleteDirectory(file);
		} else {
		    result = file.delete();
		}
		if (!result) //if the delete failed for some reason early exit
		{
		    return result;
		}
	    }
	    result = dir.delete();
	}
	return result;
	
    }

}


/*
 * $Log: FileUtils.java,v $
 * Revision 1.2  2008/09/17 08:16:06  pah
 * result of merge of pah_community_1611 branch
 *
 * Revision 1.1.2.1  2008/05/17 20:55:14  pah
 * safety checkin before interop
 *
 */
