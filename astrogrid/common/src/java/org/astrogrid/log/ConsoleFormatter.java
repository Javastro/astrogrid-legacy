/*
 * $Id ConsoleFormatter.java $
 *
 */

package org.astrogrid.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * For formatting errors to console nicely
 *
 * @author M Hill
 */

public class ConsoleFormatter extends Formatter
{
   
   /**
    * Format the given log record and return the formatted string.
    * <p>
    * The resulting formatted String will normally include a
    * localized and formated version of the LogRecord's message field.
    * The Formatter.formatMessage convenience method can (optionally)
    * be used to localize and format the message field.
    *
    * @param record the log record to be formatted.
    * @return the formatted log record
    */
   public String format(LogRecord record)
   {
      Date d = new Date(record.getMillis());

      SimpleDateFormat format = new SimpleDateFormat("yy-MM-DD HH:mm:ss.S");

      String msg = format.format(d)+" "+record.getLevel()+" "+record.getMessage()+"\n";

      if (record.getThrown() != null)
      {
         StringWriter writer = new StringWriter();
         record.getThrown().printStackTrace(new PrintWriter(writer));
         msg = msg + "\n"+writer.toString();
      }
      
      return msg;
   }
   
   
}

/*
$Log: ConsoleFormatter.java,v $
Revision 1.1  2003/09/11 17:53:54  mch
Added logTo() methods and made sure trace can go to console

*/
