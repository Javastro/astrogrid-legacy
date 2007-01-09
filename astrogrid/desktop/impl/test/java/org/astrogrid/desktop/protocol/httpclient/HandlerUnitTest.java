/**
 * 
 */
package org.astrogrid.desktop.protocol.httpclient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import junit.framework.TestCase;

/** test of the httpclient handler
 * @author Noel Winstanley
 * @since Jan 4, 200712:24:30 PM
 */
public class HandlerUnitTest extends TestCase {

	protected void setUp() throws Exception {
		// necessary to find the correct handler. - otherwise can't create a url.
		System.setProperty("java.protocol.handler.pkgs","org.astrogrid.desktop.protocol");
			
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
public void testAvailableURL() throws Exception {
		
		URL u = new URL("httpclient://www.astrogrid.org");
		URLConnection conn = u.openConnection();
		assertNotNull(conn);
		conn.getInputStream().close();
	}

	public void testUnknownURL()  {
		URLConnection conn = null;
		try {
		URL u = new URL("httpclient://www.astrogrid.org/nonexistent.txt");
		conn = u.openConnection();

		fail("expected to chuck");
		} catch (FileNotFoundException e) {
			// expected
		} catch (IOException e) {
			fail(e.getMessage());
		} 
	}
	
	public void testUnknownServer()  {
		try {
		URL u = new URL("httpclient://www.astrogrid78732.org/nonexistent.txt");
		URLConnection conn = u.openConnection();
		fail("expected to chuck");
		} catch (UnknownHostException e) {
			// expected
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}

