/*
 * $Id: ServletHelper.java,v 1.14 2004/11/12 10:44:54 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.query.condition.CircleCondition;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.slinger.targets.TargetMaker;

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
         request.getScheme()+"://"+request.getServerName() +":" + request.getServerPort()+request.getContextPath()
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
      Account user = Account.ANONYMOUS;
      //for information only
      if (request.getParameter("UserName") != null) {
         user = new Account(request.getParameter("UserName"), "Unknown", null);
      }
      return user;
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

      String targetResponse = request.getParameter("TargetResponse");
      if ((targetResponse != null) && targetResponse.trim().toLowerCase().equals("true")) {
         //target is the http response
         tableSpec.setTarget(null);
      }
      else {
         String targetUri = request.getParameter("TargetURI");   //direction - eg URI
         if ((targetUri != null) && (targetUri.trim().length()>0)) {
               
            try {
               tableSpec.setTarget(TargetMaker.makeIndicator(targetUri));
            }
            catch (URISyntaxException e) {
               throw new IllegalArgumentException("Invalid target: "+targetUri+" ("+e+")");
            }
            catch (MalformedURLException e) {
               throw new IllegalArgumentException("Invalid target: "+targetUri+" ("+e+")");
            }
         }
         else {
            //throw new IllegalArgumentException("Bad Target; TargetResponse not 'true' and TargetURI not set");
            //assume targetresponse true (for things like cone searches)
            tableSpec.setTarget(null);
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
   
   /** Creates a Circle function condition from parameters in the given request.
    * Accepts POS=(ra,dec) and RA=ra&DEC=dec, and SIZE and SR for search radius.
    * Accepts all-lower case as well as all-upper case
    */
   public static CircleCondition makeCircleCondition(HttpServletRequest request) {
      
      String radiusparam = request.getParameter("SIZE");
      if (radiusparam == null) { radiusparam = request.getParameter("size"); }
      if (radiusparam == null) { radiusparam = request.getParameter("SR"); }
      if (radiusparam == null) { radiusparam = request.getParameter("sr"); }
      if (radiusparam == null) { radiusparam = request.getParameter("RADIUS"); }
      if (radiusparam == null) { radiusparam = request.getParameter("radius"); }
      if (radiusparam == null) {
         throw new IllegalArgumentException("No Radius given as SIZE or SR or RADIUS");
      }
      double radius = Double.parseDouble(radiusparam);
      
      double ra;
      double dec;

      String pos = request.getParameter("POS");
      if (pos == null)     {  request.getParameter("pos"); }
      if (pos != null) {
         int comma = pos.indexOf(",");
         ra = Double.parseDouble(pos.substring(0,comma));
         dec = Double.parseDouble(pos.substring(comma+1));
         return new CircleCondition(ra, dec, radius);
      }

      String raparam = request.getParameter("RA");
      if (raparam == null) {  raparam = request.getParameter("ra"); }
      if (raparam == null) {  raparam = request.getParameter("Ra"); }
      if (raparam == null) {
         throw new IllegalArgumentException("No RA given");
      }
      
      String decparam = request.getParameter("DEC");
      if (decparam == null) { decparam = request.getParameter("dec"); }
      if (decparam == null) { decparam = request.getParameter("Dec"); }
      if (raparam == null) {
         throw new IllegalArgumentException("No DEC given");
      }
      
      ra = Double.parseDouble(raparam);
      dec = Double.parseDouble(decparam);
      
      return new CircleCondition(ra, dec, radius);
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












