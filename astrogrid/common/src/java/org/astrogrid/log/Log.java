/*
 $Id: Log.java,v 1.9 2004/06/18 11:07:02 jdt Exp $
 */

package org.astrogrid.log;

import org.apache.commons.logging.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * A covenience singleton, providing easy to use access to logging facilities,
 * for example:
 * <pre>
 *     Log.logError("Could not do task xxx");
 * or
 *     Log.logError("Could not do task xxxx",exception);
 * </pre>
 * if you have an exception to log.
 * <p>
 * trace code can be logged as follows:
 * <pre>
 *     Log.trace("Starting xxx...");
 * </pre>
 * and turned on/off by:
 * <pre>
 *     Log.traceOn();
 * or
 *     Log.traceOff();
 * </pre>
 * <p>
 * Assertion-style checks can be made as follows:
 * <pre>
 *     Log.affirm(condition that should be true, "Message if it is not");
 * </pre>
 * ('assert' has become a keyword in JDK 1.4)
 *
 * The purpose of this class is to act as a 'wrapper' around whatever
 * implentation of error, trace & userMessage logging the astrogrid project settles
 * on.  If and when the project switches from using one to another, this class
 * will be rewritten to route messages to the new system.
 * <p>
 * The methods are all static, so that routines that need to report errors do not
 * need to be concerned about tracking log instances. So it is <i>easier</i> for the programmer
 * to use this rather than System,out.println etc, as this will also print exceptions and log it
 * all to file if required.
 * <p>
 * <b>Note</b> that this is not some abstracted/factoried class.  Its
 * purpose is to reduce the work of porting; therefore it is not an implementation
 * of some generalised abstract interface.  An implementation of an interface, with highly generalised
 * methods, would tend to break at runtime, the last place you want a logging
 * system to break.  However if this class is rewritten, and something forgotten
 * or a method not written correctly, the project will break at compile time.
 * </p>
 * In particular this applies to the <code>getImplementation()</code> method.  This returns
 * the actual implementation, so that we can carry out such tasks as setting
 * up dialog boxes, file logging, etc at the beginning of an application, using
 * the facilities available for that particular implementation.  We do not want
 * this wrapper to imply that such facilities are available when they are not.
 * <p>
 * This static wrapper assumes errors will be logged to the console by default, and there
 * is also an option for specifying a file to log to.  Any more complex requirements
 * should refer to the implementation using <code>getImplementation()</code>
 * <p>
 * This implementation uses the commons-logging package from apache, itself a wrapper
 * for plugin logging implementations.  It exists as a stop-gap measure for code
 * that still needs to be converted to use commons-logging. It's also a bit easier
 * to use as it requires no class instances and provides some handy methods for
 * doing things like logging to file or console...
 * <p>
 * Error types.  There are two types of errors; alarms and errors. Errors apply
 * to problems with code; alarms to 'expected' problems with the system.  For example, if
 * the sofware is monitoring equipment and the equipment fails, then an alarm should
 * be raised.  Similarly there are two levels of trace; trace for reporting routing
 * through code, debug for reporting extra information about state (eg fine failed connection
 * details).
 * <P>
 * @author           : M Hill
 *
 */

public class Log
{
   public static boolean traceOn = true;

   private static org.apache.commons.logging.Log
      logger = LogFactory.getLog( "Application wide" );


   /**
    * A simple static method implementing an assertion check.  Named
    * affirm to avoid JDK 1.4s warnings about using assert.  Throws an
    * implementation-specific assertion (runtime) exception if the condition
    * is not true
    */
   public static void affirm(boolean assertion, String errorMessage)
   {
      if (!assertion)
      {
         throw new AssertionError(errorMessage);
      }
   }

   /** Convenience method for logging alarms */
   public static void logAlarm( String userMessage )
   {
      logger.error(userMessage);
   }

   /** Convenience method for logging alarms */
   public static void logAlarm(String userMessage, String detailMessage )
   {
      logger.error(userMessage+"\n"+detailMessage);
   }

   /** Convenience method for logging warnings */
   public static void logWarning( String userMessage )
   {
      logger.warn(userMessage);
   }

   /** Convenience method for logging info messages */
   public static void logInfo( String userMessage )
   {
      logger.info(userMessage);
   }

   /** Convenience method for logging info messages */
   public static void logInfo(String userMessage, String usefulInfo )
   {
      logger.info(userMessage+"\n"+usefulInfo);
   }

   /** Convenience method for logging program errors */
   public static void logError( String userMessage )
   {
      logger.error(userMessage);
   }

   /** Convenience method for logging program errors */
   public static void logError( String userMessage, Throwable th )
   {
      logger.error(userMessage, th);
   }

   /** Convenience method for logging program errors */
   public static void logError( String userMessage, String usefulInfo, Throwable th )
   {
      logger.error(userMessage+"\n"+usefulInfo, th);
   }

