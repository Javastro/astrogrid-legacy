/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.easymock.MockControl;

import junit.framework.TestCase;

/** unit test for cone search interface
 * @author Noel Winstanley
 * @since Jun 13, 200612:41:49 PM
 */
public class ConeUnitTest extends TestCase {

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
 		
		cone = new ConeImpl(mockReg,mockMs);
		url = new URL("http://www.astrogrid.org/cone");
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		mockReg = null;
		regControl = null;
		mockMs = null;
		msControl = null;
		cone = null;
		url = null;
	}
	protected Registry mockReg;
	protected MockControl regControl;
	protected MyspaceInternal mockMs;
	protected MockControl msControl;
	protected Cone cone;
	protected URL url;
	/*
	 * Test method for 'org.astrogrid.desktop.modules.nvo.ConeImpl.getRegistryQuery()'
	 */
	public void testGetRegistryQuery() {

		assertNotNull(cone.getRegistryQuery());
	}

	public void testGetAdqlRegistryQuery() {
		assertNotNull(cone.getRegistryAdqlQuery());
	}
	
	
	public void testGetlRegistryXQuery() {
		assertNotNull(cone.getRegistryXQuery());
	}
	
	/*
	 * Test method for 'org.astrogrid.desktop.modules.nvo.ConeImpl.constructQuery(URI, double, double, double)'
	 */
	public void testConstructQuery() throws InvalidArgumentException, NotFoundException, URISyntaxException {
		URL u1 = cone.constructQuery(new URI(url.toString()),1.0,2.0,3.0);
		assertEquals(url.getHost(),u1.getHost());
		assertEquals(url.getPath(),u1.getPath());
		assertEquals("RA=1.0&DEC=2.0&SR=3.0",u1.getQuery());

	}
	
	
}
