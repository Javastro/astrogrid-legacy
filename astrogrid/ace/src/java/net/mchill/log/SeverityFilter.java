package net.mchill.log;

/**
 * SeverityFilter implements LogFilter, and is used to filter log events
 * based on their severity level.  Any messages of worse severity than
 * the one set here, will be kept - those of equal or better will be
 * binned.
 *
 * @author M Hill
 */

public class SeverityFilter implements LogFilter
{
   private Severity level = null;
   
   public SeverityFilter(Severity aSeverity)
   {
      setSeverity(aSeverity);
   }
   
   public void setSeverity(Severity aSeverity)
   {
      level = aSeverity;
   }
   
   public boolean keepEvent(LogEvent event)
   {
      return ((level==null) || (!level.isWorseThan(event.getSeverity())));
   }

   /** Returns true if the given severity is appropriate for keeping.  Can
    * be used (particularly for debug/trace) to decide whether to even make
    * the event in the first place, thus reducing overheads when trace/debug is off */
   public boolean keepEvent(Severity severity)
   {
      return ((level==null) || (!level.isWorseThan(severity)));
   }
}

