package org.astrogrid.config;


import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Abstract JUnit test case for ConfigurableTest
 * - extend to test implementations of Configurable.
 */

public class FactoryTest extends TestCase {
	/** Logger */
	private static Log log = LogFactory.getLog(FactoryTest.class);
   /**
    * Tests property creation
    */
   public void testPropertyMaker() throws IOException {
      PropertyConfig configA = ConfigFactory.getPropertyConfig("Arthur");
      log.debug("Got Arthur "+configA);
      assertNotNull(configA);
      
      PropertyConfig configB = ConfigFactory.getPropertyConfig("Bernard");
      log.debug("Got Bernard "+ configB);
      assertNotNull(configB);
      assertNotSame(configA, configB);

      configA.setProperty("Fruit", "Banana");
      configB.setProperty("Fruit", "Apple");
      assertNotSame(configA.getProperty("Fruit"), configB.getProperty("Fruit"));
      
      PropertyConfig configC = ConfigFactory.getPropertyConfig("Arthur");
      log.debug("Got Charlie "+configC);
      assertNotNull(configC);
      assertEquals(configA, configC);

      //make sure xml ones are not mixed in with property ones
      try {
         XmlConfig configXml = ConfigFactory.getXmlConfig("Arthur");
		 assertNotSame(configA, configXml);
      }
      catch(RuntimeException elloelloello) { //yes, i know, i know, but I need to debug this remotely, so every bit of logging helps
      	log.error("Exception from ConfigFactory.getXmlConfig ", elloelloello);
      	throw elloelloello;
      }
      
   };

   /**
    * Tests xml config creation
    */
   public void testXmlMaker() throws IOException
   {
    try {
          log.trace("testXmlMaker");
          XmlConfig configA = ConfigFactory.getXmlConfig("Arthur");
          log.debug("Arthur "+configA);
          assertNotNull(configA);
        
          XmlConfig configB = ConfigFactory.getXmlConfig("Bernard");
          log.debug("Bernard "+configB);
          assertNotNull(configB);
          assertNotSame(configA, configB);
        
          XmlConfig configC = ConfigFactory.getXmlConfig("Arthur");
          log.debug("Charlie "+configA);
          assertNotNull(configC);
          assertEquals(configA, configC);
        
          //make sure property ones are not mixed in with xml ones
        
          PropertyConfig configP = ConfigFactory.getPropertyConfig("Arthur");
          log.debug("Paul "+configP);
          assertNotSame(configA, configP);
    } catch (RuntimeException e) {
        log.error("Exception caught in testXmlMaker", e);
        throw e;
	}       
   }

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(FactoryTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
}
