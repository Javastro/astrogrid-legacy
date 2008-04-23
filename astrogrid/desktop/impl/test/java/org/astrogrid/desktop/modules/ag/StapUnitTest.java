/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URL;

import junit.framework.TestCase;

import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.desktop.modules.ivoa.SiapUnitTest;
import static org.easymock.EasyMock.*;

/** 
 * @author Noel Winstanley
 * @since Jun 13, 20067:02:10 PM
 * @TEST extend
 */
public class StapUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mockReg =createMock(Registry.class);
		mockMs = createMock(MyspaceInternal.class);
		replay(mockReg,mockMs);
		stap = new StapImpl(mockReg,mockMs);
		url = new URL("http://www.astrogrid.org/cone");		
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		mockReg = null;
		mockMs = null;
		stap = null;
		url = null;
	}
	
	protected Registry mockReg;
	protected MyspaceInternal mockMs;
	protected Stap stap;
	protected URL url;

	public void testGetRegistryAdqlQuery() {
		assertNotNull(stap.getRegistryAdqlQuery());
	}
	public void testGetRegistryXQuery() {
		assertNotNull(stap.getRegistryXQuery());
	}

}
