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
import java.util.logging.Level;
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

      SimpleDateFormat format = new SimpleDateFormat("yy-MM-DD HH:mm:ss");

      String msg = format.format(d)+" "+record.getLevel()+" "+record.getMessage();

      if (record.getThrown() != null)
      {
         if (record.getLevel().intValue() < Level.SEVERE.intValue())
         {
            msg = msg + " ("+record.getThrown()+")\n";
         }
         else
         {
            StringWriter writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            msg = msg + "\n\n"+writer.toString();
         }
      }
      else
      {
         msg = msg +"\n";
      }

      return msg;
   }


}

/*
$Log: ConsoleFormatter.java,v $
Revision 1.3  2003/09/15 11:47:14  mch
Fixes to handle the built-in loggers hidden console output

Revision 1.2  2003/09/14 22:34:02  mch
Nicer warning messages

Revision 1.1  2003/09/11 17:53:54  mch
Added logTo() methods and made sure trace can go to console

*/
