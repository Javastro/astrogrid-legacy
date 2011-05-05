package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.io.mime.MimeTypes;
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
   * Handles a GET request.
   * The request parameters define the query to be executed.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If a response cannot be written.
   * @throws TapException If the query fails.
   */
  @Override
  protected void performGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, TapException {
    query(request, response);
  }

  /**
   * Handles POST requests, which are not allowed.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException On failure to output a response.
   */
  @Override
  public void performPost(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, TapException {
    query(request, response);
  }

  /**
   * Executes a query. The HTTP request defines the query and the results are
   * written to the HTTP response.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws TapException If the query cannot be performed.
   * @throws IOException If the results cannot written to the HTTP response.
   */
  protected void query(HttpServletRequest request, HttpServletResponse response)
      throws TapException, IOException {
    String language = getQueryLanguage(request);
    String adql = getQueryText(request, language);
    String mimeType = getMimeType(request);
    Query query = makeQuery(request, adql, mimeType, response);
    Principal principal = query.getGuard().getX500Principal(); // May be null
    String source = String.format("%s (%s) via TAP, synchronous query", request.getRemoteHost(), request.getRemoteAddr());
    try {
      new Querier(principal, query, source).ask();
    } catch (Throwable ex) {
      throw new TapException(ex);
    }
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
      } else if (language.toUpperCase().equals("ADQL") || language.toUpperCase().equals("ADQL-2.0")) {
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
   * Determines the MIME-type for the requested format. If no format has been
   * requested, MIME-type for VOTable/TABLEDATA is returned.
   *
   * @param request The HTTP request containing the format request.
   * @return The MIME type.
   * @throws TapException If an unsupported format is requested.
   */
  protected String getMimeType(HttpServletRequest  request) throws TapException {
    try {
     return MimeTypes.toMimeType(request.getParameter("FORMAT"));
    }
    catch (Exception e) {
      throw new TapException(e);
    }
  }

  protected Query makeQuery(HttpServletRequest  request,
                            String              adql,
                            String              mimeType,
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
    response.setContentType(mimeType);
    query.getResultsDef().setFormat(mimeType);
    
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
