package net.mchill.log;

/**
 * handler interface for Log.  Log handlers implement the action
 * that should be taken on a message being logged.  A handler might
 * be a status bar, or a console-writer, or a file-writer, etc.
 *
 * Interface for any classes that wish to listen to the message log.
 * handlers must implement the method messageLogged() which is called
 * by Log whenever a message is logged with it.
 *
 * @version          :
 * @Created          : 09/01/01
 * @Last Update      :
 *
 * @author           : M Hill
 *
 * @see Log
 *
/*--------------------------------------------------------------------*/

public interface LogHandler
{
   /** Called by MessageLog whenever a message is logged, with the message details */
   void messageLogged(LogEvent event);
}
