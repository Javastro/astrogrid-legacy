/*
 $Id: Log.java,v 1.1.1.1 2003/08/25 18:36:28 mch Exp $
 */

package org.astrogrid.log;

import net.mchill.log.LogEvent;
import net.mchill.log.Severity;

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
 * In particular this applies to a <code>getImplementation()</code> method.  This might return
 * the actual implementation, so that we can carry out such tasks as setting
 * up dialog boxes, file logging, etc at the beginning of an application, using
 * the facilities available for that particular implementation.  We do not want
 * this wrapper to imply that such facilities are available when they are not.
 * <p>
 * This static wrapper assumes errors will be logged to the console by default, and there
 * is also an option for specifying a file to log to.  Any more complex requirements
 * should refer to the implementation using <code>getImplementation()</code> or by directly
 * accessing the logging package used.
 *
 * <p>
 * This implementation <b>uses the <code>net.mchill.log</code></b> package
 * which provides console, file, and tcp/ip logging, as
 * well as user messaging to status lines and userMessage boxes.
 *
 * @Created          : Oct 2002
 * @author           : M Hill
 */

public class Log
{
   public static boolean traceOn = true;

   /**
    * A simple static method implementing an assertion check.  Renamed to
    * affirm to avoid JDK 1.4s warnings about using assert.  Throws an
    * implementation-specific assertion (runtime) exception if the condition
    * is not true
    */
   public static void affirm(boolean assertion, String errorMessage)
   {
      net.mchill.log.Log.affirm(assertion, errorMessage);
   }

   /** Convenience method for logging alarms */
   public static void logAlarm( String userMessage )
   {
      net.mchill.log.Log.logEvent(new LogEvent(Severity.ALARM, userMessage));
   }

   /** Convenience method for logging alarms */
   public static void logAlarm(String userMessage, String detailMessage )
   {
      net.mchill.log.Log.logEvent(new LogEvent(null, Severity.ALARM, userMessage, detailMessage, null));
   }

   /** Convenience method for logging warnings */
   public static void logWarning( String userMessage )
   {
      net.mchill.log.Log.logEvent(new LogEvent(Severity.WARNING, userMessage));
   }

   /** Convenience method for logging warnings */
   public static void logWarning(Object aSource, String userMessage, Throwable th )
   {
      net.mchill.log.Log.logEvent(new LogEvent(aSource, Severity.WARNING, userMessage, "", th));
   }

   /** Convenience method for logging warnings */
   public static void logWarning(Object aSource, String userMessage, String moreInfo )
   {
      net.mchill.log.Log.logEvent(new LogEvent(aSource, Severity.WARNING, userMessage, moreInfo, null));
   }

   /** Convenience method for logging warnings */
   public static void logWarning(Object aSource, String userMessage, String moreInfo, Throwable th )
   {
      net.mchill.log.Log.logEvent(new LogEvent(aSource, Severity.WARNING, userMessage, moreInfo, th));
   }

   /** Convenience method for logging info messages */
   public static void logInfo( String userMessage )
   {
      net.mchill.log.Log.logEvent(new LogEvent(Severity.INFO, userMessage));
   }

   /** Convenience method for logging info messages */
   public static void logInfo(Object aSource, String userMessage )
   {
      net.mchill.log.Log.logEvent(new LogEvent(aSource, Severity.INFO, userMessage, "", null));
   }

   /** Convenience method for logging info messages */
   public static void logInfo(String userMessage, String usefulInfo )
   {
      net.mchill.log.Log.logEvent(new LogEvent(null, Severity.INFO, userMessage, usefulInfo, null));
   }

   /** Convenience method for logging program errors */
   public static void logError( String userMessage )
   {
      net.mchill.log.Log.logEvent(new LogEvent(Severity.ERROR, userMessage));
   }

   /** Convenience method for logging program errors */
   public static void logError( String userMessage, Throwable th )
   {
      net.mchill.log.Log.logEvent(new LogEvent(null,Severity.ERROR, userMessage, " ", th));
   }

   /** Convenience method for logging program errors */
   public static void logError( String userMessage, String moreInfo, Throwable th )
   {
      net.mchill.log.Log.logEvent(new LogEvent(null,Severity.ERROR, userMessage, moreInfo, th));
   }

   /** Convenience method for logging program errors */
   public static void logError(Object trap, String userMessage, Throwable th )
   {
      net.mchill.log.Log.logEvent(new LogEvent(trap, Severity.ERROR, userMessage, "", th));
   }

   /** Convenience method for logging program errors */
   public static void logError(Object trap, String userMessage, String moreInfo, Throwable th )
   {
      net.mchill.log.Log.logEvent(new LogEvent(trap, Severity.ERROR, userMessage, moreInfo, th));
   }

   /** Convenience method for logging program error messages that
    * will be reported with some other severity (eg warning) */
   public static void logError(Severity severity, String userMessage, String moreInfo, Throwable th )
   {
      net.mchill.log.Log.logEvent(new LogEvent(null,severity, userMessage, moreInfo, th));
   }

   /** Convenience method for logging assertion-style debugging info
    * and trace output. */
   public static void logDebug( String userMessage )
   {
      logDebug(userMessage, null);
   }

   /** Convenience method for logging assertion-style debugging info.
    * For example, if a connection fails, the user will be notified
    * but you might use this to add more detailed technical
    * information.*/
   public static void logDebug( String userMessage, Throwable th )
   {
      net.mchill.log.Log.logDebug(userMessage, th);
   }

   /** Convenience method for adding trace code that can be distributed like
    * other log messages. */
   public static void trace( String userMessage )
   {
      if (traceOn)
      {
         net.mchill.log.Log.trace(userMessage);
      }
   }

   /** Switch trace on - ie allow trace messages to be logged */
   public static void traceOn()     { traceOn = true; }

   /** Switch trace off - ie stop trace messages from being logged */
   public static void traceOff()    { traceOn = false; }

   /** Output goes to given file as well as console
    */
   public static void logToFile(String filename)
   {
      //the net.mchill.log implementation assumes that if you're logging to file
      //before you call any log statements, and you haven't explicitly specified
      //logging to console, then it will log to file instead of console.  So here
      //we specify console explicitly
      net.mchill.log.Log.addHandler(new net.mchill.log.Log2Console());
      net.mchill.log.Log.addHandler(new net.mchill.log.Log2File(filename));
   }

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

