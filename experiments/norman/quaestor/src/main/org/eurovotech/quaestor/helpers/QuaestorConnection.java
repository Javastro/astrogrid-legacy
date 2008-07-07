// Helper class: maintain a connection to a Quaestor service

package org.eurovotech.quaestor.helpers;

import org.eurovotech.quaestor.QuaestorException;
import org.eurovotech.quaestor.helpers.HttpResult;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


/**
 * Encapsulates a connection to a Quaestor service.
 *
 * <p>The primary interface to the Quaestor service is the HTTP
 * interface described elsewhere.  This class provides a wrapper for
 * this interface, making it easier to use Quaestor from a Java application.
 */
public class QuaestorConnection {
    
    private static final int METHOD_GET    = 1;
    private static final int METHOD_HEAD   = 2;
    private static final int METHOD_POST   = 3;
    private static final int METHOD_PUT    = 4;
    private static final int METHOD_DELETE = 5;

    /** 
     * Indicates that the content is in the default RDF format
     * @see #getModel(String,int)
     */
    public static final int FORMAT_DEFAULT = 0;
    /** 
     * Indicates that the content is in the RDF/XML format
     * @see #getModel(String,int)
     */
    public static final int FORMAT_RDFXML  = 1;
    /** 
     * Indicates that the content is in Notation3
     * @see #getModel(String,int)
     */
    public static final int FORMAT_N3      = 2;


    /** Base URL for the Quaestor service.  Note this is static, and
     * so affects all subsequent knowledgebases */
    //private static URL baseURL = null;

    /** The full URL for a specific knowledgebase */
    private URL kbURL = null;
    /** The URL for a specific knowledgebase, but with a dummy
     * submodel name so that it can be used as a base URL when
     * constructing submodel URLs. */
    private URL kbURLbase = null;

    private static java.util.regex.Pattern kbname;
    static {
        kbname = java.util.regex.Pattern.compile("^[a-zA-Z][a-zA-Z0-9-]*$");
    }

    /**
     * Create a new connection to an existing Quaestor knowledgebase.
     * @param quaestorURL the base URL of a Quaestor service, which
     * must not be null
     * @param knowledgebase the name of an existing knowledgebase
     * @throws QuaestorException if the knowledgebase does not already exist
     * @throws IllegalArgumentException if the first argument is null
     */
    public QuaestorConnection(URL quaestorURL, String knowledgebase) 
            throws QuaestorException, IllegalArgumentException {

        if (quaestorURL == null)
            throw new IllegalArgumentException("Null argument for quaestorURL");
        checkValidName(knowledgebase);
        if (! isPresent(quaestorURL, knowledgebase))
            throw new QuaestorException
                    ("Knowledgebase " + knowledgebase
                     + " does not exist (tried "
                     + makeKbURL(quaestorURL, knowledgebase, null) + ")");
        kbURL = makeKbURL(quaestorURL, knowledgebase, null);
        kbURLbase = makeKbURL(quaestorURL, knowledgebase, ".");
    }

    /**
     * Closes the connection to the knowledgebase
     */
    public void close() {
        // nothing to do
    }

    /**
     * Returns a stream from which the model, or a given submodel, may
     * be read.  Equivalent to <code>getModel(null,
     * FORMAT_DEFAULT)</code>
     */
    public InputStream getModel()
            throws QuaestorException {
        return getModel(null, FORMAT_DEFAULT);
    }

    /**
     * Returns a stream from which the model, or a given submodel, may
     * be read.  Equivalent to <code>getModel(submodel, FORMAT_DEFAULT)</code>
     */
    public InputStream getModel(String submodel)
            throws QuaestorException {
        return getModel(submodel, FORMAT_DEFAULT);
    }

    /**
     * Returns a stream from which the model, or a given submodel, may
     * be read.
     * @param submodel if null, then return the complete model; if
     * non-null, then return the named submodel
     * @param format one of the <code>FORMAT_...</code> constants
     */
    public InputStream getModel(String submodel, int format)
            throws QuaestorException {
        
        HttpResult r;
        if (! isValidName(submodel))
            return null;

        r = httpGet((submodel == null ? kbURL : submodelURL(submodel)),
                    getMIMETypeForFormat(format));
        if (r.getStatus() == HttpURLConnection.HTTP_OK)
            return r.getContentStream();
        else
            return null;
    }

