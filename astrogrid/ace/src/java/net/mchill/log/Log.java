package net.mchill.log;

import java.io.IOException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.NullPointerException;
import java.util.Vector;
import java.util.ArrayList;

/**
 * Log provides message logging functionality, for both programming use
 * (errors, debug & trace), as well as user messages (alarms, warnings, information).
 * This particular class acts as an event/message distributor
 * - it provides convenient methods for logging errors, and distributes these
 * to all registered LogHandler implementations.  Filters can be applied to
 * messages coming in and to individual log handlers.
 *
 * <P>It is implemented as a
 * Singleton design pattern, ensuring that only one instance of Log
 * is ever instantiated and that all messages are logged through that instance.
 *
 * The singleton instance itself is not visible, and access to the class should
 * be by the various static methods, eg logEvent(), logInfo(), etc.
 * <P>
 * Messages to be logged are instnaces of LogEvent (and subclasses), and all of
 * these are
 * forwarded to LogHandler instances, which will write
 * them out to the console, or a file, or make them available to
 * TCP/IP connections, or display them in a status line, etc, depending on
 * the implementation.
 * <P>
 * For the simplest way of using the logger, call the appropriate static convenience
 * routine, such as logError(), as follows:
 * <pre>
 *     log.Log.logError("Could not do task xxx");
 * or
 *     log.Log.logError("Could not do task xxxx",e);
 * </pre>
 * if you have an exception to log.
 *
 * <P>
 * In you initialisation code you can add other handlers as follows:
 * <pre>
 * log.Log.addHandler(new log.Log2File("log.txt"))
 * </pre>
 * and you can add a special status-bar which will display log events as follows:
 * <pre>
 * log.ui.StatusBar bar = new log.ui.StatusBar();
 * somePanel.add(bar);
 * log.Log.addHandler(bar);
 * </pre>
 *
 * @see Severity
 * @see LogHandler
 * @see LogEvent
 *
 * @version          :
 * @Created          : Oct 2000
 * @Last Update      : Aug 2001
 *
 * @author           : M Hill
 *
 */

public class Log
{
   /** Reference to the single Log instance. Note that lazy initialisation
    * is not threadsafe (even double checked locking - see JavaWorld articles)
    * unless synchronised checks are made, which hits performance. */
   private static Log fInstance = new Log();
   
   /** List of all the handlers that will receive message events*/
   private java.util.Vector handlers = new Vector();
   
   /** Input filters are applied
    * to messages as they are logged, and if they are rejected,  they
    * are not forwarded to <i>any</i> handlers...  Filters can also
    * be added to handlers.   A default severity filter is applied
    * for convenience via the setFilterSeverity() method for adding/removing
    * trace code/etc
    */
   private Vector inputFilters = new Vector();
   private SeverityFilter severityFilter = new SeverityFilter(null);

   //------------------------------------------------------------------
   /**
    * Constructor -"protected" to ensure that only Log methods can
    * instantiate a Log, ensuring its singletonness...
    * Automatically introduces a debug-level severity filter.
    **/
   protected Log()
   {
      //don't use addFilter() as fInstance is not set yet...
      inputFilters.add(severityFilter);
   }

   /**
    * A simple static method implementing an assertion check. Renamed to
    * affirm to avoid JDK 1.4s warnings about using assert.
    */
   public static void affirm(boolean assertion, String errorMessage)
   {
      if (!assertion)
         throw new AssertionException(errorMessage);

   }
   
   /**
    * Adds a handler to the Message Log.
    *
    * @param listener builder.LogListener
    */
   public static void addHandler(LogHandler handler)
   {
      fInstance.handlers.add(handler);
   }
   /**
    * Removes a handler
    *
    * @param listener builder.LogListener
    */
   public static void removeHandler(LogHandler listener)
   {
      fInstance.handlers.remove(listener);
   }

   /**
    * Removes all the handlers.
    */
   public static void removeAllHandlers()
   {
      fInstance.handlers.clear();
   }
   
   /**
    * Adds a filter to the list of input filters.
    * @param LogFilter
    */
   public static void addFilter(LogFilter filter)
   {
      fInstance.inputFilters.add(filter);
   }
   
   /**
    * Removes a filter from the list of input filters.
    * @param LogFilter
    */
   public static void removeFilter(LogFilter filter)
   {
      fInstance.inputFilters.remove(filter);
   }
   
   /**
    * Convenience routine for setting the severity level
    * of the input severity filter
    */
   public static void setFilterSeverity(Severity aSeverity)
   {
      fInstance.severityFilter.setSeverity(aSeverity);
   }
   
   /**
    * Forwards the given LogEvent to all registered handlers.
    * Synchronised to ensure listeners are handled one at a time, and
    * a copy is made of the list so that it can be iterated through
    * without worrying about add/remove methods interfering.
    *
    * @param anEvent builder.log.LogEvent
    */
   private synchronized void fireLogEvent(LogEvent anEvent)
   {
      if (handlers.size() == 0)
      {
         //default to console
         addHandler(new Log2Console());
//       System.out.println("LOG ERROR: No LogHandlers to report event to "+anEvent);
      }
      
      Object[] listCopy = handlers.toArray();
      for (int i = 0; i<listCopy.length; i++)
      {
         ((LogHandler) listCopy[i]).messageLogged(anEvent);
      }
   }
   
   /**
    * Tests event against all filters to see if it should
    * be kept.  Returns true if <b>all</b> filters want
    * to keep it
    */
   private boolean keepEvent(LogEvent anEvent)
   {
      Object[] listCopy = inputFilters.toArray();
      for (int i = 0; i<listCopy.length ; i++)
      {
         if (!((LogFilter) listCopy[i]).keepEvent(anEvent))
            return false;
      }
      return true;
   }
   
