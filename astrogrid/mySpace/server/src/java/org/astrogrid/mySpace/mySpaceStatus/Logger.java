package org.astrogrid.mySpace.mySpaceStatus;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The <code>Logger</code> class is the MySpace class for logging
 * messages.
 * 
 * <p>
 * The <code>Logger</code> class is used to log both error and
 * informational messages.  The messages may optionally be sent to
 * any combination of: (i) the standard AstroGrid log file (using
 * commons logging). (ii) a log file local to the MySpace System and
 * (iii) standard output.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 4.
 * @version Iteration 5.
 */

public class Logger
{  private static boolean astroGridLog = true; // Write AstroGrid log?
   private static boolean mySpaceLog = true;   // Write MySpace log?
   private static boolean echoLog = true;      // Echo log to standard out?

//    MySpace log file name.
   private static String mySpaceLogFileName = "./myspace.log";

   private static String accountId = null;   // Account Identifier.
   private static String actionName = null;  // Name of invoking action.

//
//PrintWriter for the MySpace log.

   private static PrintWriter mySpaceLogWriter;

//
//Logger for the Jakata commons logging.

   private static Log commonsLog = LogFactory.getLog(Logger.class); 

//
// ------------------------------------------------------------------------
//
// Constructors.

/**
 * Create a <code>MySpaceStatus</code> object.
 *
 * @param astroGridLog Flag indicating whether messages are to be
 *   written to the standard AstroGrid log.
 * @param mySpaceLog Flag indicating whether messages are to be
 *   written to the local MySpace log file.
 * @param echoLog Flag indicating whether messages are to be
 *   written to standard output.
 * @param  mySpaceLogFileName Name of the local MySpace log file.  The
 *   name should include the full, absolute directory path of the name.
 */

   public Logger (boolean astroGridLog, boolean mySpaceLog, boolean echoLog,
     String mySpaceLogFileName)
   {  this.astroGridLog = astroGridLog;
      this.mySpaceLog = mySpaceLog;
      this.echoLog = echoLog;
      this.mySpaceLogFileName = mySpaceLogFileName;

//
//   If required attempt to create an AstroGrid logging object using
//   commons logging.
//
//   Note that the error reporting is quite crude because the Logger
//   objects exist `beneath' the error reporting system.

      if (astroGridLog)
      {  try
         {
         }
         catch (Exception all)
         {  all.printStackTrace();
         }
      }

//
//   If required attempt to open a local MySpace log file.  The file
//   is opened such that any new messages are appended to an existing
//   file.

      if (mySpaceLog)
      {  try
         {  FileOutputStream fos = new FileOutputStream(
              mySpaceLogFileName, true);
            mySpaceLogWriter = new PrintWriter(fos);

            Date startDate = new Date();
            String startMessage = "===== Start of MySpace Service "
              + "logging session " + startDate.toString();

            mySpaceLogWriter.println(startMessage);
            mySpaceLogWriter.flush();
         }
         catch (Exception all)
         {  all.printStackTrace();
         }
      }
   }


/**
 * Create a <code>Logger</code> object and pass a message.  The
 * logging configuration parameters are unaltered.
 *
 * @param message Message to be logged.
 */

   public Logger (String message)
   {  this.appendMessage(message);
   }


/**
 * Constructor with no arguments.
 */

  public Logger ()
  {
  }


//
// ------------------------------------------------------------------------
//
// Methods.

/**
 * Set the details of the current Account using the MySpace system.
 *
 * @param account Account identifier or name.
 */

   public void setAccount (String accountId)
   {  this.accountId = accountId;
   }


/**
 * Set the details of the current action.
 *
 * @param actionName Name of the current action.
 */

   public void setActionName (String actionName)
   {  this.actionName = actionName;
   }


/**
 * Append a message to any of the logs which are currently in operation.
 */

   public void appendMessage(String message)
   {
//
//   Assemble a message header comprising a date stamp and the current
//   userId and communityId.

      Date currentDate = new Date();

      String messageHead = "--- MySpace " + currentDate.toString()
        + " (";

      if (accountId != null)
      {  messageHead = messageHead +  accountId + ", ";
      }

      if (actionName != null)
      { messageHead = messageHead  +  actionName;
      }

      messageHead = messageHead + ") --- \n";

//
//   Assemble the complete, final message.

      String completeMessage = messageHead + "  " + message + "\n";

//
//   If required append the message to the AstroGrid log.

      if (astroGridLog)
      {  try
         {  commonsLog.info(completeMessage);
         }
         catch (Exception all)
         {  all.printStackTrace();
         }
      }

//
//   If required append the message to the local MySpace log.

      if (mySpaceLog)
      {  try
         {  mySpaceLogWriter.println(completeMessage);
            mySpaceLogWriter.flush();
         }
         catch (Exception all)
         {  all.printStackTrace();
         }
      }

//
//   If required write the message to standard output.  Note that in
//   this case only the basic message, and not the header, is output.

      if (echoLog)
      {  System.out.println(message);
      }
   }


/**
 * Close the log files.
 */

   public void close()
   {

//
//   The standard AstroGrid log would be closed here if any closing
//   was required.

//
//   If required close the local MySpace log.

      if (mySpaceLog)
      {  try
         {  Date closeDate = new Date();
            String closeMessage = "--- Close of MySpace logging "
              + "session " + closeDate.toString() + "\n\n\n";

            mySpaceLogWriter.println(closeMessage);
            mySpaceLogWriter.flush();
            mySpaceLogWriter.close();
         }
         catch (Exception all)
         {  all.printStackTrace();
         }
      }
   }
}