   /** Convenience method for logging assertion-style debugging info
    * and trace output. */
   public static void logDebug( String userMessage )
   {
      logger.debug(userMessage);
   }

   /** Convenience method for logging assertion-style debugging info.
    * For example, if a connection fails, the user will be notified
    * but you might use this to add more detailed technical
    * information.*/
   public static void logDebug( String userMessage, Throwable th )
   {
      logger.debug(userMessage, th);
   }

   /** Convenience method for adding trace code that can be distributed like
    * other log messages. */
   public static void trace( String userMessage )
   {
      if (traceOn)
      {
         logger.trace(userMessage);
      }
   }

   /** Switch trace on - ie allow trace messages to be logged */
   public static void traceOn()     { setLevelFinest(); traceOn = true; }

   /** Switch trace off - ie stop trace messages from being logged */
   public static void traceOff()    { traceOn = false; }

   /** Not strictly necessary, but makes sure that trace code can be logged,
    * and writes out a 'starting' message to the log, which can be very
    * useful when trawling through large log files
    */
   public static void starting(String msg)
   {
      traceOn();

      trace("Logging Started "+new Date()+" "+msg+"...");
   }

   /** Makes sure the logging system will log trace code */
   public static void setLevelFinest()
   {
   }

   /** Direct output to file (as well)
    */
   public static void logToFile(String filename) throws IOException
   {
     // Handler handler = new FileHandler(filename, true); //append
     // handler.setFormatter(new SimpleFormatter());
     // logger.addHandler(handler);
   }

   /**
    * Direct output to stream (as well) in simple format
    */
   public static void logToStream(OutputStream out) throws IOException
   {
      //logger.addHandler(new StreamHandler(out, new SimpleFormatter()));
   }

   /**
    * Direct output to stream (as well) in XML format
    */
   public static void logXmlToStream(OutputStream out) throws IOException
   {
      //logger.addHandler(new StreamHandler(out, new XMLFormatter()));
   }

   /**
    * Direct output to console (as well).  This is a bit messy for the
    * built-in logger, which has its own way of outputting to the console
    * that we need to remove/change
    */
   public static void logToConsole() throws IOException
   {
      //remove all existing console handlers
     // Enumeration loggerIndex = LogManager.getLogManager().getLoggerNames();

     // while (loggerIndex.hasMoreElements())
      //{
      //   Logger aLogger = LogManager.getLogManager().getLogger((String) loggerIndex.nextElement());

         //remove existing console handlers
      //   Handler[] handlers = aLogger.getHandlers();

       //  for (int i=0;i<handlers.length;i++)
        // {
        //    if (handlers[i] instanceof ConsoleHandler)
        //    {
        //       aLogger.removeHandler(handlers[i]);
        //    }
        // }
      //}



      //Handler handler = new ConsoleHandler();
      //handler.setLevel(Level.FINEST);
      //handler.setFilter(null);
      //handler.setFormatter(new ConsoleFormatter());
      //logger.addHandler(handler);
   }

   /**
    * Returns implementation - this allows applications to do what they need
    * with the logger, while still letting 99% of the code do its stuff through
    * the standard methods above.  Hopefully giving us the best of all worlds...
    * <p>Note that this returns the correct class, not some abstract Object,
    * so that code using it is correctly checked at compile time
    *
   public static Logger getImplementation()
   {
      return LogFactory.getLog("lklk").
      //return logger;
   }
	 */

   /**
    * Test harness
    */
   public static void main(String[] args) throws Exception
   {
      Log.trace("Testing Log wrapper...");
      Log.logError("This is a program error with a runtime exception",new RuntimeException("An example runtime exception"));
      Log.logAlarm("This is a user alarm","Don't worry about it");
      Log.logInfo("This is a bit of info that might be routed to a status line");
      Log.trace("...done basic test");
//      new AceProducer(new URL("http://aladin.u-strasbg.fr/java/alapre-test.pl?-c=name+cdfs&out=image&fmt=JPEG&resolution=STAND&qual=GOODS+WFI-B99+____"), null);
   }

}
/*
$Log: Log.java,v $
Revision 1.9  2004/06/18 11:07:02  jdt
Coding standards tidying up.

Revision 1.8  2003/12/16 11:29:47  mch
Removed specialist setup methods

Revision 1.7  2003/10/31 16:53:31  mch
Added deprecated tag

Revision 1.6  2003/09/24 19:10:55  mch
Added message to starting message

Revision 1.5  2003/09/15 11:47:14  mch
Fixes to handle the built-in loggers hidden console output

Revision 1.4  2003/09/11 17:53:54  mch
Added logTo() methods and made sure trace can go to console

Revision 1.3  2003/09/11 16:33:59  mch
Added logToXxxx methods

Revision 1.2  2003/09/06 17:31:49  mch
Logger Wrapper for JDK1.4 built-in logging

Revision 1.1  2003/09/06 17:11:33  mch
Initial Logger Wrapper (for net.mchill.log.*)

 */


