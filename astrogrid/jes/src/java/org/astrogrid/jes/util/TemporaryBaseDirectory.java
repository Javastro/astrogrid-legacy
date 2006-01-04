package org.astrogrid.jes.util;

import java.io.File;
import java.io.IOException;

/**
 * An implementation of {@link org.astrogrid.jes.util.BaseDirectory}
 * for testing. The directory is created with a unique name in the
 * temporary-files directory during construction
 * 
 * @author Guy Rixon
 */
public class TemporaryBaseDirectory implements BaseDirectory {
  
  private File directory;
  
  /**
   * Creates a new instance of TemporaryBaseDirectory.
   * Creates also the directory to which the object refers,
   * as a subdirectory of the default temporary-files directory.
   * A gloss on the temporary-file mechanism of java.io.File
   * is used to make sure that the temporary directory does not classh
   * with any existing directories or any directories created in
   * parallel.
   */
  public TemporaryBaseDirectory() throws IOException {
    
    // Create a temporary file whose name is the name of the desired
    // directory with ".tmp" appended. This file and its name have
    // the usual guarantees of uniqueness specified in the API
    // contract for java.io.File. If we now create a directory
    // whose name is the same as the file but without the ".tmp"
    // suffix, then we can be sure that we do not clash with any
    // existing drectories and that there is no race condition. 
    File tmpFile = File.createTempFile("jes-test-base-directory",".tmp");
    
    // Derive the name for the directory.
    int dirNameEnd = tmpFile.getName().indexOf(".tmp");
    String dirName = tmpFile.getName().substring(0, dirNameEnd);
    this.directory = new File(tmpFile.getParent(), dirName);
    
    // Create the directory in the file system.
    this.directory.mkdir();
  }
  
  /**
   * Gets the directory that was created during construction.
   *
   * @return The directory
   */
  public File getDir() {
    return this.directory;
  }
  
}
