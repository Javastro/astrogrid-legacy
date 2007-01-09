/**
 * 
 */
package org.astrogrid.desktop.protocol.fallback;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.TestCase;

/** Unit test for the fallback url handler.
 * @author Noel Winstanley
 * @since Jan 4, 200712:49:03 PM
 */
public class HandlerUnitTest extends TestCase {

	protected void setUp() throws Exception {
		// necessary to find the correct handler. - otherwise can't create a url.
		System.setProperty("java.protocol.handler.pkgs","org.astrogrid.desktop.protocol");
				
		super.setUp();
		
		http1 = new URL("http://www.astrogrid.org");

		httpclient1 = new URL("httpclient://www.astrogrid.org");
		httpclient2 = new URL("httpclient://wiki.astrogrid.org/");
		
		httpclientNone1 = new URL("httpclient://www.astrogrid.org/notexists.txt");
		httpclientNone2 = new URL("httpclient://www.unknownDomain/nothing.txt");
		
		classpath1 = new URL("classpath:/org/astrogrid/desktop/protocol/classpath/Handler.class");
		classpath2 = new URL("classpath:/org/astrogrid/desktop/protocol/fallback/Handler.class");
		
		classpathNone1 = new URL("classpath:/doesntexist/foo.txt");
		classpathNone2 = new URL("classpath:/notehtereither/foo");
		
	}

	URL http1;
	
	URL httpclient1;
	URL httpclient2;
	URL httpclientNone1;
	URL httpclientNone2;
	
	URL classpath1;
	URL classpath2;
	URL classpathNone1;
	URL classpathNone2;
	
	protected void tearDown() throws Exception {
		super.tearDown();
		http1 = null;
		httpclient1 = null;
		httpclient2 = null;
		httpclientNone1 = null;
		httpclientNone2 = null;
		classpath1 = null;
		classpath2 = null;
		classpathNone1 = null;
		classpathNone2 = null;
	}
	
	
	public void testIndividualOKURLS() throws Exception{
		URL[] oks = new URL[] {http1, httpclient1,httpclient2, classpath1, classpath2};
		for (int i = 0; i < oks.length; i++) {
			assertNotNull(oks[i]);
			URLConnection conn = oks[i].openConnection();
			assertNotNull(conn);
			conn.getInputStream().close();
		}
	}
	
	public void testIndividualBadURLS() {
		URL[] oks = new URL[] {httpclientNone1,httpclientNone2, classpathNone1, classpathNone2};
		for (int i = 0; i < oks.length; i++) {
			assertNotNull(oks[i]);
			try {
			URLConnection conn = oks[i].openConnection();
			conn.getInputStream().close();
			fail("expected to barf:" + oks[i]);
			} catch (IOException e) {
				// ok
			}
		}
	}
	
	////////////////////HTTP - fails
	
	public void testHttpClientHttp() throws Exception{
		try {
		URL u = new URL("fallback:" + httpclient1.toString() + "," + http1.toString());
		URLConnection conn = u.openConnection();
		fail("expected to chuck");
		} catch (MalformedURLException e) {
			// ok
		}
	}
	
	public void testHttpHttpClient() throws Exception{
	try {
		URL u = new URL("fallback:" + http1.toString() + "," + httpclient2.toString());
		URLConnection conn = u.openConnection();
		fail("expected to chuck");
	} catch (MalformedURLException e) {
		// ok
	}	
	}

/////////////////////////////////// HTTP CLIENT
	public void testHttpCLientHttpClient1() throws Exception{
		URL u = new URL("fallback:" + httpclient1.toString() + "," + httpclient2.toString());
		URLConnection conn = u.openConnection();
		assertNotNull(conn);
		conn.getInputStream().close();
	}
	
	public void testHttpClientHttpClient2() throws Exception{
		URL u = new URL("fallback:" + httpclientNone1.toString() + "," + httpclient2.toString());
		URLConnection conn = u.openConnection();
		assertNotNull(conn);
		conn.getInputStream().close();		
	}
	
	public void testHttpClientHttpClientNeither() throws Exception{
		try {
		URL u = new URL("fallback:" + httpclientNone1.toString() + "," + httpclientNone2.toString());
		URLConnection conn = u.openConnection();
		assertNotNull(conn);
		conn.getInputStream().close();
		fail("expected to chuck");
		} catch (FileNotFoundException e) {
			// ok.
		}
	}

	
	////// classpath
	public void testClasspathClasspath1() throws Exception{
		URL u = new URL("fallback:" + classpath1.toString() + "," + classpath2.toString());
		URLConnection conn = u.openConnection();
		assertNotNull(conn);
		conn.getInputStream().close();		
	}
	
	public void testClasspathClasspath2() throws Exception {
		URL u = new URL("fallback:" + classpathNone1.toString() + "," + classpath2.toString());
		URLConnection conn = u.openConnection();
		assertNotNull(conn);
		conn.getInputStream().close();			
	}
	
	public void testClasspathClasspathNeither() throws Exception {
		try {
		URL u = new URL("fallback:" + classpathNone1.toString() + "," + classpathNone2.toString());
		URLConnection conn = u.openConnection();
		assertNotNull(conn);
		conn.getInputStream().close();
		fail("expected to chuck");
		} catch (FileNotFoundException e) {
			// ok
		}
	}
	
	// mixed http client, classpath usage.
	
	
	
	public void testHttpClientClasspath1() throws Exception {
		URL u = new URL("fallback:" + httpclient1.toString() + "," + classpath1.toString());
		URLConnection conn = u.openConnection();
		assertNotNull(conn);
		conn.getInputStream().close();		
	}
	
	public void testHttpClientClasspath2() throws Exception {
		URL u = new URL("fallback:" + httpclientNone1.toString() + "," + classpath1.toString());
		URLConnection conn = u.openConnection();
		assertNotNull(conn);
		conn.getInputStream().close();		
	}
	
	public void testHttpClientClasspathNeither() throws Exception {
		try {
		URL u = new URL("fallback:" + httpclientNone1.toString() + "," + classpathNone1.toString());
		URLConnection conn = u.openConnection();
		assertNotNull(conn);
		conn.getInputStream().close();
		fail("expected to chuck");
		} catch (FileNotFoundException e) {
			// ok
		}
	}
	
}
