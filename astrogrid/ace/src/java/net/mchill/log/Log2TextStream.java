/*-------------------------------------------------------------------------

 * $Workfile: Log2File.java $

 * ------------------------------------------------------------------------

 * $Revision: 1.1.1.1 $
 * $Author: mch $
 * $Date: 2003/08/25 18:35:57 $
 * $Log: Log2TextStream.java,v $
 * Revision 1.1.1.1  2003/08/25 18:35:57  mch
 * Reimported to fit It02 source structure
 *
 * Revision 1.1.1.1  2002/11/05 14:54:46  mch
 * Initial Checkin
 *
 *
 * 1     19/12/01 10:12 Hillmc
 * existing (Juice-derived) log package included
 *

 * ------------------------------------------------------------------------

 * Package : mfp2.log

 * Description : See javadoc below

-------------------------------------------------------------------------*/

package net.mchill.log;

import java.io.*;

/**
 * A LogHandler that listens to the
 * message log, recording any logged messages/events to the given stream
 * as text
 * There is no need to keep a reference unless you wish to close the file, in
 * which case you call the close() method, which deregisters the object
 * from the MessageLog, and without a reference it will eventually
 * be garbage collected.
 * <p>
 * The file is opened and closed at each write, to ensure no losses due
 * to buffering.
 * <p>
 * Stack dumps can be switched off by using the doStackTrace(false) method,
 * which can also be set in the alternate constructor.  This allows us to
 * do 'neat' summary files, and more comprehensive debug files.
 *
 * @version
 * @Created     Oct 2000
 * @Last Update
 *
 * @author  M Hill
 *
 */

public class Log2TextStream extends FilteredHandlerAdaptor
{
   //file to log to - default log.txt
   protected OutputStream out;
   
   //whether to print stack dumps
   boolean doStackTrace = true;
   
   //maximum log file size - 0=unlimited
   int maxSize = 0;
   
   /**
    * Default constructor
    */
   public Log2TextStream(OutputStream newOut)
   {
      out = newOut;
   }
   
   /**
    * Switches stack dump printing on/off
    */
   public void doStackTrace(boolean b)
   {
      doStackTrace = b;
   }

   /**
    * Implementation of LogHandler method, called whenever a message is logged with
    * the MessagLog singleton.  Opens file based on fileName, writes text, and closes
    * again.  This ensures all data is flushed.
    *
    * @param LogEvent
    */
   public void messageLogged(LogEvent anEvent)
   {
      //no point in trying to handle errors to do with this.
      if ((anEvent.getSource() == this)
          && (anEvent.getSeverity().isWorseThan(Severity.INFO)))
          return;

      // open the Message Log file - don't need to open with flush as it
      //will be closed (--> flushed) at end anyway, and will just make the
      //itty bitty prints below take longer
      PrintWriter msgLog = new PrintWriter(out, false);
         
      // Write message to file
      msgLog.print( anEvent.toString() );

      // Write exception details
      if (anEvent.getException() == null)
      {
         msgLog.println();
      }
      else
      {
         if (doStackTrace)
         {
            msgLog.println();
            anEvent.getException().printStackTrace(msgLog);
         }
         else
         {
            msgLog.println(":  "+anEvent.getException());
         }
      }
         
      // Close File
      msgLog.close();
      
   }
}
