package org.astrogrid.datacenter.config;

import java.io.IOException;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Abstract JUnit test case for ConfigurableTest
 * - extend to test implementations of Configurable.
 */

public class ConfigurationTest extends TestCase
{

   private static final String testPropertyFile = "test.properties";
   private static final String nonExistantFile = "testNotHere.properties";

   public void testLoad() throws IOException
   {
      //load from URL
      URL testUrl = getClass().getResource(testPropertyFile);
      Configuration.load(testUrl);
      //load from stream
      Configuration.load(getClass().getResourceAsStream(testPropertyFile));
      //load from file - should fail
      //make sure we get an exception if we try loading a non-existant file
      try
      {
         Configuration.load(nonExistantFile);

         fail("Should have thrown an exception loading this nonexistant file '"
                 +nonExistantFile
                 +"' (if it does exist, delete it for this test!)");
      }
      catch (IOException ioe) {} //fine do nothing

      //look at locations - should include url above
      String loadedFrom = Configuration.getLocations();

      if (loadedFrom.indexOf(testUrl.toString()) == -1)
      {
         fail("Url of loaded file "+testUrl+" not in locations loaded from");
      }

   }

   public void testConfiguration() throws IOException
   {
      //if this throws a NulPointerException the test properties file is probably missing
      Configuration.load(getClass().getResource(testPropertyFile));

      //check it's loaded a value
      String fruit = Configuration.getProperty("TEST.FRUIT");
      assertEquals("Didn't get right result from '"+testPropertyFile+"'", fruit, "APPLE");

      //check default is NOT returned if there IS a value
      fruit = Configuration.getProperty("TEST.FRUIT", "Banana");
      assertEquals("Should return APPLE as it's defined", fruit, "APPLE");

      //check default IS returned if there is NOT a value
      fruit = Configuration.getProperty("TEST.CRASH.DUMMY", "Hatstand");
      assertEquals("Should return default HATSTAND", fruit, "Hatstand");

      //check we can set a property
      Configuration.setProperty("TMP.TESTSET", "Hovercraft");
      fruit = Configuration.getProperty("TMP.TESTSET");
      assertEquals("set property didn't", fruit, "Hovercraft");

   }

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(ConfigurationTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
}
