/*
 * $Id: ServletHelper.java,v 1.2 2010/04/11 21:19:20 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.returns.ReturnImage;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.slinger.sourcetargets.URISourceTargetMaker;

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
   
   public static void setUrlStem(String aStem) {
      if (!aStem.endsWith("/")) {
         aStem = aStem+"/";
      }
      //check it's a valid URL and should be able to open a stream to it.
      try {
         new URL(aStem).openStream();
      }
      catch (IOException e) {
         log.error("URL stem "+aStem+" is invalid or unreachable ("+e+")");
         return; //don't set it
      }
      
      log.info("Service Stem set to '"+aStem+"'");
      if (urlStem == null) {
         urlStem = aStem;
      }
      else if (!urlStem.equals(aStem)) {
         log.error("Trying to set service URL stem to "+aStem+" when already "+urlStem+"; ignoring new stem");
      }
   }

   /** Provides static access to the url stem (eg http://grendel12.roe.ac.uk/pal-6df)  */
   public static String getUrlStem() {
      if (urlStem != null) {
         return urlStem;
      }
      //see if we can get it from axis stuff
      if (AxisDataServer.getMessageContextUrlStem() != null) {
         setUrlStem(AxisDataServer.getMessageContextUrlStem());
         return urlStem;
      }
      //see if we can get it from servlet stuff
      if (getRequestUrlStem() != null) {
         setUrlStem(getRequestUrlStem());
         return urlStem;
      }
      
      //if not, get it we can get it from config
      String configStem = ConfigFactory.getCommonConfig().getString("datacenter.url");
      if (configStem != null) {
         if (!configStem.endsWith("/")) {
            configStem = configStem+"/";
         }
      }
      return configStem;
   }

   public static String getRequestUrlStem() {
      return null;
   }
   
   public static String getUrlStem(HttpServletRequest request) {
      return request.getScheme()+"://"+request.getServerName() +":" + request.getServerPort()+request.getContextPath();
   }

   /**
    * Gets the user details from the request */
   public static Principal getUser(HttpServletRequest request)  {
      if (request.getParameter("UserName") != null) {
         //for information only
         return new LoginAccount(request.getParameter("UserName"), "Unknown");
      }
      else {
         return LoginAccount.ANONYMOUS;
      }
   }

   /**
    * Convenience routine for JSPs; decides where target should be from
    * the parameters in the given request.  The parameter names should match
    * those assigned in resultsForm.xml 
    * @TOFIX: LIMIT is currently being ignored, is this problematic? 
    * Can have a getLimit() method and set limit in actual query instead? */
   public static ReturnSpec makeReturnSpec(HttpServletRequest request)  {
      
      ReturnSpec returnSpec = null;
      
      String format = request.getParameter("Format");
      if ( (format != null) && (format.trim().length()>0)) {
         if (ReturnImage.isImageFormat(new String[] { format })) {
            returnSpec = new ReturnImage(null);
         }
      }
      if (returnSpec == null) {
         returnSpec = new ReturnTable(null);
      }

      // This is a dummy placeholder
      fillReturnSpec(returnSpec, request);
      
      return returnSpec;
   }


   /**
    * Convenience routine for JSPs; returns true if this is an 'ask count' request
    *  from the resultsForm.xml */
   public static boolean isCountReq(HttpServletRequest request)  {
      String format = request.getParameter("Format");
      if ( (format != null) && (format.trim().length()>0) && (format.trim().toLowerCase().equals("count"))) {
         return true;
      }
      else {
         return false;
      }
   }

      /**
    * Convenience routine for JSPs; decides where target should be from
    * the parameters in the given request.  The parameter names should match
    * those assigned in resultsForm.xml */
   public static void fillReturnSpec(ReturnSpec returnSpec, HttpServletRequest request)  {

      String targetResponse = request.getParameter("TargetResponse");
      if ((targetResponse != null) && targetResponse.trim().toLowerCase().equals("true")) {
         //target is the http response
         returnSpec.setTarget(null);
      }
      else {
         String targetUri = request.getParameter("TargetURI");   //direction - eg URI
         if ((targetUri != null) && (targetUri.trim().length()>0)) {

            try {
               returnSpec.setTarget(URISourceTargetMaker.makeSourceTarget(targetUri));
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
            returnSpec.setTarget(null);
         }
      }
      
      String format = request.getParameter("Format");
      if ( (format != null) && (format.trim().length()>0)) {
         returnSpec.setFormat(format);
      }
      
      String compression = request.getParameter("Compression");
      if ( (compression != null) && (compression.trim().length()>0)) {
         try {
            returnSpec.setCompression(compression);
         }
         catch (QueryException e) {
            throw new IllegalArgumentException("Invalid compression: "+
                compression+" ("+e+")");
         }
      }
      /*
      // Limit is no longer part of ReturnSpec, it's managed 
      // directly by the Query class.
      String limit = request.getParameter("Limit");
      if ( (limit != null) && (limit.trim().length()>0)) {
        throw new IllegalArgumentException("Limit is ignored!");
      }
      */
   }
   
   /** Convenience routine for extracting the catalog name of a conesearch */
   public static String getCatalogName(HttpServletRequest request)
   {
      String catName = "";
      // First check for joint param DSACATTAB
      String catTabName = request.getParameter("DSACATTAB");
      if ((catTabName == null) || ("".equals(catTabName))) {
         // If not present, look for individual params
         catName = request.getParameter("DSACAT");
         if ((catName == null) || ("".equals(catName))) {
            throw new IllegalArgumentException(
                  "DSACAT parameter (catalogue name) is missing or empty");
         }
      }
      else {
         int uIndex = catTabName.indexOf('.');
         if (uIndex != -1) {   //Dot found
            catName = catTabName.substring(0,uIndex);
         }
         else {
            throw new IllegalArgumentException(
               "DSACATTAB parameter does not contain expected '.' character");
         }
         if ((catName == null) || ("".equals(catName))) {
            throw new IllegalArgumentException(
                  "DSACATTAB parameter defines empty catalogue name");
         }
      }
      return catName;
   }

   /** Convenience routine for extracting the table name of a conesearch */
   public static String getTableName(HttpServletRequest request)
   {
      String tabName = "";
      // First check for joint param DSACATTAB
      String catTabName = request.getParameter("DSACATTAB");
      if ((catTabName == null) || ("".equals(catTabName))) {
         // If not present, look for individual params
         tabName = request.getParameter("DSATAB");
         if ((tabName == null) || ("".equals(tabName))) {
            throw new IllegalArgumentException(
                  "DSATAB parameter (table name) is missing or empty");
         }
      }
      else {
         int uIndex = catTabName.lastIndexOf('.');
         if (uIndex != -1) {   //Dot found
            tabName = catTabName.substring(uIndex+1);
         }
         else {
            throw new IllegalArgumentException(
               "DSACATTAB parameter does not contain expected '.' character");
         }
         if ((tabName == null) || ("".equals(tabName))) {
            throw new IllegalArgumentException(
                  "DSACATTAB parameter defines empty table name");
         }
      }
      return tabName;
   }

   /** Convenience routine for extracting the radius of a conesearch */
   public static double getRadius(HttpServletRequest request)
   {
      String radiusparam = request.getParameter("SIZE");
      if (radiusparam == null) { radiusparam = request.getParameter("size"); }
      if (radiusparam == null) { radiusparam = request.getParameter("SR"); }
      if (radiusparam == null) { radiusparam = request.getParameter("sr"); }
      if (radiusparam == null) { radiusparam = request.getParameter("RADIUS"); }
      if (radiusparam == null) { radiusparam = request.getParameter("radius"); }
      if (radiusparam == null) {
         throw new IllegalArgumentException("No Radius given as SIZE or SR or RADIUS");
      }
      try {
         return Double.parseDouble(radiusparam);
      }   
      catch (NumberFormatException e) {
         throw new IllegalArgumentException(
             "Bad search radius '" + radiusparam + 
             "' given, expected real number");
      }
   }

   /** Convenience routine for extracting the RA of a conesearch */
   public static double getRa(HttpServletRequest request)
   {
      String pos = request.getParameter("POS");
      if (pos == null) {  
        request.getParameter("pos"); 
      }
      if (pos != null) {
         int comma = pos.indexOf(",");
         try {
            return Double.parseDouble(pos.substring(0,comma));
         }
         catch (Exception e) {
            throw new IllegalArgumentException(
              "Couldn't parse RA value from POS field");
         }
      }
      String raparam = request.getParameter("RA");
      if (raparam == null) {  
         raparam = request.getParameter("ra"); 
      }
      if (raparam == null) {
         raparam = request.getParameter("Ra"); 
      }
      if (raparam == null) {
         throw new IllegalArgumentException("No RA given");
      }
      try {
         return Double.parseDouble(raparam);
      } 
      catch (NumberFormatException e) {
         throw new IllegalArgumentException(
             "Bad RA '" + raparam + "' given, expected real number");
      }

   }
   /** Convenience routine for extracting the RA of a conesearch */
   public static double getDec(HttpServletRequest request)
   {
      String pos = request.getParameter("POS");
      if (pos == null)     {  request.getParameter("pos"); }
      if (pos != null) {
         int comma = pos.indexOf(",");
         try {
            return Double.parseDouble(pos.substring(comma+1));
         }
         catch (Exception e) {
            throw new IllegalArgumentException(
               "Couldn't parse Dec value from POS field");
         }
      }
      String decparam = request.getParameter("DEC");
      if (decparam == null) { decparam = request.getParameter("dec"); }
      if (decparam == null) { decparam = request.getParameter("Dec"); }
      if (decparam == null) {
         throw new IllegalArgumentException("No DEC given");
      }
      try {
         return Double.parseDouble(decparam);
      }
      catch (NumberFormatException e) {
         throw new IllegalArgumentException(
             "Bad DEC '" + decparam + "' given, expected real number");
      }
   }

   /** Convenience routine for returning the correct 'HTML' snippet that
    * refreshes the page given by the URL - which should point to the same page
    * that contains the snippet */
   public static String makeRefreshSnippet(int secs, String url) {
         return("<META HTTP-EQUIV='Refresh' CONTENT='"+secs+";URL="+url+"'>");
   }

   /** Returns the stylesheet to be used for the center's html pages in the form
    * of a 'link' element.  Returns an empty string if none configured */
   public static String getCssLink() {
      String cssName = ConfigFactory.getCommonConfig().getString("datacenter.stylesheet",  "default.css");
      if (cssName.length() == 0) {
         return "";
      }
      else {
         return "<LINK href='"+cssName+"' rel='stylesheet' type='text/css'>";
      }
   }
   
   /**
    * Returns an error as string suitable for display in a browser as an html
    * page
    */
   public static String exceptionAsHtmlPage(String title, Throwable th, String details) {
     // Keep error title in sync with coneSearch test in InstallationSelfCheck
      return
         "<html>\n"+
         "<head><title>ASTROGRID DSA ERROR: "+makeSafeForHtml(title)+"</title></head>\n"+
         "<body>\n"+
         exceptionAsHtml(title, th, details)+
         "</body>\n"+
         "</html>\n";
   }
   /**
    * Returns an error as string suitable for display in a browser as an html
    * page
    */
   public static String errorAsHtmlPage(String title, String details) {
      return
         "<html>\n"+
         "<head><title>ASTROGRID DSA ERROR: "+makeSafeForHtml(title)+"</title></head>\n"+
         "<body>\n"+
			"<p>"+makeSafeForHtml(details)+"</p>" +
         "</body>\n"+
         "</html>\n";
   }
   /**
    * Returns an message as string suitable for display in a browser as an html
    * page
    */
   public static String messageAsHtmlPage(String title, String details) {
      return
         "<html>\n"+
         "<head><title>ASTROGRID DSA MESSAGE: "+makeSafeForHtml(title)+"</title></head>\n"+
         "<body>\n"+
			"<p>"+makeSafeForHtml(details)+"</p>" +
         "</body>\n"+
         "</html>\n";
   }

   /** Returns exception suitable for a paragraph in an hmtl page */
   public static String exceptionAsHtml(String title, Throwable th, String details) {

     // Keep error title in sync with coneSearch test in InstallationSelfCheck
      String s =
         "<h1>ASTROGRID DSA ERROR REPORT</h1>\n";
      
      if (th != null) {
         s=s+
            "<h2>"+makeSafeForHtml(th.getMessage())+"</h2>\n";
      }
      s =s + "<p>"+title+"</p>\n";

      if (th != null) {
         StringWriter sw = new StringWriter();
         th.printStackTrace(new PrintWriter(sw));
         String stack = sw.toString();

         s = s +
         "<p><b>"+makeSafeForHtml(th.toString())+"</b></p>\n"+
         "<p>\n"+
         "<pre>"+makeSafeForHtml(stack)+"</pre>\n"+
         "</p>\n"+
         "<p>\n";
      }
      
      s=s+
         "<pre>"+makeSafeForHtml(details)+"</pre>\n"+
         "</p>\n";

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
