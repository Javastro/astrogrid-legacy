/*
 * $Id: HtmlDataServer.java,v 1.6 2004/03/15 17:12:28 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.queriers.TargetIndicator;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.store.Agsl;

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
         out.write(exceptionAsHtml("askQuery("+user+", "+query+", Writer)", th));
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
         return exceptionAsHtml("submitQuery("+user+", "+query+", "+target+")", th);
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
   public static TargetIndicator makeTarget(String resultsTarget) throws MalformedURLException {
      TargetIndicator target = null;
      if ( (resultsTarget == null) || (resultsTarget.trim().length()==0)) {
         target = null; //so caller knows that it has to use some other target
      }
      else {
         if (resultsTarget.startsWith("mailto:")) {
            target = new TargetIndicator(resultsTarget.substring(7));
         }
         else {
            target = new TargetIndicator(new Agsl(resultsTarget));
         }
      }
      return target;
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
         return exceptionAsHtml("abortQuery("+user+", "+queryId+")", th);
      }
   }

   /**
    * Returns a QuerierStatus in html form.
    */
   public static String queryStatusAsHtml(String queryId, QuerierStatus status) {

      StringBuffer html = new StringBuffer(
         "<html>\n"+
         "<head><title>Status of Query "+queryId+"</title></head>\n"+
         "<body>\n"+
         "<h1>Status of Query "+queryId+"</h1>\n");

      if (status == null) {
         return html.toString()+
                  "<b>No Query found for that ID</b>\n";
      }
      
      html.append("<b>"+status.getState().toString()+"</b>\n");
      
      String[] details= status.getDetails();
      
      for (int i=0;i<details.length;i++) {
         details[i].replaceAll("\n", "<br/>");
         html.append("<p>"+details[i]+"</p>\n");
      }
      
      html.append(
         "</body>\n"+
         "</html>\n");
      
      return html.toString();

   }

   /**
    * Returns a ServerStatus in html form.
    */
   public String serverStatusAsHtml() {

      StringBuffer html = new StringBuffer(
         "<html>\n"+
         "<head><title>Datacenter Service Status </title></head>\n"+
         "<body>\n"+
         "<h1>Datacenter Service Status at "+new Date()+"</h1>\n"+
         "<p>Started: "+server.startTime+"</p>"+
         "<p>Num Queries: "+server.getNumQueries()+"</p>"+
         "<p>Free memory:"+Runtime.getRuntime().freeMemory()+"</p>"+
         "<p>Max memory:"+Runtime.getRuntime().maxMemory()+"</p>"+
         "<p>Total memory:"+Runtime.getRuntime().totalMemory()+"</p>"+
         "</body>\n"+
         "</html>\n");
      
      server.querierManager.getQueriers().size();
      
      return html.toString();
   }
   
   /**
    * Returns an error in html form.
    */
   public static String exceptionAsHtml(String title, Throwable th, String details) {

      StringWriter sw = new StringWriter();
      th.printStackTrace(new PrintWriter(sw));
      String stack = sw.toString();

      return
         "<html>\n"+
         "<head><title>ERROR: "+title+"</title></head>\n"+
         "<body>\n"+
         "<h1>ERROR REPORT</h1>\n"+
         "<b>"+title+"</b>\n"+
         "<p><b>"+makeSafeForHtml(th.toString())+"</b></p>\n"+
         "<p>\n"+
         "<pre>"+makeSafeForHtml(stack)+"</pre>\n"+
         "</p>\n"+
         "<p>\n"+
         "<pre>"+makeSafeForHtml(details)+"</pre>\n"+
         "</p>\n"+
         "</body>\n"+
         "</html>\n";
   }

   /**
    * Deals with special characters */
   public static String makeSafeForHtml(String s) {
      return s.replaceAll(">", "&gt;").replaceAll("<", "&lt;");
   }
   
   /** Convenience routine for exceptionAsHtml(String, Exception, String)   */
   public static String exceptionAsHtml(String title, Throwable th) {
      return exceptionAsHtml(title, th, "");
   }
}







