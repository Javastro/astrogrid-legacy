/**
 * LogFilter defines the interface that all filters must implement
 *
 * @author M Hill
 */

package net.mchill.log;

public interface LogFilter
{
   /** Returns true if the event is suitable for keeping */
   public boolean keepEvent(LogEvent event);
}

