package org.astrogrid.dataservice.service.cone;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.security.authorization.AccessPolicy;
import org.astrogrid.security.HttpsServiceSecurityGuard;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.webapp.DefaultServlet;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.io.mime.MimeTypes;


/**
 * A servlet for processing Cone Queries.
 * Takes three parameters (RA, DEC and SR) that define the query, and
 * the standard returns definition parameters (see ???)
 *
 * @author M Hill
 * @author K Andrews
 * @author G Rixon
 */
public class SubmitCone extends DefaultServlet {
   
   DataServer server = new DataServer();

   /**
    * Handles an HTTP GET request.
    * Returns a representation of a table query results. If the query fails,
    * returns a VOTable as an error document.
    *
    * @param request The HTTP request.
    * @param response The HTTP response.
    * @throws IOException If cone search is not enabled.
    * @throws IOException If the test database is needed and not available.
    * @throws IOException If an error document cannot be sent.
    */
   @Override
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws IOException {

      // Initialise SampleStars plugin if required (may not be initialised
      // if admin has not run the self-tests)
      String plugin = ConfigFactory.getCommonConfig().getString(
           "datacenter.querier.plugin");
      if (plugin.equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) {
         // This has no effect if the plugin is already initialised
         SampleStarsPlugin.initialise();  // Just in case
      }

      // Check that conesearch interface is enabled
      String coneEnabled = ConfigFactory.getCommonConfig().getString(
         "datacenter.implements.conesearch");
      if ( (coneEnabled == null) || 
          (coneEnabled.equals("false") || coneEnabled.equals("FALSE")) ) { 
        // Conesearch is not enabled, so throw an exception
         IOException ioe =  new IOException(
             "Conesearch interface is disabled in config");
         doTypedError(request, response, 
           "Conesearch interface is disabled in DSA/catalog configuration", ioe);
         throw ioe;
      }
      
      // Check authorization.
      try {
        HttpsServiceSecurityGuard sg = new HttpsServiceSecurityGuard();
        sg.loadHttpsAuthentication(request);
        String policyClassName =
            SimpleConfig.getSingleton().getString("cone.search.access.policy");
        Class policyClass = Class.forName(policyClassName);
        sg.setAccessPolicy((AccessPolicy)policyClass.newInstance());
        sg.decide(null);
      }
      catch (Exception e) {
        doTypedError(request, response, "Access denied", e);
        return;
      }

      // Extract the query parameters
      double ra=0, dec=0, radius=0;
      String catalogName="", tableName="";
      try {
         catalogName = ServletHelper.getCatalogName(request);
         tableName = ServletHelper.getTableName(request);
         radius = ServletHelper.getRadius(request);
         ra = ServletHelper.getRa(request);
         dec = ServletHelper.getDec(request);
         ReturnSpec returnSpec = ServletHelper.makeReturnSpec(request);

         String raColName = TableMetaDocInterpreter.getConeRAColumnByName(
             catalogName, tableName);
         String decColName = TableMetaDocInterpreter.getConeDecColumnByName(
             catalogName, tableName);
         String units = TableMetaDocInterpreter.getConeUnitsByName(
             catalogName, tableName);

         // Create the query
         Query coneQuery = new Query(
               catalogName, tableName, units, raColName, decColName, 
               ra, dec, radius, returnSpec);

         if (returnSpec.getTarget() == null) {
            //if a target is not given, we do an (ask) Query 
            // to the response stream.
            returnSpec.setTarget(new WriterTarget(response.getWriter(), false));
            
            if (ServletHelper.isCountReq(request)) {
               // This one is a blocking request
               long count = server.askCount(ServletHelper.getUser(request), coneQuery, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via SubmitCone servlet");
               response.setContentType(MimeTypes.PLAINTEXT);
               response.getWriter().write(""+count);
            }
            else {
               // This one is a blocking request
               // TOFIX KONA CHECK VALID CONTENT TYPE FOR NORMAL CONE
               // (NOT JUST BROWSER)
               response.setContentType(coneQuery.getResultsDef().getFormat());
               server.askQuery(ServletHelper.getUser(request), coneQuery, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via SubmitCone servlet");
            }
         }
         else {
            //otherwise we direct the response to the target and put status 
            //info to the browser
            response.setContentType(MimeTypes.HTML);

            response.getWriter().println(
               "<html>"+
               "<head><title>Submitting Query</title></head>"+
               "<body>"+
               "<p>Submitting, please wait...</p>");
            response.getWriter().flush();

            // This one is a non-blocking request
            String id = server.submitQuery(ServletHelper.getUser(request), coneQuery, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via SubmitCone servlet");
      
            URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/admin/queryStatus.jsp");
            //indicate status
            response.getWriter().println("Cone Query has been submitted, and assigned ID "+id+"."+
                                          "<a href='"+statusUrl+"?ID="+id+"'>Query Status Page</a>\n");
            response.getWriter().flush();

            //redirect to status
            response.getWriter().write("<META HTTP-EQUIV='Refresh' CONTENT='0;URL="+statusUrl+"?ID="+id+"'>"+
                                      "</body></html>");
            response.getWriter().flush();
         }
      }
      catch (java.lang.IllegalArgumentException ex) {
        //Typically thrown if a bad table name is given
        doPotentialConfigError(request, response, ra, dec, radius, ex);
      } 
      catch (java.lang.NullPointerException ex) {
        //Typically thrown if a bad column name is given
        doPotentialConfigError(request, response, ra, dec, radius, ex);
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th+
             "conesearching table '" +tableName +"' in catalog '" +
             catalogName +"' with RA= " + Double.toString(ra) + 
             ", Dec = " + Double.toString(dec) + 
             ", radius = " + Double.toString(radius),
             th);

         doTypedError(request, response, 
             "conesearching table '" +tableName +"' in catalog '" +
             catalogName +"' with RA = " + Double.toString(ra) + 
             ", Dec = " + Double.toString(dec) + 
             ", radius = " + Double.toString(radius),
             th);
      }
   }

   private void doPotentialConfigError(
       HttpServletRequest request, HttpServletResponse response, 
       double ra, double dec, double radius,
       Exception ex) throws IOException {

      String errorResponseString = 
         "Searching Cone with RA = " + Double.toString(ra) + 
         ", Dec = " + Double.toString(dec) + ", radius = " + Double.toString(radius) +"</p>" +
         "<p>An error has occurred (see below); this may be because this datacenter installation is misconfigured.\n" +
         "<p>Admin note: please check the datacenter metadoc file to see that your conesearchable tables are correctly configured.\n";
          
      LogFactory.getLog(request.getContextPath()).error( ex+errorResponseString,ex);

      doTypedError(request, response, errorResponseString, ex);
   }

   protected void doTypedError(HttpServletRequest request, HttpServletResponse response, String title, Throwable th) throws IOException {
      String format = request.getParameter("Format");
      if ( (format == null) || (format.trim().length() == 0) ||
         (format.toLowerCase().equals("votable")) ) {
         try {
           response.setContentType(MimeTypes.VOTABLE);
         }
         catch (RuntimeException re) {
         }
         String error = th.getMessage();
         error = error.replaceAll("&", "&amp;"); 
         error = error.replaceAll("<", "&lt;");
         error = error.replaceAll(">", "&gt;");

         PrintWriter writer = response.getWriter();
         writer.println("<?xml version='1.0' encoding='UTF-8'?>");
         writer.println("<!DOCTYPE VOTABLE SYSTEM \"http://us-vo.org/xml/VOTable.dtd\">");
         writer.println("<VOTABLE version=\"1.0\">");
         writer.println("<DESCRIPTION>Error processing query</DESCRIPTION>");
         writer.println("<INFO ID=\"Error\" name=\"Error\" value=\"" +
              error + "\"/>");
         writer.println("</VOTABLE>");
      }
      else {
         doError(response, title, th);
      }
   }
}
