/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;

/** JUnit test that verifies my understanding of Kona's Tap / DSA implementation
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 22, 200811:33:10 AM
 */
public class TapServiceTest extends TestCase {

    private static final String QUERY = "Select Top 100 a.BF From bftohip as a";
    private static final String FORMAT =  "application/x-votable+xml;tabledata";
    
    protected void setUp() throws Exception {
        super.setUp();
        // endpoint from the registration.
        u = new URL("http://cass123-zone1.ast.cam.ac.uk/Hipparcos/tapservice/jobs");
        client = new HttpClient();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        u = null;
        client = null;
    }
    
    URL u;
    HttpClient client;
    
    
    public void notestCreateDeleteQuery() throws Exception {
        // we just post to the job list.
        final PostMethod postMethod = new PostMethod(u.toString());
        postMethod.setRequestBody(new NameValuePair[] {
                new NameValuePair("ADQL",QUERY),
                new NameValuePair("FORMAT",FORMAT)
        });
        int code = client.executeMethod(postMethod);        
        assertEquals(HttpStatus.SC_SEE_OTHER,code); // expecting a 'see other'
       
        Header location = postMethod.getResponseHeader("Location");
        assertNotNull(location);
        final URL jobID = new URL(location.getValue()); // verify it parses as a url
        postMethod.releaseConnection();
        
        // deletion bit.
        final DeleteMethod deleteMethod = new DeleteMethod(jobID.toString());

        code = client.executeMethod(deleteMethod);
        assertEquals(HttpStatus.SC_SEE_OTHER,code);
        
        location = deleteMethod.getResponseHeader("Location");
        assertNotNull(location);
        final URL list = new URL(location.getValue());
        
        assertEquals(u,list); // test we've be redirected back to the joblist.  
        deleteMethod.releaseConnection();
    }
    
    public void testCreateRunQuery() throws Exception {
        // we just post to the job list.
        final PostMethod createMethod = new PostMethod(u.toString());
        createMethod.setRequestBody(new NameValuePair[] {
                new NameValuePair("ADQL",QUERY),
                new NameValuePair("FORMAT",FORMAT)
        });
        int code = client.executeMethod(createMethod);        
        createMethod.releaseConnection();
        System.err.println(createMethod.getResponseBodyAsString());
        assertEquals(HttpStatus.SC_SEE_OTHER,code); // expecting a 'see other'
       
        Header location = createMethod.getResponseHeader("Location");
        assertNotNull(location);
        final URL jobID = new URL(location.getValue()); // verify it parses as a url

        // start it
       
        final URL phaseURL = mkSubURL(jobID,"phase");
        final PostMethod startMethod = new PostMethod(phaseURL.toString());
        startMethod.setRequestBody(new NameValuePair[] {
                new NameValuePair("PHASE","RUN")
        });        
        code = client.executeMethod(startMethod);
        System.err.println(startMethod.getResponseBodyAsString());
        assertEquals(HttpStatus.SC_SEE_OTHER,code);
        location = createMethod.getResponseHeader("Location");
        createMethod.releaseConnection();
        assertNotNull(location);
        final URL returnedPhase = new URL(location.getValue());
        assertEquals(jobID,returnedPhase);
        System.err.println(jobID);
        // then poll on phase.

        
        
        // then check on /error or /results
            
    }
    
    
    private String checkPhase(final URL jobID) throws Exception {
        final GetMethod gm = new GetMethod(mkSubURL(jobID,"phase").toString());
        final int code = client.executeMethod(gm);
        assertEquals(HttpStatus.SC_OK,code);
        return null; // unfinished
    }
    
    
    public void testMkSubURL() throws Exception {
        final URL noTrail = new URL("http://foo.bar/path/pathmore");
        final URL trail = new URL("http://foo.bar/path/pathmore/");
        assertFalse(noTrail.equals(trail));
        
        final URL u = mkSubURL(noTrail,"phase");
        final URL u1 = mkSubURL(trail,"phase");
        assertEquals(u,u1);
        
        final URL u2 = mkSubURL(trail,"/phase");
        final URL u3 = mkSubURL(noTrail,"/phase");
        assertEquals(u,u2);
        assertEquals(u2,u3);
        
    }
    
    /** create a sub url, respecting existing path of root, and taking care of trailing /, etc 
     * @throws MalformedURLException */
    private URL mkSubURL(final URL root, final String sub) throws MalformedURLException {
        final String path = StringUtils.stripEnd(root.toString(),"/");
        final String particle = StringUtils.stripStart(sub,"/");
        return new URL(path + "/" + particle);
    }
    

}
