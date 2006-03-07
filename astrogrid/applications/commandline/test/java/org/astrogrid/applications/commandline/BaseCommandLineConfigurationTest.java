package org.astrogrid.applications.commandline;

import java.net.URL;
import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;

/**
 * JUnit tests for BasicCommandLineConfiguration.
 *
 * @author Guy Rixon
 */
public class BaseCommandLineConfigurationTest extends TestCase {
  
  public void testApplicationDescriptionCreation() throws Exception {
    
    // This configuration should create a new application description
    // in a temporary directory.
    CommandLineConfiguration c1 = new BasicCommandLineConfiguration();
    URL u1 = c1.getApplicationDescriptionUrl();
    assertNotNull(u1);
    u1.openConnection(); // This throws an exception if u1 is no good.
    
    // This configuration should use the previous directory and description.
    SimpleConfig.getSingleton().setProperty(
        "cea.base.dir", 
        c1.getBaseDirectory().getAbsolutePath()
    );
    BasicCommandLineConfiguration c2 = new BasicCommandLineConfiguration();
    URL u2 = c2.getApplicationDescriptionUrl();
    assertNotNull(u2);
    u2.openConnection(); // This throws an exception if u1 is no good.
    assertTrue(u2.toString().equals(u1.toString()));
  }
  
}