    /**
     * Retrieve the metadata for the model
     * @return an input stream from which the metadata can be read, or
     * null on error
     */
    public InputStream getMetadata(int format)
            throws QuaestorException {
        HttpResult r = httpGet(submodelURL("?metadata"),
                               getMIMETypeForFormat(format));
        if (r.getStatus() == HttpURLConnection.HTTP_OK)
            return r.getContentStream();
        else
            return null;
    }

    /**
     * Uploads RDF data to a knowledgebase submodel.
     * @param submodel the submodel to be stocked with the content.
     * If this already exists, its contents are replaced; if it does
     * not, it is created
     * @param rdfdata the RDF assertions as a string
     * @param tbox true if the assertions are TBox data (that is,
     * ontology assertions) and false if they are ABox data (that is,
     * instance data)
     * @param format one of the <code>FORMAT_...</code> constants
     */
    public boolean putRDF(String submodel,
                          String rdfdata,
                          boolean tbox,
                          int format)
            throws QuaestorException {
        HttpResult r = httpPut(submodelURL(submodel + (tbox ? "?tbox" : "?abox")),
                               rdfdata,
                               getMIMETypeForFormat(format));
        return r.getStatus() == HttpURLConnection.HTTP_NO_CONTENT;
    }

    /**
     * Uploads RDF data to a knowledgebase submodel.
     * @param submodel the submodel to be stocked with the content.
     * If this already exists, its contents are replaced; if it does
     * not, it is created
     * @param rdfdata the RDF assertions as a stream
     * @param tbox true if the assertions are TBox data (that is,
     * ontology assertions) and false if they are ABox data (that is,
     * instance data)
     * @param format one of the <code>FORMAT_...</code> constants
     */
    public boolean putRDF(String submodel,
                          InputStream rdfdata,
                          boolean tbox,
                          int format)
            throws QuaestorException {
        HttpResult r = httpPut(submodelURL(submodel + (tbox ? "?tbox" : "?abox")),
                               rdfdata,
                               getMIMETypeForFormat(format));
        return r.getStatus() == HttpURLConnection.HTTP_NO_CONTENT;
    }

    /**
     * Makes a SPARQL query over the model.
     * @param queryString the SPARQL query as a string
     * @return a stream containing the query results, or null on error
     */
    public InputStream query(String queryString)
            throws QuaestorException {
        return query(queryString, "application/xml");
    }

    /**
     * Makes a SPARQL query over the model.
     * @param queryString the SPARQL query as a string
     * @param format the MIME type the caller wishes to receive
     * @return a stream containing the query results
     * @throws QuaestorException if there is a problem making or
     * parsing the query
     */
    public InputStream query(String queryString, String format)
            throws QuaestorException {
        HttpResult r = httpPost(kbURL,
                                queryString,
                                new String[] {
                                    "Content-Type", "application/sparql-query",
                                    "Accept", format 
                                });
        if (r.getStatus() == HttpURLConnection.HTTP_OK) {
            return r.getContentStream();
        } else {
            try {
                String contents = r.getContentAsString();
                throw new QuaestorException("Error parsing or making query: "
                                            + contents);
            } catch (java.io.IOException e) {
                throw new QuaestorException("IO error reading query results", e);
            }
        }
    }

