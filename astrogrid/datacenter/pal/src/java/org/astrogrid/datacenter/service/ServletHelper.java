/*
 * $Id: ServletHelper.java,v 1.6 2004/10/25 00:49:17 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.slinger.TargetIndicator;

/**
 * A set of dataserver methods for helping serving data in HTML form, eg for servlets
 * or JSPs
 * <p>
 * @author M Hill
 */

public class ServletHelper
{
   
   protected static Log log = LogFactory.getLog(ServletHelper.class);

   private static String urlStem = null;
   
   /** Provides static access to the url stem (eg http://grendel12.roe.ac.uk/pal-6df) which
    * will have to have been set from a servlet call to setUrlStem beforehand. May be a
    * static way of doing this? Returns stem without final slash */
   public static String getUrlStem() {
      if (urlStem != null) {
         return urlStem;
      }
      //might be set in config for authority stuff
      String configStem = SimpleConfig.getSingleton().getString("datacenter.url", null);
      if (configStem != null) {
         if (!configStem.endsWith("/")) {
            configStem = configStem+"/";
         }
      }
      return configStem;
   }

   public static void setUrlStem(HttpServletRequest request) {
      setUrlStem(
         request.getServerName() +":" + request.getServerPort()+"/"+request.getContextPath()
      );
   }

   public static void setUrlStem(String aStem) {
      if (!aStem.endsWith("/")) {
         aStem = aStem+"/";
      }
      log.info("Service Stem set to '"+aStem+"'");
      if (urlStem == null) {
         urlStem = aStem;
      }
      else if (!urlStem.equals(aStem)) {
         log.error("Trying to set service URL stem to "+aStem+" when already "+urlStem+"; ignoring new stem");
      }
   }

   /**
    * Gets the user details from the request */
   public static Account getUser(HttpServletRequest request)  {
      return Account.ANONYMOUS;
   }

   /**
    * Convenience routine for JSPs; decides where target should be from
    * the parameters in the given request.  The parameter names should match
    * those assigned in resultsForm.xml */
   public static ReturnSpec makeReturnSpec(HttpServletRequest request)  {

      ReturnTable returns = new ReturnTable(null);

      fillReturnSpec(returns, request);
      
      return returns;
   }

   /**
    * Convenience routine for JSPs; decides where target should be from
    * the parameters in the given request.  The parameter names should match
    * those assigned in resultsForm.xml */
   public static void fillReturnSpec(ReturnTable tableSpec, HttpServletRequest request)  {

      TargetIndicator target = null;

      if (request.getParameter("TargetBrowser") != null) {
         target = null;
      }
      else if (request.getParameter("TargetURI") != null) {
         String targetUri = request.getParameter("Target");   //direction - eg URI
         if ( (targetUri == null) || (targetUri.trim().length()==0)) {
            throw new IllegalArgumentException("No Target URI given");
         }
            
         try {
            target = TargetIndicator.makeIndicator(targetUri);
         }
         catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid target: "+target+" ("+e+")");
         }
         catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid target: "+target+" ("+e+")");
         }
      }
      
      String format = request.getParameter("Format");
      if ( (format != null) && (format.trim().length()>0)) {
         tableSpec.setFormat(format);
      }
      
      String compression = request.getParameter("Compression");
      if ( (compression != null) && (compression.trim().length()>0)) {
         tableSpec.setCompression(compression);
      }

      String limit = request.getParameter("Limit");
      if ( (limit != null) && (limit.trim().length()>0)) {
         tableSpec.setLimit(Integer.parseInt(limit));
      }

   }
   

   
   
   /** Convenience routine for returning the correct 'HTML' snippet that
    * refreshes the page given by the URL - which should point to the same page
    * that contains the snippet */
   public static String makeRefreshSnippet(int secs, String url) {
         return("(Refreshes every "+secs+" seconds)"+
                "<META HTTP-EQUIV='Refresh' CONTENT='"+secs+";URL="+url+"'>");
   }

   /** Returns the stylesheet to be used for the center's html pages in the form
    * of a 'link' element.  Returns an empty string if none configured */
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
    * standard datacenter page */
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
}











