package org.astrogrid.config;


import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Tests the XML aspects of the Config implementations
 */

public class XmlTest extends TestCase
{

   private static final String testPropertyFile = "test.xml";
   private static final String nonExistantFile = "testNotHere.xml";

   

   /**
    *Tests getting properties
    */
   public void testGet() throws IOException
   {
      Config config = ConfigFactory.getConfig(XmlTest.class);

      //if this throws a NulPointerException the test properties file is probably missing
      Document d = config.getDom("nokey", getClass().getResource(testPropertyFile));

      //check it's loaded a value
      NodeList fruit = d.getElementsByTagName("Fruit");
      assertNotNull(fruit);
      assertTrue(fruit.getLength() == 2);
   }

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(XmlTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
}
