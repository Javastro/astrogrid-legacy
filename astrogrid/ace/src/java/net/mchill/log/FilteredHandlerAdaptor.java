package net.mchill.log;

import java.util.*;

/**
 * An adaptor for FilteredLogHandlers.  It partially implements a
 * filtered log handler, so that we can quickly develop new
 * handlers without having to rewrite the addFilter/removeFilter, etc
 * methods each time.
 *
 * @version          :
 * @Created          : 09/01/01
 * @Last Update      :
 *
 * @author           : M Hill
 *
 * @see Log @seeFilteredLogHandler
 */

public class FilteredHandlerAdaptor implements FilteredHandler {

   /**The filters that will be applied to any events.  Applied as an AND - ie
    * any filter that does not want the event will cause that event to be filtered
    * out */
   Vector filters = new Vector();
   
   /** add a Filter that log events will be checked against, to
    * see if this handler will ignore them or not
    *
    * @param LogFilter
    */
   public void addFilter(LogFilter filter)
   {
      filters.add(filter);
   }
   
   /** remove a Filter that log events will be checked against, to
    * see if this handler will ignore them or not
    *
    * @param LogFilter
    */
   public void removeFilter(LogFilter filter)
   {
      filters.add(filter);
   }
   
   /**
    * Tests event against all filters to see if it should
    * be kept.  Returns true if <b>all</b> filters want
    * to keep it
    */
   public boolean keepEvent(LogEvent anEvent)
   {
      Object[] listCopy = filters.toArray();
      for (int i = 0; i<listCopy.length ; i++)
      {
         if (!((LogFilter) listCopy[i]).keepEvent(anEvent))
            return false;
      }
      return true;
   }

   /** Dummy messageLogged - if this adaptor is actually being subclassed
    * as a handler, this must be implemented. */
   public void messageLogged(LogEvent anEvent)
   {}
      
}
