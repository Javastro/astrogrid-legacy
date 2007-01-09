/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.util.TablesImplUnitTest;
import org.easymock.MockControl;

/** Unit test for siap interface.
 * @author Noel Winstanley
 * @since Jun 13, 20062:01:10 PM
 */
public class SiapUnitTest extends TestCase {

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
		siap = new SiapImpl(mockReg,mockMs);
		url = new URL("http://www.astrogrid.org/cone");		
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		mockReg = null;
		regControl = null;
		mockMs = null;
		msControl = null;
		siap = null;
		url = null;
	}
	protected Registry mockReg;
	protected MockControl regControl;
	protected MyspaceInternal mockMs;
	protected MockControl msControl;
	protected Siap siap;
	protected URL url;
	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQuery(URI, double, double, double)'
	 */
	public void testConstructQuery() throws InvalidArgumentException, NotFoundException, URISyntaxException {
		URL u1 = siap.constructQuery(new URI(url.toString()),1.0,2.0,3.0);
		assertEquals(url.getHost(),u1.getHost());
		assertEquals(url.getPath(),u1.getPath());
		assertEquals("POS=1.0%2C2.0&SIZE=3.0",u1.getQuery());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQueryF(URI, double, double, double, String)'
	 */
	public void testConstructQueryF() throws InvalidArgumentException, NotFoundException, URISyntaxException {
		URL u1 = siap.constructQueryF(new URI(url.toString()),1.0,2.0,3.0,"FITS");
		assertEquals(url.getHost(),u1.getHost());
		assertEquals(url.getPath(),u1.getPath());
		assertEquals("POS=1.0%2C2.0&SIZE=3.0&FORMAT=FITS",u1.getQuery());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQueryS(URI, double, double, double, double)'
	 */
	public void testConstructQueryS() throws InvalidArgumentException, NotFoundException, URISyntaxException {
		URL u1 = siap.constructQueryS(new URI(url.toString()),1.0,2.0,3.0,4.0);
		assertEquals(url.getHost(),u1.getHost());
		assertEquals(url.getPath(),u1.getPath());
		assertEquals("POS=1.0%2C2.0&SIZE=3.0%2C4.0",u1.getQuery());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQuerySF(URI, double, double, double, double, String)'
	 */
	public void testConstructQuerySF() throws InvalidArgumentException, NotFoundException, URISyntaxException {
		URL u1 = siap.constructQuerySF(new URI(url.toString()),1.0,2.0,3.0,4.0,"FITS");
		assertEquals(url.getHost(),u1.getHost());
		assertEquals(url.getPath(),u1.getPath());
		assertEquals("POS=1.0%2C2.0&SIZE=3.0%2C4.0&FORMAT=FITS",u1.getQuery());
	}

	/** test that siap execute() returns values in the siap datamodel */
	public void testExecute() throws Exception {
		URL localSiapURL = TablesImplUnitTest.class.getResource("siap.vot");
		assertNotNull(localSiapURL);
		Map[] r = siap.execute(localSiapURL);
		assertNotNull(r);
		assertTrue(r.length > 0);
		for (int i = 0; i < r.length; i++) {
			assertNotNull(r[i]);
			assertEquals(r[0].keySet(),r[i].keySet());
			if (i > 0) {
				assertFalse(r[0].values().equals(r[i].values()));
			}
			for (Iterator j = r[i].values().iterator(); j.hasNext() ; ) {
				assertNotNull(j.next());
			}
		}
		// now check that at least some of the UCDs have been mapped to datamodel names.
		assertFalse(r[0].containsKey("VOX:Image_AccessReference"));
		assertTrue(r[0].containsKey("AccessReference"));
	}
	
	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.getRegistryQuery()'
	 */
	public void testGetRegistryQuery() {
	
		assertNotNull(siap.getRegistryQuery());
	}
	
	public void testGetAdqlRegistryQuery() {
		assertNotNull(siap.getRegistryAdqlQuery());
	}
	
	
	public void testGetlRegistryXQuery() {
		assertNotNull(siap.getRegistryXQuery());
	}
}
