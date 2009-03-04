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
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.desktop.modules.system.ui.UIContext;

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
		mockReg = createMock(Registry.class);
		mockVFS = createMock(FileSystemManager.class);
        mockCxt = createMock(UIContext.class);
        replay(mockReg,mockVFS,mockCxt);
 		
		cone = new ConeImpl(mockReg,mockVFS,mockCxt);
		url = new URL("http://www.astrogrid.org/cone");
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		mockReg = null;
		cone = null;
		url = null;
		mockVFS = null;
		mockCxt = null;
	}
	protected FileSystemManager mockVFS;
	protected Registry mockReg;
    protected UIContext mockCxt;	
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
		final URL u1 = cone.constructQuery(new URI(url.toString()),1.0,2.0,3.0);
		assertEquals(url.getHost(),u1.getHost());
		assertEquals(url.getPath(),u1.getPath());
		assertEquals("RA=1.0&DEC=2.0&SR=3.0",u1.getQuery());

	}
	
	
}
