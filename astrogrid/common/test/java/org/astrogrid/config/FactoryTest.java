package org.astrogrid.config;


import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Abstract JUnit test case for ConfigurableTest
 * - extend to test implementations of Configurable.
 */

public class FactoryTest extends TestCase
{

   /**
    * Tests property creation
    */
   public void testPropertyMaker() throws IOException
   {
      PropertyConfig configA = ConfigFactory.getPropertyConfig("Arthur");
      
      assertNotNull(configA);
      
      PropertyConfig configB = ConfigFactory.getPropertyConfig("Bernard");
      
      assertNotNull(configB);
      assertNotSame(configA, configB);

      configA.setProperty("Fruit", "Banana");
      configB.setProperty("Fruit", "Apple");
      assertNotSame(configA.getProperty("Fruit"), configB.getProperty("Fruit"));
      
      PropertyConfig configC = ConfigFactory.getPropertyConfig("Arthur");
      
      assertNotNull(configC);
      assertEquals(configA, configC);

      //make sure xml ones are not mixed in with property ones
      XmlConfig configXml = ConfigFactory.getXmlConfig("Arthur");
      assertNotSame(configA, configXml);
   }

   /**
    * Tests xml config creation
    */
   public void testXmlMaker() throws IOException
   {
      XmlConfig configA = ConfigFactory.getXmlConfig("Arthur");
      
      assertNotNull(configA);
      
      XmlConfig configB = ConfigFactory.getXmlConfig("Bernard");
      
      assertNotNull(configB);
      assertNotSame(configA, configB);
      
      XmlConfig configC = ConfigFactory.getXmlConfig("Arthur");
      
      assertNotNull(configC);
      assertEquals(configA, configC);
      
      //make sure property ones are not mixed in with xml ones

      PropertyConfig configP = ConfigFactory.getPropertyConfig("Arthur");
      assertNotSame(configA, configP);
      
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
