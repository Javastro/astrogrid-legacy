package org.astrogrid.config;


import java.io.IOException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests AttomConfig class.
 */

public class AttomTest extends TestCase
{
   private static final String testPropertyFile = "test.properties";
   private static final String nonExistentFile = "testNotHere.properties";

   /**
    * We can only really test initialisation/loading once...
    */
   public void testLoad() throws IOException
   {
      AttomConfig.setConfigFilename("org/astrogrid/config/"+testPropertyFile);
      
      //force initialisation - and it will barf if this key does not exist
      try {
         AttomConfig.getProperty("TEST.FRUIT");
      }
      catch (ConfigException ioe) {
         fail("Should have been able to find TEST.FRUIT");
      }
      
      try {
         AttomConfig.setConfigFilename(testPropertyFile);
         
         fail("Should not be able to set config filename after initialisation");
      }
      catch (ConfigException ioe) {} //fine do nothing

   }

   /**
    * Some keys are valid, some are not - this tests that the assertions for
    * invalid keys are working
    */
   public void testPropertyKeys()
   {
      try {
         AttomConfig.getProperty("wibble:wibble");
         fail("Should have thrown an assertion error given a key with a colon in ");
      }
      catch (AssertionError ae) {}
         
      try {
         AttomConfig.getProperty("wibble wibble");
         fail("Should have thrown an assertion error given a key with a space in ");
      }
      catch (AssertionError ae) {}
         
      try {
         AttomConfig.getProperty("wibble=wibble");
         fail("Should have thrown an assertion error given a key with an equals in ");
      }
      catch (AssertionError ae) {}
         
   }
   
   /**
    * These tests require assertions to be enabled.
    * java must be run with the -ea option.
    */
   public void testAssertionsEnabled() {
      try {
         assert false;
         fail("Assertions must be enabled for these tests.  Run java with the -ea option.");
      } catch (AssertionError ae) {
         //expected
         return;
      }
   }

   /**
    * test that properties are got and sot propertly...
    */
   public void testConfiguration() throws IOException
   {
      //check it's loaded a value
      String fruit = AttomConfig.getProperty("TEST.FRUIT").toString();
      assertEquals("Didn't get right result from '"+testPropertyFile+"'", fruit, "APPLE");

      //check default is NOT returned if there IS a value
      fruit = AttomConfig.getProperty("TEST.FRUIT", "Banana").toString();
      assertEquals("Should return APPLE as it's defined", fruit, "APPLE");

      //check default IS returned if there is NOT a value
      fruit = AttomConfig.getProperty("TEST.CRASH.DUMMY", "Hatstand").toString();
      assertEquals("Should return default HATSTAND", fruit, "Hatstand");

      //check we can set a property
      AttomConfig.setProperty("TMP.TESTSET", "Hovercraft");
      fruit = AttomConfig.getProperty("TMP.TESTSET").toString();
      assertEquals("set property didn't", fruit, "Hovercraft");

      //check if we get a nonexistent property it throws exception
      try {
         fruit = AttomConfig.getString("TEST.CRASH.DUMMY");
         fail("Did not throw exception for missing property");
      }
      catch (PropertyNotFoundException pnfe) {} //good, supposed to
      
   }

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(AttomTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
}

/*
 $Log: AttomTest.java,v $
 Revision 1.1  2004/02/17 03:40:21  mch
 Changed to use AttomConfig

 */
