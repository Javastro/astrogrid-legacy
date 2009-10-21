package org.astrogrid.dataservice.service.tap;

import javax.servlet.http.HttpServletRequest;

/**
 * The TAP instructions for starting an ADQL query. The instructions are
 * constructed by partsing an HTTP request. The object is immutable after
 * construction.
 *
 * @author Guy Rixon
 */
public class TapAdqlQuery {

  /**
   * The text of the query.
   */
  final String adql;

  /**
   * The query language. This might distinguish versions or dialects of ADQL.
   */
  final String language;

  /**
   * The MIME-type for the format of the query results.
   */
  final String format;

  /**
   * The version of the TAP protocol.
   */
  final String version;

  /**
   * Constructs a query from an HTTP request.
   */
  public TapAdqlQuery(HttpServletRequest request) throws TapException {

    // Validate and record the TAP version.
    version = request.getParameter("VERSION");
    if (version != null) {
      if (!version.equals("1.0")) {
        throw new TapException(String.format("TAP version %s is not supported here", version));
      }
    }

    // Validate and record the query language.
    String l = request.getParameter("LANG");
    if (l == null) {
      throw new TapException("Parameter LANG (query language) was not set");
    } else {
      language = l.trim().toUpperCase();
      if (language.length() == 0) {
        throw new TapException("Parameter LANG (query language) was empty");
      } else if (!language.equals("ADQL")) {
        throw new TapException(String.format("Query-language %s is not supported here", language));
      }
    }

    // Record the query text.
    String q = request.getParameter("QUERY");
    if (q == null) {
      throw new TapException("Parameter QUERY was not set");
    } else {
      adql = q.trim().toUpperCase();
      if (adql.length() == 0) {
        throw new TapException("Parameter QUERY was empty");
      }
    }

    // Validate and record the output format.
    String f = request.getParameter("FORMAT");
    if (f == null) {
      format = "application/x-votable+xml";
    } else {
      f = f.trim().toLowerCase();
      if (f.equals("application/x-votable+xml")) {
        format = f;
      }
      else if (f.equals("votable")) {
        format = "application/x-votable+xml";
      }
      else if (f.equals("text/xml")) {
        format = f;
      }
      else if (f.equals("text/csv")) {
        format = f;
      }
      else if (f.equals("csv")) {
        format = "text/csv";
      }
      else if (f.equals("text/html")) {
        format = f;
      }
      else if (f.equals("html")) {
        format = "text/html";
      }
      else {
        throw new TapException(String.format("Format %s is not supported here", f));
      }
    }
  }

  /**
   * Supplies the text of the query.
   * @return The query text.
   */
  public String getQueryText() {
    return adql;
  }

  /**
   * Supplies the output format.
   * @return The MIME-type for the format.
   */
  public String getFormat() {
    return (format == null)? "application/x-votable+xml" : format;
  }

  /**
   * Supplies the query language.
   * @return The name of the language.
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Supplies the TAP version.
   * @return The version.
   */
  public String getTapVersion() {
    return version;
  }
}
