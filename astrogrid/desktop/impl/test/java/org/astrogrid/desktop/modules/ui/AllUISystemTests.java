/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.desktop.ARTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/** tests the UI components.
 * at present, just verifies that each can be displayed
 * will add more UI testing later.
 * @todo move scope test elsewhere.
 * @author Noel Winstanley
 * @since Jun 6, 20062:33:54 AM
 */
public class AllUISystemTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("UI Module Tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(LookoutUISystemTest.class);
		suite.addTestSuite(AstroscopeUISystemTest.class);
		suite.addTestSuite(HelioscopeUISystemTest.class);
		suite.addTestSuite(ApplicationLauncherUISystemTest.class);
		suite.addTestSuite(WorkflowBuilderUISystemTest.class);
		suite.addTestSuite(ParameterizedWorkflowLauncherUISystemTest.class);
		suite.addTestSuite(VospaceBrowserUISystemTest.class);
		suite.addTestSuite(RegistryBrowserUISystemTest.class);
		//$JUnit-END$
		return new ARTestSetup(suite, true);
	}

}
