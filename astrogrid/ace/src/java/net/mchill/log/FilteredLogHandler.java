package net.mchill.log;

/**
 * This filtered handler interface extends the LogHandler interface, defining
 * extra methods that must be provided by any log handlers that can filter
 * the events they will display.
 *
 * @version          :
 * @Created          : 09/01/01
 * @Last Update      :
 *
 * @author           : M Hill
 *
 * @see Log
 * @see LogHandler
 *
/*--------------------------------------------------------------------*/

public interface FilteredLogHandler extends LogHandler {

   /** add a Filter that log events will be checked against, to
    * see if this handler will ignore them or not
    *
    * @param LogFilter
    */
   void addFilter(LogFilter filter);

   /** remove a Filter that log events will be checked against, to
    * see if this handler will ignore them or not
    *
    * @param LogFilter
    */
   void removeFilter(LogFilter filter);

}
