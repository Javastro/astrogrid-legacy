/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URL;

import junit.framework.TestCase;

import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.desktop.modules.ivoa.SiapUnitTest;
import org.easymock.MockControl;

/** @implement this class - based on {@link SiapUnitTest}
 * @author Noel Winstanley
 * @since Jun 13, 20067:02:10 PM
 */
public class StapUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		regControl = MockControl.createControl(Registry.class);
		msControl = MockControl.createControl(MyspaceInternal.class);
		mockReg = (Registry)regControl.getMock();
		mockMs = (MyspaceInternal)msControl.getMock();
		regControl.replay(); // don't expect any calls to either mock.
 		msControl.replay();	
		stap = new StapImpl(mockReg,mockMs);
		url = new URL("http://www.astrogrid.org/cone");		
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		mockReg = null;
		regControl = null;
		mockMs = null;
		msControl = null;
		stap = null;
		url = null;
	}
	
	protected Registry mockReg;
	protected MockControl regControl;
	protected MyspaceInternal mockMs;
	protected MockControl msControl;
	protected Stap stap;
	protected URL url;

	public void testGetRegistryAdqlQuery() {
		assertNotNull(stap.getRegistryAdqlQuery());
	}
	public void testGetRegistryXQuery() {
		assertNotNull(stap.getRegistryXQuery());
	}

}
