/*
 * $Id: SubmitQuery.java,v 1.5 2004/11/11 20:42:50 mch Exp $
 */

package org.astrogrid.datacenter.servlet;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.query.AdqlQueryMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.SqlParser;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.datacenter.service.ServletHelper;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.delegate.VoSpaceResolver;
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
         String id = server.submitQuery(ServletHelper.getUser(request), query, this);
         response.getWriter().write(id);
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th);
         doError(response, "Submitting "+query+" -> "+ServletHelper.makeReturnSpec(request),th);
      }
   }
   
   /** Creates a query from the parameters in the given request.  Static public so other servlets can use it */
   public static Query makeQuery(HttpServletRequest request) throws IOException, ParserConfigurationException, QueryException, SAXException, URISyntaxException {
      
      String adqlUri = request.getParameter("AdqlUri");
      String adqlSql = request.getParameter("AdqlSql");
      String adqlXml = request.getParameter("AdqlXml");
      
      Account user = ServletHelper.getUser(request);
      
      
      Query query = null;
      
      if (adqlUri != null) {
         InputStream in = null;
         if (Ivorn.isIvorn(adqlUri)) {
            in = VoSpaceResolver.resolveInputStream(user.toUser(), new Ivorn(adqlUri));
         }
         else if (Agsl.isAgsl(adqlUri)) {
            in = new Agsl(adqlUri).openInputStream(user.toUser());
         }
         else if (Msrl.isMsrl(adqlUri)) {
            in = new Msrl(adqlUri).openInputStream();
         }
         else {
            in = new URL(adqlUri).openStream();
         }
         
         query = AdqlQueryMaker.makeQuery(in);
      }
      else if (adqlSql != null) {
         query = SqlParser.makeQuery(adqlSql);
      }
      else if (adqlXml != null) {
         query = AdqlQueryMaker.makeQuery(adqlXml);
      }
      else {
         throw new QueryException("Must specify AdqlUri, AdqlXml or AdqlSql parameter");
      }
      
      ServletHelper.fillReturnSpec( (ReturnTable) query.getResultsDef(), request);
      
      
      return query;
   }
}
