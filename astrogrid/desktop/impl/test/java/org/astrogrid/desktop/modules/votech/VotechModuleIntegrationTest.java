/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.votech.VoMon;

/**
 * @author Noel Winstanley
 * @since Jan 8, 20077:43:42 PM
 */
public class VotechModuleIntegrationTest extends InARTestCase {

	public void testVoMon() throws Exception {
		VoMon vo = assertServiceExists(VoMon.class,"votech.vomon");
		vo.checkAvailability(null);
	}
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(VotechModuleIntegrationTest.class));
    }

}
