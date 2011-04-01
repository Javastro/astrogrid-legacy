package org.astrogrid.dataservice.service.cone;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.security.authorization.AccessPolicy;
import org.astrogrid.security.HttpsServiceSecurityGuard;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.Configuration;
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.query.returns.ReturnTable;


/**
 * A servlet for processing Cone Queries.
 * Takes three parameters (RA, DEC and SR) that define the query, and
 * the standard returns definition parameters (see ???)
 *
 * @author M Hill
 * @author K Andrews
 * @author G Rixon
 */
public class SubmitCone extends HttpServlet {
  private static Log log = LogFactory.getLog(SubmitCone.class.getName());
   
  private DataServer server = new DataServer();

  private boolean isEnabled = false;

  @Override
  public void init() {
    // Initialise SampleStars plugin if required
    // (may not be initialised if admin has not run the self-tests)
    String plugin = ConfigFactory.getCommonConfig().getString("datacenter.querier.plugin");
    if (plugin.equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) {
      try {
        // This has no effect if the plugin is already initialised
        SampleStarsPlugin.initialise();
      } catch (DatabaseAccessException ex) {
        log.fatal("Can't initialize the sample-stars plugin: " + ex);
        throw new RuntimeException(ex);
      }
    }

    // The cone-search interface may be turned off in the service configuration.
    try {
      isEnabled = Configuration.isConeSearchEnabled();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

   /**
    * Performs a cone search. The VOTable of results is written to the
    * HTTP response. If the search fails, the failure may be reported either
    * by an error document in VOTable format or by throwing Servlet exception,
    * which generally causes the serlet container to write an error document
    * in HTML; the latter kind of report is only used if the request parameter
    * ErrorsInHtml is set.
    *
    * @param request The HTTP request.
    * @param response The HTTP response.
    * @throws IOException If cone search is not enabled.
    * @throws IOException If the test database is needed and not available.
    * @throws IOException If an error document cannot be sent.
    */
  @Override
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws IOException,
                                                         ServletException {

    // The cone-search standard requires error documents to be VOTables.
    // The caller may request non-standard handling of errors s.t. they
    // are readable in a web browser.
    boolean errorsInHtml = (request.getParameter("ErrorsInHtml") != null);

    try {
      // Check that cone-search is allowed.
      if (!isEnabled) {
        throw new IllegalStateException("Cone-search is disabled in the service configuration");
      }

      // Check authorization.
      HttpsServiceSecurityGuard sg = new HttpsServiceSecurityGuard();
      sg.loadHttpsAuthentication(request);
      String policyClassName =
        SimpleConfig.getSingleton().getString("cone.search.access.policy");
      Class policyClass = Class.forName(policyClassName);
      sg.setAccessPolicy((AccessPolicy)policyClass.newInstance());
      sg.decide(null);

      // Perform the search and write results to the HTTP response.
      executeConeSearch(request, response);
    }
    catch (Throwable th) {
      if (errorsInHtml) {
        throw new ServletException(th);
      }
      else {
        writeErrorAsVotable(th, response);
      }
    }
  }

  /**
   * Writes an error document in VOTable format.
   *
   * @param th The exception from whch the error message is taken.
   * @param response The HTTP response.
   * @throws IOException If the error document cannot be written.
   */
  private void writeErrorAsVotable(Throwable           th,
                                   HttpServletResponse response) throws IOException {
    response.setContentType("text/xml");
    response.setCharacterEncoding("UTF-8");
    String error = th.getMessage();
    if (error == null) {
      error = "(unknown error)";
    }
    else {
      error = error.replaceAll("&", "&amp;");
      error = error.replaceAll("<", "&lt;");
      error = error.replaceAll(">", "&gt;");
    }
    PrintWriter writer = response.getWriter();
    writer.println("<?xml version='1.0' encoding='UTF-8'?>");
    writer.println("<!DOCTYPE VOTABLE SYSTEM \"http://us-vo.org/xml/VOTable.dtd\">");
    writer.println("<VOTABLE version=\"1.0\">");
    writer.println("<DESCRIPTION>Error processing query</DESCRIPTION>");
    writer.println("<INFO ID=\"Error\" name=\"Error\" value=\"" + error + "\"/>");
    writer.println("</VOTABLE>");
  }

  /**
   * Executes the cone search expressed in the parameters of the HTTP response,
   * writing the VOTable of results to the HTTP response.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws Throwable On any error.
   */
  private void executeConeSearch(HttpServletRequest  request,
                                 HttpServletResponse response) throws Throwable {
    String catalogName = ServletHelper.getCatalogName(request);
    String tableName = ServletHelper.getTableName(request);
    double radius = ServletHelper.getRadius(request);
    double ra = ServletHelper.getRa(request);
    double dec = ServletHelper.getDec(request);
    String format = MimeTypes.toMimeType(request.getParameter("Format"));
    response.setContentType(format.toString());
    response.setCharacterEncoding("UTF-8");
    ReturnSpec returnSpec = new ReturnTable(new WriterTarget(response.getWriter(), false));
    returnSpec.setFormat(format.toString());

    String raColName = TableMetaDocInterpreter.getConeRAColumnByName(catalogName, tableName);
    String decColName = TableMetaDocInterpreter.getConeDecColumnByName(catalogName, tableName);
    String units = TableMetaDocInterpreter.getConeUnitsByName(catalogName, tableName);

    Query coneQuery = new Query(catalogName,
                                tableName,
                                units,
                                raColName,
                                decColName,
                                ra,
                                dec,
                                radius,
                                returnSpec);

    String label = request.getRemoteHost()+
                   " (" +
                   request.getRemoteAddr() +
                   ") via SubmitCone servlet";
    server.askQuery(ServletHelper.getUser(request), coneQuery, label);
  }
}
