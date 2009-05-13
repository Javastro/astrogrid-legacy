/*
 * $Id: AsciiTableSupport.java,v 1.1.1.1 2009/05/13 13:20:50 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.out;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.astrogrid.cfg.ConfigFactory;

/**
 * Provides some methods for writing tables in ASCII text. Mostly help for
 * dates.
 *
 * @author M Hill
 */

public abstract class AsciiTableSupport implements TableWriter {
   
   /** Key used to configure date formats */
   public static final String DATE_FORMAT_KEY = "default.date.pattern";

   protected static final String defaultDatePattern = "yyyy-MM-dd'T'HH:mm:ss";
   protected DateFormat dateFormat = null;

   protected AsciiTableSupport() {
      dateFormat = new SimpleDateFormat(ConfigFactory.getCommonConfig().getString(DATE_FORMAT_KEY, defaultDatePattern));
      
   }
   
   /** Returns an empty string if the given string is null, otherwise returns
    * the string.  Prevents annoying 'null' turning up if there is no entry. In
    *  general, use it for header info; for cell values, it's best to use something
    * to distinguish between 'no value' and 'empty value'
    */
   public String emptyIfNull(String s) {
      if (s==null) return ""; else return s;
   }
            
   
   
}

/*
 $Log: AsciiTableSupport.java,v $
 Revision 1.1.1.1  2009/05/13 13:20:50  gtr


 Revision 1.1  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.1  2005/02/11 15:58:27  mch
 Some fixes before split



 */







