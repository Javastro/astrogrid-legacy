package org.astrogrid.mySpace.delegate.helper;

import java.io.*;
import java.util.*;

/**
 * The <code>DeLogger</code> class is for logging messages from within 
 * the mySpace delegate.
 * 
 * <p>
 * The <code>DeLogger</code> class is mostly used to log debugging
 * imessages.  The messages may optionally be sent to either of: (i)
 * a log file local to the MySpace delegate or (ii) standard output.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 4.
 */

public class DeLogger
{  private static boolean mySpaceLog = true;   // Write MySpace log?
   private static boolean echoLog = true;      // Echo log to standard out?

//    MySpace log file name.
   private static String mySpaceLogFileName = "./myspacedelegate.log";

   private static String userId = null;        // User Identifier.
   private static String communityId = null;   // Community Identifier.

//
//PrintWriter for the MySpace log.

   private static PrintWriter mySpaceLogWriter;

//
// ------------------------------------------------------------------------
//
// Constructors.

/**
 * Create a <code>MySpaceStatus</code> object.
 *
 * @param mySpaceLog Flag indicating whether messages are to be
 *   written to the local MySpace delegate log file.
 * @param echoLog Flag indicating whether messages are to be
 *   written to standard output.
 * @param  mySpaceLogFileName Name of the local MySpace delegate log file.
 *   The name should include the full, absolute directory path of the name.
 */

   public DeLogger (boolean mySpaceLog, boolean echoLog,
     String mySpaceLogFileName)
   {  this.mySpaceLog = mySpaceLog;
      this.echoLog = echoLog;
      this.mySpaceLogFileName = mySpaceLogFileName;

//
//   If required attempt to open a local MySpace log file.  The file
//   is opened such that any new messages are appended to an existing
//   file.

      if (mySpaceLog)
      {  try
         {  FileOutputStream fos = new FileOutputStream(
              mySpaceLogFileName, true);
//             "/home/avo/myspacedata/myspace.log", true);
            mySpaceLogWriter = new PrintWriter(fos);

            Date startDate = new Date();
            String startMessage = " ===== Start of MySpace delegate "
              + "logging session " + startDate.toString();

            mySpaceLogWriter.println(startMessage);
            mySpaceLogWriter.flush();
            mySpaceLogWriter.close();
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

   public DeLogger (String message)
   {  this.appendMessage(message);
   }


/**
 * Constructor with no arguments.
 */

  public DeLogger ()
  {
  }


//
// ------------------------------------------------------------------------
//
// Methods.

/**
 * Set the details of the current user of the MySpace system.
 */

   public void setUser (String userId, String communityId)
   {  this.userId = userId;
      this.communityId = communityId;
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

      String messageHead = "--- MySpace delegate " + currentDate.toString()
        + " (";

      if (userId != null)
      {  messageHead = messageHead + "user: " + userId + "@";
      }

      if (communityId != null)
      { messageHead = messageHead  + communityId;
      }

      messageHead = messageHead + ") --- \n";

//
//   Assemble the complete, final message.

      String completeMessage = messageHead + message;

//
//   If required append the message to the local MySpace log.

      if (mySpaceLog)
      {  try
         {  FileOutputStream fos = new FileOutputStream(
              mySpaceLogFileName, true);
//             "/home/avo/myspacedata/myspace.log", true);
            mySpaceLogWriter = new PrintWriter(fos);

            mySpaceLogWriter.println(completeMessage);
            mySpaceLogWriter.flush();
            mySpaceLogWriter.close();
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
//   If required close the local MySpace log.

      if (mySpaceLog)
      {  try
         {  FileOutputStream fos = new FileOutputStream(
              mySpaceLogFileName, true);
//             "/home/avo/myspacedata/myspace.log", true);
            mySpaceLogWriter = new PrintWriter(fos);

            Date closeDate = new Date();
            String closeMessage = " ===== Close of MySpace logging "
              + "session " + closeDate.toString() + "\n\n";

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
