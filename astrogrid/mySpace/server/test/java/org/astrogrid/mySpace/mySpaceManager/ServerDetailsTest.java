package org.astrogrid.mySpace.mySpaceManager;

import junit.framework.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceManager.ServerDetails;

/**
 * Junit tests for the <code>ServerDetails</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 4.
 */

public class ServerDetailsTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public ServerDetailsTest (String name)
   {  super(name);
   }

/**
 * Test the <code>getName</code> method.
 */

   public void testGetName()
   {  ServerDetails server = new ServerDetails("alpha", 40,
        "http://www.somewhere.ac.uk/alpha", "/home/alpha");

      Assert.assertEquals(server.getName(), "alpha");
   }

/**
 * Test the <code>getExpiryPeriod</code> method.
 */

   public void testGetExpiryPeriod()
   {  ServerDetails server = new ServerDetails("alpha", 40,
        "http://www.somewhere.ac.uk/alpha", "/home/alpha");

      Assert.assertEquals(server.getExpiryPeriod(), 40);
   }

/**
 * Test the <code>getURI</code> method.
 */

   public void testGetURI()
   {  ServerDetails server = new ServerDetails("alpha", 40,
        "http://www.somewhere.ac.uk/alpha", "/home/alpha");

      Assert.assertEquals(server.getURI(), 
        "http://www.somewhere.ac.uk/alpha");
   }

/**
 * Test the <code>getDirectory</code> method.
 */

   public void testGetDirectory()
   {  ServerDetails server = new ServerDetails("alpha", 40,
        "http://www.somewhere.ac.uk/alpha", "/home/alpha");

      Assert.assertEquals(server.getDirectory(), "/home/alpha");
   }

/**
 * Test the <code>toString</code> method.
 */

   public void testToString()
   {  ServerDetails server = new ServerDetails("alpha", 40,
        "http://www.somewhere.ac.uk/alpha", "/home/alpha");

      Assert.assertEquals(server.toString(), 
        "alpha (40 days, http://www.somewhere.ac.uk/alpha).");

      ServerDetails server2 = new ServerDetails();
      Assert.assertEquals(server2.toString(), 
        "(The object is undefined.)");
   }

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (ServerDetailsTest.class);
      junit.swingui.TestRunner.run (ServerDetailsTest.class);
   }
}