    /**
     * Create a new knowledgebase with the given name.  On success,
     * this returns a connection to the knowledgebase.
     *
     * @param knowledgebase the name of the new knowledgebase
     * @param description a brief textual description of the knowledgebase
     * @return a connection to the knowledgebase
     * @throws QuaestorException if there is a problem creating the
     * knowledgebase, for example if it already exists
     */
    public static QuaestorConnection createKnowledgebase(URL quaestorURL,
                                                         String knowledgebase,
                                                         String description)
            throws QuaestorException {
        checkValidName(knowledgebase);
        try {
            URL newURL = makeKbURL(quaestorURL, knowledgebase, null);
            HttpResult r = httpPut(newURL, description, "text/plain");
            String content = r.getContentAsString();
            if (r.getStatus() == HttpURLConnection.HTTP_NO_CONTENT)
                return new QuaestorConnection(quaestorURL, knowledgebase);
            else
                throw new QuaestorException
                        ("Failed to create new knowledgebase: "
                         + content);
        } catch (java.io.IOException e) {
            throw new QuaestorException("Error creating knowledgebase", e);
        }
    }

    /**
     * Deletes the knowledgebase to which we are connected
     * @return true on success
     */
    public boolean deleteKnowledgebase()
            throws QuaestorException {
        HttpResult r = httpDelete(kbURL);
        return r.getStatus() == HttpURLConnection.HTTP_NO_CONTENT;
    }

    /**
     * Returns true if the indicated knowledgebase is available.
     * @param quaestorURL the base URL of a Quaestor service, which
     * must not be null
     * @param knowledgebase the name of an existing knowledgebase
     * @return true if a connection to this knowledgebase with the
     * same parameters would succeed
     */
    public static boolean isPresent(URL quaestorURL, String knowledgebase) {
        if (quaestorURL == null)
            return false;
        try {
            checkValidName(knowledgebase);
            HttpResult r = httpHead(makeKbURL(quaestorURL, knowledgebase, null));
            return r.getStatus() == HttpURLConnection.HTTP_OK;
        } catch (QuaestorException e) {
            // we'll take that as a no, then...
            return false;
        }
    }

    private static String getMIMETypeForFormat(int format) {
        String res;
        switch (format) {
          case FORMAT_DEFAULT:
            res = "text/rdf+n3";
            break;
          case FORMAT_RDFXML:
            res = "application/rdf+xml";
            break;
          case FORMAT_N3:
            res = "text/rdf+n3";
            break;
          default:
            throw new IllegalArgumentException("Unrecognised RDF format "
                                               + format);
        }
        return res;
    }

    /* ******************** Static worker classes ******************** */

    /**
     * Perform an HTTP GET request on the given URL
     * @param url the URL to retrieve
     * @return the HTTP response encapsulated in a HttpResult object
     */
    public static HttpResult httpGet(URL url) 
            throws QuaestorException {
        return httpTransaction(METHOD_GET, url, null);
    }

    /**
     * Perform an HTTP GET request on the given URL
     * @param url the URL to retrieve
     * @param contentType the content type which is to be included in
     * an HTTP <code>Accept</code> header
     * @return the HTTP response encapsulated in a HttpResult object
     */
    public static HttpResult httpGet(URL url, String contentType)
            throws QuaestorException {
        return httpTransaction(METHOD_GET, url, null,
                               new String[] {"Accept", contentType});
    }

    /**
     * Perform an HTTP GET request on the given URL
     * @param url the URL to retrieve
     * @param headers an array of header pairs, as described in
     * {@link #httpTransaction} 
     * @return the HTTP response encapsulated in a HttpResult object
     */
    public static HttpResult httpGet(URL url, String[] headers)
            throws QuaestorException {
        return httpTransaction(METHOD_GET, url, null, headers);
    }

    /**
     * Perform an HTTP HEAD request on the given URL
     * @param url the URL to retrieve
     * @return the HTTP response encapsulated in a HttpResult object
     */
    public static HttpResult httpHead(URL url)
            throws QuaestorException {
        return httpTransaction(METHOD_HEAD, url, null, null);
    }

    /**
     * Perform an HTTP POST request on the given URL
     * @param url the URL to retrieve
     * @param content a string containing the payload to be posted to
     * the service
     * @param contentType the content type of the uploaded contents,
     * which is to be included in an HTTP <code>Content-Type</code> header
     * @return the HTTP response encapsulated in a HttpResult object
     */
    public static HttpResult httpPost(URL url,
                                      String content,
                                      String contentType)
            throws QuaestorException {
        return httpTransaction(METHOD_POST, url, content,
                               new String[] {"Content-Type", contentType});
    }