   /*-------------------------------------------------------------------*/
   /**
    * This is the most flexible of the public logXxxx methods, and the
    * one which ultimately all logXxxx methods must call.
    * <p>
    * It filters the event using the filters list, and then simply forwards the
    * given event to all the handlers that are
    * registered with the singleton, such as log file and status bars.
    * <p>
    * Normally the convenience methods logAlarm() etc will be used, but this
    * method allows subclasses of LogEvent to be introduced which may contain
    * more information.
    * <p>
    * The method is synchronized at the fireLogEvent method so that the
    * various listeners are handled in a threadsafe manner (ie, one at a time)
    *
    **/
   public static void logEvent( LogEvent anEvent )
   {
      if (fInstance.inputFilters != null)
      {
         // let all listeners know
         fInstance.fireLogEvent(anEvent);
      }
   }

   /** Convenience method for logging general event */
   public static void logEvent( Severity severity, String message )
   {
      logEvent(new LogEvent(severity, message));
   }

   /** Convenience method for logging general event */
   public static void logEvent( Severity severity, String userMessage, String moreInfo, Throwable th )
   {
      logEvent(new LogEvent(null, severity, userMessage, moreInfo, th));
   }

   /** Convenience method for logging general event */
   public static void logEvent(Object source, Severity severity, String userMessage, String moreInfo, Throwable th )
   {
      logEvent(new LogEvent(source, severity, userMessage, moreInfo, th));
   }

   /** Convenience method for logging alarms */
   public static void logAlarm( String message )
   {
      logEvent(new LogEvent(Severity.ALARM, message));
   }
   
   /** Convenience method for logging alarms */
   public static void logAlarm(Object aSource, String message )
   {
      logEvent(new LogEvent(aSource, Severity.ALARM, message, "", null));
   }

   /** Convenience method for logging alarms */
   public static void logAlarm(String userMessage, String detailMessage )
   {
      logEvent(new LogEvent(null, Severity.ALARM, userMessage, detailMessage, null));
   }

   /** Convenience method for logging warnings */
   public static void logWarning( String message )
   {
      logEvent(new LogEvent(Severity.WARNING, message));
   }

   /** Convenience method for logging warnings */
   public static void logWarning(Object aSource, String message )
   {
      logEvent(new LogEvent(aSource, Severity.WARNING, message, "", null));
   }
   
   /** Convenience method for logging warnings */
   public static void logWarning(Object aSource, String message, Throwable th )
   {
      logEvent(new LogEvent(aSource, Severity.WARNING, message, "", th));
   }

   /** Convenience method for logging warnings */
   public static void logWarning(Object aSource, String message, String moreInfo )
   {
      logEvent(new LogEvent(aSource, Severity.WARNING, message, moreInfo, null));
   }
   
   /** Convenience method for logging warnings */
   public static void logWarning(Object aSource, String message, String moreInfo, Throwable th )
   {
      logEvent(new LogEvent(aSource, Severity.WARNING, message, moreInfo, th));
   }

   /** Convenience method for logging info messages */
   public static void logInfo( String message )
   {
      logEvent(new LogEvent(Severity.INFO, message));
   }

   /** Convenience method for logging info messages */
   public static void logInfo(Object aSource, String message )
   {
      logEvent(new LogEvent(aSource, Severity.INFO, message, "", null));
   }
   
   /** Convenience method for logging info messages */
   public static void logInfo(String message, String usefulInfo )
   {
      logEvent(new LogEvent(null, Severity.INFO, message, usefulInfo, null));
   }
   
   /** Convenience method for logging program errors */
   public static void logError( String message )
   {
      logEvent(new LogEvent(Severity.ERROR, message));
   }
   
   /** Convenience method for logging program errors */
   public static void logError( String message, Throwable th )
   {
      logEvent(new LogEvent(null,Severity.ERROR, message, "", th));
   }
   
   /** Convenience method for logging program errors */
   public static void logError( String message, String moreInfo, Throwable th )
   {
      logEvent(new LogEvent(null,Severity.ERROR, message, moreInfo, th));
   }
   
   /** Convenience method for logging program errors */
   public static void logError(Object trap, String message, Throwable th )
   {
      logEvent(new LogEvent(trap, Severity.ERROR, message, "", th));
   }

   /** Convenience method for logging program errors */
   public static void logError(Object trap, String message, String moreInfo, Throwable th )
   {
      logEvent(new LogEvent(trap, Severity.ERROR, message, moreInfo, th));
   }
   
   /** Convenience method for logging program error messages that
    * will be reported with some other severity (eg warning) */
   public static void logError(Severity severity, String message, String moreInfo, Throwable th )
   {
      logEvent(new LogEvent(null,severity, message, moreInfo, th));
   }

   /** Convenience method for logging assertion-style debugging info
    * and tracetrace output. */
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
      if (fInstance.severityFilter.keepEvent(Severity.DEBUG))
      {
         logEvent(new LogEvent(null, Severity.DEBUG, message, "", th));
      }
   }
   
   /** Convenience method for adding trace code that can be distributed like
    * other log messages. */
   public static void trace( String message )
   {
      //check against severity filter here to reduce performance overhead when switching
      //off trace
      if (fInstance.severityFilter.keepEvent(Severity.TRACE))
      {
         logEvent(new LogEvent(null, Severity.TRACE, message, "", null));
      }
   }
}

