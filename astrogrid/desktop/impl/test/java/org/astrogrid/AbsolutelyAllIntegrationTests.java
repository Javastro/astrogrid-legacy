/**
 * 
 */
package org.astrogrid;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.framework.AllFrameworkIntegrationTests;
import org.astrogrid.desktop.modules.background.AllBackgroundIntegrationTests;
import org.astrogrid.desktop.modules.cds.AllCdsIntegrationTests;
import org.astrogrid.desktop.modules.ivoa.AllIvoaIntegrationTests;
import org.astrogrid.desktop.modules.plastic.AllPlasticIntegrationTests;
import org.astrogrid.desktop.modules.system.contributions.AllSystemContributionIntegrationTests;
import org.astrogrid.desktop.modules.system.pref.AllPreferenceIntegrationTests;
import org.astrogrid.desktop.modules.votech.AllVotechIntegrationTests;

/**All integration tests, run inside a single instance of AR
 * these tests are repeatable, in general.
 * @author Noel Winstanley
 * @since Jun 12, 20062:27:48 PM
 */
public class AbsolutelyAllIntegrationTests {

	public static Test suite() {
		TestSuite inar = new TestSuite("All Integration tests");
		inar.addTest(org.astrogrid.acr.AllAcrIntegrationTests.suite());
		inar.addTest(org.astrogrid.desktop.modules.ag.AllAstrogridIntegrationTests.suite());
		inar.addTest(org.astrogrid.desktop.modules.system.AllSystemIntegrationTests.suite());
		inar.addTest(AllPreferenceIntegrationTests.suite());
		inar.addTest(AllSystemContributionIntegrationTests.suite());
        inar.addTest(AllIvoaIntegrationTests.suite());
        inar.addTest(AllFrameworkIntegrationTests.suite());
        inar.addTest(org.astrogrid.desktop.modules.system.transformers.AllTransformerIntegrationTests.suite());
        inar.addTest(AllCdsIntegrationTests.suite());
        inar.addTest(AllBackgroundIntegrationTests.suite());
        inar.addTest(AllVotechIntegrationTests.suite());
		inar.addTest(AllPlasticIntegrationTests.suite());
		return new ARTestSetup(inar);
	}
	

}
