package org.astrogrid.mySpace.mySpaceStatus;

import junit.framework.*;

import java.io.File;
import org.astrogrid.mySpace.mySpaceStatus.Logger;

/**
 * Junit tests for the <code>Logger</code> class.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 5.
 * @version Iteration 5.
 */

public class LoggerTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public LoggerTest (String name)
   {  super(name);
   }

/**
 * Test the <code>Logger</code> class.  
 */

   public void testLogger()
   {  String logFile = "test.log";

      Logger logger = new Logger(false, true, true, logFile);

      logger.setAccount("some user");
      logger.setActionName("some action name");
      logger.appendMessage("Some message...");

      Logger logger2 = new Logger("Another message...");

      logger.close();


      File outFile = new File(logFile);
      Assert.assertTrue(outFile.exists() );

      try
      {  outFile.delete();
      }
      catch (Exception all)
      {  System.out.println("Failed to delete test log file.");
         all.printStackTrace();
      }
   }


/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (LoggerTest.class);
      junit.swingui.TestRunner.run (LoggerTest.class);
   }
}
