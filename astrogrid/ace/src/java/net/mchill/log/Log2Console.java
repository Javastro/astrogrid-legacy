package net.mchill.log;

import java.util.Date;

/**
 * A LogHandler that writes all messages and
 * information out to the console.  Note that the console used is
 * System.out for exception stack dumps too, to prevent messages interlacing
 *
 * @version          :
 * @Created          :
 * @Last Update      :
 *
 * @author           : M Hill
 *
 */

public class Log2Console extends FilteredHandlerAdaptor
{
   
   /**
    * Constructor - NB - no longer automatically registers with MessageLog,
    * as I think that is naughty.  MCH.
    *
    **/
   public Log2Console()
   {
      System.out.println("Logging to Console started at "+new Date()+"...");
      System.out.println("");
   }

   /**
    * Implementation of LogHandler messageLogged.
    * Writes out all the information available to the console (System.out)
    * including exception stack dumps if present.
    * Synchronized to avoid different threads mixing up errors
    */
   public synchronized void  messageLogged(LogEvent anEvent)
   {
      //for trace, print summary message - don't bother with timestamp
      if (anEvent.getSeverity() != Severity.TRACE)
         System.out.print(anEvent.getSeverity()+" ");
      
      System.out.println(anEvent.getText());
      
      //if there's an exception, print it out as a stack trace - specify
      //System.out, otherwise it will be directed to System.err (also
      //the console normally) and this means it will interleave with any
      //other stuff going out at the same time.
      if (anEvent.getException() != null) {
         anEvent.getException().printStackTrace(System.out);
      }

      //separator line
      System.out.println("");
   }
}
