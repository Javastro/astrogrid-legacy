package org.astrogrid.tools.log;

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
 * and turn it on/off by:
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
 * NB; at the moment the implementation is using the net.mchill.log package
 * which is not 'standard'.  At some point it needs to be converted over
 * to log4j or JDKs 1.4 as necessary. (Or all three, then whichever is
 * wanted can be included in the class path... hmmm...)
 *
 * @Created          : Oct 2002
 * @author           : M Hill
 */

public class Log
{
   public static boolean traceOn = true;

   /**
    * A simple static method implementing an assertion check.  Renamed to
    * affirm to avoid JDK 1.4s warnings about using assert.
    */
   public static void affirm(boolean assertion, String errorMessage)
   {
      //net.mchill.log.Log.affirm(assertion, errorMessage);
   }

   /** Convenience method for logging alarms */
   public static void logAlarm( String message )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(net.mchill.log.Severity.ALARM, message));
   }

   /** Convenience method for logging alarms */
   public static void logAlarm(String userMessage, String detailMessage )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(null, net.mchill.log.Severity.ALARM, userMessage, detailMessage, null));
   }

   /** Convenience method for logging warnings */
   public static void logWarning( String message )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(net.mchill.log.Severity.WARNING, message));
   }

   /** Convenience method for logging warnings */
   public static void logWarning(Object aSource, String message, Throwable th )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(aSource, net.mchill.log.Severity.WARNING, message, "", th));
   }

   /** Convenience method for logging warnings */
   public static void logWarning(Object aSource, String message, String moreInfo )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(aSource, net.mchill.log.Severity.WARNING, message, moreInfo, null));
   }

   /** Convenience method for logging warnings */
   public static void logWarning(Object aSource, String message, String moreInfo, Throwable th )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(aSource, net.mchill.log.Severity.WARNING, message, moreInfo, th));
   }

   /** Convenience method for logging info messages */
   public static void logInfo( String message )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(net.mchill.log.Severity.INFO, message));
   }

   /** Convenience method for logging info messages */
   public static void logInfo(Object aSource, String message )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(aSource, net.mchill.log.Severity.INFO, message, "", null));
   }

   /** Convenience method for logging info messages */
   public static void logInfo(String message, String usefulInfo )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(null, net.mchill.log.Severity.INFO, message, usefulInfo, null));
   }

   /** Convenience method for logging program errors */
   public static void logError( String message )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(net.mchill.log.Severity.ERROR, message));
   }

   /** Convenience method for logging program errors */
   public static void logError( String message, Throwable th )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(null,net.mchill.log.Severity.ERROR, message, "", th));
   }

   /** Convenience method for logging program errors */
   public static void logError( String message, String moreInfo, Throwable th )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(null,net.mchill.log.Severity.ERROR, message, moreInfo, th));
   }

   /** Convenience method for logging program errors */
   public static void logError(Object trap, String message, Throwable th )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(trap, net.mchill.log.Severity.ERROR, message, "", th));
   }

   /** Convenience method for logging program errors */
   public static void logError(Object trap, String message, String moreInfo, Throwable th )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(trap, net.mchill.log.Severity.ERROR, message, moreInfo, th));
   }

   /** Convenience method for logging program error messages that
    * will be reported with some other severity (eg warning) */
   public static void logError(String foo /*net.mchill.log.Severity severity*/, String message, String moreInfo, Throwable th )
   {
      //net.mchill.log.Log.logEvent(new net.mchill.log.LogEvent(null,severity, message, moreInfo, th));
   }

   /** Convenience method for logging assertion-style debugging info
    * and trace output. */
   public static void logDebug( String message )
   {
      logDebug(message, null);
   }

   /** Convenience method for logging assertion-style debugging info.
    * For example, if a connection fails, the user will be notified
    * but you might use this to add more detailed technical
    * information.*/
   public static void logDebug( String message, Throwable th )
   {
      //net.mchill.log.Log.logDebug(message, th);
   }

   /** Convenience method for adding trace code that can be distributed like
    * other log messages. */
   public static void trace( String message )
   {
      if (traceOn)
      {
         //net.mchill.log.Log.trace(message);
      }
   }

   /** Switch trace on - ie allow trace messages to be logged */
   public static void traceOn()     { traceOn = true; }

   /** Switch trace off - ie stop trace messages from being logged */
   public static void traceOff()    { traceOn = false; }
}