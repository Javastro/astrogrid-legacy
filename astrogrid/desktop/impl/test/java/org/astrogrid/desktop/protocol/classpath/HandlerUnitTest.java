/**
 * 
 */
package org.astrogrid.desktop.protocol.classpath;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.TestCase;

/** test for classpath url handler.
 * @author Noel Winstanley
 * @since Jan 4, 200712:17:05 PM
 */
public class HandlerUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		// necessary to find the correct handler. - otherwise can't create a url.
		System.setProperty("java.protocol.handler.pkgs","org.astrogrid.desktop.protocol");
		super.setUp();
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	
	public void testAvailableAbsoluteFile() throws Exception {
		
		URL u = new URL("classpath:/org/astrogrid/desktop/protocol/classpath/Handler.class");
		URLConnection conn = u.openConnection();
		assertNotNull(conn);
	}

	// interesting, but not useful - relative files are resolved relative to the handler class
	// (which is in the same package as this unit test).
	public void testAvailableRelativeFile() throws Exception {
		
		URL u = new URL("classpath:HandlerUnitTest.class");
		URLConnection conn = u.openConnection();
		assertNotNull(conn);
	}
	
	public void testUnknownFile()  {
		try {
		URL u = new URL("classpath:/an/unknwon/file");
		URLConnection conn = u.openConnection();
		fail("expected to chuck");
		} catch (FileNotFoundException e) {
			// expected
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
