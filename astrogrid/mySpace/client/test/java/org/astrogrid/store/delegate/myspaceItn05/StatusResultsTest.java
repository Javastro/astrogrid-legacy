package org.astrogrid.store.delegate.myspaceItn05;

import junit.framework.*;
import java.util.*;

import org.astrogrid.store.delegate.myspaceItn05.StatusResults;
import org.astrogrid.store.delegate.myspaceItn05.StatusCodes;

/**
 * Junit tests for the <code>StatusResults</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 5.
 */

public class StatusResultsTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public StatusResultsTest (String name)
   {  super(name);
   }

/**
 * Test the <code>get</code> methods.
 */

   public void testGets()
   {  int    severity = StatusCodes.ERROR;
      String message = "Some arbitrary message.";
      Date   timeStampDate = new Date(0);
      long   timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

      
      Assert.assertEquals(status.getSeverity(), severity);
      Assert.assertEquals(status.getMessage(), message);
      Assert.assertEquals(status.getTimeStamp(), timeStamp);
   }


/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (StatusResultsTest.class);
      junit.swingui.TestRunner.run (StatusResultsTest.class);
   }
}
