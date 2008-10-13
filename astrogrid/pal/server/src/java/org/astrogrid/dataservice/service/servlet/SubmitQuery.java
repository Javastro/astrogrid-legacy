/*
 * $Id: SubmitQuery.java,v 1.5 2008/10/13 10:51:35 clq2 Exp $
 */

package org.astrogrid.dataservice.service.servlet;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.sourcetargets.URISourceTargetMaker;
import org.astrogrid.webapp.DefaultServlet;
import org.xml.sax.SAXException;

/**
 * A servlet for submitting a query asynchronously.
 * The parameters are:
 * AdqlUrl, AdqlSql or AdqlXml that specify the query to be run.
 * Target: where the results are to be sent
 * Format: what format the results should be sent in (eg VOTABLE, CSV - defaults to VOTABLE)
 *
 * @author mch
 */
public class SubmitQuery extends DefaultServlet {
   
   DataServer server = new DataServer();
   
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      Query query = null;
      
      try {
         query = makeQuery(request);
         
         if (query.getTarget() == null) {
            //get upset
            throw new QueryException("Must specify a Target argument for the results to be sent to when submitting Queries (otherwise use AskQuery)", null);
         }
         //submit query - and return just the query ID
         response.setContentType("text/plain");
         String id = server.submitQuery(ServletHelper.getUser(request), query, request.getRemoteHost()+" ("+request.getRemoteAddr()+") via SubmitQuery servlet");
         response.getWriter().write(id);
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th+" submitting query",th);
         doError(response, "Submitting "+query+" -> "+ServletHelper.makeReturnSpec(request),th);
      }
   }
   
   /** Creates a query from the parameters in the given request.  Static public so other servlets can use it */
   public static Query makeQuery(HttpServletRequest request) throws IOException, ParserConfigurationException, QueryException, SAXException, URISyntaxException {
      
      String adqlUri = request.getParameter("AdqlUri");
      String adqlSql = request.getParameter("AdqlSql");
      String adqlXml = request.getParameter("AdqlXml");
      
      Principal user = ServletHelper.getUser(request);
      
      Query query = null;
      
      //has the adql been given as a URI to read?
      if (adqlUri != null) {
         SourceIdentifier source = 
             URISourceTargetMaker.makeSourceTarget(adqlUri, 
                                                   ServletHelper.getUser(request));
         query = new Query(source.openInputStream());
      }
      else if (adqlSql != null) {
         throw new QueryException("Query creation from adql/sql is not supported");
      }
      else if (adqlXml != null) {
         query = new Query(adqlXml);
      }
      else {
         throw new QueryException("Must specify AdqlUri or AdqlXml parameter");
      }
      if (!ServletHelper.isCountReq(request)) {
         ServletHelper.fillReturnSpec(query.getResultsDef(), request ); 
      }
      return query;
   }
}
