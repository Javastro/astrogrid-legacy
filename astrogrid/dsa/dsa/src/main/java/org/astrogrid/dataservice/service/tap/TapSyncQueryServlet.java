package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.security.HttpsServiceSecurityGuard;
import org.astrogrid.slinger.targets.WriterTarget;

/**
 * A servlet to handle synchronous data and metadata queries under the IVOA
 * TAP protocol.
 * <p>
 * Requests should be redirected to this servlet when the REQUEST parameter is
 * set to doQuery. This is not checked within this servlet
 *
 * @author Guy Rixon
 */
public class TapSyncQueryServlet extends AbstractTapServlet {

  /**
   * The engine for executing queries.
   */
  DataServer server = new DataServer();

  /**
   * Handles a GET request.
   * The request parameters define the query to be executed.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If a response cannot be written.
   * @throws TapException If the query fails.
   */
  @Override
  protected void performGet(HttpServletRequest  request,
                            HttpServletResponse response) throws IOException,
                                                                 TapException {
    String language = getQueryLanguage(request);
    String adql = getQueryText(request, language);
    String format = getOutputFormat(request);
    Query query = makeQuery(request, adql, format, response);
    Principal principal = query.getGuard().getX500Principal();
    principal = (principal == null)? LoginAccount.ANONYMOUS : principal;
    String source = String.format("%s (%s) via TAP, synchronous query",
                                  request.getRemoteHost(),
                                  request.getRemoteAddr());
    try {
      server.askQuery(principal, query, source);
    } catch (Throwable ex) {
      throw new TapException(ex);
    }

  }

  /**
   * Handles POST requests, which are not allowed.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException On failure to output a response.
   */
  @Override
  public void performPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
    response.sendError(response.SC_METHOD_NOT_ALLOWED);
  }

  /**
   * Validates the requested query-language.
   *
   * @param request The HTTP request.
   * @throws TapException If the language request is missing.
   * @thows TapException If the requested language is not supported.
   */
  protected String getQueryLanguage(HttpServletRequest request) throws TapException {
    String language = request.getParameter("LANG");
    if (language == null) {
      throw new TapException("Parameter LANG (query language) was not set");
    } else {
      language = language.trim();
      if (language.length() == 0) {
        throw new TapException("Parameter LANG (query language) was empty");
      } else if (language.toUpperCase().equals("ADQL")) {
        return "ADQL";
      }
      else {
        throw new TapException(String.format("Query-language %s is not supported here", language));
      }
    }
  }

  /**
   * Obtains the text of the query.
   * Assumes that the query language is sensitive to case and return the
   * given mix of case.
   *
   * @param request The HTTP request.
   * @param language The query language.
   * @return The query text.
   * @throws TapException If the text is missing.
   */
  protected String getQueryText(HttpServletRequest request,
                                String             language) throws TapException {
    // Obtain the query.
    String adql = request.getParameter("QUERY");
    if (adql == null) {
      throw new TapException("Parameter QUERY (query text) was not set");
    } else {
      adql = adql.trim();
      if (adql.length() == 0) {
        throw new TapException("Parameter QUERY (query text) was empty");
      }
      return adql;
    }
  }

  /**
   * Determines the format of the output document. If the FORMAT parameter is
   * missing, the default is application/x-votable+xml.
   *
   * @param request The HTTP request.
   * @return The MIME type.
   * @throws TapException If the format is an empty parameter.
   * @throws TapException If the requested format is not supported.
   */
  protected String getOutputFormat(HttpServletRequest request) throws TapException {
    // Determine the requested format for output. We only do VOTable.
    String format = request.getParameter("FORMAT");
    if (format == null) {
      return "application/x-votable+xml";
    } else {
      format = format.trim();
      if (format.length() == 0) {
        throw new TapException("Parameter FORMAT (output format) was empty");
      }
      else if (format.toLowerCase().equals("votable")) {
        return "application/x-votable+xml";
      }
      else if (format.equals("application/x-votable+xml")) {
        return format;
      }
      else if (format.equals("text/xml")) {
        return format;
      }
      else {
        throw new TapException(String.format("Output format %s is not supported here", format));
      }
    }
  }



  protected Query makeQuery(HttpServletRequest  request,
                            String              adql,
                            String              format,
                            HttpServletResponse response) throws IOException,
                                                                 TapException {

    // Form the basic query with the query text.
    Query query;
    try {
      query = new Query(adql);
    } catch (QueryException ex) {
      throw new TapException(ex);
    }

    // Arrange for the output to go to the HTTP response
    query.getResultsDef().setTarget(new WriterTarget(response.getWriter(), false));
    
    // Set the MIME type for the output.
    response.setContentType(format);
    // How does the server know it's to be ADQL? Is it a default?
    
    // Set the user's name. This is mainly used for logging.
    HttpsServiceSecurityGuard guard = new HttpsServiceSecurityGuard();
    query.setGuard(guard);
    try {
      guard.loadHttpsAuthentication(request);
    }
    catch (Exception e) {
      // Ignore.
    }

    return query;
  }

}
