/**
 * 
 */
package org.astrogrid;


import junit.framework.Test;
import junit.framework.TestSuite;

/** Hook to run all unit tests in the project.
 * useful for running within eclipse. not used within maven build - which recognizes
 * individuals tests according to names
 * @author Noel Winstanley
 * @since Jun 6, 20062:35:36 PM
 */
public class AbsolutelyAllUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("All unit tests for workbench");
		suite.addTest(AllUnitTests.suite());
		suite.addTest(org.astrogrid.acr.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.alternatives.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.framework.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.hivemind.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.contributions.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.converters.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.transformers.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ivoa.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ivoa.resource.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.nvo.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ag.recorder.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ag.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.plastic.AllUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ui.sendto.AllUnitTests.suite());
		return suite;
	}

}
