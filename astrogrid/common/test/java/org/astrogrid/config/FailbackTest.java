/*
 * $Id: FailbackTest.java,v 1.3 2004/03/01 23:13:31 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.config;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests config class.
 */

public class FailbackTest extends TestCase
{
   private static final String testPropertyFile = "test.properties";
   private static final String nonExistentFile = "testNotHere.properties";

   /**
    * We can only really test initialisation/loading once...
    */
   public void testLoad() throws IOException
   {
      FailbackConfig config = new FailbackConfig(FailbackTest.class);
         
      config.setConfigFilename("org/astrogrid/config/"+testPropertyFile);
      
      //force initialisation - and it will barf if this key does not exist
      try {
         config.getProperty("TEST.FRUIT");
      }
      catch (ConfigException ioe) {
         fail("Should have been able to find TEST.FRUIT");
      }
      
      try {
         config.setConfigFilename(testPropertyFile);
         
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
      FailbackConfig config = new FailbackConfig(FailbackTest.class);
         
      try {
         config.getProperty("wibble:wibble");
         fail("Should have thrown an assertion error given a key with a colon in ");
      }
      catch (AssertionError ae) {}
         
      try {
         config.getProperty("wibble wibble");
         fail("Should have thrown an assertion error given a key with a space in ");
      }
      catch (AssertionError ae) {}
         
      try {
         config.getProperty("wibble=wibble");
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
   public void testStore() throws IOException
   {
      FailbackConfig config = new FailbackConfig(FailbackTest.class);
         
      //check it's loaded a value
      String fruit = config.getProperty("TEST.FRUIT").toString();
      assertEquals("Didn't get right result from '"+testPropertyFile+"'", fruit, "APPLE");

      //check default is NOT returned if there IS a value
      fruit = config.getProperty("TEST.FRUIT", "Banana").toString();
      assertEquals("Should return APPLE as it's defined", fruit, "APPLE");

      //check default IS returned if there is NOT a value
      fruit = config.getProperty("TEST.CRASH.DUMMY", "Hatstand").toString();
      assertEquals("Should return default HATSTAND", fruit, "Hatstand");

      //check we can set a property
      config.setProperty("TMP.TESTSET", "Hovercraft");
      fruit = config.getProperty("TMP.TESTSET").toString();
      assertEquals("set property didn't", fruit, "Hovercraft");

      //check if we get a nonexistent property it throws exception
      try {
         fruit = config.getString("TEST.CRASH.DUMMY");
         fail("Did not throw exception for missing property");
      }
      catch (PropertyNotFoundException pnfe) {} //good, supposed to
      
   }

   /**
    * test property types
    */
   public void testTypes() throws IOException
   {
      FailbackConfig config = new FailbackConfig(FailbackTest.class);
         
      String okUrl = "http://www.google.com";
      
      //set some properties so we *know* the types are correct
      config.setProperty("TEST.URL.OK", okUrl);
      config.setProperty("TEST.URL.BAD", "bad:/very.bad");
      
      config.setProperty("TEST.INT", "12  "); //includes spaces as this should not cause problems

      //check URL gets expected
      assertEquals("Unexpected value", okUrl, config.getUrl("TEST.URL.OK").toString());
      
      //check bad urls throw exception
      try {
         config.getUrl("TEST.URL.BAD");
         fail("Did not throw exception for bad url");
      }
      catch (ConfigException ce) {
         if (!(ce.getCause() instanceof MalformedURLException)) {
            fail("Unexpected failure reading bad url");
         }
      }
      
      //check int gets expected
      assertEquals("Unexpected value", 12, config.getInt("TEST.INT"));
      
      //check bad int throws exception
      try {
         config.getInt("TEST.URL.BAD");
         fail("Did not throw exception for bad int");
      }
      catch (ConfigException ce) {
         if (!(ce.getCause() instanceof NumberFormatException)) {
            fail("Unexpected failure reading bad int");
         }
      }

      //check strings work OK...
      assertEquals("Unexpected value", "APPLE", config.getString("TEST.FRUIT"));

      //..even for non-strings
      config.setProperty("TEST.URL.INTOBJ", new Integer(12));
      assertEquals("Unexpected value", "12", config.getString("TEST.URL.INTOBJ"));
      
      //check returns default OK
      assertEquals(5, config.getInt("TEST.NOT.THERE", 5));
      
      //check returns default OK
      assertEquals(new URL("http://something/path"), config.getUrl("TEST.NOT.THERE", new URL("http://something/path")));
      
      //check returns default OK
      assertEquals("Hi there", config.getString("TEST.NOT.THERE", "Hi there"));
      
   }
   
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(FailbackTest.class);
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
 $Log: FailbackTest.java,v $
 Revision 1.3  2004/03/01 23:13:31  mch
 Fixed failed tests

 Revision 1.2  2004/03/01 23:05:31  mch
 Increased test coverage

 Revision 1.1  2004/03/01 14:16:18  mch
 Fixed config tests following new Failback config

 Revision 1.3  2004/02/19 23:27:51  mch
 Added head

 Revision 1.2  2004/02/17 14:46:51  mch
 Increased test coverage

 Revision 1.1  2004/02/17 03:40:21  mch
 Changed to use config

 */


