/**
 * 
 */
package org.astrogrid.desktop.protocol;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.protocol.classpath.HandlerUnitTest;

/**
 * @author Noel Winstanley
 * @since Jan 4, 20071:32:14 PM
 */
public class AllProtocolUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit Tests for url protocol handlers");
		suite.addTestSuite(HandlerUnitTest.class);
		suite.addTestSuite(org.astrogrid.desktop.protocol.fallback.HandlerUnitTest.class);
		suite.addTestSuite(org.astrogrid.desktop.protocol.httpclient.HandlerUnitTest.class);
		return suite;
	}

}
