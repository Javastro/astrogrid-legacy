package org.astrogrid.myspace.delegate;

import junit.framework.*;
import java.util.*;

import org.astrogrid.myspace.delegate.StatusMessage;

/**
 * Junit tests for the <code>StatusMessage</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 5.
 */

public class StatusMessageTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public StatusMessageTest (String name)
   {  super(name);
   }

/**
 * Test the <code>get</code> methods.
 */

   public void testGets()
   {  int    severity = StatusCodes.ERROR;
      String message = "Failed to access item in registry.";
      Date timeStamp = new Date(0);

      StatusMessage status = new StatusMessage(severity, message, timeStamp);

      Assert.assertEquals(status.getSeverity(), severity);
      Assert.assertEquals(status.getMessage(), message);
      Assert.assertEquals(status.getTimeStamp(), timeStamp);
   }


/**
 * Test the <code>toString</code> method.
 */

   public void testToString()
   {  String message = "Failed to access item in registry.";
      Date timeStamp = new Date(0);

      StatusMessage status = new StatusMessage(StatusCodes.ERROR,  message,
        timeStamp);

      Assert.assertEquals(status.toString(),
       "!Error: Failed to access item in registry.");
   }

/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (StatusMessageTest.class);
      junit.swingui.TestRunner.run (StatusMessageTest.class);
   }
}
