package org.astrogrid.mySpace.mySpaceStatus;

import java.io.*;
import java.util.*;

/**
 * The <code>MySpaceStatus</code> class is used for recording error
 * status information in the MySpace system.
 * 
 * <p>
 * All the member variables in the <code>MySpaceStatus</code> class are
 * declared static, so that they act as global variables.  Thus, there
 * is no need to pass a <code>MySpaceStatus</code> object throughout
 * the MySpace system in order to record the status information.  Rather
 * <code>MySpaceStatus</code> objects can be created locally and used
 * to set global status information.
 * </p>
 * <p>
 * The <code>MySpaceStatus</code> class is used to track whether errors
 * or warnings have occurred and to accumulate a Vector of
 * human-readable error, warning and informational messages.
 * (Informational messages are purely to supply information to the User;
 * they do not imply any misadventure.)  To accumulate a message and
 * set the status, simply create a <code>MySpaceStatus</code> and
 * invoke its <code>addMessage</code> method:
 * </p>
 * <p>
 * <code>MySpaceStatus msStatus = new MySpaceStatus();</code>
 * </p>
 * <p>
 * <code>msStatus.addMessage("message text", "e");</code>
 * </p>
 * <p>
 * or more conveniently these details can be passed using a constructor:
 * </p>
 * <p>
 * <code>MySpaceStatus msStatus = new MySpaceStatus("message text", "e");</code>
 * </p>
 * <p>
 * In both cases the first argument is the message text, and the second is
 * the type of event being described, coded as follows:
 * </p>
 * <ul>
 *   <li><code>"i"</code> - information (that is, nothing is amiss),</li>
 *   <li><code>"w"</code> - warning,</li>
 *   <li><code>"e"</code> - error.</li>
 * </ul>
 * <p>
 * In addition to setting error and warnings there are also methods to
 * get back the error (or rather success) and warnings flags and to 
 * retrieve the stack of messages (each represented as a
 * <code>MySpaceMessage</code>).  Note that warnings are not considered
 * errors and <code>MySpaceStatus</code> will report success if warnings
 * but no errors have been set.  There is also a convenience method to
 * report the messages to standard output.
 * 
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class MySpaceStatus
{  private static boolean successStatus = true;   // Running success status.
   private static boolean warningStatus = false;; // Running warning status.

   private static Vector messages = new Vector();
                   // Log of MySpace info, warning and error messages.

//
// Constructors.

/**
 * Create a <code>MySpaceStatus</code> object.
 */

   public MySpaceStatus ()
   {
   }

/**
 * Create a <code>MySpaceStatus</code> object and accumulate a message
 * and its associated type (<code>"i"</code>, <code>"w"</code> or
 * <code>"e"</code>, see above).
 */

   public MySpaceStatus (String message, String type)
   {  this.addMessage(message, type);
   }

//
// Get methods.

/**
 * Return the global success flag.  A value of true is returned if no
 * errors have been set.
 */

   public boolean getSuccessStatus()
   {  return successStatus;
   }

/**
 * Return the global warnings flag.  A value of true is returned if any
 * warnings have been set.
 */

   public boolean getWarningStatus()
   {  return warningStatus;
   }

/**
 * Return a Vector of <code>MySpaceMessage</code>s containing any messages
 * that have been set.
 */

   public Vector getMessages()
   {  return messages;
   }

//
// Other methods.

/**
 * Accumulate a new message in the <code>MySpaceStatus</code>.
 */

   public void addMessage(String message,  String type)
   {  MySpaceMessage newMessage = new MySpaceMessage(message, type);

      messages.add(newMessage);
      if (type.equals("e"))
      {  successStatus = false;
      }
      else if (type.equals("w"))
      {  warningStatus = true;
      }
   }

/**
 * Output any accumulated messages to standard output.
 */

   public void outputMessages()
   {  int numMessages = messages.size();

      for (int loop=0; loop<numMessages; loop++)
      {  MySpaceMessage currentMessage =
           (MySpaceMessage)messages.elementAt(loop);

         String message = currentMessage.getMessage();
         String type = currentMessage.getType();

         if (type.equals("i"))
         {  System.out.println("!(" + loop + ") Info:    " + message);
         }
         else if (type.equals("w"))
         {  System.out.println("!(" + loop + ") Warning: " + message);
         }
         else if (type.equals("e")) 
         {  System.out.println("!(" + loop + ") Error:   " + message);
         }
         else
         {  System.out.println("!(" + loop + ") Unknown: " + message);
         }
      }
   }

/**
 * Reset the <code>MySpaceStatus</code>.  The error and warning statuses
 * are reset (to success and no warnings, respectively) and any
 * accumulated messages are lost.
 */

   public void reset()
   {  successStatus = true;
      warningStatus = false;

      messages = new Vector();
   }
}
