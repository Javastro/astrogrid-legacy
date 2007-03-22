/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 22, 20072:55:36 PM
 */
public class AllAuthUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for Sessions & Authorisation");
		//$JUnit-BEGIN$
		suite.addTestSuite(SessionManagerImplUnitTest.class);
		suite.addTestSuite(MutablePrincipalUnitTest.class);
		suite.addTestSuite(UnauthenticatedPrincipalUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
