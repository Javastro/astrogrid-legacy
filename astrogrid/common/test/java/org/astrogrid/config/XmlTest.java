package org.astrogrid.config;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.w3c.dom.Node;

/**
 * Tests XmlConfig class
 */

public class XmlTest extends TestCase
{

   private static final String testPropertyFile = "test.xml";
   private static final String nonExistantFile = "testNotHere.xml";

   //the usual way I would expect this to be used
   private static final XmlConfig config = ConfigFactory.getXmlConfig(XmlTest.class);
   
   /**
    * Tests loading methods
    */
   public void testLoad() throws IOException
   {
      //load from URL
      URL testUrl = getClass().getResource(testPropertyFile);
      config.loadUrl(testUrl);
      //load from file - should fail
      //make sure we get an exception if we try loading a non-existant file
      try
      {
         config.loadFile(nonExistantFile);

         fail("Should have thrown an exception loading this nonexistant file '"
                 +nonExistantFile
                 +"' (if it does exist, delete it for this test!)");
      }
      catch (IOException ioe) {} //fine do nothing

      //look at locations - should include url above
      String loadedFrom = config.getLocations();

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
      config.autoLoad();
      
//      config.setProperty("TEST.FRUIT","Unix is not fruit");
      
      //try again, this time making sure that System Property is set
      System.setProperty("AG_XMLCONFIG", getClass().getResource(testPropertyFile).toString());
      
      config.autoLoad();
      
      //check that property has been set from system environment
      Node car = config.getProperty("Car");
      assertNotNull(car);
      //assertEquals("Should return APPLE", fruit, "APPLE");
   }
   

   /**
    *Tests getting properties
    */
   public void testGet() throws IOException
   {
      //if this throws a NulPointerException the test properties file is probably missing
      config.loadUrl(getClass().getResource(testPropertyFile));

      //check it's loaded a value
      Node[] fruit = config.getProperties("Fruit");
      assertNotNull(fruit);
      assertTrue(fruit.length == 2);

      //check it complains if you try loading a single value when there are lots
      try {
         config.getProperty("Fruit");
         fail("Should have reported too many fruit");
      }
      catch (AssertionError ae) {} //ignore - supposed to happen
      
      //check it gets a single value
      Node carNode = config.getProperty("Car");
      assertNotNull(carNode);
      
      //check it gets a string value for both one-line and multi-line tags
      String car = config.getPropertyValue("Car");
      assertNotNull(car);
      String film = config.getPropertyValue("Film");
      assertNotNull(film);
      
      
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
