// JUnit test cases for Quaestor

package org.eurovotech.quaestor;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import junit.framework.Assert;
import junit.framework.TestCase;

public class QuaestorTest
        extends TestCase {

    private URL baseURL;
    private String testKB;

    private static final int METHOD_GET    = 111;
    private static final int METHOD_POST   = 222;
    private static final int METHOD_PUT    = 333;
    private static final int METHOD_DELETE = 444;

    public QuaestorTest(String name)
            throws Exception {
        super(name);
        // I should include here some mechanism for checking that the 
        // Quaestor server is indeed running in Tomcat.

        // Generalise this URL with a property
        baseURL = new URL("http://localhost:8080/quaestor/.");

        testKB = "testing";
    }

    public void testGetTopPage()
            throws Exception {
        HttpResult r = httpGet(new URL(baseURL, "."));
        assertEquals(HttpURLConnection.HTTP_OK,
                     r.getStatus());
        assertEquals(r.getContentType(), "text/html");
    }

    public void testCreateKnowledgebase()
            throws Exception {
        URL kbURL = makeURL();

        // Attempt to delete the knowledgebase, whether or not it's there.
        // Don't care about the return value.
        HttpResult r = httpDelete(kbURL);

        // create the new knowledgebase
        r = httpPut(kbURL, "My test knowledgebase");
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, r.getStatus());
        assertNull(r.getContent());

        // Does ?metadata work?
        // (don't test the content type here, it's currently wrong)
        // XXX fixme
        kbURL = makeURL("?metadata");
        r = httpGet(kbURL);
        assertEquals(HttpURLConnection.HTTP_OK, r.getStatus());
        // content-type may be followed by charset info
        assertTrue(r.getContentType().startsWith("text/plain"));
        assertEquals("My test knowledgebase", r.getContent());
    }

    // try that one again -- should fail, since we can't create 
    // the knowledgebase twice
    public void testCreateKnowledgebaseAgain()
            throws Exception {
        HttpResult r = httpPut(makeURL(),
                               "My test knowledgebase");
        assertEquals(4, r.getStatus()/100);
    }

    /* ******************** Other test methods ******************** */

    public void testAddOntology()
            throws Exception {
        HttpResult r = httpTransaction
                (METHOD_PUT,
                 makeURL("ontology"),
                 "<?xml version='1.0'?><rdf:RDF xmlns='urn:example#' xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' xmlns:owl='http://www.w3.org/2002/07/owl#' xml:base='urn:example'><owl:Ontology rdf:about=''/><owl:Class rdf:ID='c1'/></rdf:RDF>",
                 new String[] { "Content-Type", "application/rdf+xml" });
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, r.getStatus());
        assertNull(r.getContent());
    }

    public void testGetOntology ()
            throws Exception {
        HttpResult r = httpGet(makeURL("ontology"),
                               new String[] {"Accept", "*/*"});
        assertEquals(HttpURLConnection.HTTP_OK, r.getStatus());
        assertEquals("application/rdf+xml", r.getContentType());
        // the actual content is a bit of a fuss to check

        r = httpGet(makeURL("ontology"),
                    new String[] {"Accept", "application/n3"});
        assertEquals(HttpURLConnection.HTTP_OK, r.getStatus());
        assertEquals("application/n3", r.getContentType());
        // the actual content is a bit of a fuss to check
    }
    
    public void testAddInstances ()
            throws Exception {
        HttpResult r = httpTransaction
                (METHOD_PUT,
                 makeURL("instances"),
                 "<urn:example#i1> a <urn:example#c1>.",
                 new String[] { "Content-Type", "application/n3" });
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, r.getStatus());
        assertNull(r.getContent());
    }

    public void testSparqlQueries() 
            throws Exception {
        String query = "SELECT ?i where { ?i a <urn:example#c1>}";
        HttpResult r;
        r = httpPost(makeURL("ontology"), query);
        assertEquals(4, r.getStatus()/100);

        // check XML response
        r = httpPost(makeURL(), query);
        assertEquals(HttpURLConnection.HTTP_OK, r.getStatus());
        assertEquals("application/xml", r.getContentType());
        // don't check actual content string

        // check n-triple response
        r = httpTransaction(METHOD_POST, makeURL(), query,
                            new String[] {"Accept", "text/plain"});
        assertEquals(HttpURLConnection.HTTP_OK, r.getStatus());
        assertEquals("text/plain", r.getContentType());
        // don't check actual content

        String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
        r = httpGet(makeURL("?sparql="+encodedQuery));
        assertEquals(HttpURLConnection.HTTP_OK, r.getStatus());
        assertEquals("application/xml", r.getContentType());

        // the same, requesting text/plain
        r = httpGet(makeURL("?sparql="+encodedQuery),
                    new String[] {"Accept", "text/plain"});
        assertEquals(HttpURLConnection.HTTP_OK, r.getStatus());
        assertEquals("text/plain", r.getContentType());

        // ...and requesting CSV
        r = httpGet(makeURL("?sparql="+encodedQuery),
                    new String[] {"Accept", "text/csv"});
        assertEquals(HttpURLConnection.HTTP_OK, r.getStatus());
        assertEquals("text/csv;header=present", r.getContentType());
    }
                            
    /* ******************** deletion of knowledgebases ******************** */

    public void testDeleteKnowledgebase()
            throws Exception {
        HttpResult r = httpDelete(makeURL());
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, r.getStatus());
        assertNull(r.getContent());
    }

    // should fail when done a second time
    public void testDeleteKnowledgebaseAgain()
            throws Exception {
        HttpResult r = httpDelete(makeURL());
        assertEquals(4, r.getStatus()/100);
        //assertEquals("", r.getContent());
    }

    /* ********** Helper methods ********** */

    private URL makeURL() 
            throws java.net.MalformedURLException {
        return makeURL(null);
    }
    private URL makeURL(String submodel) 
            throws java.net.MalformedURLException {
        if (submodel == null)
            return new URL(baseURL, "kb/"+testKB);
        else if (submodel.startsWith("?"))
            return new URL(baseURL, "kb/"+testKB+submodel);
        else
            return new URL(baseURL, "kb/"+testKB+"/"+submodel);
    }

    private HttpResult httpGet(URL url) 
            throws Exception {
        return httpTransaction(METHOD_GET, url, null);
    }

    private HttpResult httpGet(URL url, String[] headers)
            throws Exception {
        return httpTransaction(METHOD_GET, url, null, headers);
    }

    private HttpResult httpPost(URL url, String content)
            throws Exception {
        return httpTransaction(METHOD_POST, url, content);
    }

    private HttpResult httpPut(URL url, String content)
            throws Exception {
        return httpTransaction(METHOD_PUT, url, content);
    }

    private HttpResult httpDelete(URL url)
            throws Exception {
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
    private HttpResult httpTransaction(int method, URL url, String content)
            throws Exception {
        return httpTransaction(method, url, content, null);
    }

    /**
     * Generic HTTP transaction handler.
     * @param method one of GET, POST, etc...
     * @param url the URL to interact with
     * @param content the content to be sent to the server, or null if 
     * none is to be sent
     * @param headers a list of Strings which are interpreted in
     * pairs, as (header-name, header-value)
     * @return an instance of {@link HttpResult}, with the results of
     * the transaction; the content is returned a a trimmed String, or as
     * null if that trimmed string would be of zero length
     */
    private HttpResult httpTransaction(int method,
                                       URL url,
                                       String content,
                                       String[] headers)
            throws Exception {
        assert url.getProtocol().equals("http");
        String methodString = null;
        switch (method) {
          case METHOD_GET:    methodString = "GET";    break;
          case METHOD_PUT:    methodString = "PUT";    break;
          case METHOD_POST:   methodString = "POST";   break;
          case METHOD_DELETE: methodString = "DELETE"; break;
          default: throw new IllegalArgumentException
                    ("Unrecognised method " + method);
        }
        
        HttpURLConnection c = (HttpURLConnection)url.openConnection();
        try {
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

                OutputStreamWriter w
                        = new OutputStreamWriter(c.getOutputStream());
                w.write(content);
                w.flush();
                w.close();
            }

            StringBuffer sb = new StringBuffer();
            BufferedReader r
                    = new BufferedReader
                    (new InputStreamReader(c.getInputStream()));
            String line;
            while ((line = r.readLine()) != null)
                sb.append(line);
            r.close();

            String res = sb.toString().trim();
            if (res.length() == 0)
                res = null;

            return new HttpResult(c.getResponseCode(),
                                  c.getContentType(),
                                  res);
        } catch (java.io.IOException e) {
            if (c.getResponseCode() >= 400) {
                // fake error generated by getInputStream (grrr)
                java.io.InputStream is = c.getErrorStream();
                String contentString;
                if (is == null) {
                    contentString = null;
                } else {
                    StringBuffer sb = new StringBuffer();
                    BufferedReader r
                            = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = r.readLine()) != null)
                        sb.append(line);
                    r.close();
                    contentString = sb.toString().trim();
                    if (contentString.length() == 0)
                        contentString = null;
                }
                return new HttpResult(c.getResponseCode(),
                                      c.getContentType(),
                                      contentString);
            } else {
                // it actually is an error
                throw e;
            }
        }
    }

    public static class HttpResult {
        private int status;
        private String responseContentType;
        private String responseContent;

        public HttpResult(int status, String contentType, String content) {
            this.status = status;
            this.responseContentType = contentType;
            this.responseContent = content;
        }
        public int getStatus() {
            return status;
        }
        public String getContentType() {
            return responseContentType;
        }
        public String getContent() {
            return responseContent;
        }
        public String toString() {
            return "Status=" + status
                    + ", content-type=" + responseContentType
                    + ", content=" + responseContent;
        }
    }
}
