package org.astrogrid.security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * TestUtilities for the unit tests.
 * 
 * @author Guy Rixon
 */
public class TestUtilities {
  
  /**
   * Copies an entity found as a class resource to a file.
   *
   * @param clazz The class whose resource is to be copied.
   * @param resource The resource to be copied
   * @param file The file in which to put the copy.
   */
  static public void copyResourceToFile(Class  clazz,
                                        String resource, 
                                        File   file) throws Exception {
    assert resource != null;
    assert file != null;
    
    InputStream is = clazz.getResourceAsStream(resource);
    if (is == null) {
      throw new Exception(clazz.getName() +
                          " does not have a resource " +
                          resource);
    }
    BufferedInputStream bis = new BufferedInputStream(is);
    
    FileOutputStream fos = new FileOutputStream(file);
    BufferedOutputStream bos = new BufferedOutputStream(fos);
    
    while (true) {
      int c = bis.read();
      if (c == -1) {
        break;
      }
      else {
        bos.write(c);
      }
    }
    bos.close();
    bis.close();
  }
  
}
