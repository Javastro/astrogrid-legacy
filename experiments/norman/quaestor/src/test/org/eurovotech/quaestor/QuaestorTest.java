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
        URL kbURL = new URL(baseURL, "kb/"+testKB);

        // Attempt to delete the knowledgebase, whether or not it's there.
        // Don't care about the return value.
        HttpResult r = httpDelete(kbURL);

        // create the new knowledgebase
        r = httpPut(kbURL, "My test knowledgebase");
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, r.getStatus());
        assertEquals("", r.getContent());

        // Does ?metadata work?
        // (don't test the content type here, it's currently wrong)
        kbURL = new URL(baseURL, "kb/"+testKB+"?metadata");
        System.out.println("retrieving metadata url: " + kbURL);
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
        HttpResult r = httpPut(new URL(baseURL, "kb/"+testKB),
                               "My test knowledgebase");
        assertEquals(4, r.getStatus()/100);
    }

    // Other test methods    
        
    public void testDeleteKnowledgebase()
            throws Exception {
        HttpResult r = httpDelete(new URL(baseURL, "kb/"+testKB));
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, r.getStatus());
        assertEquals("", r.getContent());
    }

    // should fail when done a second time
    public void testDeleteKnowledgebaseAgain()
            throws Exception {
        HttpResult r = httpDelete(new URL(baseURL, "kb/"+testKB));
        assertEquals(4, r.getStatus()/100);
        //assertEquals("", r.getContent());
    }

    /* ********** Helper methods ********** */

    private HttpResult httpGet(URL url) 
            throws Exception {
        return httpTransaction("GET", url, null);
    }

    private HttpResult httpPost(URL url, String content)
            throws Exception {
        return httpTransaction("POST", url, content);
    }

    private HttpResult httpPut(URL url, String content)
            throws Exception {
        return httpTransaction("PUT", url, content);
    }

    private HttpResult httpDelete(URL url)
            throws Exception {
        return httpTransaction("DELETE", url, null);
    }

    /**
     * Generic HTTP transaction handler.
     * @param method one of GET, POST, etc...
     * @param url the URL to interact with
     * @param content the content to be sent to the server, or null if 
     * none is to be sent
     */
    private HttpResult httpTransaction(String method, URL url, String content)
            throws Exception {
        assert url.getProtocol().equals("http");
        HttpURLConnection c = (HttpURLConnection)url.openConnection();
        try {
            c.setRequestMethod(method);
            c.setDoInput(true);

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

            return new HttpResult(c.getResponseCode(),
                                  c.getContentType(),
                                  sb.toString());
        } catch (java.io.IOException e) {
            if (c.getResponseCode() >= 400) {
                // fake error generated by getInputStream (grrr)
                java.io.InputStream is = c.getErrorStream();
                String contentString;
                if (is == null) {
                    contentString = "";
                } else {
                    StringBuffer sb = new StringBuffer();
                    BufferedReader r
                            = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = r.readLine()) != null)
                        sb.append(line);
                    r.close();
                    contentString = sb.toString();
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
    }
}
