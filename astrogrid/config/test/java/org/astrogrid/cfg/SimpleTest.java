package org.astrogrid.cfg;


import java.io.IOException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests CfgFactory.getCommonConfig(). class.  As this is a static singleton wrapped around
 * PropertyConfig, it also exercises PropertyConfig
 */

public class SimpleTest extends TestCase
{

   private static final String testPropertyFile = "test.properties";
   private static final String nonExistentFile = "file://testNotHere.properties";

   /**
    * Tests loading methods
    */
   public void testLoad() throws IOException
   {
      //load from URL
      URL testUrl = getClass().getResource(testPropertyFile);
      assertNotNull("Test not set up properly, '"+testPropertyFile+"' missing");
      
      ConfigFactory.getCommonConfig().loadFromUrl(testUrl);
      //load from file - should fail
      //make sure we get an exception if we try loading a non-existant file
      try
      {
         ConfigFactory.getCommonConfig().loadFromUrl(new URL(nonExistentFile));

         fail("Should have thrown an exception loading this nonexistant file '"
                 +nonExistentFile
                 +"' (if it does exist, delete it for this test!)");
      }
      catch (IOException ioe) {} //fine do nothing

      //look at locations - should include url above
      String loadedFrom = ConfigFactory.getCommonConfig().loadedFrom();

      if (loadedFrom.indexOf(testUrl.toString()) == -1)
      {
         fail("Url of loaded file "+testUrl+" not in locations loaded from");
      }

   }

   
   /**
    * Some keys are valid, some are not - this tests that the assertions for
    * invalid keys are working
    */
   public void testPropertyKeys()
   {
      try {
         ConfigFactory.getCommonConfig().getProperty("wibble:wibble");
         fail("Should have thrown an assertion error given a key with a colon in ");
      }
      catch (AssertionError ae) {}
         
      try {
         ConfigFactory.getCommonConfig().getProperty("wibble wibble");
         fail("Should have thrown an assertion error given a key with a space in ");
      }
      catch (AssertionError ae) {}
         
      try {
         ConfigFactory.getCommonConfig().getProperty("wibble=wibble");
         fail("Should have thrown an assertion error given a key with an equals in ");
      }
      catch (AssertionError ae) {}
         
   }
   
 /**
 * These tests require assertions to be enabled.
 * java must be run with the -ea option.
 */
   public void testAssertionsEnabled() {
      try{
         assert false;
         fail("Assertions must be enabled for these tests.  Run java with the -ea option.");
      } catch (AssertionError ae) {
         //expected
         return;
      }
   }

   public void testConfiguration() throws IOException
   {
      //if this throws a NulPointerException the test properties file is probably missing
      ConfigFactory.getCommonConfig().loadFromUrl(getClass().getResource(testPropertyFile));

      //check it's loaded a value
      String fruit = ConfigFactory.getCommonConfig().getString("TEST.FRUIT");
      assertEquals("Didn't get right result from '"+testPropertyFile+"'", "APPLE", fruit );

      //check default is NOT returned if there IS a value
      fruit = ConfigFactory.getCommonConfig().getString("TEST.FRUIT", "Banana");
      assertEquals("Should return APPLE as it's defined", fruit, "APPLE");

      //check default IS returned if there is NOT a value
      fruit = ConfigFactory.getCommonConfig().getString("TEST.CRASH.DUMMY", "Hatstand");
      assertEquals("Should return default HATSTAND", fruit, "Hatstand");

      //check we can set a property
      ConfigFactory.getCommonConfig().getString("TMP.TESTSET", "Hovercraft");
      fruit = ConfigFactory.getCommonConfig().getString("TMP.TESTSET");
      assertEquals("set property didn't", fruit, "Hovercraft");

   }

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(SimpleTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
}
