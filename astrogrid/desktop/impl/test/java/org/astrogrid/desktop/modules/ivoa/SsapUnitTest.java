/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import static org.easymock.EasyMock.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.desktop.modules.system.ui.UIContext;

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
		mockReg = createMock(Registry.class);
        mockVFS = createMock(FileSystemManager.class);        
        mockCxt = createMock(UIContext.class);        
		replay(mockReg,mockVFS,mockCxt);
		ssap = new SsapImpl(mockReg,mockVFS,mockCxt);
		url = new URL("http://www.astrogrid.org/cone");		
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		mockReg = null;
		ssap = null;
		url = null;
		mockVFS = null;
	}
    protected FileSystemManager mockVFS;	
    protected UIContext mockCxt;    
	protected Registry mockReg;
	protected Ssap ssap;
	protected URL url;
	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQuery(URI, double, double, double)'
	 */
	public void testConstructQuery() throws InvalidArgumentException, NotFoundException, URISyntaxException {
		final URL u1 = ssap.constructQuery(new URI(url.toString()),1.0,2.0,3.0);
		assertEquals(url.getHost(),u1.getHost());
		assertEquals(url.getPath(),u1.getPath());
		assertEquals("POS=1.0%2C2.0&SIZE=3.0&REQUEST=queryData",u1.getQuery());
	}



	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.constructQueryS(URI, double, double, double, double)'
	 */
	public void testConstructQueryS() throws InvalidArgumentException, NotFoundException, URISyntaxException {
		final URL u1 = ssap.constructQueryS(new URI(url.toString()),1.0,2.0,3.0,4.0);
		assertEquals(url.getHost(),u1.getHost());
		assertEquals(url.getPath(),u1.getPath());
		assertEquals("POS=1.0%2C2.0&SIZE=3.0%2C4.0&REQUEST=queryData",u1.getQuery());
	}



	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.SiapImpl.getRegistryQuery()'
	 */
	public void testGetRegistryQuery() {
	
		assertNotNull(ssap.getRegistryQuery());
	}
	public void testGetRegistryXQuery() {
		
		assertNotNull(ssap.getRegistryXQuery());
	}
	public void testGetRegistryAdqlQuery() {
		
		assertNotNull(ssap.getRegistryAdqlQuery());
	}

}
