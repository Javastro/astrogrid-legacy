package org.astrogrid.config;


import java.io.IOException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests SimpleConfig class.  As this is a static singleton wrapped around
 * PropertyConfig, it also exercises PropertyConfig
 */

public class SimpleTest extends TestCase
{

   private static final String testPropertyFile = "test.properties";
   private static final String nonExistentFile = "testNotHere.properties";

   /**
    * Tests loading methods
    */
   public void testLoad() throws IOException
   {
      //load from URL
      URL testUrl = getClass().getResource(testPropertyFile);
      SimpleConfig.load(testUrl);
      //load from file - should fail
      //make sure we get an exception if we try loading a non-existant file
      try
      {
         SimpleConfig.load(nonExistentFile);

         fail("Should have thrown an exception loading this nonexistant file '"
                 +nonExistentFile
                 +"' (if it does exist, delete it for this test!)");
      }
      catch (IOException ioe) {} //fine do nothing

      //look at locations - should include url above
      String loadedFrom = SimpleConfig.getLocations();

      if (loadedFrom.indexOf(testUrl.toString()) == -1)
      {
         fail("Url of loaded file "+testUrl+" not in locations loaded from");
      }

   }

   /**
    * Tests auto load.  Not entirely sure what this 'should' do - all depends
    * on the environment
    */
   public void testAutoload() throws IOException
   {
      //SimpleConfig.autoLoad(); //should not call this twice as second call ignored - jdt
      
      SimpleConfig.setProperty("TEST.FRUIT","Unix is not fruit");
      
      //try again, this time making sure that System Property is set
      System.setProperty("AG_CONFIG", getClass().getResource(testPropertyFile).toString());
      
      SimpleConfig.autoLoad();
      
      //check that property has been set from system environment
      String fruit = SimpleConfig.getProperty("TEST.FRUIT");
      assertEquals("Should return APPLE", "APPLE",fruit);
   }
   
   /**
    * Some keys are valid, some are not - this tests that the assertions for
    * invalid keys are working
    */
   public void testPropertyKeys()
   {
      try {
         SimpleConfig.getProperty("wibble:wibble");
         fail("Should have thrown an assertion error given a key with a colon in ");
      }
      catch (AssertionError ae) {}
         
      try {
         SimpleConfig.getProperty("wibble wibble");
         fail("Should have thrown an assertion error given a key with a space in ");
      }
      catch (AssertionError ae) {}
         
      try {
         SimpleConfig.getProperty("wibble=wibble");
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
      SimpleConfig.load(getClass().getResource(testPropertyFile));

      //check it's loaded a value
      String fruit = SimpleConfig.getProperty("TEST.FRUIT");
      assertEquals("Didn't get right result from '"+testPropertyFile+"'", fruit, "APPLE");

      //check default is NOT returned if there IS a value
      fruit = SimpleConfig.getProperty("TEST.FRUIT", "Banana");
      assertEquals("Should return APPLE as it's defined", fruit, "APPLE");

      //check default IS returned if there is NOT a value
      fruit = SimpleConfig.getProperty("TEST.CRASH.DUMMY", "Hatstand");
      assertEquals("Should return default HATSTAND", fruit, "Hatstand");

      //check we can set a property
      SimpleConfig.setProperty("TMP.TESTSET", "Hovercraft");
      fruit = SimpleConfig.getProperty("TMP.TESTSET");
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
