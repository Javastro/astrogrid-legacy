/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

import org.astrogrid.io.Piper;

/** Tests the behaviour of 'undocumented' url protocol handlers.
 * @author Noel Winstanley
 * @since Jan 2, 20072:52:49 PM
 */
public class URLSchemeLibraryTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	// just seems to return the 'nested' url - maybe without doing some kind of translation.
	public void testVerbatimScheme() throws Exception {
		URL v = new URL("verbatim:http://www.slashdot.org");
		System.out.println(v);
		InputStream is = v.openStream();
		Piper.pipe(is,System.out);
	}
	
	public void testSystemResourceScheme() throws Exception {
		URL v = new URL("systemresource:/org/astrogrid/desktop/modules/system/URLSchemeUnitTest.class");
		System.out.println(v);
		InputStream is = v.openStream();
		Piper.pipe(is,System.out);		
	}

}
