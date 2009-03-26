/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.net.URI;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.votech.VoMon;
import org.votech.VoMonBean;

/** Tests vomon component is connecting to a vomon server that is available.
 * @author Noel Winstanley
 * @since Jan 9, 20071:29:34 AM
 */
public class VoMonSystemTest extends InARTestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		vomon = (VoMon)getACR().getService(VoMon.class);
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		vomon = null;
	}
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(VoMonSystemTest.class));
    }
	VoMon vomon;
	
	public void testParsedServices() throws Exception {
		vomon.reload();
		// assume that if this is here, all is well.
		VoMonBean bean = vomon.checkAvailability(new URI("ivo://uk.ac.le.star/filemanager"));
	
		assertNotNull(bean);
	}

	
}
