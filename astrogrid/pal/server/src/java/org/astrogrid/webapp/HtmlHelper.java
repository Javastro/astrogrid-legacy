/*
 * $Id: HtmlHelper.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.webapp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Principal;
import java.util.Enumeration;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.astrogrid.account.LoginAccount;

/**
 * A set of methods for helping serving data in HTML form, eg for servlets
 * or JSPs
 * <p>
 * @author M Hill
 */

public class HtmlHelper
{

   /**
    * Gets the user details from the request */
   public static Principal getUser(HttpServletRequest request)  {
      return LoginAccount.ANONYMOUS;
   }

   
   /** Convenience routine for returning the correct 'HTML' snippet that
    * refreshes the page given by the URL - which should point to the same page
    * that contains the snippet */
   public static String makeRefreshSnippet(int secs, String url) {
         return("(Refreshes every "+secs+" seconds)"+
                "<META HTTP-EQUIV='Refresh' CONTENT='"+secs+";URL="+url+"'>");
   }

   /** Returns the stylesheet filename to be used for the center's html pages in the form
    * of a full URL including 'link' element.  Returns an empty string if none configured
   public static String getCssLink() {
      String cssName = SimpleConfig.getProperty("datacenter.stylesheet",  "default.css");
      if (cssName.length() == 0) {
         return "";
      }
      else {
         return "<LINK href='"+cssName+"' rel='stylesheet' type='text/css'>";
      }
   }

   /** Convenience routine that returns the complete <HEAD> element for the
    * standard datacenter page *
   public static String getHeadElement(String title) {
      return "<HEAD>\n"+
             "  <TITLE>"+title+" ("+DataServer.getDatacenterName()+")</TITLE>\n"+
             "  "+getCssLink()+"\n"+
             "</HEAD>\n";
   }

   
   /**
    * Returns an error as string suitable for display in a browser as an html
    * page
    */
   public static String exceptionAsHtmlPage(String title, Throwable th, String details) {
      return
         "<html>\n"+
         "<head><title>ERROR: "+makeSafeForHtml(title)+"</title></head>\n"+
         "<body>\n"+
         exceptionAsHtml(title, th, details)+
         "</body>\n"+
         "</html>\n";
   }

   /** Returns exception suitable for a paragraph in an hmtl page */
   public static String exceptionAsHtml(String title, Throwable th, String details) {

      String s =
         "<h1>ERROR REPORT</h1>\n"+
         "<h2>"+makeSafeForHtml(title)+"</h2>\n";
      if (th != null) {
         StringWriter sw = new StringWriter();
         th.printStackTrace(new PrintWriter(sw));
         String stack = sw.toString();

         s = s +
         "<p><b>"+makeSafeForHtml(th.toString())+"</b></p>\n"+
         "<p>\n"+
         "<pre>"+makeSafeForHtml(stack)+"</pre>\n"+
         "</p>\n"+
         "<p>\n"+
         "<pre>"+makeSafeForHtml(details)+"</pre>\n"+
         "</p>\n";
      }
      return s;
   }

   /**
    * Deals with special characters */
   public static String makeSafeForHtml(String s) {
      if (s==null) {
         return "";
      }
      s = s.replaceAll(">", "&gt;").replaceAll("<", "&lt;");
      return s.replaceAll("\n","<br/>");
   }
   
   /** Convenience routine for exceptionAsHtml(String, Exception, String)   */
   public static String exceptionAsHtmlPage(String title, Throwable th) {
      return exceptionAsHtmlPage(title, th, "");
   }
   
   /** Creates suitable hidden input fields so that all parameters passed in
    * are preserved when the form is submitted
    */
   public static void preserveParams(PrintWriter out, ServletRequest request) {
      Enumeration e = request.getParameterNames();
      while (e.hasMoreElements()) {
         String key = (String)e.nextElement();
         String[] values = request.getParameterValues(key);
         for(int i = 0; i < values.length; i++) {
            out.print("<input type='hidden' name='"+key+"' value='"+values[i]+"' />");
         }
         out.println();
      }
   }

   /** Dumps all the parameters in the given request to the given writer.
    * Useful for debug at the bottom of the screen
    */
   public static void dumpAllParams(PrintWriter out, ServletRequest request) {
      Enumeration e = request.getParameterNames();
      while (e.hasMoreElements()) {
         String key = (String)e.nextElement();
         String[] values = request.getParameterValues(key);
         out.print(" " + key + " = ");
         for(int i = 0; i < values.length; i++) {
            out.print(values[i] + " ");
         }
         out.println();
      }
   }
}











