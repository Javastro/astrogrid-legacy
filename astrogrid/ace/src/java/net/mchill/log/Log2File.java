package net.mchill.log;

import java.io.*;

/**
 * A LogHandler that listens to the
 * message log, recording any logged messages/events in the given
 * file.
 * <p>
 * Use: When you want to start logging errors to file (probably at the
 * beginning of your main() method), add the following line:
 * <pre>
 *       Log.addListener(new LogFile("[location to output log]"));
 * </pre>
 * There is no need to keep a reference unless you wish to close the file, in
 * which case you call the close() method, which deregisters the object
 * from the MessageLog, and without a reference it will eventually
 * be garbage collected.
 * <p>
 * The file is opened and closed at each write, to ensure no losses due
 * to buffering.
 *
 * @version          :
 * @Created          : Oct 2000
 * @Last Update      :
 *
 * @author           : M Hill
 *
 */

public class Log2File extends FilteredHandlerAdaptor
{
   //file to log to - default log.txt
   private java.lang.String fileName ="Log.txt";
   boolean doStackTrace = true;
   
   /**
    * Constructor.  Boolean indicates whether to print stack traces for
    * exceptions, or the just the exception message
    **/
   public Log2File(String aFileName, boolean doPrint)
   {
      fileName = aFileName;
      doStackTrace = doPrint;
   }
   /** Convenience constructor assuming exception stack traces will be
    * written out */
   public Log2File(String aFileName)
   {
      this(aFileName, true);
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

      try
      {
         // open the Message Log file - don't need to open with flush as it
         //will be closed (--> flushed) at end anyway, and will just make the
         //itty bitty prints below take longer
         PrintWriter msgLog = new PrintWriter(new FileWriter( fileName, true ), false); //open with append.
         
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
      catch( IOException e )
      {
         // This is tricky - don't really want to write to console in
         // case there isn't one.  Could log a new error, but with this as
         // the source, so it gets ignored above, but there is a danger
         // we can still end up with recursive icks with two log files...
         // on the other hand, at least some logHandler is likely to catch
         // that and display it.
         Log.logError( "Error writing to Event Log file " + fileName+", error "+e);
      }
      
   }
}
