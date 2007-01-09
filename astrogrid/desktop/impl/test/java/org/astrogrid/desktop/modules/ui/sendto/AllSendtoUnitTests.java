/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 21, 20063:31:47 PM
 */
public class AllSendtoUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit Tests for SendTo");
		//$JUnit-BEGIN$
		suite.addTestSuite(AbstractPreferredTransferableConsumerUnitTest.class);
		suite.addTestSuite(VoDataFlavourUnitTest.class);
		suite.addTestSuite(ParameterValueTransferableUnitTest.class);
		//$JUnit-END$
		return suite;
	}

}