    /**
     * Perform an HTTP POST request on the given URL
     * @param url the URL to retrieve
     * @param content a string containing the payload to be posted to
     * the service
     * @param headers an array of header pairs, as described in
     * {@link #httpTransaction} 
     * @return the HTTP response encapsulated in a HttpResult object
     */
    public static HttpResult httpPost(URL url,
                                      String content,
                                      String[] headers)
            throws QuaestorException {
        return httpTransaction(METHOD_POST, url, content, headers);
    }

    /**
     * Perform an HTTP PUT request on the given URL
     * @param url the URL to retrieve
     * @param content a string containing the payload to be posted to
     * the service
     * @param contentType the content type of the uploaded contents,
     * which is to be included in an HTTP <code>Content-Type</code> header
     * @return the HTTP response encapsulated in a HttpResult object
     */
    public static HttpResult httpPut(URL url, String content, String contentType)
            throws QuaestorException {
        return httpTransaction(METHOD_PUT, url, content,
                               new String[] {"Content-Type", contentType});
    }

    /**
     * Perform an HTTP PUT request on the given URL
     * @param url the URL to retrieve
     * @param content the content which is to be uploaded to the service
     * @param contentType the content type of the uploaded contents,
     * which is to be included in an HTTP <code>Content-Type</code> header
     * @return the HTTP response encapsulated in a HttpResult object
     */
    public static HttpResult httpPut(URL url,
                                     InputStream content,
                                     String contentType)
            throws QuaestorException {
        return httpTransaction(METHOD_PUT, url, content,
                               new String[] {"Content-Type", contentType});
    }

    /**
     * Perform an HTTP DELETE request on the given URL
     * @param url the URL to be deleted
     * @return the HTTP response encapsulated in a HttpResult object
     */
    public static HttpResult httpDelete(URL url)
            throws QuaestorException {
        return httpTransaction(METHOD_DELETE, url, null);
    }

    /**
     * Generic HTTP transaction handler.
     * @param method one of METHOD_GET, METHOD_POST, etc...
     * @param url the URL to interact with
     * @param content the content to be sent to the server, or null if 
     * none is to be sent
     * @return an instance of {@link HttpResult}, with the results of
     * the transaction; the content is returned a a trimmed String, or as
     * null if that trimmed string would be of zero length
     */
    private static HttpResult httpTransaction(int method, URL url, String content)
            throws QuaestorException {
        return httpTransaction(method, url, content, null);
    }

