/**
 * $Id: RendererSupport.java,v 1.1.1.1 2005/02/16 15:02:46 mch Exp $
 */
package org.astrogrid.storebrowser.html;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Some useful functions for renderers
 */
public class RendererSupport  {
   
   private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

   /** Returns the given string, or "" if it's null.  A convenience method so that
    * empty strings don;t turn up as 'null' on screen */
   public static String emptyNuller(String s) {
      if (s == null) {
         return "";
      }
      else {
         return s;
      }
   }
   
   

   /**
    * One day I'll work out how to do this as a popup
    */
   public void writeMessageBox(HttpServletResponse response, String title, String header, String message) throws IOException {
      writeMessageBox(response.getWriter(), title, header, message);
   }
   
   public void writeMessageBox(Writer out, String title, String header, String message) throws IOException {
      out.write(
         "<html>"+
         "<head><title>"+title+"</title></head>"+
         "<body>"+
         "<h1>"+header+"</h1>"+
         message+
         "</body>"+
         "</html>"
      );
   }
   
   public String date(Date d) {
      if (d==null) {
         return "";
      }
      else {
         return dateFormat.format(d);
      }
   }
   
}
