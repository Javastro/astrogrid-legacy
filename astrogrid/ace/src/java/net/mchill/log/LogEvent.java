package net.mchill.log;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;


/**
 * Wrapper class for the information about messages logged
 * with Log.
 *
 * @version
 * @Created
 * @Last Update
 *
 * @author    M Hill
 *
 * @see Log
 */

public class LogEvent implements Serializable
{
   protected Severity fSeverity;          // Severity of the event
   protected String fUserText = "";       // Event text to display to user
   protected String fUsefulInfo = "";     // Extra text that might be used to describe tekky stuff
   protected Throwable fException = null; // related exception (if any)

   /** What generated the event.  This is transient, as when we
    * store or transmit it we don't want to copy in whole swathes
    * of other info.  Instead we use fSourceName string. */
   protected transient Object fSource = null;
   
   /** Used instead of fSource when the event is serialised */
   protected String fSourceName = null;

   /** The thread that generated the event.  We don't want to
    * store the actual thread, as if we serialise & store/transmit the event
    * we don't end up with a thread class being generated along
    * with its runnable when it's retrieved.  */
   protected String threadName = Thread.currentThread().getName();
   
   /** Timestamp marking when the event was created */
   protected final Date fTimestamp = new Date();
   
   /** Null argument constructor for serialising **/
   public LogEvent()
   {
   }

   /** Simple constructor taking severity and string message **/
   public LogEvent( Severity aSeverity, String someUserText )
   {
      fSeverity = aSeverity;
      fUserText = someUserText;
   }
   
   /**
    * Full constructor.
    **/
   public LogEvent( Object aSource, Severity aSeverity, String someUserText, String someUsefulText, Throwable anException )
   {
      fSeverity = aSeverity;
      fUserText = someUserText;
      fUsefulInfo = someUsefulText;
      fException = anException;
      fSource = aSource;
      if (aSource != null) fSourceName = aSource.toString();
   }
   
   /** Gets associated exception */
   public Throwable getException()  {  return fException;   }
   
   /** Returns severity of event, @see Severity */
   public Severity getSeverity()    {  return fSeverity; }
   
   /** Returns descriptive message text of event */
   public String getText()          {  return fUserText;  }

   /** Returns extra (usually technical) information about event */
   public String getUsefulInfo()    {  return fUsefulInfo;  }

   /** Returns true if there is any extra (usually technical) info about the event */
   public boolean hasUsefulInfo()   {  return (fUsefulInfo != null) && (fUsefulInfo.length()>0); }
   
   /** Returns true if there is an exception included with the event */
   public boolean hasException()   {  return (fException != null); }
   
   /** Returns timestamp - when the event was created - returns as a
    * copy so it can't be altered */
   public Date getTimestamp()       {  return new Date(fTimestamp.getTime());   }
   
   /** Gets associated source - ie where the event came from*/
   public Object getSource()
   {
      if (fSource != null)
         return fSource;
      else
         return fSourceName;
   }
   
   /**
    * Converts event to a suitable string for a single-line display (eg status bar,
    * files, etc).  The format is designed for Eutelsat, but seems reasonably
    * generic.
    */
   public String toString()
   {
      return toString("yy-MM-dd HH:mm:ss");
   }
   
   /**
    * Converts event to a string for a single-line display (eg status bar,
    * files, etc), using the date format given
    */
   public String toString(String dateFormat)
   {
      // Format the current time.
      SimpleDateFormat formatter = new SimpleDateFormat (dateFormat);
      String dateString = formatter.format( fTimestamp );
      
      // Construct the message
      String msg = dateString + " " +
         getSeverity().toString() + " " +
         getText();
      
      return msg;
   }

   /** Convenience routine that returns the exception's stack trace (if
    * any) as a string. */
   public String getStackTrace()
   {
      if (getException() == null)
         return "";

      StringWriter sWriter = new StringWriter();
      
      getException().printStackTrace(new PrintWriter(sWriter));
      
      return sWriter.toString();
   }
}
