/*
 * $Id: HtmlDataServer.java,v 1.20 2004/08/25 23:38:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.returns.TargetIndicator;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.io.Piper;

/**
 * A set of dataserver methods for serving data in HTML form, eg for servlets
 * or JSPs
 * <p>
 * @author M Hill
 */

public class HtmlDataServer
{
   DataServer server = new DataServer();
   
   protected static Log log = LogFactory.getLog(HtmlDataServer.class);

   /**
    * Runs a (blocking) ADQL/XML/OM query, outputting the results as votable to the given stream
    */
   public void askQuery(Account user, Query query, Writer out, String requestedFormat) throws IOException {
      
      try {
         server.askQuery(user, query, new TargetIndicator(out), requestedFormat);
      }
      catch (Throwable th) {
         log.error(th);
         out.write(exceptionAsHtmlPage("askQuery("+user+", "+query+", Writer)", th));
      }
   }
 
   /**
    * Submits a (non-blocking) ADQL/XML/OM query, returning the query's external
    * reference id.  Results will be output to given Agsl
    */
   public String submitQuery(Account user, Query query, TargetIndicator target, String requestedFormat) throws IOException {
      
      try {
         return server.submitQuery(user, query, target, requestedFormat);
      }
      catch (Throwable th) {
         log.error(th);
         return exceptionAsHtmlPage("submitQuery("+user+", "+query+", "+target+")", th);
      }
   }


   public QuerierStatus getQuerierStatus(Account user, String queryId) {
      try {
         return server.getQueryStatus(user, queryId);
      }
      catch (Throwable th) {
         log.error(th);
         return null;
      }
   }

   /**
    * Convenience routine for JSPs; decides where target should be from
    * resultsTarget string */
   public static TargetIndicator makeTarget(String resultsTarget) throws MalformedURLException, URISyntaxException {
      if ( (resultsTarget == null) || (resultsTarget.trim().length()==0)) {
         return null; //so caller knows that it has to use some other target
      }

      return TargetIndicator.makeIndicator(resultsTarget);
   }

   
   
   /**
    * Returns status of a as html. NB the id given is the *datacenter's* id
    * user getQuerierStatus
   public String getQueryStatusHtml(Account user, String queryId)
   {
      try {
         return queryStatusAsHtml(queryId, server.getQueryStatus(user, queryId));
      }
      catch (Throwable th) {
         log.error(th);
         return exceptionAsHtml("getQueryStatusAsHtml("+user+", "+queryId+")", th);
      }
   }

   /**
    * Request to stop a query.  This might not be successful - depends on the
    * back end.  NB the id given is the *datacenters* id.
    */
   public String abortQuery(Account user, String queryId) {
      try {
         return queryStatusAsHtml(queryId, server.abortQuery(user, queryId));
      }
      catch (Throwable th) {
         log.error(th);
         return exceptionAsHtmlPage("abortQuery("+user+", "+queryId+")", th);
      }
   }

   /**
    * Returns a QuerierStatus in html form.
    */
   public static String queryStatusAsHtml(String queryId, QuerierStatus status) {

      StringBuffer html = new StringBuffer(
         "<h1>Status of Query "+makeSafeForHtml(queryId)+"</h1>\n");

      if (status == null) {
         return html.toString()+
                  "<b>No Query found for that ID</b>\n";
      }
      
      html.append("<h2>"+makeSafeForHtml(status.getState().toString())+"</h2>\n");

      html.append("<p><b>"+makeSafeForHtml(status.getNote().replaceAll("\n","<br/>"))+"</b></p>\n");
      
      String[] details= status.getDetails();
      
      for (int i=0;i<details.length;i++) {
         html.append("<p>"+makeSafeForHtml(details[i])+"</p>\n");
      }
      
      return html.toString();

   }

   /**
    * Returns a ServerStatus in html form.
    */
   public String serverStatusAsHtml() {

      String[] running = server.querierManager.getRunning();
      
      StringBuffer html = new StringBuffer(
         "<h1>Datacenter Service Status at "+new Date()+"</h1>\n"+
         "<p>Started: "+server.startTime+"</p>"+
         "<h3>Memory</h3>"+
         "<p>Free:"+Runtime.getRuntime().freeMemory()+"<br/>"+
         "Max:"+Runtime.getRuntime().maxMemory()+"<br/>"+
         "Total:"+Runtime.getRuntime().totalMemory()+"<br/></p>\n"+
         "<h3>Load</h3>"+
         "<p>Running Queries: "+running.length+"</p>");

      for (int i=0;i<running.length;i++) {
         html.append("<a href='queryStatus.jsp?ID="+running[i]+"'>"+running[i]+"</a><br/>\n");
      }
   
      String[] done = server.querierManager.getRan();

      html.append(
         "</p>"+
         "<h3>History</h3>"+
         "<p>Closed Queries: "+done.length+"</p>");

      for (int i=0;i<done.length;i++) {
         html.append("<a href='queryStatus.jsp?ID="+done[i]+"'>"+done[i]+"</a><br/>\n");
      }

      html.append("</p>");
      
      return html.toString();
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
         "<head><title>ERROR: "+title+"</title></head>\n"+
         "<body>\n"+
         exceptionAsHtml(title, th, details)+
         "</body>\n"+
         "</html>\n";
   }

   /** Returns exception suitable for a paragraph in an hmtl page */
   public static String exceptionAsHtml(String title, Throwable th, String details) {

      StringWriter sw = new StringWriter();
      th.printStackTrace(new PrintWriter(sw));
      String stack = sw.toString();

      return
         "<h1>ERROR REPORT</h1>\n"+
         "<h2>"+title+"</h2>\n"+
         "<p><b>"+makeSafeForHtml(th.toString())+"</b></p>\n"+
         "<p>\n"+
         "<pre>"+makeSafeForHtml(stack)+"</pre>\n"+
         "</p>\n"+
         "<p>\n"+
         "<pre>"+makeSafeForHtml(details)+"</pre>\n"+
         "</p>\n";
   }

   /**
    * Deals with special characters */
   public static String makeSafeForHtml(String s) {
      s = s.replaceAll(">", "&gt;").replaceAll("<", "&lt;");
      return s.replaceAll("\n","<br/>");
   }
   
   /** Convenience routine for exceptionAsHtml(String, Exception, String)   */
   public static String exceptionAsHtmlPage(String title, Throwable th) {
      return exceptionAsHtmlPage(title, th, "");
   }
}









