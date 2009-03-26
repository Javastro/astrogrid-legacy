/**
 * 
 */
package org.astrogrid.desktop.protocol.fallback;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.TestCase;

/** Unit test for the fallback url handler.
 * @author Noel Winstanley
 * @since Jan 4, 200712:49:03 PM
 */
public class HandlerUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		// necessary to find the correct handler. - otherwise can't create a url.
		System.setProperty("java.protocol.handler.pkgs","org.astrogrid.desktop.protocol");
				
		super.setUp();
		
		// just get a reference to ourselves.
		file = this.getClass().getResource("HandlerUnitTest.class");
		assertNotNull(file);

		
		classpath1 = new URL("classpath:/org/astrogrid/desktop/protocol/classpath/Handler.class");
		classpath2 = new URL("classpath:/org/astrogrid/desktop/protocol/fallback/Handler.class");
		
		classpathNone1 = new URL("classpath:/doesntexist/foo.txt");
		classpathNone2 = new URL("classpath:/notehtereither/foo");
		
	}

	URL file;

	
	URL classpath1;
	URL classpath2;
	URL classpathNone1;
	URL classpathNone2;
	
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		file = null;
		classpath1 = null;
		classpath2 = null;
		classpathNone1 = null;
		classpathNone2 = null;
	}
	
	
	public void testIndividualOKURLS() throws Exception{
		URL[] oks = new URL[] {file,classpath1, classpath2};
		for (int i = 0; i < oks.length; i++) {
			assertNotNull(oks[i]);
			URLConnection conn = oks[i].openConnection();
			assertNotNull(conn);
			conn.getInputStream().close();
		}
	}
	
	public void testIndividualBadURLS() {
		URL[] oks = new URL[] { classpathNone1, classpathNone2};
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
	
	
}
