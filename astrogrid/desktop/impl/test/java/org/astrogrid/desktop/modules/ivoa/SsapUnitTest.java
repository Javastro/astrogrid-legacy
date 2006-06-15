/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.TestCase;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.easymock.MockControl;

/** Unit test for ssap interface.
 * @author Noel Winstanley
 * @since Jun 13, 20062:01:10 PM
 */
public class SsapUnitTest extends TestCase {

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
		ssap = new SsapImpl(mockReg,mockMs);
		url = new URL("http://www.astrogrid.org/cone");		
	}
	protected Registry mockReg;
	protected MockControl regControl;
	protected MyspaceInternal mockMs;
	protected MockControl msControl;
	protected Ssap ssap;
	protected URL url;
	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQuery(URI, double, double, double)'
	 */
	public void testConstructQuery() throws InvalidArgumentException, NotFoundException, URISyntaxException {
		URL u1 = ssap.constructQuery(new URI(url.toString()),1.0,2.0,3.0);
		assertEquals(url.getHost(),u1.getHost());
		assertEquals(url.getPath(),u1.getPath());
		assertEquals("POS=1.0%2C2.0&SIZE=3.0",u1.getQuery());
	}



	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQueryS(URI, double, double, double, double)'
	 */
	public void testConstructQueryS() throws InvalidArgumentException, NotFoundException, URISyntaxException {
		URL u1 = ssap.constructQueryS(new URI(url.toString()),1.0,2.0,3.0,4.0);
		assertEquals(url.getHost(),u1.getHost());
		assertEquals(url.getPath(),u1.getPath());
		assertEquals("POS=1.0%2C2.0&SIZE=3.0%2C4.0",u1.getQuery());
	}



	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.getRegistryQuery()'
	 */
	public void testGetRegistryQuery() {
	
		assertNotNull(ssap.getRegistryQuery());
	}

}
