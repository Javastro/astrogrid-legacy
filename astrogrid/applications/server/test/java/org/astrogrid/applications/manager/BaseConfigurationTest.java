package org.astrogrid.applications.manager;

import junit.framework.TestCase;

/**
 * JUnit tests for BaseConfiguration.
 *
 * @author Guy Rixon.
 */
public class BaseConfigurationTest extends TestCase {
    
  public void testTemporaryLocation() throws Exception {
    BaseConfiguration c = new BaseConfiguration();
    System.out.println("Base directory is " + c.baseDirectory.getAbsoluteFile());
    System.out.println("Records directory is " + c.recordsDirectory.getAbsoluteFile());
    System.out.println("Temporary-files directory is " + c.temporaryFilesDirectory.getAbsoluteFile());
    assertTrue(c.getRecordsDirectory().exists());
    assertTrue(c.getTemporaryFilesDirectory().exists()); 
  }
  
}