    /**
     * Generic HTTP transaction handler.
     * @param method one of GET, POST, etc...
     * @param url the URL to interact with
     * @param content the content to be sent to the server, or null if 
     * none is to be sent; this may be either a String or an InputStream
     * @param headers a list of Strings which are interpreted in
     * pairs, as (header-name, header-value)
     * @return an instance of {@link HttpResult}, with the results of
     * the transaction; the content is returned a a trimmed String, or as
     * null if that trimmed string would be of zero length
     * @throws QuaestorException if there are any problems (including
     * IOException, reading the result of the transaction
     */
    private static HttpResult httpTransaction(int method,
                                              URL url,
                                              Object content,
                                              String[] headers)
            throws QuaestorException {
        assert url.getProtocol().equals("http");
        String methodString = null;
        switch (method) {
          case METHOD_GET:    methodString = "GET";    break;
          case METHOD_HEAD:   methodString = "HEAD";   break;
          case METHOD_PUT:    methodString = "PUT";    break;
          case METHOD_POST:   methodString = "POST";   break;
          case METHOD_DELETE: methodString = "DELETE"; break;
          default: throw new IllegalArgumentException
                    ("Unrecognised method " + method);
        }

        try {
            HttpURLConnection c = null;
            try {
                c = (HttpURLConnection)url.openConnection();
                c.setRequestMethod(methodString);
                c.setDoInput(true);

                if (headers != null) {
                    if (headers.length%2 != 0)
                        throw new IllegalArgumentException
                                ("Odd number of headers in httpTransaction");
                    for (int i=0; i<headers.length; i+=2) {
                        assert i+1 < headers.length;
                        c.setRequestProperty(headers[i], headers[i+1]);
                    }
                }

                if (content == null) {
                    c.setDoOutput(false);
                } else {
                    c.setDoOutput(true);

                    if (content instanceof String) {
                        OutputStreamWriter w
                                = new OutputStreamWriter(c.getOutputStream());
                        w.write((String)content);
                        w.flush();
                        w.close();
                    } else if (content instanceof InputStream) {
                        byte[] b = new byte[1024];
                        int nread;
                        InputStream s = (InputStream)content;
                        java.io.OutputStream w = c.getOutputStream();
                        while ((nread = s.read(b)) >= 0)
                            w.write(b, 0, nread);
                        w.flush();
                        w.close();
                    } else {
                        assert false : "impossible content object";
                    }
                }

                return new HttpResult(c.getResponseCode(),
                                      c.getContentType(),
                                      c.getInputStream());
            } catch (java.io.IOException e) {
                if (c != null && c.getResponseCode() >= 400) {
                    // fake error generated by getInputStream (grrr)
                    return new HttpResult(c.getResponseCode(),
                                          c.getContentType(),
                                          c.getErrorStream());
                } else {
                    // it actually is an error
                    throw new QuaestorException
                            ("Error in connection to Quaestor service", e);
                }
            }
        } catch (java.io.IOException e) {
            // This is an exception thrown by the c.getResponseCode()
            // within the IOException handler.
            Throwable cause = e.getCause();
            if (cause instanceof java.net.ConnectException) {
                throw new QuaestorException
                        ("Couldn't connect to Quaestor (is the server running?): " + cause);
            } else {
                // I think this really shouldn't happen.
                throw new QuaestorException("Weirdo IOException: " + e);
            }
        }
    }

    /* ******************** Various helper methods ******************** */

    private static URL makeKbURL(URL baseURL,
                                 String knowledgebase,
                                 String submodel) {
        assert baseURL != null && knowledgebase != null;
        try {
            String path = baseURL.getPath();
            String kb;
            if (submodel == null)
                kb = (knowledgebase == null ? "" : knowledgebase);
            else
                kb = (knowledgebase == null ? "" : knowledgebase)
                        + "/" + submodel;
            if (path.endsWith("/"))
                return new URL(baseURL, "kb/" + kb);
            else
                return new URL(baseURL.getProtocol(),
                               baseURL.getHost(),
                               baseURL.getPort(),
                               path + "/kb/" + kb);
        } catch (MalformedURLException e) {
            throw new AssertionError("Bad knowledgebase URL " + baseURL
                                     + "+" + knowledgebase);
        }
    }

    private URL submodelURL(String submodel) {
        assert submodel != null && submodel.length() > 0;
        try {
            if (submodel.charAt(0) == '?')
                return new URL(kbURL.getProtocol(),
                               kbURL.getHost(),
                               kbURL.getPort(),
                               kbURL.getPath() + submodel);
            else
                return new URL(kbURLbase, submodel);
        } catch (MalformedURLException e) {
            throw new AssertionError("Malformed URL with base=" + kbURL
                                     + " and submodel " + submodel);
        }
    }

    private URL makeModelURL(String submodel) {
        try {
            if (submodel == null)
                return kbURL;
//             else if (submodel.startsWith("?"))
//                 return new URL(kbURL, submodel);
            else
                return new URL(kbURL, submodel);
        } catch (java.net.MalformedURLException e) {
            throw new AssertionError("Malformed KB URL: " + e);
        }
    }

    private static boolean isValidName(String s) {
        if (s == null)
            return false;
        java.util.regex.Matcher m = kbname.matcher(s);
        return m.matches();
    }
        
    private static void checkValidName(String s)
            throws QuaestorException {
        if (! isValidName(s))
            throw new QuaestorException
                    ("Invalid name '" + s
                     + "' (may be letters, digits and dash only)");
    }

    /* ******************** inner classes ******************** */


}
