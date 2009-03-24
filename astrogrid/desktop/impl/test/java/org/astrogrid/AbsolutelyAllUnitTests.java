/**
 * 
 */
package org.astrogrid;


import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.modules.ag.vfs.AllVfsUnitTests;
import org.astrogrid.desktop.modules.auth.AllAuthUnitTests;
import org.astrogrid.desktop.modules.system.AllSystemUnitTests;
import org.astrogrid.desktop.modules.system.messaging.AllMessagingUnitTests;
import org.astrogrid.desktop.modules.system.ui.AllSystemUiUnitTests;
import org.astrogrid.desktop.modules.ui.AllUIUnitTests;
import org.astrogrid.desktop.modules.ui.actions.AllActionsUnitTests;
import org.astrogrid.desktop.modules.ui.comp.AllUIComponentUnitTests;
import org.astrogrid.desktop.modules.ui.dnd.AllDndUnitTests;
import org.astrogrid.desktop.modules.ui.fileexplorer.AllFileExplorerUnitTests;
import org.astrogrid.desktop.modules.ui.folders.AllUiFoldersUnitTests;
import org.astrogrid.desktop.modules.ui.scope.AllScopeUnitTests;
import org.astrogrid.desktop.modules.ui.voexplorer.AllVoexplorerUnitTests;
import org.astrogrid.desktop.modules.votech.AllVotechUnitTests;
import org.astrogrid.desktop.thirdparty.AllThirdPartyUnitTests;

/** Runs all unit tests - handy hook for within eclipse
 * @author Noel Winstanley
 * @since Jun 6, 20062:35:36 PM
 */
public class AbsolutelyAllUnitTests {

	public static Test suite() {
		final TestSuite suite = new TestSuite("All unit tests for workbench");
		suite.addTest(AllThirdPartyUnitTests.suite());

		suite.addTest(AllStartupUnitTests.suite());
		suite.addTest(org.astrogrid.acr.AllAcrUnitTests.suite());
		
		suite.addTest(org.astrogrid.desktop.alternatives.AllAlternativesUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.framework.AllFrameworkUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.hivemind.AllHivemindUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.protocol.AllProtocolUnitTests.suite());
		
		suite.addTest(AllSystemUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.pref.AllPreferenceUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.contributions.AllSystemContributionUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.converters.AllSystemConverterUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.system.transformers.AllSystemTransformerUnitTests.suite());
		
		suite.addTest(org.astrogrid.desktop.modules.ivoa.AllIvoaUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ivoa.resource.AllResourceUnitTests.suite());
		
		suite.addTest(org.astrogrid.desktop.modules.ag.AllAstrogridUnitTests.suite());
		suite.addTest(AllVfsUnitTests.suite());
		suite.addTest(AllAuthUnitTests.suite());
			
		suite.addTest(AllVotechUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.util.AllUtilUnitTests.suite());
		
		suite.addTest(AllSystemUiUnitTests.suite());
		suite.addTest(AllUIComponentUnitTests.suite());
		suite.addTest(AllUIUnitTests.suite());
		suite.addTest(org.astrogrid.desktop.modules.ui.voexplorer.srql.AllSRQLUnitTests.suite());
		suite.addTest(AllDndUnitTests.suite());
		suite.addTest(AllUiFoldersUnitTests.suite());
		suite.addTest(AllFileExplorerUnitTests.suite());
		suite.addTest(AllVoexplorerUnitTests.suite());
		suite.addTest(AllScopeUnitTests.suite());
		suite.addTest(AllActionsUnitTests.suite());
		suite.addTest(AllMessagingUnitTests.suite());
		return suite;
	}

}
