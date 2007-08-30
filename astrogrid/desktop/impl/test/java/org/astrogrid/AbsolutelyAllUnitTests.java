/**
 * 
 */
package org.astrogrid;


import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.modules.auth.AllAuthUnitTests;
import org.astrogrid.desktop.modules.system.ui.AllSystemUiUnitTests;
import org.astrogrid.desktop.modules.ui.comp.AllUIComponentUnitTests;
import org.astrogrid.desktop.modules.ui.dnd.AllDndUnitTests;
import org.astrogrid.desktop.modules.ui.folders.AllUiFoldersUnitTests;
import org.astrogrid.desktop.modules.ui.scope.AllScopeUnitTests;
import org.astrogrid.desktop.modules.ui.voexplorer.storage.AllVOExplorerStorageUnitTests;
import org.astrogrid.desktop.modules.votech.AllVotechUnitTests;

/** Runs all unit tests - handy hook for within eclipse
 * @author Noel Winstanley
 * @since Jun 6, 20062:35:36 PM
 */
public class AbsolutelyAllUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("All unit tests for workbench");
		suite.addTest(AllStartupUnitTests.suite());
		suite.addTest(org.astrogrid.acr.AllAcrUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.alternatives.AllAlternativesUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.framework.AllFrameworkUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.hivemind.AllHivemindUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.pref.AllPreferenceUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.contributions.AllSystemContributionUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.converters.AllSystemConverterUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.transformers.AllSystemTransformerUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ivoa.AllIvoaUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ivoa.resource.AllResourceUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ag.AllAstrogridUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.plastic.AllPlasticUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.util.AllUtilUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.dialogs.registry.srql.AllSRQLUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.protocol.AllProtocolUnitTests.suite());
		suite.addTest(AllVotechUnitTests.suite());
		suite.addTest(AllUIComponentUnitTests.suite());
		suite.addTest(AllDndUnitTests.suite());
		suite.addTest(AllAuthUnitTests.suite());
		suite.addTest(AllUiFoldersUnitTests.suite());
		suite.addTest(AllVOExplorerStorageUnitTests.suite());
		suite.addTest(AllSystemUiUnitTests.suite());
		suite.addTest(AllScopeUnitTests.suite());
		return suite;
	}

}
